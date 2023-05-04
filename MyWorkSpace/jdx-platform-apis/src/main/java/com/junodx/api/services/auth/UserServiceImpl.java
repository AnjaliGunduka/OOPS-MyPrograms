package com.junodx.api.services.auth;

import com.amazonaws.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.connectors.aws.sns.SnsMessageResponse;
import com.junodx.api.connectors.messaging.SnsMessageHandler;
import com.junodx.api.connectors.messaging.payloads.EntityPayload;
import com.junodx.api.connectors.messaging.payloads.EventType;
import com.junodx.api.controllers.payloads.*;
import com.junodx.api.controllers.users.UserErrorCodes;
import com.junodx.api.controllers.users.payloads.*;
import com.junodx.api.controllers.users.payloads.UserUpdate.*;
import com.junodx.api.dto.mappers.UserMapStructMapper;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.auth.*;
import com.junodx.api.models.patient.PatientChart;
import com.junodx.api.models.patient.PatientChartEntry;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.Phone;
import com.junodx.api.models.core.types.ClientTypes;
import com.junodx.api.models.patient.PatientDetails;
import com.junodx.api.models.patient.Vital;
import com.junodx.api.models.providers.Provider;
import com.junodx.api.repositories.AuthorityRepository;
import com.junodx.api.repositories.RefreshTokenRepository;
import com.junodx.api.repositories.UserRepository;
import com.junodx.api.repositories.auth.*;
import com.junodx.api.repositories.commerce.OrderRepository;
import com.junodx.api.security.JwtUtils;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.exceptions.JdxServiceException;
import com.junodx.api.services.mail.MailService;
import com.junodx.api.services.patients.PatientDetailsService;
import com.junodx.api.services.providers.ProviderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
@Service(value = "userService")
public class UserServiceImpl extends ServiceBase implements UserDetailsService, UserService  {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${jdx.connectors.aws.userTopic}")
    private String userTopic;

    @Value("${jdx.connectors.aws.sns.disabled}")
    private boolean snsDisabled;

    @Value("${jdx.authentication.verification.code.expiry}")
    private int verificationCodeExpirationDuration;

    @Value("${jdx.default.system.user}")
    private String defaultUserEmail;

    @Value("${jdx.default.system.clientId}")
    private String defaultSystemClientId;

    @Value("${jdx.clientids.patientportal}")
    private String patientPortalClientId;

    @Value("${jdx.clientids.labportal}")
    private String labPortalClientId;

    @Value("${jdx.clientids.providerportal}")
    private String providerPortalClientId;

    @Value("${jdx.client.patientportal.defaults.role}")
    private String defaultPatientPortalRole;

    @Value("${jdx.client.labportal.defaults.role}")
    private String defaultLabPortalRole;

    @Value("${jdx.client.providerportal.defaults.role}")
    private String defaultProviderPortalRole;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserDeviceRepository userDeviceRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private EmailChangeRepository emailChangeRepository;

    @Autowired
    private PatientDetailsService patientDetailsService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ForgotPasswordCodeRepository forgotPasswordCodeRepository;

    @Autowired
    private PatientChartEntryRepository patientChartEntryRepository;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private MailService mailService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private OrderRepository ordersRepository;

    private UserMapStructMapper userMapper;

