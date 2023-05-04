package com.junodx.api.models.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.connectors.aws.sns.SnsMessageResponse;
import com.junodx.api.connectors.aws.sns.SnsWriter;
import com.junodx.api.connectors.messaging.SnsMessageHandler;
import com.junodx.api.connectors.messaging.payloads.EventType;
import com.junodx.api.controllers.payloads.SalesforceChangedDataMap;
import com.junodx.api.controllers.payloads.SalesforceChartUpdateInfo;
import com.junodx.api.controllers.payloads.SalesforceRecordChanged;
import com.junodx.api.controllers.payloads.SalesforceUserUpdateAccountInfo;
import com.junodx.api.controllers.payloads.SalesforceChartUpdateInfo.TestResult;
import com.junodx.api.controllers.users.payloads.ChangePasswordPayload;
import com.junodx.api.controllers.users.payloads.RegisterUserPayload;
import com.junodx.api.controllers.users.payloads.UserForgotPasswordPayload;
import com.junodx.api.controllers.users.payloads.UserUpdatePasswordPayload;

import com.junodx.api.controllers.users.payloads.UserUpdate.UserUpdatePreferencesPayload;
import com.junodx.api.dto.mappers.UserMapStructMapper;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.auth.types.GenderTerms;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.core.Meta;

import com.junodx.api.models.patient.Medication;
import com.junodx.api.models.patient.PatientChart;
import com.junodx.api.models.patient.PatientChartEntry;
import com.junodx.api.models.patient.PatientDetails;

