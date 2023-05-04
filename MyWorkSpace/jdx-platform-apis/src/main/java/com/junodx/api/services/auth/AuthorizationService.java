package com.junodx.api.services.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.exceptions.UserNotActivatedException;
import com.junodx.api.controllers.payloads.IDTokenObject;
import com.junodx.api.controllers.payloads.JwtResponse;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.core.types.ClientTypes;
import com.junodx.api.security.*;
import com.junodx.api.security.exceptions.TokenRefreshException;
import com.junodx.api.models.auth.*;
import com.junodx.api.repositories.AuthorityRepository;
import com.junodx.api.repositories.OAuthClientRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.exceptions.JdxServiceException;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static com.nimbusds.jose.JWSAlgorithm.RS256;

@Service
@Transactional
public class AuthorizationService extends ServiceBase {

    @Value("${jdx.authentication.verification.code.expiry}")
    private int verificationCodeExpirationDuration;

    @Value("${jdx.default.system.user}")
    private String defaultUserEmail;

    @Value("${jdx.default.system.clientId}")
    private String defaultSystemClientId;

    //TODO need to externalize this configuration to a config service where these can be added dynamically
    @Value("${jdx.clientids.patientportal}")
    private String patientPortalClientId;

    @Value("${jdx.clientids.labportal}")
    private String labPortalClientId;

    @Value("${jdx.clientids.providerportal}")
    private String providerPortalClientId;

    @Autowired
    private OAuthClientRepository oAuthClientRepository;

    @Autowired
    private JwtConfigurationLabPool jwtConfigurationLab;

    @Autowired
    private JwtConfigurationPatientPool jwtConfigurationPatientPool;

    @Autowired
    private JwtConfigurationProviderPool jwtConfigurationProviderPool;

    @Autowired
    private  AuthenticationManager authenticationManager;

    @Autowired
    private  UserService userService;

    @Autowired
    private  AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private  TokenService tokenService;

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    public AuthorizationService(){
        mapper = new ObjectMapper();
    }