    private ObjectMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserServiceImpl(){
        mapper = new ObjectMapper();
      //  mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    }

    public UserServiceImpl(UserMapStructMapper map){
        this.mapper = new ObjectMapper();
        this.userMapper = map;
        //  mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    }

  //  @Autowired
  //  private BCryptPasswordEncoder passwordEncoder;

    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<com.junodx.api.models.auth.User> user = userRepository.findByUsername(userId);
        if(user.isEmpty()){
            log.error("Invalid username or password.");
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        com.junodx.api.models.auth.User u = user.get();

        return new org.springframework.security.core.userdetails.User(u.getEmail(), "", getAuthorities(u));
    }


    public List getAuthorities(com.junodx.api.models.auth.User user) {
        return user.getAuthorities();
    }

    public Page<User> search(Optional<String> lastName,
                             Optional<String> firstName,
                             Optional<String> state,
                             Optional<String> city,
                             Optional<String> postalCode,
                             Optional<String> email,
                             Optional<String> practiceId,
                             Optional<String> providerId,
                             Optional<String> xifinId,
                             Optional<String> stripeId,
                             Optional<UserType> type,
                             Optional<UserStatus> status,
                             Pageable pageable)
    {
        String oLastName = null;
        String oFirstName = null;
        String oState = null;
        String oCity = null;
        String oPostalCode = null;
        String oEmail = null;
        String oPracticeId = null;
        String oProviderId = null;
        String oXifinId = null;
        String oStripeId = null;
        UserType oType = null;
        UserStatus oStatus = null;

        if(lastName.isPresent()) oLastName = lastName.get();
        if(firstName.isPresent()) oFirstName = firstName.get();
        if(state.isPresent()) oState = state.get();
        if(city.isPresent()) oCity = city.get();
        if(state.isPresent()) oState = state.get();
        if(postalCode.isPresent()) oPostalCode = postalCode.get();
        if(email.isPresent()) oEmail = email.get();
        if(practiceId.isPresent()) oPracticeId = practiceId.get();
        if(providerId.isPresent()) oProviderId = providerId.get();
        if(xifinId.isPresent()) oXifinId = xifinId.get();
        if(stripeId.isPresent()) oStripeId = stripeId.get();
        if(type.isPresent()) oType = type.get();
        if(status.isPresent()) oStatus = status.get();

        return userRepository.search(oLastName, oFirstName, oState, oCity, oPostalCode, oEmail, oXifinId, oStripeId, oType, oStatus, pageable);
    }

    public Page<User> findAllByUserType(UserType type, Pageable pageable) {
        return userRepository.findAllByUserTypeIs(type, pageable);
    }

    @Override
    public Optional<com.junodx.api.models.auth.User> findOne(String id) {
        return userRepository.findById(id);
    }



    @Override
    public Optional<com.junodx.api.models.auth.User> findOneByEmailAndClientId(String email, String clientId) {
        return userRepository.findUserByEmailAndClientId(email, clientId);
    }

    public ServiceBase.ServiceResponse<User> findOneByEmailAndClientId(String email, String clientId, String[] includes){
        if(email != null){
            Optional<User> returnUser = userRepository.findUserByEmailAndClientId(email, clientId);
            if(returnUser.isPresent()){
                User user = returnUser.get();
                user = loadAdditionalData(user, includes);
                return new ServiceBase.ServiceResponse(LogCode.SUCCESS, user, true);
            }
        }

        return new ServiceBase.ServiceResponse(LogCode.RESOURCE_GET_ERROR, "", false);
    }

    public Page<User> findAllUsersByProviderId(String providerId, UserType type, Pageable pageable){
        Optional<Provider> provider = providerService.getProvider(providerId, new String[]{});
        if(provider.isPresent())
            return userRepository.findUsersByPatientDetails_ProvidersAndUserTypeIs(provider.get(), type, pageable);

        return null;
    }

    public Page<User> findAllUsersByLastName(String lastName, Pageable pageable){
        return userRepository.findUsersByLastName(lastName, pageable);
    }

    public Page<User> findAllUsersByCity(String city, Pageable pageable){
        return userRepository.findUsersByPrimaryAddress_City(city, pageable);
    }

    public Page<User> findAllUsersByState(String state, Pageable pageable){
        return userRepository.findUsersByPrimaryAddress_State(state, pageable);
    }

    public Page<User> findAllUsersByPostalCode(String zip, Pageable pageable){
        return userRepository.findUsersByPrimaryAddress_PostalCode(zip, pageable);
    }

    public User getSystemUser() throws JdxServiceException {
        Optional<User> systemUser = userRepository.findUserByEmailAndClientId(defaultUserEmail, defaultSystemClientId);
        if(systemUser.isEmpty())
            throw new JdxServiceException("Cannot obtain system user to perform action");

        return systemUser.get();
    }

    @Override
    public User save(User userDto, UserDetailsImpl updater) throws JdxServiceException {
        try {
            userDto.setMeta(buildMeta(updater));
            //authorityRepository.save(userDto.getAuthorities());
            userDto = userRepository.save(userDto);
            if(userDto != null)
                sendUserStatus(userDto, EventType.CREATE);

            return userDto;
        }
        catch(Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot save user ");
        }
    }

    @Override
    public User update(User user, UserDetailsImpl updater) {
        try {
            Optional<User> foundUser = userRepository.findById(user.getId());
            if(foundUser.isPresent()) {
                if(user.getStatus() != null && !foundUser.get().getStatus().equals(user.getStatus()))
                    foundUser.get().setStatus(user.getStatus());
                if(user.getDateOfBirth() != null && !foundUser.get().getDateOfBirth().equals(user.getDateOfBirth()))
                    foundUser.get().setDateOfBirth(user.getDateOfBirth());
                if(user.getPrimaryAddress() != null && !foundUser.get().getPrimaryAddress().equals(user.getPrimaryAddress()))
                    foundUser.get().setPrimaryAddress(user.getPrimaryAddress());
                if(user.getBillingAddress() != null && !foundUser.get().getBillingAddress().equals(user.getBillingAddress()))
                    foundUser.get().setBillingAddress(user.getBillingAddress());
                if(user.getPrimaryPhone() != null && user.getPrimaryPhone().getPhoneNumber() != null && !foundUser.get().getPrimaryPhone().getPhoneNumber().equals(user.getPrimaryPhone().getPhoneNumber()))
                    foundUser.get().setPrimaryPhone(user.getPrimaryPhone());
                if(user.getPreferences() != null && !foundUser.get().getPreferences().equals(user.getPreferences()))
                    foundUser.get().setPreferences(user.getPreferences());
                if(user.getPatientDetails() != null && !foundUser.get().getPatientDetails().equals(user.getPatientDetails()))
                    foundUser.get().setPatientDetails(user.getPatientDetails());
                if(user.getFirstName() != null && !foundUser.get().getFirstName().equals(user.getFirstName()))
                    foundUser.get().setFirstName(user.getFirstName());
                if(user.getLastName() != null && !foundUser.get().getLastName().equals(user.getLastName()))
                    foundUser.get().setLastName(user.getLastName());
                if(user.getStripeCustomerId() != null && !foundUser.get().getStripeCustomerId().equals(user.getStripeCustomerId()))
                    foundUser.get().setStripeCustomerId(user.getStripeCustomerId());
                if(user.getXifinPatientId() != null && !foundUser.get().getXifinPatientId().equals(user.getXifinPatientId()))
                    foundUser.get().setXifinPatientId(user.getXifinPatientId());

                foundUser.get().setMeta(buildMeta(updater));
                //authorityRepository.save(userDto.getAuthorities());
                user = userRepository.save(foundUser.get());

                if(user != null)
                    sendUserStatus(user, EventType.UPDATE);

                return user;
            } else
                throw new JdxServiceException("Cannot update user, user it not found");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot update user " + e.getMessage());
        }
    }

    public PostRegistrationPayload register(RegisterUserPayload user) throws JdxServiceException {
        if(user == null)
            throw new JdxServiceException("Cannot register user as the request payload can't be found");

        User u = null;
        boolean existingNotActivatedUser = false;

        VerificationCode verificationCode = new VerificationCode();
        PostRegistrationPayload response = new PostRegistrationPayload();
        try {
            Optional<User> foundUser = userRepository.findUserByEmailAndClientId(user.getEmail(), user.getClientId());
            if (foundUser.isPresent()) {
                if (foundUser.get().isActivated()) {
                    response.setAlreadyRegistered(true);
                    response.setEmail(user.getEmail());
                    response.setVerificationCodeSent(false);
                    response.setRequiresVerification(false);

                    return response;
                } else {
                    response.setAlreadyRegistered(true);
                    u = foundUser.get();

                    if(!validatePasswordAgainstRules(user.getPassword()))
                        throw new JdxServiceException(UserErrorCodes.PASSWORD_DOES_NOT_ABIDE_RULES.code,
                                UserErrorCodes.PASSWORD_DOES_NOT_ABIDE_RULES.statusCode,
                                UserErrorCodes.PASSWORD_DOES_NOT_ABIDE_RULES.message,
                                "The password entered does not conform the psasword rules (8 or more chars, at least 1 UPPERCASE letter, at least 1 lowercase letter, at least 1 number and at least 1 special character");
                    if(u.getPassword() == null || (u.getPassword() != null && u.getPassword().equals(""))) {
                        u.setPassword(passwordEncoder.encode(user.getPassword()));
                        u = createDefaultAuthorities(u);
                    }
                    if(u.getFirstName() == null)
                        u.setFirstName(user.getFirstName());
                    if(u.getLastName() == null)
                        u.setLastName(user.getLastName());
                    if(u.getClientId() == null)
                        u.setClientId(user.getClientId());

                    existingNotActivatedUser = true;
                }
            } else {
                UserDetailsImpl systemUser = UserDetailsImpl.build(getSystemUser());
                u = new User();
                u.setEmail(user.getEmail());
                u.setUserType(user.getUserType());
                u.setFirstName(user.getFirstName());
                u.setLastName(user.getLastName());
                u.setClientId(user.getClientId());
logger.info("Password is: " + user.getPassword());
                if(!validatePasswordAgainstRules(user.getPassword()))
                    throw new JdxServiceException(UserErrorCodes.PASSWORD_DOES_NOT_ABIDE_RULES.code,
                            UserErrorCodes.PASSWORD_DOES_NOT_ABIDE_RULES.statusCode,
                            UserErrorCodes.PASSWORD_DOES_NOT_ABIDE_RULES.message,
                            "The password entered does not conform the psasword rules (8 or more chars, at least 1 UPPERCASE letter, at least 1 lowercase letter, at least 1 number and at least 1 special character");

                u.setPassword(passwordEncoder.encode(user.getPassword()));
                u = createDefaultAuthorities(u);

                //If the username is not present, create one here
                String[] emailTokens = u.getEmail().split("@");
                if (emailTokens == null || emailTokens.length == 2) {
                    Random rnd = new Random();
                    int number = rnd.nextInt(99999999);
                    u.setUsername("juno_" + emailTokens[0] + String.valueOf(number));
                } else {
                    Random rnd = new Random();
                    int number = rnd.nextInt(999999999);
                    u.setUsername("juno_" + u.getFirstName().toLowerCase(Locale.ROOT) + String.valueOf(number));
                }

                u = save(u, systemUser);

            }
            if (u != null) {
                verificationCode.generateCode();
                verificationCode.setUser(u);
                verificationCode.setCreatedAt(Calendar.getInstance());
                Calendar expiresAt = Calendar.getInstance();
                expiresAt = Calendar.getInstance();
                expiresAt.add(Calendar.HOUR_OF_DAY, verificationCodeExpirationDuration);
                verificationCode.setExpiresAt(expiresAt);

                verificationCode = verificationCodeRepository.save(verificationCode);

                //Generate idToken for user
                String token = null;
                if(existingNotActivatedUser) {
                    response.setAlreadyRegistered(true);
                    if(Calendar.getInstance().getTimeInMillis() - u.getMeta().getCreatedAt().getTimeInMillis() <= 900000) {
                        token = jwtUtils.generateTokenFromUser(u, verificationCodeExpirationDuration * 60 * 60 * 1000);
                        response.setIdToken(token);
                    }
                }
                else {
                    token = jwtUtils.generateTokenFromUser(u, verificationCodeExpirationDuration * 60 * 60 * 1000);
                    response.setIdToken(token);
                    response.setAlreadyRegistered(false);
                }


                if (!mailService.sendEmail(user.getEmail(), "Junodx New Account Verification Code", verificationCode.getCode(), false, false))
                    throw new JdxServiceException("Cannot send verification code");

                response.setRequiresVerification(true);
                response.setEmail(user.getEmail());
                response.setExpiration(verificationCode.getExpiresAt());
                response.setVerificationCodeSent(true);
                response.setCreatedAt(verificationCode.getCreatedAt());

                return response;
            }
        } catch (JdxServiceException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot register user: " + e.getMessage());
        }

        return response;
    }

    public boolean validatePasswordAgainstRules(String password) {
        if(password == null)
            return false;

        if(password.length()>=8)
        {
            Pattern uppercase = Pattern.compile("[A-Z]");
            Pattern lowercase = Pattern.compile("[a-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
            //Pattern eight = Pattern.compile (".{8}");


            Matcher hasUpperCaseLetter = uppercase.matcher(password);
            Matcher hasLowerCaseLetter = lowercase.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasUpperCaseLetter.find() && hasLowerCaseLetter.find() && hasDigit.find() && hasSpecial.find();

        }
        else
            return false;
    }

    public PostRegistrationPayload sendVerificationCode(String email, String clientId) throws  JdxServiceException {
        try {
            PostRegistrationPayload response = new PostRegistrationPayload();
            Optional<User> user = userRepository.findUserByEmailAndClientId(email, clientId);
            if (user.isEmpty())
                throw new JdxServiceException("User with email " + email + " does not exist in the system");

            //Delete previous tokens and create a new one
            verificationCodeRepository.deleteAllByUser_Id(user.get().getId());

            VerificationCode verificationCode = new VerificationCode();
            verificationCode.generateCode();
            verificationCode.setUser(user.get());
            verificationCode.setCreatedAt(Calendar.getInstance());
            Calendar expiresAt = Calendar.getInstance();
            expiresAt = Calendar.getInstance();
            expiresAt.add(Calendar.HOUR_OF_DAY, verificationCodeExpirationDuration);
            verificationCode.setExpiresAt(expiresAt);

            verificationCode = verificationCodeRepository.save(verificationCode);

            if (!mailService.sendEmail(user.get().getEmail(), "Junodx New Account Verification Code", verificationCode.getCode(), false, false))
                throw new JdxServiceException("Cannot send verification code");

            response.setEmail(user.get().getEmail());
            response.setExpiration(verificationCode.getExpiresAt());
            response.setVerificationCodeSent(true);
            response.setCreatedAt(verificationCode.getCreatedAt());

            return response;
        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot resend verification code " +  e.getMessage());
        }
    }

    public PostRegistrationPayload resendVerificationCodeAuthenticated(User user) throws JdxServiceException {
        try {
            PostRegistrationPayload response = new PostRegistrationPayload();
            if (user == null)
                throw new JdxServiceException("Unable to resend verification code, user is not found");

            VerificationCode verificationCode = new VerificationCode();
            verificationCode.generateCode();
            verificationCode.setUser(user);
            verificationCode.setCreatedAt(Calendar.getInstance());
            Calendar expiresAt = Calendar.getInstance();
            expiresAt = Calendar.getInstance();
            expiresAt.add(Calendar.HOUR_OF_DAY, verificationCodeExpirationDuration);
            verificationCode.setExpiresAt(expiresAt);

            verificationCode = verificationCodeRepository.save(verificationCode);

            if (!mailService.sendEmail(user.getEmail(), "Junodx New Account Verification Code", verificationCode.getCode(), false, false))
                throw new JdxServiceException("Cannot send verification code");

            //Generate idToken for user
            String token = jwtUtils.generateTokenFromUser(user, verificationCodeExpirationDuration*60*60*1000);

            response.setEmail(user.getEmail());
            response.setExpiration(verificationCode.getExpiresAt());
            response.setVerificationCodeSent(true);
            response.setIdToken(token);
            response.setRequiresVerification(true);

            return response;
        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot resend verification code " +  e.getMessage());
        }
    }

    public VerificationCodeResponsePayload verify(UserVerificationPayload payload) throws JdxServiceException {
        try {
            VerificationCodeResponsePayload response = new VerificationCodeResponsePayload();
            if (payload.getEmail() == null || payload.getCode() == null)
                throw new JdxServiceException("Cannot verify code as email or the code itself are not present");

            Optional<VerificationCode> verificationCode = verificationCodeRepository.findVerificationCodeByCode(payload.getCode());
            if (verificationCode.isEmpty()) {
                response.setSuccess(false);
            } else {

                if (verificationCode.get().getUser() == null)
                    throw new JdxServiceException("Cannot find user that matches this verification code");

                if (!verificationCode.get().getUser().getEmail().equals(payload.getEmail()))
                    throw new JdxServiceException("Code does match requesting user email address");

                verificationCode.get().getUser().setStatus(UserStatus.ACTIVATED);
                verificationCode.get().getUser().setActivated(true);
                verificationCode.get().getUser().setActivationTs(Calendar.getInstance());

                UserDetailsImpl systemUser = UserDetailsImpl.build(getSystemUser());
                verificationCode.get().getUser().setMeta(updateMeta(verificationCode.get().getUser().getMeta(), systemUser));
logger.info("About to save user from verification code " + mapper.writeValueAsString(verificationCode.get().getUser()));
                User u = userRepository.save(verificationCode.get().getUser());
                if(u != null)
                    sendUserStatus(u, EventType.UPDATE);

                verificationCodeRepository.delete(verificationCode.get());

                response.setUserId(verificationCode.get().getUser().getId());
                response.setSuccess(true);
                response.setEmail(verificationCode.get().getUser().getEmail());

            }
            return response;
        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot verify code for user " + e.getMessage());
        }
    }

    public boolean doesEmailExistForClientId(String email, Optional<String> clientId) throws JdxServiceException {
        try {
     logger.info("Looking for " + email + " @" + clientId.get());
            if(clientId.isPresent()) {
                Optional<User> user = userRepository.findUserByEmailAndClientId(email, clientId.get());
                if(user.isPresent())
                    return true;
            }
            else {
                Optional<User> user = userRepository.findByEmail(email);
                if(user.isPresent())
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot test for existence of email against client id");
        }

        return false;
    }

    public User addRole(String userId, Authority newRole, UserDetailsImpl updater) throws JdxServiceException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty())
            throw new JdxServiceException("Cannot find user to add role to");

        if(user.get().getAuthorities().stream().filter(x->x.getName().equals(newRole.getName())).findAny().isEmpty())
            user.get().addAuthority(newRole);

        user.get().setMeta(updateMeta(user.get().getMeta(), updater));

        return userRepository.save(user.get());
    }

    public User removeRole(String userId, Authority removeRole, UserDetailsImpl updater) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty())
            throw new JdxServiceException("Cannot find user to remove role from");

        Optional<Authority> foundAuthority = user.get().getAuthorities().stream().filter(x->x.getName().equals(removeRole.getName())).findAny();
        if(foundAuthority.isPresent())
            user.get().removeAuthority(foundAuthority.get());
        else
            throw new JdxServiceException("Cannot find authority to remove from user");

        user.get().setMeta(updateMeta(user.get().getMeta(), updater));

        return userRepository.save(user.get());
    }