import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.models.providers.Provider;
import com.junodx.api.repositories.AuthorityRepository;
import com.junodx.api.repositories.RefreshTokenRepository;
import com.junodx.api.repositories.UserRepository;
import com.junodx.api.repositories.auth.EmailChangeRepository;
import com.junodx.api.repositories.auth.ForgotPasswordCodeRepository;
import com.junodx.api.repositories.auth.PatientChartEntryRepository;
import com.junodx.api.repositories.auth.VerificationCodeRepository;
import com.junodx.api.repositories.commerce.OrderRepository;
import com.junodx.api.security.JwtUtils;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.auth.UserService;
import com.junodx.api.services.auth.UserServiceImpl;
import com.junodx.api.services.exceptions.JdxServiceException;
import com.junodx.api.services.mail.MailService;
import com.junodx.api.services.patients.PatientDetailsService;
import com.junodx.api.services.providers.ProviderService;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private AuthorityRepository authorityRepository;

	@Mock
	private VerificationCodeRepository verificationCodeRepository;

	@Mock
	private EmailChangeRepository emailChangeRepository;

	@Mock
	private PatientDetailsService patientDetailsService;

	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@Mock
	private ForgotPasswordCodeRepository forgotPasswordCodeRepository;

	@Mock
	private PatientChartEntryRepository patientChartEntryRepository;

	@Mock
	private ProviderService providerService;

	@Mock
	private MailService mailService;

	@Mock
	private JwtUtils jwtUtils; 
	@Mock

	private OrderRepository ordersRepository;
	@Mock
	private UserMapStructMapper userMapper;

	@Mock
	private UserService userService;
	@Mock
	private ObjectMapper mapper;
	@Spy
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	@Mock
	private VerificationCode verificationCodeMock;
	@Mock
	private TestResult testResult;
	@Mock
	private SalesforceRecordChanged SalesforceRecordChange;
	@Mock
	private SalesforceChangedDataMap SalesforceChangedDataMapped;
	@Mock
	private SalesforceChartUpdateInfo SalesforceChartUpdateInfo;
	@Mock
	private UserDetailsImpl userDetailsImpl;
	private static UserDetails usedetails;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		usedetails = new org.springframework.security.core.userdetails.User("no-email@junodx.com", "",
				new ArrayList<>());
		mapper = new ObjectMapper();

	}

	@Test
	void testCreateLoadUserByUsername() {
		String userId = "1L";
		assertEquals(userId, DataBuilder.mockUser().getId());
		when(userRepository.findByUsername(userId)).thenReturn(Optional.of(DataBuilder.mockUser()));
		assertEquals(userServiceImpl.loadUserByUsername(userId), usedetails);
	}

	@Test
	public void testCreateLoadUserByUsernameException() throws UsernameNotFoundException {
		String userId = "1L";
		assertEquals(userId, DataBuilder.mockUser().getId());
		when(userRepository.findByUsername(userId)).thenReturn(Optional.empty());
		Exception exception = assertThrows(UsernameNotFoundException.class,
				() -> userServiceImpl.loadUserByUsername(userId));
		assertEquals("Invalid username or password.", exception.getMessage());
	}

	@Test
	void testCreatesearch() {
		Pageable paging = PageRequest.of(0, 2);
		String lastName = "User";
		String firstName = "Juno_test";
		String state = "CA";
		String city = "San Diego";
		String postalCode = "98077";
		String email = "no-email@junodx.com";
		String practiceId = "2L";
		String providerId = "12345";
		String xifinId = "1234";
		String stripeId = "34677676";
		UserType type = UserType.STANDARD;
		UserStatus status = UserStatus.ACTIVATED;
		List<User> users = new ArrayList<>();
		users.add(DataBuilder.mockUser());
		assertEquals(lastName, DataBuilder.mockUser().getLastName());
		assertEquals(firstName, DataBuilder.mockUser().getFirstName());
		assertEquals(email, DataBuilder.mockUser().getEmail());
		assertEquals(city, DataBuilder.mockUser().getBillingAddress().getCity());
		assertEquals(state, DataBuilder.mockUser().getBillingAddress().getState());
		assertEquals(postalCode, DataBuilder.mockUser().getBillingAddress().getPostalCode());
		assertEquals(practiceId, DataBuilder.getMockPractice().getId());
		assertEquals(providerId, DataBuilder.getMockProvider().getId());
		assertEquals(xifinId, DataBuilder.getMockProvider().getXifinId());
		assertEquals(stripeId, DataBuilder.mockUser().getStripeCustomerId());
		assertEquals(type, DataBuilder.mockUser().getUserType());
		assertEquals(status, DataBuilder.mockUser().getStatus());
		final Page<User> pages = new PageImpl<>(users, paging, 5);
		when(userRepository.search(lastName, firstName, state, city, postalCode, email, xifinId, stripeId, type, status,
				paging)).thenReturn(pages);
		assertEquals(userServiceImpl.search(Optional.of(lastName), Optional.of(firstName), Optional.of(state),
				Optional.of(city), Optional.of(postalCode), Optional.of(email), Optional.of(practiceId),
				Optional.of(providerId), Optional.of(xifinId), Optional.of(stripeId), Optional.of(type),
				Optional.of(status), paging), pages);
	}

	@Test
	void testfindAllByUserType() {
		Pageable paging = PageRequest.of(0, 2);
		List<User> users = new ArrayList<>();
		users.add(DataBuilder.mockUser());
		final Page<User> pages = new PageImpl<>(users, paging, 5);
		UserType type = UserType.STANDARD;
		assertEquals(type, DataBuilder.mockUser().getUserType());
		when(userRepository.findAllByUserTypeIs(type, paging)).thenReturn(pages);
		assertEquals(userServiceImpl.findAllByUserType(type, paging), pages);
	}

	@Test
	void testfindOne() {
		String id = "1L";
		assertEquals(id, DataBuilder.mockUser().getId());
		when(userRepository.findById(id)).thenReturn(Optional.of(DataBuilder.mockUser()));
		assertEquals(userServiceImpl.findOne(id).getClass(), Optional.of(DataBuilder.mockUser()).getClass());
	}

	@Test
	void testfindOneByEmailAndClientId() {
		String email = "no-email@junodx.com";
		String clientId = "12345";
		assertEquals(email, DataBuilder.mockUser().getEmail());
		assertEquals(clientId, DataBuilder.mockUser().getClientId());
		when(userRepository.findUserByEmailAndClientId(email, clientId))
				.thenReturn(Optional.of(DataBuilder.mockUser()));
		assertEquals(userServiceImpl.findOneByEmailAndClientId(email, clientId).getClass(),
				Optional.of(DataBuilder.mockUser()).getClass());
	}

	@Test
	void testfindOneByEmailAndClientIds() {
		String email = "no-email@junodx.com";
		String clientId = "12345";
		assertEquals(email, DataBuilder.mockUser().getEmail());
		assertEquals(clientId, DataBuilder.mockUser().getClientId());
		String[] includes = { "anjali", "gunduka" };
		when(userRepository.findUserByEmailAndClientId(email, clientId))
				.thenReturn(Optional.of(DataBuilder.mockUser()));
		assertEquals(userServiceImpl.findOneByEmailAndClientId(email, clientId, includes).getClass(),
				ServiceBase.ServiceResponse.class);
	}

	@Test
	void testfindOneByEmailAndClientIdException() {
		String email = "no-email@junodx.com";
		String clientId = "12345";
		assertEquals(email, DataBuilder.mockUser().getEmail());
		assertEquals(clientId, DataBuilder.mockUser().getClientId());
		String[] includes = { "anjali", "gunduka" };
		assertEquals(userServiceImpl.findOneByEmailAndClientId(email, clientId, includes).getClass(),
				ServiceBase.ServiceResponse.class);
	}

	@Test
	void testfindAllUsersByProviderId() {
		Pageable paging = PageRequest.of(0, 2);
		List<User> users = new ArrayList<>();
		users.add(DataBuilder.mockUser());
		final Page<User> pages = new PageImpl<>(users, paging, 5);
		String[] includes = {};
		UserType type = UserType.STANDARD;
		String providerId = "12345";
		assertEquals(providerId, DataBuilder.getMockProvider().getId());
		assertEquals(type, DataBuilder.mockUser().getUserType());
		when(providerService.getProvider(providerId, includes)).thenReturn(Optional.of(DataBuilder.getMockProvider()));
		when(userRepository.findUsersByPatientDetails_ProvidersAndUserTypeIs(Mockito.any(), Mockito.any(),
				Mockito.any())).thenReturn(pages);
		assertEquals(userServiceImpl.findAllUsersByProviderId(providerId, type, paging), pages);
	}

	@Test
	void testfindAllUsersByProviderIdException() {
		Pageable paging = PageRequest.of(0, 2);
		UserType type = UserType.STANDARD;
		String providerId = "12345";
		assertEquals(providerId, DataBuilder.getMockProvider().getId());
		assertEquals(type, DataBuilder.mockUser().getUserType());
		assertEquals(userServiceImpl.findAllUsersByProviderId(providerId, type, paging), null);
	}

	@Test
	void testfindAllUsersByLastName() {
		Pageable paging = PageRequest.of(0, 2);
		List<User> users = new ArrayList<>();
		users.add(DataBuilder.mockUser());
		final Page<User> pages = new PageImpl<>(users, paging, 5);
		String lastName = "User";
		assertEquals(lastName, DataBuilder.mockUser().getLastName());
		when(userRepository.findUsersByLastName(lastName, paging)).thenReturn(pages);
		assertEquals(userServiceImpl.findAllUsersByLastName(lastName, paging), pages);
	}

	@Test
	void testfindAllUsersByCity() {
		String city = "San Diego";
		Pageable paging = PageRequest.of(0, 2);
		List<User> users = new ArrayList<>();
		users.add(DataBuilder.mockUser());
		final Page<User> pages = new PageImpl<>(users, paging, 5);
		assertEquals(city, DataBuilder.mockUser().getBillingAddress().getCity());
		when(userRepository.findUsersByPrimaryAddress_City(city, paging)).thenReturn(pages);
		assertEquals(userServiceImpl.findAllUsersByCity(city, paging), pages);
	}

	@Test
	void testfindAllUsersByState() {
		String state = "CA";
		Pageable paging = PageRequest.of(0, 2);
		List<User> users = new ArrayList<>();
		users.add(DataBuilder.mockUser());
		final Page<User> pages = new PageImpl<>(users, paging, 5);
		assertEquals(state, DataBuilder.mockUser().getBillingAddress().getState());
		when(userRepository.findUsersByPrimaryAddress_State(state, paging)).thenReturn(pages);
		assertEquals(userServiceImpl.findAllUsersByState(state, paging), pages);
	}

	@Test
	void testfindAllUsersByPostalCode() {
		String postalCode = "98077";
		Pageable paging = PageRequest.of(0, 2);
		List<User> users = new ArrayList<>();
		users.add(DataBuilder.mockUser());
		final Page<User> pages = new PageImpl<>(users, paging, 5);
		assertEquals(postalCode, DataBuilder.mockUser().getBillingAddress().getPostalCode());
		when(userRepository.findUsersByPrimaryAddress_PostalCode(postalCode, paging)).thenReturn(pages);
		assertEquals(userServiceImpl.findAllUsersByPostalCode(postalCode, paging), pages);
	}

	@Test
	void testgetSystemUser() {
		String defaultUserEmail = null;
		String defaultSystemClientId = null;
		when(userRepository.findUserByEmailAndClientId(defaultUserEmail, defaultSystemClientId))
				.thenReturn(Optional.of(DataBuilder.mockUser()));
		assertEquals(userServiceImpl.getSystemUser().getClass(), DataBuilder.mockUser().getClass());
	}

	@Test
	public void testgetSystemUserException() throws JdxServiceException {
		String defaultUserEmail = null;
		String defaultSystemClientId = null;
		when(userRepository.findUserByEmailAndClientId(defaultUserEmail, defaultSystemClientId))
				.thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> userServiceImpl.getSystemUser());
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testsave() {
		User user = User.createDummyUser();
		when(userRepository.save(user)).thenReturn(user);
		assertEquals(userServiceImpl.save(user, DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilder.mockUser().getClass());
	}

	@Test
	public void testsaveException() throws JdxServiceException {
		User user2 = User.createDummyUser();
		when(userRepository.save(user2)).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.save(user2, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

//	@Test
//	void testupdate() {
//		User user = User.createDummyUser();
//		user.setStatus(UserStatus.PROVISIONAL);
//		user.setDateOfBirth("1997-12-15");
//		user.setPrimaryAddress(DataBuilder.getMockAddress());
//		user.setBillingAddress(DataBuilder.getMockAddress());
//		user.setPrimaryPhone(DataBuilder.getMockPhone());
//		user.setPreferences(DataBuilder.getMockPreferences());
//		user.setPatientDetails(DataBuilder.mockPatientDetails());
//		user.setLastName("User");
//	 	user.setFirstName("Juno_test");
//		user.setStripeCustomerId("34677676");
//		user.setXifinPatientId("1234");
//		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//		when(userRepository.save(user)).thenReturn(user);
//		assertEquals(userServiceImpl.update(user, DataBuilderOrder.userDetailsImpl), user);
//
//	}

	@Test
	void testupdate() {
		User user = User.createDummyUser();
		user.setPrimaryAddress(DataBuilder.getMockAddress());
		user.setBillingAddress(DataBuilder.getMockAddress());
		user.setDateOfBirth("1997-12-15");
		user.setPreferences(DataBuilder.getMockPreferences());
		user.setPatientDetails(DataBuilder.mockPatientDetails());
		user.setStripeCustomerId("34677676");
		user.setXifinPatientId("1234");
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		when(userRepository.save(Mockito.any())).thenReturn(DataBuilder.mockUserForUpdate());
		assertEquals(
				userServiceImpl.update(DataBuilder.mockUserForUpdate(), DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilder.mockUserForUpdate().getClass());

	}

	@Test
	public void testupdateException() throws JdxServiceException {
		User user2 = new User();
		user2.setUsername(null);
		user2.setId(null);
		user2.setPassword("aa");
		user2.setActivated(true);
		user2.setMeta(null);
		user2.setDateOfBirth("12-1997-08");
		when(userRepository.findById(user2.getId())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.update(user2, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	User user = User.createDummyUser();

	@Test
	void testregister() {
		User user = User.createDummyUser();
		user.setMeta(DataBuilder.getMockMeta());
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(user));
		doReturn(user).when(userServiceImpl).createDefaultAuthorities(user);
		when(verificationCodeRepository.save(Mockito.any())).thenReturn(DataBuilder.getVerificationCode());
		long verificationCodeExpirationDuration = 0;
		when(jwtUtils.generateTokenFromUser(user, verificationCodeExpirationDuration * 60 * 60 * 1000))
				.thenReturn(null);
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		assertEquals(userServiceImpl.register(DataBuilder.mockRegisterUserPayload()).getClass(),
				DataBuilder.mockPostRegistrationPayload().getClass());
	}

//	@Test
//	void testregisterGenerateToken() {
//		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
//				.thenReturn(Optional.of(DataBuilder.mockUser()));
//		when(verificationCodeRepository.save(Mockito.any())).thenReturn(DataBuilder.getVerificationCode());
//		long verificationCodeExpirationDuration = 0;
//		when(jwtUtils.generateTokenFromUser(user, verificationCodeExpirationDuration * 60 * 60 * 1000))
//				.thenReturn(null);
//		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
//				Mockito.anyBoolean())).thenReturn(true);
//		assertEquals(userServiceImpl.register(DataBuilder.mockRegisterUserPayload()).getClass(),
//				DataBuilder.mockPostRegistrationPayload().getClass());
//	}

	@Test
	void testregisterWithoutUserExample() {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUserForUpdatePatientPortal().setAuthorities(authority);
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.empty());
		doReturn(DataBuilder.mockUserForUpdatePatientPortal()).when(userServiceImpl).getSystemUser();
		ReflectionTestUtils.setField(userServiceImpl, "patientPortalClientId", "9348-8892-9342-01");
		assertEquals(userServiceImpl.register(DataBuilder.mockRegisterUserPayload()).getClass(),
				DataBuilder.mockPostRegistrationPayload().getClass());
	}

	@Test
	public void testregisterMailException() throws JdxServiceException {
		User user = User.createDummyUser();
		user.setMeta(DataBuilder.getMockMeta());
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(user));
		doReturn(user).when(userServiceImpl).createDefaultAuthorities(user);
		when(verificationCodeRepository.save(Mockito.any())).thenReturn(DataBuilder.getVerificationCode());
		long verificationCodeExpirationDuration = 0;
		when(jwtUtils.generateTokenFromUser(user, verificationCodeExpirationDuration * 60 * 60 * 1000))
				.thenReturn(null);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.register(DataBuilder.mockRegisterUserPayload()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testregisterWithoutUserExampleException() throws JdxServiceException {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUserForUpdatePatientPortal().setAuthorities(authority);
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.empty());
		doReturn(DataBuilder.mockUserForUpdatePatientPortal()).when(userServiceImpl).getSystemUser();
		ReflectionTestUtils.setField(userServiceImpl, "patientPortalClientId", "9348-8892-9342-01");
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.register(DataBuilder.mockRegisterUserPayloads()));
		assertEquals("There was an issue with the user's password.", e.getMessage());
	}

	@Test
	void testregisterWithoutUserExamples() {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUserForUpdatePatientPortal().setAuthorities(authority);
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.empty());
		doReturn(DataBuilder.mockUserForUpdatePatientPortal()).when(userServiceImpl).getSystemUser();
		ReflectionTestUtils.setField(userServiceImpl, "patientPortalClientId", "9348-8892-9342-01");
		assertEquals(userServiceImpl.register(DataBuilder.mockRegisterUserPayloadEmails()).getClass(),
				DataBuilder.mockPostRegistrationPayload().getClass());
	}

	@Test
	void testcreateDefaultAuthoritiePatientPortal() {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUserForUpdatePatientPortal().setAuthorities(authority);
		ReflectionTestUtils.setField(userServiceImpl, "patientPortalClientId", "9348-8892-9342-01");
		assertEquals(userServiceImpl.createDefaultAuthorities(DataBuilder.mockUserForUpdatePatientPortal()).getClass(),
				DataBuilder.mockUserForUpdatePatientPortal().getClass());

	}

	@Test
	void testcreateDefaultAuthoritiePatientPortalException() {
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.createDefaultAuthorities(DataBuilder.mockUserForUpdatePatientPortalException()));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	void testcreateDefaultAuthoritieDifferentClientException() {
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.createDefaultAuthorities(DataBuilder.mockUserForUpdateExceptions()));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());

	}

	@Test
	void testsendUserStatusException() {
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.sendUserStatus(DataBuilder.mockUser(), EventType.CREATE));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	void testcreateDefaultAuthoritieLabPortal() {
		Authority auth = new Authority();
		auth.setName("ROLE_ADMIN");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUserForUpdateLabPortal().setAuthorities(authority);
		ReflectionTestUtils.setField(userServiceImpl, "labPortalClientId", "9358-4292-7682-02");
		assertEquals(userServiceImpl.createDefaultAuthorities(DataBuilder.mockUserForUpdateLabPortal()).getClass(),
				DataBuilder.mockUserForUpdateLabPortal().getClass());
	}

	@Test
	void testcreateDefaultAuthoritieProvider() {
		Authority auth = new Authority();
		auth.setName("ROLE_PRACTICE_REP");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUserForUpdateLabPortal().setAuthorities(authority);
		ReflectionTestUtils.setField(userServiceImpl, "providerPortalClientId", "0352-8232-9622-03");
		assertEquals(userServiceImpl.createDefaultAuthorities(DataBuilder.mockUserForUpdateproviderPortal()).getClass(),
				DataBuilder.mockUserForUpdateproviderPortal().getClass());
	}

	@Test
	void testregisterAnotherExamples() {
		User user = User.createDummyUser();
		user.setPassword("jhon");
		user.setMeta(DataBuilder.getMockMeta());
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(user));
		when(verificationCodeRepository.save(Mockito.any())).thenReturn(DataBuilder.getVerificationCode());
		long verificationCodeExpirationDuration = 0;
		when(jwtUtils.generateTokenFromUser(user, verificationCodeExpirationDuration * 60 * 60 * 1000))
				.thenReturn(null);
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		assertEquals(userServiceImpl.register(DataBuilder.mockRegisterUserPayload()).getClass(),
				DataBuilder.mockPostRegistrationPayload().getClass());
	}

	@Test
	void testregisterAnotherExampleForFirstName() {
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.mockUserFirst()));
		when(verificationCodeRepository.save(Mockito.any())).thenReturn(DataBuilder.getVerificationCode());
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		assertEquals(userServiceImpl.register(DataBuilder.mockRegisterUserPayload()).getClass(),
				DataBuilder.mockPostRegistrationPayload().getClass());
	}

	@Test
	@ExceptionHandler
	public void testregisterSendEmailServiceException() throws JdxServiceException {
		User user = User.createDummyUser();
		String newEmail = null;
		user.setMeta(DataBuilder.getMockMeta());
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(user));
		doReturn(user).when(userServiceImpl).createDefaultAuthorities(user);
		when(verificationCodeRepository.save(Mockito.any())).thenReturn(DataBuilder.getVerificationCode());
		long verificationCodeExpirationDuration = 0;
		when(jwtUtils.generateTokenFromUser(user, verificationCodeExpirationDuration * 60 * 60 * 1000))
				.thenReturn(null);
		when(mailService.sendEmail(newEmail, null, null, false, false)).thenReturn(false);
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.register(DataBuilder.mockRegisterUserPayload()));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	void testregisterAnotherExamp() {
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.mockUserForUpdates()));
		assertEquals(userServiceImpl.register(DataBuilder.mockRegisterUserPayload()).getClass(),
				DataBuilder.mockPostRegistrationPayload().getClass());
	}

	@Test
	void testregisterAnotherExample() {
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.mockUserForUpdate()));
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.register(DataBuilder.mockRegisterUserPayloads()));
		assertEquals("There was an issue with the user's password.", exception.getMessage());
	}

	@Test
	public void testregisterExceptions() throws JdxServiceException {
		RegisterUserPayload registerUserPayload = null;
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.register(registerUserPayload));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	void testvalidatePasswordAgainstRules() {
		String password = null;
		userServiceImpl.validatePasswordAgainstRules(password);
	}

	@Test
	void testsendVerificationCode() {
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(user));
		verify(verificationCodeRepository, times(0)).deleteAllByUser_Id(Mockito.anyString());
		when(verificationCodeRepository.save(Mockito.any())).thenReturn(DataBuilder.getVerificationCode());
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		assertEquals(userServiceImpl.sendVerificationCode(Mockito.anyString(), Mockito.anyString()).getClass(),
				DataBuilder.mockPostRegistrationPayload().getClass());
	}

	@Test
	public void testsendVerificationCodeException() throws JdxServiceException {
		String email = "anu@gmail.com";
		String clientId = "12345";
		when(userRepository.findUserByEmailAndClientId(email, clientId)).thenReturn(Optional.empty());
		Exception exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.sendVerificationCode(email, clientId));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	public void testsendVerificationCodeSendEmailServiceException() throws JdxServiceException {
		String newEmail = null;
		String email = "anu@gmail.com";
		String clientId = "12345";
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(user));
		verify(verificationCodeRepository, times(0)).deleteAllByUser_Id(Mockito.anyString());
		when(verificationCodeRepository.save(Mockito.any())).thenReturn(DataBuilder.getVerificationCode());
		when(mailService.sendEmail(newEmail, null, null, false, false)).thenReturn(false);
		Exception exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.sendVerificationCode(email, clientId));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	public void testsendVerificationCodeSendEmailServiceExceptions() throws JdxServiceException {
		String email = "no-email@junodx.com";
		String clientId = "12345";
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.mockUser()));
		when(verificationCodeRepository.save(Mockito.any())).thenReturn(DataBuilder.getVerificationCodes());
		Exception exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.sendVerificationCode(email, clientId));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	void testresendVerificationCodeAuthenticated() {
		when(verificationCodeRepository.save(Mockito.any())).thenReturn(DataBuilder.getVerificationCode());
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		int verificationCodeExpirationDuration = 0;
		when(jwtUtils.generateTokenFromUser(user, verificationCodeExpirationDuration * 60 * 60 * 1000))
				.thenReturn("anyToken");
		assertEquals(userServiceImpl.resendVerificationCodeAuthenticated(user).getClass(),
				DataBuilder.mockPostRegistrationPayload().getClass());
	}

	@Test
	public void testresendVerificationCodeSendEmailServiceException() throws JdxServiceException {
		String newEmail = null;
		when(verificationCodeRepository.save(Mockito.any())).thenReturn(DataBuilder.getVerificationCode());
		when(mailService.sendEmail(newEmail, null, null, false, false)).thenReturn(false);
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.resendVerificationCodeAuthenticated(user));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	public void testresendVerificationCodeSendEmailServiceExceptions() throws JdxServiceException {
		when(verificationCodeRepository.save(Mockito.any())).thenReturn(DataBuilder.getVerificationCode());
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.resendVerificationCodeAuthenticated(user));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	public void testresendVerificationCodeAuthenticatedException() throws JdxServiceException {
		User user = null;
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.resendVerificationCodeAuthenticated(user));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	void testverify() {
		when(verificationCodeRepository.findVerificationCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.getVerificationCode()));
		when(userRepository.save(Mockito.any())).thenReturn(user);
		verify(verificationCodeRepository, times(0)).delete(DataBuilder.getVerificationCode());
		String defaultUserEmail = null;
		String defaultSystemClientId = null;
		when(userRepository.findUserByEmailAndClientId(defaultUserEmail, defaultSystemClientId))
				.thenReturn(Optional.of(user));
		assertEquals(userServiceImpl.verify(DataBuilder.payload()).getClass(),
				DataBuilder.mockVerificationCodeResponsePayload().getClass());
	}

	@Test
	public void testverifyException() throws JdxServiceException {
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.verify(DataBuilder.payloads()));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	public void testverifyVerificationCodeException() throws JdxServiceException {
		when(verificationCodeRepository.findVerificationCodeByCode(Mockito.anyString())).thenReturn(Optional.empty());
		assertEquals(userServiceImpl.verify(DataBuilder.payload()).getClass(),
				DataBuilder.mockVerificationCodeResponsePayload().getClass());
	}

	@Test
	public void testverifyUserIdException() throws JdxServiceException {
		when(verificationCodeRepository.findVerificationCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.getVerificationCodes()));
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.verify(DataBuilder.payload()));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	public void testverifyUseremailException() throws JdxServiceException {
		when(verificationCodeRepository.findVerificationCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.getVerificationCode()));
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.verify(DataBuilder.payloadss()));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	void testdoesEmailExistForClientId() {
		String email = "no-email@junodx.com";
		String clientId = "12345"; 
		assertEquals(email, DataBuilder.mockUser().getEmail());
		assertEquals(clientId, DataBuilder.mockUser().getClientId());
		when(userRepository.findUserByEmailAndClientId(email, clientId)).thenReturn(Optional.of(user));
		assertEquals(userServiceImpl.doesEmailExistForClientId(email, Optional.of(clientId)), true);
	}