    public JwtResponse authorize(IDTokenObject idObject) throws JdxServiceException {
        boolean labPortalUser = false;

        try {
            if (idObject == null)
                return null;

            String idToken = idObject.getToken();
            if (idToken == null)
                return null;

            //Pick the appropriate jwt configuration
            JwtConfiguration jwtConfiguration = null;

            if(idObject.getClientId() == null)
                throw new JdxServiceException("ClientId must be present in authorization request");

            if (idObject.getClientId() != null && idObject.getClientId().equals(providerPortalClientId))
                jwtConfiguration = jwtConfigurationProviderPool;
            else if (idObject.getClientId() != null && idObject.getClientId().equals(patientPortalClientId))
                jwtConfiguration = jwtConfigurationPatientPool;
            else if(idObject.getClientId() != null && idObject.getClientId().equals(labPortalClientId)) {
                jwtConfiguration = jwtConfigurationLab;
                labPortalUser = true;
            }
            else
                throw new Exception("clientId provided does not match existing configurations");

            /*
            logger.info("JWT type: " + jwtConfiguration.getClass());
            logger.info("JWT Configuration PoolUrl: " + jwtConfiguration.getCognitoIdentityPoolUrl());
            logger.info("JWT Configuration JwkUrl: " + jwtConfiguration.getJwkUrl());
            logger.info("JWT Configuration IdPoolId: " + jwtConfiguration.getIdentityPoolId());
            logger.info("JWT Configuration region: " + jwtConfiguration.getRegion());
            logger.info("JWT Configuration userPoolid: " + jwtConfiguration.getUserPoolId());
            logger.info("JWT Configuration cxn timeout: " + jwtConfiguration.getConnectionTimeout());
            logger.info("JWT Configuration http header: " + jwtConfiguration.getHttpHeader());
            logger.info("JWT Configuration read timeout: " + jwtConfiguration.getReadTimeout());
            logger.info("JWT Configuration jwkSetUrl: " + jwtConfiguration.getJwkUrl());

            logger.info("JWT JWK URL for lab jwtConfig " + jwtConfigurationLab.getJwkUrl());

             */

            //Prepare ID Token processor
            ResourceRetriever resourceRetriever =
                    new DefaultResourceRetriever(jwtConfiguration.getConnectionTimeout(),
                            jwtConfiguration.getReadTimeout());
            URL jwkSetURL = new URL(jwtConfiguration.getJwkUrl());
            JWKSource keySource = new RemoteJWKSet(jwkSetURL, resourceRetriever);
            ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
            JWSKeySelector keySelector = new JWSVerificationKeySelector(RS256, keySource);
            jwtProcessor.setJWSKeySelector(keySelector);


            String deviceId = idObject.getDeviceId();
            String provider = idObject.getProvider();
            String email = "";
            String username = "";
            String name = "";

            if(idObject.getProvider().equals("cognito")) {
                JWTClaimsSet claims = jwtProcessor.process(getBearerToken(idToken), null);
                //Validate token from IdP
                validateIssuer(jwtConfiguration, claims);

                //Exit if this id Token is not a valid ID Token
                verifyIfIdToken(jwtConfiguration, claims);

                email =  getEmailFrom(jwtConfiguration, claims);//AwsCognitoIdTokenProcessor.getEmailFrom(claims);
                username = getUserNameFrom(jwtConfiguration, claims);
                name = getNameFrom(jwtConfiguration, claims);
            } else if (idObject.getProvider().equals("juno")) {
                if(!jwtUtils.validateJwtToken(idObject.getToken()))
                    throw new JdxServiceException("Cannot validate inbound idToken for provider Juno");
                email = jwtUtils.getEmailFromJwtToken(idObject.getToken());
                username = jwtUtils.getUserNameFromJwtToken(idObject.getToken());
                name = jwtUtils.getNameFromJwtToken(idObject.getToken());
            }

            String firstName = null;
            String lastName = null;
            if (name != null) {
                String[] substrings = name.split(" ");
                if(substrings.length >= 1)
                    firstName = substrings[0];
                if(substrings.length >= 2)
                    lastName = substrings[1];
            }

            //TODO do work with the provider to find which app is calling

            User defaultSystemUser = userService.getSystemUser(); //findOneByEmailAndClientId(defaultUserEmail, defaultSystemClientId);
            UserDetailsImpl systemUser = null;
            if (defaultSystemUser != null) {
                List<GrantedAuthority> grants = new ArrayList<>();
                for (Authority authority : defaultSystemUser.getAuthorities())
                    grants.add(new SimpleGrantedAuthority(authority.getName()));

                 systemUser = new UserDetailsImpl(1L,
                        defaultSystemUser.getUsername(),
                        defaultSystemUser.getEmail(),
                        defaultSystemUser.getId(),
                        "",
                        idObject.getClientId(),
                        grants);
            } else
                throw new JdxServiceException("Cannot find default system user");

            //if we have an email from the token claims
            if (email != null) {
                Optional<User> lookupUser = userService.findOneByEmailAndClientId(email, idObject.getClientId());
                com.junodx.api.models.auth.User jdxUser = null;

                //Save new user, but only for lab portal - other portals require registration first
                //TODO come back to this when implementing social sign-in for patient portal
                if (lookupUser.isEmpty() && labPortalUser) {
                    jdxUser = new com.junodx.api.models.auth.User();
                    jdxUser.setEmail(email);
                    jdxUser.setUsername(username);
                    jdxUser.setFirstName(firstName);
                    jdxUser.setLastName(lastName);
                    jdxUser.setClientId(idObject.getClientId());
                    UserDevice device = new UserDevice();
                    device.setDeviceId(idObject.getDeviceId());
                    device.setUser(jdxUser);
                    device.setMeta(buildMeta(systemUser));
              //      jdxUser.addDevice(device);

                    jdxUser = userService.createDefaultAuthorities(jdxUser);
                    jdxUser.setStatus(UserStatus.ACTIVATED);

                    jdxUser.setLastLoggedIn(Calendar.getInstance());

                    device.setMeta(buildMeta(systemUser));
                    jdxUser = userService.save(jdxUser, systemUser);
                } else {
                    jdxUser = lookupUser.get();
                    //If we have a lab user and they aren't activated for some reason, then activated them now
                    if(labPortalUser && !jdxUser.isActivated())
                        jdxUser.setActivated(true);

                    if(!jdxUser.isActivated())
                        throw  new JdxServiceException("Cannot authorize user as they are not yet activated, please verify and activate this account beforehand");

                    //Update the local user from our IdP
                    /*
                    if (firstName != null && !firstName.equals(jdxUser.getFirstName()))
                        jdxUser.setFirstName(firstName);
                    if (lastName != null && !lastName.equals(jdxUser.getLastName()))
                        jdxUser.setLastName(lastName);
                    if(username != null && !username.equals(jdxUser.getUsername()))
                        jdxUser.setUsername(username);

                     */

                    if(idObject.getDeviceId() != null) {
                        UserDevice device = new UserDevice();
                        device.setDeviceId(idObject.getDeviceId());
                        device.setUser(jdxUser);
                        device.setMeta(buildMeta(systemUser));
             //           jdxUser.addDevice(device);
                    }

                    if(labPortalUser)
                        jdxUser.setStatus(UserStatus.ACTIVATED);
                    else if(jdxUser.getStatus() == null || jdxUser.getStatus().equals("") || !jdxUser.isActivated())
                        jdxUser.setStatus(UserStatus.PROVISIONAL);

                    jdxUser.setLastLoggedIn(Calendar.getInstance());

                    //jdxUser = userService.update(jdxUser, systemUser);
                    jdxUser = userService.save(jdxUser, systemUser);
                }
                RefreshToken refreshToken = null;
                refreshToken = tokenService.getRefreshTokenIfNotExpired(jdxUser);
                if (refreshToken == null)
                    refreshToken = tokenService.createRefreshToken(jdxUser);

                //Always create a new access token on authorize
                AccessToken accessToken = tokenService.createAccessToken(jdxUser);

                return new JwtResponse(accessToken.getToken(),
                        refreshToken.getToken(),
                        jdxUser.getId(),
                        jdxUser.getUsername(),
                        jdxUser.getEmail(),
                        jdxUser.getAuthoritiesAsStringList(),
                        jdxUser.getFirstName() + " " + jdxUser.getLastName(),
                        idObject.getClientId(),
                        (tokenService.getAccessTokenDuration() / 1000));
            }

            return null;
        } catch(Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot authorize user");
        }
    }