    public List<Authority> getRoles(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty())
            throw new JdxServiceException("Cannot find user");

        return user.get().getAuthorities();
    }


    public void forgotPassword(UserForgotPasswordPayload payload) throws JdxServiceException {
        ForgotPasswordCode forgotPasswordCode = new ForgotPasswordCode();

        if(payload == null)
            throw new JdxServiceException("Cannot find request payload");

        Optional<User> user = userRepository.findUserByEmailAndClientId(payload.getEmail(), payload.getClientId());
        if(user.isPresent()) {
            forgotPasswordCode.setUser(user.get());
            forgotPasswordCode.setCreatedAt(Calendar.getInstance());
            Calendar expiresAt = Calendar.getInstance();
            expiresAt.add(Calendar.MINUTE, 10);
            forgotPasswordCode.setExpiresAt(expiresAt);
            forgotPasswordCode.setClientId(payload.getClientId());
            String salt = forgotPasswordCode.generateCode();
            String encodedString = payload.getEmail() + ":" + payload.getClientId() + ":" + salt;
            String token = Base64.encodeAsString(encodedString.getBytes());

            forgotPasswordCode.setCode(token);

            forgotPasswordCode = forgotPasswordCodeRepository.save(forgotPasswordCode);

            if (!mailService.sendEmail(user.get().getEmail(), "Junodx Forgot Password", payload.getDestinationUrl() + "?token=" + forgotPasswordCode.getCode(), false, false))
                throw new JdxServiceException("Cannot send forgot password email to customer");
        } else
            throw new JdxServiceException("Cannot send forgot password email to customer");
    }

    public boolean updatePassword(UserUpdatePasswordPayload payload) throws JdxServiceException {
        try {
            Optional<ForgotPasswordCode> code = forgotPasswordCodeRepository.findForgotPasswordCodeByCode(payload.getToken());
            if(code.isEmpty())
                throw new JdxServiceException("Invalid forgot password code, cannot chane password. Please get another password link email");

            if(code.get().getCode().equals(payload.getToken())) {
                byte[] decodedBytes = Base64.decode(payload.getToken());
                String decodedString = new String(decodedBytes);
                String[] tokens = decodedString.split(":");
                if(tokens.length != 3)
                    throw new JdxServiceException("Cannot decode update password token");

                String email = tokens[0];
                String clientId = tokens[1];

                if(code.get().getExpiresAt().before(Calendar.getInstance()))
                    throw new JdxServiceException("Invalid forgot password code. The code has expired, please get another password link email");

                Optional<User> user = userRepository.findUserByEmailAndClientId(email, clientId);
                if(user.isEmpty())
                    throw new JdxServiceException("Cannot find user to update password for");

                user.get().setPassword(passwordEncoder.encode(payload.getPassword()));

                UserDetailsImpl systemUser = UserDetailsImpl.build(getSystemUser());

                user.get().setMeta(updateMeta(user.get().getMeta(), systemUser));
                User u = userRepository.save(user.get());
                if(u != null) {
                    forgotPasswordCodeRepository.delete(code.get());
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot update user's password " + e.getMessage());
        }
        throw new JdxServiceException("Cannot update user's password");
    }

    public boolean changePassword(ChangePasswordPayload payload, UserDetailsImpl updater) throws JdxServiceException {
        try {
            if(payload.getUserId() == null || payload.getExistingPassword() == null || payload.getNewPassword() == null)
                throw new JdxServiceException("Cannot change password, must include userId, existing and new passwords");

            Optional<User> user = userRepository.findById(payload.getUserId());
            if (user.isEmpty())
                throw new JdxServiceException("Cannot find user to update password for");

            if(passwordEncoder.matches(payload.getExistingPassword(), user.get().getPassword())) {
                user.get().setPassword(passwordEncoder.encode(payload.getNewPassword()));

                user.get().setMeta(updateMeta(user.get().getMeta(), updater));
                User u = userRepository.save(user.get());
                if (u != null) {
                    return true;
                }
            } else
                throw new JdxServiceException("Existing password does not match password on file, cannot change password");
        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot update user's password " + e.getMessage());
        }
        return false;
    }

    public EmailChangePayload startEmailUpdate(String userId, String newEmail, String clientId, UserDetailsImpl updater) throws JdxServiceException {
        try {
            if (userId == null)
                throw new JdxServiceException("Cannnot change email address because userId is not provided");

            if (newEmail == null)
                throw new JdxServiceException("Cannot change email address since the email to change to is not provided");

            if (clientId == null)
                throw new JdxServiceException("Cannot change email address since the clientId is not specified");

            //Find the original user first
            Optional<User> userToUpdate = userRepository.findById(userId);
            Optional<User> anyExistingUsersWithEmail = userRepository.findUserByEmailAndClientId(userId, clientId);

            if(userToUpdate.isEmpty())
                throw new JdxServiceException("Cannot find user to update with id: " + userId);

            if(anyExistingUsersWithEmail.isPresent())
                throw new JdxServiceException("Cannot change to the email address " + newEmail + " as another account is using that address already");

            //Create Email change record

            //Send verification code
            VerificationCode verificationCode = new VerificationCode();

            verificationCode.generateCode();
            verificationCode.setUser(userToUpdate.get());
            verificationCode.setCreatedAt(Calendar.getInstance());
            Calendar expiresAt = Calendar.getInstance();
            expiresAt = Calendar.getInstance();
            expiresAt.add(Calendar.HOUR_OF_DAY, verificationCodeExpirationDuration);
            verificationCode.setExpiresAt(expiresAt);

            verificationCode = verificationCodeRepository.save(verificationCode);

            EmailChange emailChange = new EmailChange();
            emailChange.setExistingEmail(userToUpdate.get().getEmail());
            emailChange.setNewEmail(newEmail);
            emailChange.setUserId(userId);
            emailChange.setCreatedAt(Calendar.getInstance());
            Calendar expires = Calendar.getInstance();
            expires.add(Calendar.HOUR_OF_DAY, verificationCodeExpirationDuration);
            emailChange.setExpiresAt(expires);

            emailChange = emailChangeRepository.save(emailChange);

            if (!mailService.sendEmail(newEmail, "Junodx Email change verification code", verificationCode.getCode(), false, false))
                throw new JdxServiceException("Cannot send verification code");

            EmailChangePayload response = new EmailChangePayload();
            response.setExistingEmail(userToUpdate.get().getEmail());
            response.setChangeToEmail(newEmail);
            response.setUserId(userId);
            response.setVerificationCodeExpiresAt(verificationCode.getExpiresAt());
            response.setVerificationCodeSent(true);

            return response;

        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot modify the customer's email address due to: " + e.getMessage());
        }
    }


    public User finishEmailUpdate(UserVerificationPayload payload, UserDetailsImpl updater) throws JdxServiceException {
        try {
            User response = null;
            if (payload.getEmail() == null || payload.getCode() == null)
                throw new JdxServiceException("Cannot verify code as email or the code itself are not present");

            if(payload.getClientId() == null)
                throw new JdxServiceException("Must provide the client Id for the user to modify");


            Optional<VerificationCode> verificationCode = verificationCodeRepository.findVerificationCodeByCode(payload.getCode());

            if (verificationCode.isEmpty()) {
                throw new JdxServiceException("Cannot verify email change for user as the code is invalid or expired");
            } else {
                if(verificationCode.get().getUser() != null) {
                    if(verificationCode.get().getUser().getId() != null) {
                        Optional<EmailChange> emailChangeData = emailChangeRepository.findEmailChangeByUserId(verificationCode.get().getUser().getId());
                        if(emailChangeData.isEmpty())
                            throw new JdxServiceException("Cannot find email change data to modify email address to");

                        if(emailChangeData.get().getExpiresAt().getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
                            throw new JdxServiceException("Cannot change email address as the timeframe to accept it has expired. Please start the email change process again");

                        if(!payload.getEmail().equals(emailChangeData.get().getExistingEmail()))
                            throw new JdxServiceException("Email provided in verification does not match the existing email address on file for this user");

                        Optional<User> user = userRepository.findById(verificationCode.get().getUser().getId());
                        if(user.isEmpty())
                            throw new JdxServiceException("Cannot find user to modify email for");

                        if(!payload.getClientId().equals(user.get().getClientId()))
                            throw new JdxServiceException("Client id provided in email change verification does not match that for this user");

                        if(emailChangeData.get().getNewEmail() != null && !emailChangeData.get().getNewEmail().equals(emailChangeData.get().getExistingEmail())) {
                            user.get().setEmail(emailChangeData.get().getNewEmail());

                            user.get().setMeta(updateMeta(user.get().getMeta(), updater));

                            User u = userRepository.save(user.get());

                            if(u != null) {
                                sendUserStatus(u, EventType.UPDATE);

                                return u;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new JdxServiceException("Cannot verify email change code for user");
        }

        throw new JdxServiceException("Cannot process email change");
    }


    //Use Json node-based processing as auto-serializing a User object will cause defaults to be set which is undesired behavior
    public User update(UserUpdatePayload user, UserDetailsImpl updater) throws JdxServiceException {
        try {
             if (user != null && user.getId() != null) {
                Optional<User> updateO = userRepository.findById(user.getId());

                if(updateO.isEmpty())
                    throw new JdxServiceException("Cannot find user at " + user.getId() + " to update");

                //PatientDetails patientDetails = user.getPatientDetails();

                    if (StringUtils.isNotBlank(user.getFirstName()) && !user.getFirstName().equals(updateO.get().getFirstName()))
                        updateO.get().setFirstName(user.getFirstName());

                    if (StringUtils.isNotBlank(user.getLastName()) && !user.getLastName().equals(updateO.get().getLastName()))
                        updateO.get().setLastName(user.getLastName());

                    if (StringUtils.isNotBlank(user.getDateOfBirth()) && !user.getDateOfBirth().equals(updateO.get().getDateOfBirth()))
                        updateO.get().setDateOfBirth(user.getDateOfBirth());

                    if(user.getPrimaryPhone() != null)
                        updatePrimaryPhone(updateO.get(), user.getPrimaryPhone());

                    if (user.getPrimaryAddress() != null) {
                        if(updateO.get().getPrimaryAddress() == null)
                            updateO.get().setPrimaryAddress(new Address());

                        updateUserAddress(updateO.get().getPrimaryAddress(), user.getPrimaryAddress());
                    }

                    if (user.getBillingAddress() != null) {
                        if(updateO.get().getBillingAddress() == null)
                            updateO.get().setBillingAddress(new Address());
                        updateUserAddress(updateO.get().getBillingAddress(), user.getBillingAddress());
                    }

                    if (StringUtils.isNotBlank(user.getStripeCustomerId()) && !user.getStripeCustomerId().equals(updateO.get().getStripeCustomerId()))
                        updateO.get().setStripeCustomerId(user.getStripeCustomerId());

                    if (user.getPatientDetails() != null)
                        updatePatientDetails(updateO.get().getPatientDetails(), user.getPatientDetails());

                    if(user.getPreferences() != null)
                        updatePreferenceDetails(updateO.get(), user.getPreferences(), updater);


                    /*
                    List<UserDevice> updatedDevices = new ArrayList<>();
                    if (update.getDevices() == null)
                        update.setDevices(user.getDevices());
                    else if (user.getDevices() != null) {
                        List<UserDevice> devices = new ArrayList<>(user.getDevices());
                        for(UserDevice device : devices) {
                            Optional<UserDevice> d = update.getDevices().stream().filter(x->x.getDeviceId().equals(device.getDeviceId())).findFirst();
                            if(d.isPresent()) {
                                update.getDevices().remove(d.get());
                                if(device.getDeviceInfo() != null) d.get().setDeviceInfo(device.getDeviceInfo());
                                if(device.getBrowserInfo() != null) d.get().setBrowserInfo(device.getBrowserInfo());
                                d.get().setLastAccessFrom(Calendar.getInstance());
                                d.get().setMeta(buildMeta(updater));
                                d.get().setUser(update);
                               // updatedDevices.add(d.get());
                            } else {
                                update.addDevice(d.get());
                                //updatedDevices.add(d.get());
                            }
                        }
                    }

                     */

                    updateO.get().setMeta(updateMeta(updateO.get().getMeta(), updater));

                    //userDeviceRepository.saveAll(updatedDevices);

                    User u = userRepository.save(updateO.get());

                    //Optional<PatientDetails> updateDetails = patientDetailsService.update(user, patientDetails, updater);

                    //if(updateDetails.isPresent())
                    //    user.setPatientDetails(updateDetails.get());

//logger.info("Updated user is: " + mapper.writeValueAsString(u));

               //     log(LogCode.RESOURCE_UPDATE, "Updated a User " + user.getId(), updater);

                    //Send up to SNS
                /*
                    try {
                        sendUserStatus(u, EventType.UPDATE);
                    } catch (Exception e) {
                        logger.info("Failed to send update message for user " + user.getEmail() + " during /authorize");
                    }

                 */

                    return u;
            }
        } catch (Exception e){
            log(LogCode.RESOURCE_UPDATE_ERROR, "Failed to update user " + user.getId(), updater);
            e.printStackTrace();
            throw new JdxServiceException("Failed to update user");
        }
        throw new JdxServiceException("Failed to update user");
    }

    public User updatePreferences(String userId, UserUpdatePreferencesPayload newPreferences, UserDetailsImpl updater) throws JdxServiceException {
        if(newPreferences == null)
            throw new JdxServiceException("Cannot updated preferences, user payload is missing");

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty())
            throw new JdxServiceException("Cannot find user to update preferences for");

        User u = updatePreferenceDetails(user.get().getPreferences().getUser(), newPreferences, updater);

        return u;
    }

    public User updatePreferenceDetails(User user, UserUpdatePreferencesPayload preferences, UserDetailsImpl updater) throws JdxServiceException {
        if(preferences == null)
            throw new JdxServiceException("Cannot updated preferences, user payload is missing");



        if(user.getPreferences() == null){
            Preferences pref = new Preferences();
            user.setPreferences(pref);
            pref.setUser(user);
        }

        if(preferences.getFstPreferences() != null){
            if(preferences.getFstPreferences() == null) {
                FetalSexResultsPreferences resultsPreferences = new FetalSexResultsPreferences();
                user.getPreferences().setFstPreferences(resultsPreferences);
            }

            if(preferences.getFstPreferences().getGenderDelegated() != null && !preferences.getFstPreferences().getGenderDelegated().equals(user.getPreferences().getFstPreferences().isGenderDelegated()))
                user.getPreferences().getFstPreferences().setGenderDelegated(preferences.getFstPreferences().getGenderDelegated());

            if(preferences.getFstPreferences().getFstResultsDelegatedEmail() != null && !preferences.getFstPreferences().getFstResultsDelegatedEmail().equals(user.getPreferences().getFstPreferences().getFstResultsDelegatedEmail()))
                user.getPreferences().getFstPreferences().setFstResultsDelegatedEmail(preferences.getFstPreferences().getFstResultsDelegatedEmail());

            if(preferences.getFstPreferences().getGenderTerms() != null && !preferences.getFstPreferences().getGenderTerms().equals(user.getPreferences().getFstPreferences().getGenderTerms()))
                user.getPreferences().getFstPreferences().setGenderTerms(preferences.getFstPreferences().getGenderTerms());

            if(preferences.getFstPreferences().getGenderFanfare() != null && !preferences.getFstPreferences().getGenderFanfare().equals(user.getPreferences().getFstPreferences().isGenderFanfare()))
                user.getPreferences().getFstPreferences().setGenderFanfare(preferences.getFstPreferences().getGenderFanfare());
        }

        if(preferences.getSmsMessages() != null && !preferences.getSmsMessages().equals(user.getPreferences().isSmsMessages()))
            user.getPreferences().setSmsMessages(preferences.getSmsMessages());

        if(preferences.getOptOut() != null && !preferences.getOptOut().equals(user.getPreferences().isOptOut()))
            user.getPreferences().setOptOut(preferences.getOptOut());


        user.setMeta(updateMeta(user.getMeta(), updater));

        return user;
    }

    public User archive(String id) throws JdxServiceException {
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty())
            throw new JdxServiceException("Cannot archive user at id: " + id + " as the user is not found");

        user.get().setStatus(UserStatus.ARCHIVED);

        User u = userRepository.save(user.get());
        if(u != null)
            sendUserStatus(u, EventType.ARCHIVE);

        return u;
    }

    /*
    public PatientChart upsertChartEntry(String userId, PatientChartEntry updateChartEntry, UserDetailsImpl updater) throws JdxServiceException {
        try {
            if(userId == null || updateChartEntry == null)
                throw new JdxServiceException("Cannot update chart since neither the patient or chart details are submitted");
            Optional<User> patient = userService.findOne(userId);
            if(patient.isEmpty())
                throw new JdxServiceException("Cannot find patient to update chart for, provided id " + userId);

            if(patient.get().getPatientDetails() == null) {
                PatientDetails patientDetails = new PatientDetails();
                patientDetails.setUser(patient.get());
                patient.get().setPatientDetails(patientDetails);
            }

            if(patient.get().getPatientDetails().getChart() == null){
                PatientChart chart = new PatientChart();
                chart.setPatientDetails(patient.get().getPatientDetails());
                patient.get().getPatientDetails().setChart(chart);
            }

            Optional<PatientChartEntry> existingChart = patient.get().getPatientDetails().getChart().getChartEntries().stream().filter(x->x.getId().equals(updateChartEntry.getId())).findAny();
            if(existingChart.isEmpty()) {
                updateChartEntry.setPatientChart(patient.get().getPatientDetails().getChart());
                patient.get().getPatientDetails().getChart().addChartEntry(updateChartEntry);
            } else {
                PatientChartEntry entry = existingChart.get();
                if(StringUtils.isNotBlank(updateChartEntry.getAuthorEmail()) && !updateChartEntry.getAuthorEmail().equals(entry.getAuthorEmail()))
                    entry.setAuthorEmail(updateChartEntry.getAuthorEmail());

                if(StringUtils.isNotBlank(updateChartEntry.getAuthorName()) && !updateChartEntry.getAuthorName().equals(entry.getAuthorName()))
                    entry.setAuthorName(updateChartEntry.getAuthorName());

                if(StringUtils.isNotBlank(updateChartEntry.getTitle()) && !updateChartEntry.getTitle().equals(entry.getTitle()))
                    entry.setTitle(updateChartEntry.getTitle());

                if(StringUtils.isNotBlank(updateChartEntry.getNote()))
                    entry.setNote(updateChartEntry.getNote());

                if(updateChartEntry.getTimestamp() != null)
                    entry.setTimestamp(updateChartEntry.getTimestamp());
            }

            //TODO update this to use userService.save or update accordingly, but may want to do the save
            // at the patientDetails level and not user, and also need to send out SNS messages or not
            User user = userRepository.save(patient.get());
            if(user != null)
                return user.getPatientDetails().getChart();

        } catch (Exception e) {
            throw new JdxServiceException("Cannot update chart due to " + e.getMessage());
        }

        throw new JdxServiceException("Cannot update chart");
    }

     */

    public PatientChartEntry handleChart(String userId, PatientChartEntry entry, UserDetailsImpl updater) throws JdxServiceException {
        try {
            PatientDetails newPatientDetails = null;
            if(userId == null || entry == null)
                throw new JdxServiceException("Cannot update chart since neither the patient or chart details are submitted");
            Optional<User> user = userRepository.findById(userId);
            if(user.isEmpty())
                throw new JdxServiceException("Cannot find patient to update chart for, provided id " + userId);
            //Optional<PatientDetails> patientDetails = patientDetailsRepository.findPatientDetailsByUser_Id(userId);

            PatientDetails patientDetails = user.get().getPatientDetails();
            if (patientDetails == null) {
                newPatientDetails = new PatientDetails();
                user.get().setPatientDetails(newPatientDetails);
                newPatientDetails.setUser(user.get());
            }
            else
                newPatientDetails = patientDetails;

            PatientChart chart = newPatientDetails.getChart();
            if(chart == null) {
                chart = new PatientChart();
                newPatientDetails.setChart(chart);
                chart.setPatientDetails(newPatientDetails);
            }

            if (chart != null) {
                PatientChartEntry finalEntry = entry;
                if (chart.getChartEntries().stream().filter(x -> x.getId().equals(finalEntry.getId())).findFirst().isEmpty()) {
                    logger.info("New chart to create");

                    entry.setPatientChart(chart);
                    chart.addChartEntry(entry);

                    entry = patientChartEntryRepository.save(entry);
                } else {
                    logger.info("Have an existing chart, so updating");

                    Optional<PatientChartEntry> foundEntry = chart.getChartEntries().stream().filter(x -> x.getId().equals(finalEntry.getId())).findFirst();

                    if(StringUtils.isNotBlank(entry.getAuthorEmail()) && !entry.getAuthorEmail().equals(foundEntry.get().getAuthorEmail()))
                        foundEntry.get().setAuthorEmail(entry.getAuthorEmail());

                    if(StringUtils.isNotBlank(entry.getAuthorName()) && !entry.getAuthorName().equals(foundEntry.get().getAuthorName()))
                        foundEntry.get().setAuthorName(entry.getAuthorName());

                    if(StringUtils.isNotBlank(entry.getTitle()) && !entry.getTitle().equals(foundEntry.get().getTitle()))
                        foundEntry.get().setTitle(entry.getTitle());

                    if(StringUtils.isNotBlank(entry.getNote()))
                        foundEntry.get().setNote(entry.getNote());

                    if(entry.getTimestamp() != null)
                        foundEntry.get().setTimestamp(entry.getTimestamp());

                    entry = patientChartEntryRepository.save(foundEntry.get());
                }
                user.get().setMeta(updateMeta(user.get().getMeta(), updater));

                userRepository.save(user.get());
            }

            return entry;
        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Unable to handle chart" + e.getMessage());
        }
    }

    public PatientChartEntry getChartEntry(String userId, String chartEntryId) throws JdxServiceException {
        try {
            Optional<User> user = userRepository.findById(userId);
            if(user.isEmpty())
                throw new JdxServiceException("User not found when retrieving chart: " + userId);

            PatientDetails patientDetails = user.get().getPatientDetails();
            if (patientDetails != null) {
                PatientChart chart = patientDetails.getChart();
              logger.info("Has patient details");
                if (chart != null) {
                    logger.info("Has a chart");
                    if (chartEntryId != null) {
                        List<PatientChartEntry> chartEntries = new ArrayList<>();
                        Optional<PatientChartEntry> entry = chart.getChartEntries().stream().filter(x -> x.getId().equals(chartEntryId)).findFirst();
                        if (entry.isPresent()) {
                            logger.info("Found the chart entry");
                            return entry.get();
                        }
                    }

                }
            }

        } catch(Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Canonot obtain user's chart: " + e.getMessage());
        }
        throw new JdxServiceException("User " + userId + " does not yet have a chart");
    }

    public PatientChart getChart(String userId) throws JdxServiceException {
        try {
            Optional<User> user = userRepository.findById(userId);
            if(user.isEmpty())
                throw new JdxServiceException("User not found when retrieving chart: " + userId);

            PatientDetails patientDetails = user.get().getPatientDetails();
            if (patientDetails != null) {
                PatientChart chart = patientDetails.getChart();
                if (chart != null)
                   return chart;
            }

        } catch(Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Canonot obtain user's chart: " + e.getMessage());
        }
        throw new JdxServiceException("User " + userId + " does not yet have a chart");
    }

    @Override
    //Use the delete methods only extreme cases, otherwise everything should be archived
    public void delete(String id) {
        Optional<User> u = userRepository.findById(id);

        if(u.isPresent()){
            User user = u.get();

            refreshTokenRepository.deleteByUser(user);
            userRepository.deleteById(id);

            //Send to SNS
            //sendUserStatus(u.get(), EventType.ARCHIVE);
        }
    }

    @Override
    public void deleteByUserId(String uuid) {
        delete(uuid);
    }

    public User loadAdditionalData(User user, String[] includes){
        if(includes != null && includes.length > 0) {
            if (Arrays.stream(includes).anyMatch(s -> s.equals("patient") || s.equals("all"))) {
                Optional<PatientDetails> patient = patientDetailsService.getByUserId(user.getId(), new String[]{"all"});
                if(patient.isPresent())
                    user.setPatientDetails(patient.get());
            }
        }

        return user;

    }

    public User createDefaultAuthorities(User user) throws JdxServiceException {
        List<Authority> authorities = new ArrayList<>();
        ClientTypes client = null;

        if(user == null || user.getClientId() == null)
            throw new JdxServiceException("Cannot resolve user to create authorities");

        if(user.getClientId().equals(patientPortalClientId))
            client = ClientTypes.PATIENT;
        else if(user.getClientId().equals(labPortalClientId))
            client = ClientTypes.LAB;
        else if(user.getClientId().equals(providerPortalClientId))
            client = ClientTypes.PROVIDER;
        else
            throw new JdxServiceException("Cannot find client id to assign role to new user");


        switch(client) {
            case PATIENT:
                Authority authority = new Authority(Authority.ROLE_PATIENT);
                //Authority authority = new Authority(Authority.ROLE_ADMIN);
                authority.setUser(user);
                user.addAuthority(authority);
                break;
            case LAB:
                Authority authority2 = new Authority(Authority.ROLE_ADMIN);
                //Authority authority2 = new Authority(Authority.ROLE_ADMIN);
                authority2.setUser(user);
                user.addAuthority(authority2);
                break;
            case PROVIDER:
                Authority authority3 = new Authority(Authority.ROLE_PRACTICE_REP);
                //Authority authority3 = new Authority(Authority.ROLE_ADMIN);
                authority3.setUser(user);
                user.addAuthority(authority3);
                break;
            default:
                throw new JdxServiceException("Cannot resolve client to assign default ROLE");
        }

        return user;
    }

    @Async
    public void sendUserStatus(User o, EventType eventType) throws JdxServiceException {
        try {
            //Send SNS a message that this order was created
            //TODO is there a non-blocking way to do this?
            if (o != null && !snsDisabled) {
                EntityPayload msg = new EntityPayload();
                msg.setEntity(o);
                msg.setEvent(eventType);
                msg.setEventTs(Calendar.getInstance());
                msg.setStatus(o.getStatus().name());
                SnsMessageResponse response = SnsMessageHandler.sendSnsMessage(userTopic, msg);
                logger.info("Sending message to SNS : " +mapper.writeValueAsString(msg));
                if(response.getResponse().sequenceNumber() == null)
                    throw new JdxServiceException("Failed to send SNS message");
                else
                    logger.info("SNS response for user update is: " + response.getResponse().toString());
            } else
                logger.info("Updated to SNS disabled for event of type: " + eventType );
        } catch(Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot send SNS message for user change: " + e.getMessage());
        }
    }

    /*
    {
    "recordsChanged": [{
            "id": "00T03000009zOKBEA2",
            "changedDataMap": {
                "Finalize_Chart_Entry__c": true,
                "SystemModstamp": "2022-06-10T20:02:20.000Z",
                "LastModifiedDate": "2022-06-10T20:02:20.000Z"
            }
        }
    ],
    "recordInfo": [{
            "attributes": {
                "type": "Task",
                "url": "/services/data/v55.0/sobjects/Task/00T03000009zOKBEA2"
            },
            "Id": "00T03000009zOKBEA2",
            "Test_Result__c": "a1Y03000001zlpJEAQ",
            "WhoId": "0030300000P9ucaAAB",
            "CreatedById": "0055f000005Lv15AAC",
            "Description": "Condition: This is a test chart\r\n\r\nSymptoms:\r\n\r\nDuration:\r\n\r\nDetails:",
            "Test_Result__r": {
                "attributes": {
                    "type": "TestResult__c",
                    "url": "/services/data/v55.0/sobjects/TestResult__c/a1Y03000001zlpJEAQ"
                },
                "Juno_Id__c": "1234",
                "Id": "a1Y03000001zlpJEAQ"
            },
            "Who": {
                "attributes": {
                    "type": "Contact",
                    "url": "/services/data/v55.0/sobjects/Contact/0030300000P9ucaAAB"
                },
                "Name": "Seymour Buttz",
                "JunoUserId__c": "123",
                "Id": "0030300000P9ucaAAB"
            }
        }
    ],
    "objectChanged": "Task"
}

@JsonProperty("Description")
    @JsonProperty("Whoid")
    @JsonProperty("Contact.Name")
    @JsonProperty("Contact.JunoUserId__c")
    @JsonProperty("Test_Result__r.Juno_id__c")
     */
    public void updateChartFromSalesforce(List<SalesforceRecordChanged> records, List<SalesforceChartUpdateInfo> recordInfoList, UserDetailsImpl userContext) throws JdxServiceException {
        int userUpdate = 0;
        try {
            for (SalesforceRecordChanged record : records) {
            logger.info("Record: " + mapper.writeValueAsString(record));
                //Find an existing chart mapped by the SalesforceId
                Optional<SalesforceChartUpdateInfo> recordInfo = recordInfoList.stream().filter(x -> x.getId().equals(record.getId())).findAny();
                if (recordInfo.isEmpty()) {
                    logger.info("Cannot find mapping for record id " + record.getId());
                    continue;
                }

                logger.info("RecordInfo: " + mapper.writeValueAsString(recordInfo.get()));

                if(recordInfo.get().getWho().getJunoUserId() == null)
                    logger.info("Cannot find Juno user Id for record with id " + record.getId());

                Optional<User> user = userRepository.findById(recordInfo.get().getWho().getJunoUserId());
                if (user == null && user.isEmpty()) {
                    logger.error("user not found with the id {} ", record.getId());
                    continue;
                }


                PatientChart chart = null;
                if(user.get().getPatientDetails() == null)
                    user.get().setPatientDetails( new PatientDetails());

                if(user.get().getPatientDetails() != null) {
                    chart = user.get().getPatientDetails().getChart();
                    if(chart == null) {
                        chart = new PatientChart();
                        chart.setPatientDetails(user.get().getPatientDetails());
                        user.get().getPatientDetails().setChart(chart);
                        logger.info("ADding a chart object");
                    }
                }

                PatientChartEntry chartEntry = null;
                boolean existingChartEntry = false;


                SalesforceChangedDataMap data = record.getChangedDataMap();
                if (data != null) {
                    logger.info("Data is not null " + mapper.writeValueAsString(data));
                    if(data.getId() != null) {
                 logger.info("Data has an id and about process");
                        Optional<PatientChartEntry> patientChartEntry = chart.getChartEntries().stream().filter(x->x.getSalesforceId().equals(data.getId())).findAny();
                        if(patientChartEntry.isPresent()) {
                  logger.info("Found a chart and updating");
                            chartEntry = patientChartEntry.get();
                            existingChartEntry = true;
                        } else {
                  logger.info("Creating and adding a new chart");
                            chartEntry = new PatientChartEntry();
                            chartEntry.setSalesforceId(data.getId());
                            chartEntry.setTimestamp(Calendar.getInstance());
                            chartEntry.setPatientChart(chart);
                            chart.addChartEntry(chartEntry);
                        }


                    }
                    //TODO add processing of changedDataMap entries here
                    if(data.getDescription() != null) {
                        if(existingChartEntry && chartEntry.getNote() != null && !chartEntry.getNote().equals(data.getDescription()))
                           chartEntry.setNote(data.getDescription());
                        else if(!existingChartEntry)
                            chartEntry.setNote(data.getSalesforceAccountId());
                    }
                    //Get details about the patient
                    /*
                    if(data.getSalesforceAccountId() != null) {
                        if (existingChartEntry && chartEntry.getSalesforceId() != null && !chartEntry.getSalesforceId().equals(data.getSalesforceAccountId()))
                            logger.info("Cannot change the ownership of a chart record to a different user");
                    }

                     */

                    /*
                    if(data.getTestResult() != null) {
                        if (existingChartEntry && chartEntry.getRelatedTestReportId() != null && !chartEntry.getRelatedTestReportId().equals(data.getTestResult().getJunoResultId()))
                            chartEntry.setRelatedTestReportId(data.getTestResult().getJunoResultId());
                        else if(!existingChartEntry)
                            chartEntry.setRelatedTestReportId(data.getTestResult().getJunoResultId());

                        if (existingChartEntry && chartEntry.getRelatedTestReportSalesforceId() != null && !chartEntry.getRelatedTestReportSalesforceId().equals(data.getTestResult().getSalesforceResultId()))
                            chartEntry.setRelatedTestReportSalesforceId(data.getTestResult().getSalesforceResultId());
                        else if(!existingChartEntry)
                            chartEntry.setRelatedTestReportSalesforceId(data.getTestResult().getSalesforceResultId());
                    }

                     */
                    logger.info("Working with user " + mapper.writeValueAsString(user.get()));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot update charts due to an exception: " + e.getMessage());
        }
    }


    public void updateUserFromSalesforce(List<SalesforceRecordChanged> records, List<SalesforceUserUpdateAccountInfo> recordInfoList, UserDetailsImpl userContext) throws JdxServiceException {
        int userUpdate = 0;
        try {
            for (SalesforceRecordChanged record : records) {
                Optional<SalesforceUserUpdateAccountInfo> recordInfo = recordInfoList.stream().filter(x -> x.getId().equals(record.getId())).findAny();
                if (recordInfo.isEmpty()) {
                    logger.info("Cannot find mapping for record id " + record.getId());
                    continue;
                }

                Optional<User> user = userRepository.findById(recordInfo.get().getJunoUserId());
                if (user == null && user.isEmpty()) {
                    logger.error("user not found with the id {} ", record.getId());
                    continue;
                }
           logger.info("Have Salesforce record and user " + mapper.writeValueAsString(record) + " for " + user.get().getId());
                SalesforceChangedDataMap data = record.getChangedDataMap();
                if (data != null) {
                    if(data.getBillingAddress() != null)
                        updateUserBillingAddress(user.get(), data.getBillingAddress());

                    //Update DOB
                    if (StringUtils.isNotBlank(data.getDateOfBirth())) {
                        //validate if the value is in date format
                        //Calendar dob = DateUtil.convertStringToCalendar(data.getDateOfBirth(), DateUtil.DATE_FORMAT_YYYY_MM_DD);
                        if (data.getDateOfBirth() != null) {
                            user.get().setDateOfBirth(data.getDateOfBirth());
                        }
                    }

                    //Update email - TODO need a process to change this so that the user gets a verification at the new email address
                 //   if (StringUtils.isNotBlank(data.getEmail())) {
                 //       user.get().setEmail(data.getEmail());
                 //   }
                  /*
                  //CSR can't activate
                    if (data.getActive() != null) {
                        user.get().setActivated(data.getActive());
                    }

                   */

                    //Update first or last name
                    if (StringUtils.isNotBlank(data.getFirstName()))
                        user.get().setFirstName(data.getFirstName());

                    if (StringUtils.isNotBlank(data.getLastName()))
                        user.get().setLastName(data.getLastName());

                    /*
                    if (StringUtils.isNotBlank(data.getName())) {
                        String[] name = StringUtils.split(data.getName());
                        if (name.length == 1) {
                            user.get().setFirstName(name[0]);
                        } else if (name.length > 1) {
                            user.get().setFirstName(name[0]);
                            String lastName = "";
                            for (int i = 0; i < name.length; i++) {
                                if (i != 0) {
                                    lastName = lastName + " " + name[i];
                                }
                            }
                            user.get().setLastName(lastName.strip());
                        }
                    }

                     */

                    //Update preferences
                    //do we need to create new preferences if having null?
                    if(user.get().getPreferences() == null){
                        Preferences newPreferences = new Preferences();
                        newPreferences.setFstPreferences(new FetalSexResultsPreferences());
                        user.get().setPreferences(newPreferences);
                    }

                    if (data.getOptOut() != null && user.get().getPreferences() != null) {
                        user.get().getPreferences().setOptOut(data.getOptOut());
                    }

                    //Update phone
                    if (StringUtils.isNotBlank(data.getPhone())) {
                        Phone phone = user.get().getPrimaryPhone();
                        if (phone == null) {
                            phone = new Phone();
                            user.get().setPrimaryPhone(phone);
                        }
                        user.get().getPrimaryPhone().setPhoneNumber(data.getPhone());
                    }

                    //Update address
                    if (data.getPrimaryAddress() != null) {
                        updateUserAddress(user.get().getPrimaryAddress(), data.getPrimaryAddress());
                    }

                    /*
                    //Can't update the user status by hand
                    if (StringUtils.isNotBlank(data.getUserStatus())) {
                        UserStatus userStatus = UserStatus.getEnum(data.getUserStatus());
                        if (userStatus != null) {
                            user.get().setStatus(userStatus);
                        } else {
                            logger.info("invalid user status in the payload {} ", data.getUserStatus());
                            continue;
                        }
                    }

                     */

                    logger.info("updating user {} ", user.get().getId());
                    Meta.setMeta(user.get().getMeta(), userContext);
                    userRepository.save(user.get());
                    userUpdate++;
                }
            }
            logger.info("Updated {} users from salesforce", userUpdate);
        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot update user properly from Salesforce: " + e.getMessage());
        }
    }

    private void updateUserBillingAddress(User user, SFBillingAddress billingAddress) {
        if(user.getBillingAddress() == null)
            user.setBillingAddress(new Address());

        if (billingAddress != null) {
            Address address = user.getBillingAddress();
            if (address == null) {
                address = new Address();
                user.setBillingAddress(address);
            }
            if (StringUtils.isNotBlank(billingAddress.getBillingCity()) && !billingAddress.getBillingCity().equals(address.getCity())) {
                address.setCity(billingAddress.getBillingCity());
            }
            if (StringUtils.isNotBlank(billingAddress.getBillingCountry()) && !billingAddress.getBillingCountry().equals(address.getCountry())) {
                address.setCountry(billingAddress.getBillingCountry());
            }
            if (StringUtils.isNotBlank(billingAddress.getBillingName()) && !billingAddress.getBillingName().equals(address.getName())) {
                address.setName(billingAddress.getBillingName());
            }
            if (StringUtils.isNotBlank(billingAddress.getBillingPostalCode()) && !billingAddress.getBillingPostalCode().equals(address.getPostalCode())) {
                address.setPostalCode(billingAddress.getBillingPostalCode());
            }
            if (StringUtils.isNotBlank(billingAddress.getBillingState()) && !billingAddress.getBillingState().equals(address.getState())) {
                address.setState(billingAddress.getBillingState());
            }
            if (StringUtils.isNotBlank(billingAddress.getBillingStreet()) && !billingAddress.getBillingStreet().equals(address.getStreet())) {
                address.setStreet(billingAddress.getBillingStreet());
            }
        }
    }

    private void updateUserAddress(Address updateAddress, Address addr) {
        if (addr != null) {
            Address address = updateAddress;
            if (StringUtils.isNotBlank(addr.getCity()) && !addr.getCity().equals(address.getCity())) {
                address.setCity(addr.getCity());
            }
            if (StringUtils.isNotBlank(addr.getCountry()) && !addr.getCountry().equals(address.getCountry())) {
                address.setCountry(addr.getCountry());
            }
            if (StringUtils.isNotBlank(addr.getName()) && !addr.getName().equals(address.getName())) {
                address.setName(addr.getName());
            }
            if (StringUtils.isNotBlank(addr.getPostalCode()) && !addr.getPostalCode().equals(address.getPostalCode())) {
                address.setPostalCode(addr.getPostalCode());
            }
            if (StringUtils.isNotBlank(addr.getState()) && !addr.getState().equals(address.getState())) {
                address.setState(addr.getState());
            }
            if (StringUtils.isNotBlank(addr.getStreet()) && !addr.getStreet().equals(address.getStreet())) {
                address.setStreet(addr.getStreet());
            }
        }
    }

    public void updatePrimaryPhone(User user, Phone phone) {
        if(user.getPrimaryPhone() == null)
            user.setPrimaryPhone(new Phone());

        if(phone != null) {
            if(StringUtils.isNotBlank(phone.getPhoneNumber()) && !phone.getPhoneNumber().equals(user.getPrimaryPhone().getPhoneNumber()))
                user.getPrimaryPhone().setPhoneNumber(phone.getPhoneNumber());

            if(StringUtils.isNotBlank(phone.getCountry()) && !phone.getCountry().equals(user.getPrimaryPhone().getCountry()))
                user.getPrimaryPhone().setCountry(phone.getCountry());

            if(StringUtils.isNotBlank(phone.getAreaCode()) && !phone.getAreaCode().equals(user.getPrimaryPhone().getAreaCode()))
                user.getPrimaryPhone().setAreaCode(phone.getAreaCode());

            if(StringUtils.isNotBlank(phone.getCountryCode()) && !phone.getCountryCode().equals(user.getPrimaryPhone().getCountryCode()))
                user.getPrimaryPhone().setCountryCode(phone.getCountryCode());

            if(phone.getPhoneType() != null && !phone.getPhoneType().equals(user.getPrimaryPhone().getPhoneType()))
                user.getPrimaryPhone().setPhoneType(phone.getPhoneType());

            if(phone.getDigits() != user.getPrimaryPhone().getDigits())
                user.getPrimaryPhone().setDigits(phone.getDigits());
        }
    }

    private void updatePatientDetails(PatientDetails updatePatientDetails, UserUpdatePatientDetailsPayload newDetails) throws JdxServiceException {
        if(updatePatientDetails == null)
            throw new JdxServiceException("Must provide a valid patientDetails objects to update");

        if(newDetails == null)
            throw new JdxServiceException("Must provide new patient details to update from");

        if(newDetails.getMedicalDetails() != null) {
            if(newDetails.getMedicalDetails().getPregnant() != null && !newDetails.getMedicalDetails().getPregnant().equals(updatePatientDetails.getMedicalDetails().isPregnant()))
                updatePatientDetails.getMedicalDetails().setPregnant(newDetails.getMedicalDetails().getPregnant());

            if(newDetails.getMedicalDetails().getNoBloodTransfusion() != null && !newDetails.getMedicalDetails().getNoBloodTransfusion().equals(updatePatientDetails.getMedicalDetails().isNoBloodTransfusion()))
                updatePatientDetails.getMedicalDetails().setNoBloodTransfusion(newDetails.getMedicalDetails().getNoBloodTransfusion());

            if(newDetails.getMedicalDetails().getGestationalAge() != null && !newDetails.getMedicalDetails().getGestationalAge().equals(updatePatientDetails.getMedicalDetails().getGestationalAge()))
                updatePatientDetails.getMedicalDetails().setGestationalAge(newDetails.getMedicalDetails().getGestationalAge());

            if(newDetails.getMedicalDetails().getNoOrganTransplant() != null && !newDetails.getMedicalDetails().getNoOrganTransplant().equals(updatePatientDetails.getMedicalDetails().isNoOrganTransplant()))
                updatePatientDetails.getMedicalDetails().setNoOrganTransplant(newDetails.getMedicalDetails().getNoOrganTransplant());

            if(newDetails.getMedicalDetails().getNumberOfFetuses() != null && !newDetails.getMedicalDetails().getNumberOfFetuses().equals(updatePatientDetails.getMedicalDetails().getNumberOfFetuses()))
                updatePatientDetails.getMedicalDetails().setNumberOfFetuses(newDetails.getMedicalDetails().getNumberOfFetuses());

            if(newDetails.getMedicalDetails().getThreeOrMoreFetuses() != null && !newDetails.getMedicalDetails().getThreeOrMoreFetuses().equals(updatePatientDetails.getMedicalDetails().isThreeOrMoreFetuses()))
                updatePatientDetails.getMedicalDetails().setThreeOrMoreFetuses(newDetails.getMedicalDetails().getThreeOrMoreFetuses());

            if(newDetails.getMedicalDetails().getVitals() != null && newDetails.getMedicalDetails().getVitals().size() > 0) {
                for(Vital vital : newDetails.getMedicalDetails().getVitals()) {
                    //A matching vital is one that has the same vital type and recording timestamp, meaning its a data override of a previous reading
                    Optional<Vital> foundVital = updatePatientDetails.getMedicalDetails().getVitals().stream().filter(x->x.getType().equals(vital.getType()) && x.getRecordedAt().equals(vital.getRecordedAt())).findAny();
                    if(foundVital.isPresent()){
                        if(vital.getValue() != null && !vital.getValue().equals(foundVital.get().getValue()))
                            foundVital.get().setValue(vital.getValue());
                        if(vital.getValueType() != null && !vital.getValueType().equals(foundVital.get().getValueType()))
                            foundVital.get().setValueType(vital.getValueType());
                    }
                    else {
                        updatePatientDetails.getMedicalDetails().addVital(vital);
                    }
                }
            }
        }
    }

    private void updatePatientPreferences(Preferences updatePatientPreferences, UserUpdatePreferencesPayload newPreferences) throws JdxServiceException {
        if (updatePatientPreferences == null)
            throw new JdxServiceException("Must provide a valid patientDetails objects to update");

        if (newPreferences == null)
            throw new JdxServiceException("Must provide new patient details to update from");

        if (newPreferences.getOptOut() != null && !newPreferences.getOptOut().equals(updatePatientPreferences.isOptOut()))
            updatePatientPreferences.setOptOut(newPreferences.getOptOut());

        if (newPreferences.getSmsMessages() != null && !newPreferences.getSmsMessages().equals(updatePatientPreferences.isSmsMessages()))
            updatePatientPreferences.setSmsMessages(newPreferences.getSmsMessages());

        if (newPreferences.getFstPreferences() != null ){
            if(newPreferences.getFstPreferences().getGenderDelegated() != null && !newPreferences.getFstPreferences().getGenderDelegated().equals(updatePatientPreferences.getFstPreferences().isGenderDelegated()))
                updatePatientPreferences.getFstPreferences().setGenderDelegated(newPreferences.getFstPreferences().getGenderDelegated());

            if(newPreferences.getFstPreferences().getGenderFanfare() != null && !newPreferences.getFstPreferences().getGenderFanfare().equals(updatePatientPreferences.getFstPreferences().isGenderFanfare()))
                updatePatientPreferences.getFstPreferences().setGenderFanfare(newPreferences.getFstPreferences().getGenderFanfare());

            if(newPreferences.getFstPreferences().getGenderTerms() != null && !newPreferences.getFstPreferences().getGenderTerms().equals(updatePatientPreferences.getFstPreferences().getGenderTerms()))
                updatePatientPreferences.getFstPreferences().setGenderTerms(newPreferences.getFstPreferences().getGenderTerms());

            if(newPreferences.getFstPreferences().getFstResultsDelegatedEmail() != null && !newPreferences.getFstPreferences().getFstResultsDelegatedEmail().equals(updatePatientPreferences.getFstPreferences().getFstResultsDelegatedEmail()))
                updatePatientPreferences.getFstPreferences().setFstResultsDelegatedEmail(newPreferences.getFstPreferences().getFstResultsDelegatedEmail());
        }
    }

}