//	@Test
//	void testdoesEmailExistForClientIdAnother() {
//		String email = "no-email@junodx.com";
//		String clientId = "12345";
//		assertEquals(email, DataBuilder.mockUser().getEmail());
//		assertEquals(clientId, DataBuilder.mockUser().getClientId());
//		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
//		assertEquals(userServiceImpl.doesEmailExistForClientId(email, Optional.of(clientId)), true);
//	}

	@Test
	void testdoesEmailExistForClientIdfalse() {
		String email = "no-email@junodx.com";
		String clientId = "12345";
		assertEquals(email, DataBuilder.mockUser().getEmail());
		assertEquals(clientId, DataBuilder.mockUser().getClientId());
		assertEquals(userServiceImpl.doesEmailExistForClientId(email, Optional.of(clientId)), false);
	}

	@Test
	void testdoesEmailExistForClientIdException() {
		User user = User.createDummyUser();
		String email = "no-email@junodx.com";
		assertEquals(email, DataBuilder.mockUser().getEmail());
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.doesEmailExistForClientId(email, Optional.empty()));
	}

	@Test
	void testaddRole() {
		User user = User.createDummyUser();
		when(userRepository.findById("1L")).thenReturn(Optional.of(user));
		when(userRepository.save(user)).thenReturn(user);
		Authority authority = new Authority();
		user.getAuthorities().stream().filter(x -> x.getName().equals(authority.getName()))
				.collect(Collectors.toList());
		assertEquals(userServiceImpl.addRole("1L", authority, DataBuilder.getMockUserDetailsImpl()), user);
	}

	@Test
	public void testaddRoleException() throws JdxServiceException {
		Authority authority = null;
		UserDetailsImpl updater = null;
		when(userRepository.findById(null)).thenReturn(Optional.empty());
		Exception e = assertThrows(JdxServiceException.class, () -> userServiceImpl.addRole(null, authority, updater));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	void testremoveRole() {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		user.setAuthorities(authority);
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		when(userRepository.save(Mockito.any())).thenReturn(user);
		assertEquals(userServiceImpl.removeRole("1L", auth, DataBuilder.getMockUserDetailsImpl()), user);
	}

	@Test
	public void testremoveRoleUserExceptions() throws JdxServiceException {
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		Authority auth = null;
		Exception e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.removeRole("1L", auth, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	public void testremoveRoleAuthorityException() throws JdxServiceException {
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		Authority auth = null;
		Exception e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.removeRole("1L", auth, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	void testgetRoles() {
		User user = User.createDummyUser();
		when(userRepository.findById("1L")).thenReturn(Optional.of(user));
		assertEquals(userServiceImpl.getRoles("1L"), user.getAuthorities());
	}

	@Test
	public void testgetRolesException() throws JdxServiceException {
		when(userRepository.findById("1L")).thenReturn(Optional.empty());
		Exception e = assertThrows(JdxServiceException.class, () -> userServiceImpl.getRoles("1L"));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	public void testgetforgotPasswordException() throws JdxServiceException {
		UserForgotPasswordPayload userForgotPasswordPayload = null;
		Exception e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.forgotPassword(userForgotPasswordPayload));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	void testgetforgotPasswordUserException() {

		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.empty());
		Exception e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.forgotPassword(DataBuilder.mockUserForgotPasswordPayload()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	@ExceptionHandler
	void testgetforgotPasswordUserMailServiceException() {

		when(userRepository.findUserByEmailAndClientId("no-email@junodx.com", "89766")).thenReturn(Optional.of(user));
		when(forgotPasswordCodeRepository.save(Mockito.any())).thenReturn(DataBuilder.mockForgotPasswordCode());
		Exception e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.forgotPassword(DataBuilder.mockUserForgotPasswordPayload()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

//	@Test
//	void testgetupdatePassword() {
//		when(forgotPasswordCodeRepository.findForgotPasswordCodeByCode(
//				"am9uKzQxQGp1bm9keC5jb206OTM0OC04ODkyLTkzNDItMDE6OWEzOTA4MDktMGY5My00MmY2LTljMjAtMzEwOWZhYWU4Zjk1"
//						+ ""))
//				.thenReturn(Optional.of(DataBuilder.mockForgotPasswordCode()));
//		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
//				.thenReturn(Optional.of(DataBuilder.mockUsers()));
//		doReturn(DataBuilder.mockUsers()).when(userServiceImpl).getSystemUser();
//		when(userRepository.save(Mockito.any())).thenReturn(DataBuilder.mockUsers());
//		verify(forgotPasswordCodeRepository, times(0)).delete(DataBuilder.mockForgotPasswordCode());
//		boolean updatePassword = userServiceImpl.updatePassword(DataBuilder.mockUserUpdatePasswordPayload());
//		assertEquals(updatePassword, true);
//	}

	@Test
	void testgetupdatePasswordTokenException() {
		when(forgotPasswordCodeRepository.findForgotPasswordCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.mockForgotPasswordCodes()));
		verify(forgotPasswordCodeRepository, times(0)).delete(DataBuilder.mockForgotPasswordCodes());
		Exception e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.updatePassword(DataBuilder.mockUserUpdatePasswordPayloads()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testgetupdatePasswordTokenExpireException() {
		when(forgotPasswordCodeRepository.findForgotPasswordCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.mockForgotPasswordCodes()));
		verify(forgotPasswordCodeRepository, times(0)).delete(DataBuilder.mockForgotPasswordCodes());
		Exception e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.updatePassword(DataBuilder.mockUserUpdatePasswordPayload()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testgetupdatePasswordAnotherException() throws JdxServiceException {
		when(forgotPasswordCodeRepository.findForgotPasswordCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.mockForgotPasswordCode()));
		Exception e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.updatePassword(DataBuilder.mockUserUpdatePasswordPayload()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testgetupdatePasswordAnotherTokenException() throws JdxServiceException {
		when(forgotPasswordCodeRepository.findForgotPasswordCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.mockForgotPasswordCode()));
		Exception e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.updatePassword(DataBuilder.mockUserUpdatePasswordPayload()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testgetupdatePasswordAnothersException() throws JdxServiceException {
		when(forgotPasswordCodeRepository.findForgotPasswordCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.empty());
		Exception e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.updatePassword(DataBuilder.mockUserUpdatePasswordPayload()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	public void testgetupdatePasswordException() throws JdxServiceException {
		UserUpdatePasswordPayload userUpdatePasswordPayload = null;
		Exception e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.updatePassword(userUpdatePasswordPayload));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	void testchangePassword() {
		User user = User.createDummyUser();
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(DataBuilder.mockChangePasswordPayload().getExistingPassword(), user.getPassword()))
				.thenReturn(true);
		when(userRepository.save(user)).thenReturn(user);
		assertEquals(userServiceImpl.changePassword(DataBuilder.mockChangePasswordPayload(),
				DataBuilder.getMockUserDetailsImpl()), true);
	}

	@Test
	void testchangePasswordfalse() {
		User user = User.createDummyUser();
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(DataBuilder.mockChangePasswordPayload().getExistingPassword(), user.getPassword()))
				.thenReturn(true);
		when(userRepository.save(user)).thenReturn(null);
		assertEquals(userServiceImpl.changePassword(DataBuilder.mockChangePasswordPayload(),
				DataBuilder.getMockUserDetailsImpl()), false);
	}

	@Test
	public void testchangePasswordUserIdException() throws JdxServiceException {
		Exception e = assertThrows(JdxServiceException.class, () -> userServiceImpl
				.changePassword(DataBuilder.mockChangePasswordPayloads(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	public void testchangePasswordUserPasswordException() throws JdxServiceException {
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		assertThrows(JdxServiceException.class, () -> userServiceImpl
				.changePassword(DataBuilder.mockChangePasswordPayloadPassword(), DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	public void testchangePasswordAnotherUserException() throws JdxServiceException {
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class, () -> userServiceImpl
				.changePassword(DataBuilder.mockChangePasswordPayload(), DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	public void testchangePasswordAnothersException() throws JdxServiceException {
		Exception e = assertThrows(JdxServiceException.class, () -> userServiceImpl
				.changePassword(DataBuilder.mockChangePasswordPayloads(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	public void testchangePasswordAnotherExceptions() throws JdxServiceException {
		UserDetailsImpl users = null;
		ChangePasswordPayload changePasswordPayload = null;
		Exception e = assertThrows(JdxServiceException.class,
				() -> userServiceImpl.changePassword(changePasswordPayload, users));
		e.printStackTrace();
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void teststartEmailUpdate() {
		User user = User.createDummyUser();
		user.setEmail("anushagunduka@gmail.com");
		DataBuilder.getMockEmailChange().setExistingEmail(user.getEmail());
		DataBuilder.getMockEmailChangePayload().setExistingEmail(user.getEmail());
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.empty());
		when(verificationCodeRepository.save((Mockito.any()))).thenReturn(DataBuilder.getVerificationCode());
		when(emailChangeRepository.save((Mockito.any()))).thenReturn(DataBuilder.getMockEmailChange());
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		assertEquals(userServiceImpl
				.startEmailUpdate("1L", "anjus@gmail.com", "89766", DataBuilder.getMockUserDetailsImpl()).getClass(),
				DataBuilder.getMockEmailChangePayload().getClass());
	}

	@Test
	@ExceptionHandler
	public void teststartEmailUpdateuserIdException() throws JdxServiceException {
		String userId = null;
		assertThrows(JdxServiceException.class, () -> userServiceImpl.startEmailUpdate(userId, "anjus@gmail.com",
				"89766", DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	@ExceptionHandler
	public void teststartEmailUpdateExistingUsersWithEmailException() throws JdxServiceException {
		User user = User.createDummyUser();
		user.setId("1L");
		user.setEmail("anushagunduka@gmail.com");
		DataBuilder.getMockEmailChange().setExistingEmail(user.getEmail());
		DataBuilder.getMockEmailChangePayload().setExistingEmail(user.getEmail());
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(user));
		assertThrows(JdxServiceException.class, () -> userServiceImpl.startEmailUpdate("1L", "anushagunduka@gmail.com",
				"89766", DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	@ExceptionHandler
	public void teststartEmailUpdatenewEmailException() throws JdxServiceException {
		String newEmail = null;
		assertThrows(JdxServiceException.class,
				() -> userServiceImpl.startEmailUpdate("1L", newEmail, "89766", DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	@ExceptionHandler
	public void teststartEmailUpdateclientIdException() throws JdxServiceException {
		String clientId = null;
		assertThrows(JdxServiceException.class, () -> userServiceImpl.startEmailUpdate("1L", "anjus@gmail.com",
				clientId, DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	@ExceptionHandler
	public void teststartEmailUpdateuserToUpdateException() throws JdxServiceException {
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class, () -> userServiceImpl.startEmailUpdate("1L", "anjus@gmail.com", "87966",
				DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	@ExceptionHandler
	public void teststartEmailUpdateanyExistingUsersWithEmailException() throws JdxServiceException {
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class, () -> userServiceImpl.startEmailUpdate("1L", "anjus@gmail.com", "87966",
				DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	@ExceptionHandler
	public void teststartEmailEmailException() throws JdxServiceException {
		assertThrows(JdxServiceException.class, () -> userServiceImpl.startEmailUpdate("1L", "anjus@gmail.com", "87966",
				DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	@ExceptionHandler
	void teststartEmailUpdatemailServiceException() throws JdxServiceException {
		User user = User.createDummyUser();
		user.setEmail("anushagunduka@gmail.com");
		DataBuilder.getMockEmailChange().setExistingEmail(user.getEmail());
		DataBuilder.getMockEmailChangePayload().setExistingEmail(user.getEmail());
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		when(userRepository.findUserByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.empty());
		when(verificationCodeRepository.save((Mockito.any()))).thenReturn(DataBuilder.getVerificationCode());
		when(emailChangeRepository.save((Mockito.any()))).thenReturn(DataBuilder.getMockEmailChange());
		assertThrows(JdxServiceException.class, () -> userServiceImpl.startEmailUpdate("1L", "anjus@gmail.com", "87966",
				DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	void testfinishEmailUpdate() {
		User user = User.createDummyUser();
		user.setClientId("12345");
		DataBuilder.getVerificationCode().setUser(DataBuilder.getVerificationCode().getUser());
		when(verificationCodeRepository.findVerificationCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.getVerificationCode()));
		when(emailChangeRepository.findEmailChangeByUserId((Mockito.any())))
				.thenReturn(Optional.of(DataBuilder.getMockEmailChange()));
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		when(userRepository.save(user)).thenReturn(user);
		assertEquals(userServiceImpl.finishEmailUpdate(DataBuilder.payload(), DataBuilder.getMockUserDetailsImpl())
				.getClass(), user.getClass());
	}

	@Test
	@ExceptionHandler
	public void testfinishEmailUpdateClientException() throws JdxServiceException {
		assertThrows(JdxServiceException.class, () -> userServiceImpl.finishEmailUpdate(DataBuilder.payloadClient(),
				DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	@ExceptionHandler
	public void testfinishEmailUpdateEmailException() throws JdxServiceException {
		assertThrows(JdxServiceException.class,
				() -> userServiceImpl.finishEmailUpdate(DataBuilder.payloads(), DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	@ExceptionHandler
	public void testfinishEmailUpdateVerificationCodeByCodeException() throws JdxServiceException {
		when(verificationCodeRepository.findVerificationCodeByCode(Mockito.anyString())).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class,
				() -> userServiceImpl.finishEmailUpdate(DataBuilder.payload(), DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	public void testfinishEmailUpdateClientIdException() throws JdxServiceException {
		User user = User.createDummyUser();
		user.setClientId("12345");
		DataBuilder.getVerificationCode().setUser(DataBuilder.getVerificationCode().getUser());
		when(verificationCodeRepository.findVerificationCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.getVerificationCode()));
		when(emailChangeRepository.findEmailChangeByUserId((Mockito.any()))).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class,
				() -> userServiceImpl.finishEmailUpdate(DataBuilder.payload(), DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	void testfinishEmailUpdateTimeUpdateException() {
		User user = User.createDummyUser();
		user.setClientId("12345");
		DataBuilder.getVerificationCode().setUser(DataBuilder.getVerificationCode().getUser());
		when(verificationCodeRepository.findVerificationCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.getVerificationCode()));
		when(emailChangeRepository.findEmailChangeByUserId((Mockito.any())))
				.thenReturn(Optional.of(DataBuilder.getMockEmailChanges()));
		assertThrows(JdxServiceException.class,
				() -> userServiceImpl.finishEmailUpdate(DataBuilder.payload(), DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	void testfinishEmailUpdateEmailnotequalException() {
		User user = User.createDummyUser();
		user.setClientId("12345");
		DataBuilder.getVerificationCode().setUser(DataBuilder.getVerificationCode().getUser());
		when(verificationCodeRepository.findVerificationCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.getVerificationCode()));
		when(emailChangeRepository.findEmailChangeByUserId((Mockito.any())))
				.thenReturn(Optional.of(DataBuilder.getMockEmailChangeEmail()));
		assertThrows(JdxServiceException.class,
				() -> userServiceImpl.finishEmailUpdate(DataBuilder.payload(), DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	@ExceptionHandler
	public void testfinishEmailUpdateException() throws JdxServiceException {
		User user = User.createDummyUser();
		user.setClientId("12345");
		DataBuilder.getVerificationCode().setUser(DataBuilder.getVerificationCode().getUser());
		when(verificationCodeRepository.findVerificationCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.getVerificationCode()));
		when(emailChangeRepository.findEmailChangeByUserId((Mockito.any())))
				.thenReturn(Optional.of(DataBuilder.getMockEmailChange()));
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		// when(userRepository.save(user)).thenReturn(user);
		assertThrows(JdxServiceException.class,
				() -> userServiceImpl.finishEmailUpdate(DataBuilder.payload(), DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	@ExceptionHandler
	public void testfinishEmailClientIdnotEqualException() throws JdxServiceException {
		User user = User.createDummyUser();
		user.setClientId("12345");
		DataBuilder.getVerificationCode().setUser(DataBuilder.getVerificationCode().getUser());
		when(verificationCodeRepository.findVerificationCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.getVerificationCode()));
		when(emailChangeRepository.findEmailChangeByUserId((Mockito.any())))
				.thenReturn(Optional.of(DataBuilder.getMockEmailChangeEmails()));
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		// when(userRepository.save(user)).thenReturn(user);
		assertThrows(JdxServiceException.class, () -> userServiceImpl.finishEmailUpdate(DataBuilder.payloadDifferent(),
				DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	@ExceptionHandler
	public void testfinishEmailuserException() throws JdxServiceException {
		User user = User.createDummyUser();
		user.setClientId("12345");
		DataBuilder.getVerificationCode().setUser(DataBuilder.getVerificationCode().getUser());
		when(verificationCodeRepository.findVerificationCodeByCode(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.getVerificationCode()));
		when(emailChangeRepository.findEmailChangeByUserId((Mockito.any())))
				.thenReturn(Optional.of(DataBuilder.getMockEmailChange()));
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		assertThrows(JdxServiceException.class,
				() -> userServiceImpl.finishEmailUpdate(DataBuilder.payload(), DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	void testupdateCode() {
		User user = User.createDummyUser();
		user.setFirstName("");
		user.setLastName("");
		user.setPrimaryAddress(null);
		user.setBillingAddress(null); 
		user.setDateOfBirth("1997-12-15");
		user.setPreferences(DataBuilder.getMockPreferences());
		user.setPatientDetails(DataBuilder.mockPatientDetails());
		user.setStripeCustomerId("");
		user.setXifinPatientId("1234");
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		when(userRepository.save(Mockito.any())).thenReturn(user);
		assertEquals(userServiceImpl
				.update(DataBuilder.mockuserUpdatePayloadAdress(), DataBuilder.getMockUserDetailsImpl()).getClass(),
				user.getClass());
	}
	
	
	@Test
	void testupdateCodePatientDetailsfoundVital() {
		DataBuilder.vitals.add(DataBuilder.mockVital());
		DataBuilder.medications.add(DataBuilder.mockMedication());
		User user = User.createDummyUser();
		user.setFirstName(""); 
		user.setLastName("");
		user.setPrimaryAddress(null);
		user.setBillingAddress(null);
		user.setDateOfBirth("1997-12-15");
		user.setPreferences(DataBuilder.getMockPreferences());
		user.setPatientDetails(DataBuilder.mockPatientDetails());
		user.setStripeCustomerId("");
		user.setXifinPatientId("1234");
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		when(userRepository.save(Mockito.any())).thenReturn(user);
		assertEquals(userServiceImpl
				.update(DataBuilder.mockuserUpdatePayloadAddress(), DataBuilder.getMockUserDetailsImpl()).getClass(),
				user.getClass());
	}
	
	@Test
	void testupdateCodePatientDetailsfoundVitalelse() {
		DataBuilder.vitals.add(DataBuilder.mockVital());
		DataBuilder.vitalss.add(DataBuilder.mockVitals());
		DataBuilder.medications.add(DataBuilder.mockMedication());
		User user = User.createDummyUser();
		user.setFirstName(""); 
		user.setLastName("");
		user.setPrimaryAddress(null);
		user.setBillingAddress(null);
		user.setDateOfBirth("1997-12-15");
		user.setPreferences(DataBuilder.getMockPreferences());
		user.setPatientDetails(DataBuilder.mockPatientDetails());
		user.setStripeCustomerId("");
		user.setXifinPatientId("1234");
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		when(userRepository.save(Mockito.any())).thenReturn(user);
		assertEquals(userServiceImpl
				.update(DataBuilder.mockuserUpdatePayloadAddresselse(), DataBuilder.getMockUserDetailsImpl()).getClass(),
				user.getClass());
	}
	
	@Test
	@ExceptionHandler
	public void testupdateCodeUserIdException() throws JdxServiceException {
		assertThrows(JdxServiceException.class, () -> userServiceImpl.update(DataBuilder.mockuserUpdatePayloadId(),
				DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	@ExceptionHandler
	public void testupdateCodeUserException() throws JdxServiceException {
		when(userRepository.findById(DataBuilder.mockuserUpdatePayload().getId())).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class, () -> userServiceImpl.update(DataBuilder.mockuserUpdatePayload(),
				DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	void testupdatePreferences() {
		Preferences preferences = new Preferences("6L", user, false, false);
		User user = User.createDummyUser();
		user.setPreferences(preferences);
		assertEquals(preferences, user.getPreferences());
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		assertEquals(userServiceImpl.updatePreferences("1L", DataBuilder.mockUserUpdatePreferencesPayload(),
				DataBuilder.getMockUserDetailsImpl()).getClass(), user.getClass());
	}

	@Test
	@ExceptionHandler
	public void testupdatePreferencesuserUpdatePreferencesPayloadException() throws JdxServiceException {
		UserUpdatePreferencesPayload userUpdatePreferencesPayload = null;
		assertThrows(JdxServiceException.class, () -> userServiceImpl.updatePreferences("1L",
				userUpdatePreferencesPayload, DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	@ExceptionHandler
	public void testupdatePreferencesUserException() throws JdxServiceException {
		String userId = null;
		when(userRepository.findById(userId)).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class, () -> userServiceImpl.updatePreferences(userId,
				DataBuilder.mockUserUpdatePreferencesPayload(), DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	void testupdatePreferenceDetails() {
		assertEquals(userServiceImpl
				.updatePreferenceDetails(DataBuilder.mockUserPreferences(),
						DataBuilder.mockUserUpdatePreferencesPayload(), DataBuilder.getMockUserDetailsImpl())
				.getClass(), DataBuilder.mockUserPreferences().getClass());
	}

	@Test
	@ExceptionHandler
	public void testupdatePreferenceDetailsPreferencesPayloadException() throws JdxServiceException {
		UserUpdatePreferencesPayload userUpdatePreferencesPayload = null;
		assertThrows(JdxServiceException.class, () -> userServiceImpl.updatePreferenceDetails(user,
				userUpdatePreferencesPayload, DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	void testarchive() {
		User user = User.createDummyUser();
		when(userRepository.findById("1L")).thenReturn(Optional.of(user));
		user.setStatus(UserStatus.ARCHIVED);
		assertEquals(UserStatus.ARCHIVED, user.getStatus());
		when(userRepository.save(user)).thenReturn(user);
		assertEquals(userServiceImpl.archive("1L").getClass(), user.getClass());
	}

	@Test
	@ExceptionHandler
	public void testarchiveException() throws JdxServiceException {
		when(userRepository.findById("1L")).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class, () -> userServiceImpl.archive("1L"));

	}

	@Test
	void testhandleChart() {
		User user = User.createDummyUser();
		List<Medication> medications = new ArrayList<>();
		medications.add(DataBuilder.mockMedication());
		List<Provider> providers = new ArrayList<>();
		providers.add(DataBuilderOrder.mockapprovingProvider());
		List<PatientChartEntry> patientChartEntrys = new ArrayList<>();
		patientChartEntrys.add(DataBuilder.mockPatientChartEntry());
		when(userRepository.findById("1L")).thenReturn(Optional.of(user));
		when(patientChartEntryRepository.save(Mockito.any())).thenReturn(DataBuilder.mockPatientChartEntry());
		when(userRepository.save(Mockito.any())).thenReturn(user);
		assertEquals(userServiceImpl
				.handleChart("1L", DataBuilder.mockPatientChartEntry(), DataBuilder.getMockUserDetailsImpl())
				.getClass(), DataBuilder.mockPatientChartEntry().getClass());
	}

	@Test
	void testhandleChartPatient() {
		DataBuilder.patientChartEntrys.add(DataBuilder.mockPatientChartEntrys());
		DataBuilder.mockUser().setPatientDetails(DataBuilder.mockPatientDetails());
		List<Medication> medications = new ArrayList<>();
		medications.add(DataBuilder.mockMedication());
		List<Provider> providers = new ArrayList<>();
		providers.add(DataBuilderOrder.mockapprovingProvider());
		List<PatientChartEntry> patientChartEntrys = new ArrayList<>();
		patientChartEntrys.add(DataBuilder.mockPatientChartEntry());
		String userId = "1L";
		assertEquals(userId, DataBuilder.mockUser().getId());
		when(userRepository.findById(userId)).thenReturn(Optional.of(DataBuilder.mockUser()));
		when(patientChartEntryRepository.save(Mockito.any())).thenReturn(DataBuilder.mockPatientChartEntry());
		when(userRepository.save(Mockito.any())).thenReturn(DataBuilder.mockUser());
		assertEquals(userServiceImpl
				.handleChart(userId, DataBuilder.mockPatientChartEntry(), DataBuilder.getMockUserDetailsImpl())
				.getClass(), DataBuilder.mockPatientChartEntry().getClass());
	}

	@Test
	@ExceptionHandler
	public void testhandleChartentryException() throws JdxServiceException {
		when(userRepository.findById("1L")).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class, () -> userServiceImpl.handleChart("1L",
				DataBuilder.mockPatientChartEntry(), DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	@ExceptionHandler
	public void testhandleChartUserException() throws JdxServiceException {
		String userId = null;
		PatientChartEntry entry = null;
		assertThrows(JdxServiceException.class,
				() -> userServiceImpl.handleChart(userId, entry, DataBuilder.getMockUserDetailsImpl()));

	}

	@Test
	void testgetChartEntry() {
		User user = User.createDummyUser();
		user.setPatientDetails(DataBuilder.mockPatientDetails());
		String chartEntryId = "2L";
		String userId = "1L";
		assertEquals(chartEntryId, DataBuilder.mockPatientChartEntrys().getId());
		assertEquals(userId, DataBuilder.mockUser().getId());
		DataBuilder.patientChartEntrys.add(DataBuilder.mockPatientChartEntrys());
		DataBuilder.mockPatientCharts().setChartEntries(DataBuilder.patientChartEntrys);
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		assertEquals(userServiceImpl.getChartEntry(userId, chartEntryId).getClass(),
				DataBuilder.mockPatientChartEntrys().getClass());
	}

	@Test
	@ExceptionHandler
	public void testgetChartEntryException() throws JdxServiceException {
		String chartEntryId = "1L";
		String userId = "2L";
		when(userRepository.findById("2L")).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class, () -> userServiceImpl.getChartEntry(userId, chartEntryId));
	}

	@Test
	@ExceptionHandler
	public void testgetChartEntrieiesException() throws JdxServiceException {
		String chartEntryId = "1L";
		String userId = "2L";
		User user = User.createDummyUser();
		user.setPatientDetails(DataBuilder.mockPatientDetailException());
		List<Medication> medications = new ArrayList<>();
		medications.add(DataBuilder.mockMedication());
		List<Provider> providers = new ArrayList<>();
		providers.add(DataBuilderOrder.mockapprovingProvider());
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		assertThrows(JdxServiceException.class, () -> userServiceImpl.getChartEntry(userId, chartEntryId));
	}

	@Test
	void testgetChart() {
		User user = User.createDummyUser();
		user.setPatientDetails(DataBuilder.mockPatientDetails());
		List<Medication> medications = new ArrayList<>();
		medications.add(DataBuilder.mockMedication());
		List<Provider> providers = new ArrayList<>();
		providers.add(DataBuilderOrder.mockapprovingProvider());
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		assertEquals(userServiceImpl.getChart(Mockito.anyString()).getClass(),
				DataBuilder.mockPatientChart().getClass());
	}

	@Test
	@ExceptionHandler
	public void testgetChartException() throws JdxServiceException {
		String userId = "2L";
		when(userRepository.findById("2L")).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class, () -> userServiceImpl.getChart(userId));
	}

	@Test
	void testgetChartPatientDetailsException() {
		User user = User.createDummyUser();
		String userId = "2L";
		user.setPatientDetails(DataBuilder.mockPatientDetailsEmptyChart());
		List<Medication> medications = new ArrayList<>();
		medications.add(DataBuilder.mockMedication());
		List<Provider> providers = new ArrayList<>();
		providers.add(DataBuilderOrder.mockapprovingProvider());
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		assertThrows(JdxServiceException.class, () -> userServiceImpl.getChart(userId));
	}

	@Test
	void testdelete() { 
		User user = User.createDummyUser();
		when(userRepository.findById("73L")).thenReturn(Optional.of(user));
		verify(refreshTokenRepository, times(0)).deleteByUser(user);
		//verify(userRepository, times(0)).deleteById("73L");
		userServiceImpl.delete("73L");
	}
 
	@Test
	void testdeleteByUserId() {
		userServiceImpl.deleteByUserId("73L");
	}

	@Test
	void testloadAdditionalData() {
		User user = User.createDummyUser();
		List<Medication> medications = new ArrayList<>();
		medications.add(DataBuilder.mockMedication());
		List<Provider> providers = new ArrayList<>();
		providers.add(DataBuilderOrder.mockapprovingProvider());
		String[] includes = { "all" };
		when(patientDetailsService.getByUserId(user.getId(), includes))
				.thenReturn(Optional.of(DataBuilder.mockPatientDetails()));
		assertEquals(userServiceImpl.loadAdditionalData(user, includes).getClass(), user.getClass());
	}

	@Test
	void testupdateChartFromSalesforce() {
		User user = User.createDummyUser();
		testResult.setJunoTestResultId("12345");
		List<SalesforceRecordChanged> salesforceRecordChangeds = new ArrayList<>();
		salesforceRecordChangeds.add(DataBuilder.mockSalesforceRecordChanged());
		List<SalesforceChartUpdateInfo> salesforceChartUpdateInfos = new ArrayList<>();
		salesforceChartUpdateInfos.add(DataBuilder.mockSalesforceChartUpdateInfo());
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		userServiceImpl.updateChartFromSalesforce(salesforceRecordChangeds, salesforceChartUpdateInfos,
				DataBuilder.getMockUserDetailsImpl());
	}

	@Test
	public void testupdateChartFromSalesforceRecord() throws JdxServiceException {
		List<SalesforceRecordChanged> salesforceRecordChangeds = new ArrayList<>();
		salesforceRecordChangeds.add(DataBuilder.mockSalesforceRecordChanged());
		List<SalesforceChartUpdateInfo> salesforceChartUpdateInfos = new ArrayList<>();
		userServiceImpl.updateChartFromSalesforce(salesforceRecordChangeds, salesforceChartUpdateInfos,
				DataBuilder.getMockUserDetailsImpl());
	}
	
	@Test
	public void testupdateChartFromSalesforceJunoId() throws JdxServiceException {
		List<SalesforceRecordChanged> salesforceRecordChangeds = new ArrayList<>();
		salesforceRecordChangeds.add(DataBuilder.mockSalesforceRecordChanged());
		List<SalesforceChartUpdateInfo> salesforceChartUpdateInfos = new ArrayList<>();
		salesforceChartUpdateInfos.add(DataBuilder.mockSalesforceChartUpdateInfoException());
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		assertThrows(JdxServiceException.class, () -> 
		userServiceImpl.updateChartFromSalesforce(salesforceRecordChangeds, salesforceChartUpdateInfos,
				DataBuilder.getMockUserDetailsImpl()));
	}

 
	@Test
	public void testupdateChartFromEntriesSalesforce() throws JdxServiceException {
		DataBuilder.patientChartEntrys.add(DataBuilder.mockPatientChartEntrys());
		DataBuilder.mockPatientCharts().setChartEntries(DataBuilder.patientChartEntrys);
		testResult.setJunoTestResultId("12345");
		List<SalesforceRecordChanged> salesforceRecordChangeds = new ArrayList<>();
		salesforceRecordChangeds.add(DataBuilder.mockSalesforceRecordChanged());
		List<SalesforceChartUpdateInfo> salesforceChartUpdateInfos = new ArrayList<>();
		salesforceChartUpdateInfos.add(DataBuilder.mockSalesforceChartUpdateInfo());
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilder.mockUser()));
		userServiceImpl.updateChartFromSalesforce(salesforceRecordChangeds, salesforceChartUpdateInfos,
				DataBuilder.getMockUserDetailsImpl());
	}
	
	@Test
	void testupdateUserFromSalesforce() {
		List<SalesforceUserUpdateAccountInfo> salesforceUserUpdateAccountInfos = new ArrayList<>();
		salesforceUserUpdateAccountInfos.add(DataBuilder.mockSalesforceUserUpdateAccountInfo());
		List<SalesforceRecordChanged> salesforceRecordChangeds = new ArrayList<>();
		salesforceRecordChangeds.add(DataBuilder.mockSalesforceRecordChanged());
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		userServiceImpl.updateUserFromSalesforce(salesforceRecordChangeds, salesforceUserUpdateAccountInfos,
				DataBuilder.getMockUserDetailsImpl());
	}
	@Test
	void testupdateUserFromSalesforceEmpty() {
		List<SalesforceUserUpdateAccountInfo> salesforceUserUpdateAccountInfos = new ArrayList<>();
		//salesforceUserUpdateAccountInfos.add(DataBuilder.mockSalesforceUserUpdateAccountInfo());
		List<SalesforceRecordChanged> salesforceRecordChangeds = new ArrayList<>();
		salesforceRecordChangeds.add(DataBuilder.mockSalesforceRecordChanged());
		userServiceImpl.updateUserFromSalesforce(salesforceRecordChangeds, salesforceUserUpdateAccountInfos,
				DataBuilder.getMockUserDetailsImpl());
	}
	
	@Test
	void testupdateUserFromSalesforcePhoneEmpty() {
		User user1=new User();
		user1.setPrimaryPhone(null);
		List<SalesforceUserUpdateAccountInfo> salesforceUserUpdateAccountInfos = new ArrayList<>();
		salesforceUserUpdateAccountInfos.add(DataBuilder.mockSalesforceUserUpdateAccountInfo());
		List<SalesforceRecordChanged> salesforceRecordChangeds = new ArrayList<>();
		salesforceRecordChangeds.add(DataBuilder.mockSalesforceRecordChanged());
		when(userRepository.findById(Mockito.anyString()))
		.thenReturn(Optional.of(user1));
		userServiceImpl.updateUserFromSalesforce(salesforceRecordChangeds, salesforceUserUpdateAccountInfos,
				DataBuilder.getMockUserDetailsImpl());
	}
	
	
	@Test
	public void testupdateUserFromSalesforceAnotherException() throws JdxServiceException {
		testResult.setJunoTestResultId("12345");
		List<SalesforceRecordChanged> salesforceRecordChangeds = new ArrayList<>();
		salesforceRecordChangeds.add(DataBuilder.mockSalesforceRecordChanged());
		List<SalesforceUserUpdateAccountInfo> salesforceUserUpdateAccountInfos = new ArrayList<>();
		salesforceUserUpdateAccountInfos.add(DataBuilder.mockSalesforceUserUpdateAccountInfo());
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class, () -> userServiceImpl.updateUserFromSalesforce(salesforceRecordChangeds,
				salesforceUserUpdateAccountInfos, DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	@ExceptionHandler
	public void testupdateUserFromSalesforceException() throws JdxServiceException {
		List<SalesforceRecordChanged> salesforceRecordChangeds = null;
		List<SalesforceUserUpdateAccountInfo> salesforceUserUpdateAccountInfos = null;
		assertThrows(JdxServiceException.class, () -> userServiceImpl.updateUserFromSalesforce(salesforceRecordChangeds,
				salesforceUserUpdateAccountInfos, DataBuilder.getMockUserDetailsImpl()));

	}

}