    public JwtResponse clientCredentials(String clientId, String clientSecret) throws JdxServiceException {
        try {
            Optional<OAuthClient> client = oAuthClientRepository.findOAuthClientByClientId(clientId);
            if (client.isPresent()) {
                if(!passwordEncoder.matches(clientSecret, client.get().getClientSecret()))
                    throw new JdxServiceException("Cannot authorize oauth client, secret does not match");

                User jdxUser = null;
                boolean newUser = false;
                Authority a = new Authority(Authority.ROLE_ADMIN);
                if(client.get().getAccountUser() != null )
                    jdxUser = client.get().getAccountUser();
                else {
                    jdxUser = new User();
                    jdxUser.setUsername(client.get().getName() + "_" + client.get().getClientId());
                    jdxUser.setEmail(client.get().getName() + "-api-client@junodx.com");
                    a.setUser(jdxUser);
                    jdxUser.addAuthority(a);
                }

                //Give this user admin privs - TODO need to repeal this with a better grant
                if(jdxUser.getAuthorities() == null || jdxUser.getAuthorities().size() == 0)
                    jdxUser.addAuthority(a);

                if(newUser) {
                    Optional<User> defaultSystemUser = userService.findOneByEmailAndClientId(defaultUserEmail, defaultSystemClientId);
                    if (defaultSystemUser.isPresent()) {
                        List<GrantedAuthority> grants = new ArrayList<>();
                        for (Authority authority : defaultSystemUser.get().getAuthorities())
                            grants.add(new SimpleGrantedAuthority(authority.getName()));

                        UserDetailsImpl systemUser = new UserDetailsImpl(1L,
                                defaultSystemUser.get().getUsername(),
                                defaultSystemUser.get().getEmail(),
                                defaultSystemUser.get().getId(),
                                "",
                                clientId,
                                grants);

                        jdxUser.setLastLoggedIn(Calendar.getInstance());

                        jdxUser = userService.save(jdxUser, systemUser);
                    } else
                        return null;
                }


                RefreshToken refreshToken = null;
                refreshToken = tokenService.getRefreshTokenIfNotExpired(jdxUser);
                if (refreshToken == null)
                    refreshToken = tokenService.createRefreshToken(jdxUser);

                //Always create a new access token on authorize
                AccessToken accessToken = tokenService.createAccessToken(jdxUser);

                return new JwtResponse(accessToken.getToken(),
                        refreshToken.getToken(),
                        jdxUser.getId(),
                        jdxUser.getUsername(),
                        jdxUser.getEmail(),
                        jdxUser.getAuthoritiesAsStringList(),
                        jdxUser.getFirstName() + " " + jdxUser.getLastName(),
                        clientId,
                        (tokenService.getAccessTokenDuration() / 1000));
            }
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public JwtResponse refreshToken(String refreshToken) throws TokenRefreshException {
        return tokenService.findRefreshTokenByToken(refreshToken)
                .map(RefreshToken::getUser)
                .map(user -> {
                    AccessToken token = tokenService.createAccessToken(user);
                    return new JwtResponse(token.getToken(),
                            refreshToken,
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getAuthoritiesAsStringList(),
                            user.getFirstName() + " " + user.getLastName(),
                            user.getClientId(),
                            (tokenService.getAccessTokenDuration() / 1000));
                }).orElseThrow(() -> new TokenRefreshException(refreshToken,
                        "Refresh token is not known!"));
    }

    public JwtResponse authenticateUserCredentials(String email, String password, String clientId) throws JdxServiceException, UserNotActivatedException {
        Optional<User> user = userService.findOneByEmailAndClientId(email, clientId);
        if(user.isEmpty())
            throw new JdxServiceException("Cannot find user to authenticate");

        if(passwordEncoder.matches(password, user.get().getPassword())){
            if(!user.get().isActivated() || !user.get().getStatus().equals(UserStatus.ACTIVATED))
                throw new UserNotActivatedException(user.get());

            User jdxUser = user.get();
            RefreshToken refreshToken = null;
            refreshToken = tokenService.getRefreshTokenIfNotExpired(jdxUser);
            if (refreshToken == null)
                refreshToken = tokenService.createRefreshToken(jdxUser);

            //Always create a new access token on authorize
            AccessToken accessToken = tokenService.createAccessToken(jdxUser);

            UserDetailsImpl defaultUser = UserDetailsImpl.build(userService.getSystemUser());
            user.get().setLastLoggedIn(Calendar.getInstance());
            userService.save(user.get(), defaultUser);

            return new JwtResponse(accessToken.getToken(),
                    refreshToken.getToken(),
                    jdxUser.getId(),
                    jdxUser.getUsername(),
                    jdxUser.getEmail(),
                    jdxUser.getAuthoritiesAsStringList(),
                    jdxUser.getFirstName() + " " + jdxUser.getLastName(),
                    clientId,
                    (tokenService.getAccessTokenDuration() / 1000));
        }

        throw new JdxServiceException("Cannot authenticate user with email " + email);
    }

    public static String getUserNameFrom(JwtConfiguration jwtConfiguration, JWTClaimsSet claims) {
        return claims.getClaims().get(jwtConfiguration.getUserNameField()).toString();
    }

    private String getEmailFrom(JwtConfiguration jwtConfiguration, JWTClaimsSet claims) {
        return claims.getClaims().get(jwtConfiguration.getEmailField()).toString();
    }

    public static String getNameFrom(JwtConfiguration jwtConfiguration, JWTClaimsSet claims) {
        return claims.getClaims().get(jwtConfiguration.getNameField()).toString();
    }

    private void verifyIfIdToken(JwtConfiguration jwtConfiguration, JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception("JWT Token is not an ID Token");
        }
    }

    private void validateIssuer(JwtConfiguration jwtConfiguration, JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception(String.format("Issuer %s does not match cognito idp %s", claims.getIssuer(), jwtConfiguration.getCognitoIdentityPoolUrl()));
        }
    }

    private String getBearerToken(String token) {
        return token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
    }
}
