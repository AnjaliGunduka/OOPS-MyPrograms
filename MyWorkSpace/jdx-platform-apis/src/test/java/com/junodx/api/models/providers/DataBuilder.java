package com.junodx.api.models.providers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.connectors.messaging.payloads.EntityPayload;
import com.junodx.api.connectors.messaging.payloads.EventType;
import com.junodx.api.controllers.payloads.OrderLineItemInfo;
import com.junodx.api.controllers.payloads.SFBillingAddress;
import com.junodx.api.controllers.payloads.SalesforceAttributes;
import com.junodx.api.controllers.payloads.SalesforceChangedDataMap;
import com.junodx.api.controllers.payloads.SalesforceChartUpdateInfo;
import com.junodx.api.controllers.payloads.SalesforceOrderUpdateAccountInfo;
import com.junodx.api.controllers.payloads.SalesforceRecordChanged;
import com.junodx.api.controllers.payloads.SalesforceUserUpdateAccountInfo;
import com.junodx.api.controllers.payloads.SalesforceWhoRecord;
import com.junodx.api.controllers.payloads.SalesforceChartUpdateInfo.TestResult;
import com.junodx.api.controllers.payloads.SalesforceLineItemUpdateInfo;
import com.junodx.api.controllers.users.payloads.ChangePasswordPayload;
import com.junodx.api.controllers.users.payloads.EmailChangePayload;
import com.junodx.api.controllers.users.payloads.PostRegistrationPayload;
import com.junodx.api.controllers.users.payloads.RegisterUserPayload;
import com.junodx.api.controllers.users.payloads.UserForgotPasswordPayload;
import com.junodx.api.controllers.users.payloads.UserUpdatePasswordPayload;
import com.junodx.api.controllers.users.payloads.UserVerificationPayload;
import com.junodx.api.controllers.users.payloads.VerificationCodeResponsePayload;
import com.junodx.api.controllers.users.payloads.UserUpdate.UserUpdateFetalSexResultsPreferences;
import com.junodx.api.controllers.users.payloads.UserUpdate.UserUpdateMedicalDetails;
import com.junodx.api.controllers.users.payloads.UserUpdate.UserUpdatePatientDetailsPayload;
import com.junodx.api.controllers.users.payloads.UserUpdate.UserUpdatePayload;
import com.junodx.api.controllers.users.payloads.UserUpdate.UserUpdatePreferencesPayload;
import com.junodx.api.dto.models.auth.UserBatchDto;
import com.junodx.api.models.auth.Authority;
import com.junodx.api.models.auth.EmailChange;
import com.junodx.api.models.auth.FetalSexResultsPreferences;
import com.junodx.api.models.auth.ForgotPasswordCode;
import com.junodx.api.models.auth.Preferences;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.auth.VerificationCode;
import com.junodx.api.models.auth.types.GenderTerms;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.Phone;
import com.junodx.api.models.core.types.DataType;
import com.junodx.api.models.core.types.PhoneType;
import com.junodx.api.models.core.types.State;
import com.junodx.api.models.patient.MedicalDetails;
import com.junodx.api.models.patient.Medication;
import com.junodx.api.models.patient.PatientChart;
import com.junodx.api.models.patient.PatientChartEntry;
import com.junodx.api.models.patient.PatientDetails;
import com.junodx.api.models.patient.Vital;
import com.junodx.api.models.patient.types.MedicationType;
import com.junodx.api.models.patient.types.VitalType;
import com.junodx.api.services.auth.UserDetailsImpl;

public class DataBuilder {
 
	public static User mockUser() {
		User u = new User();
		u.setUsername("General_test_user");
		u.setFirstName("Juno_test");
		u.setLastName("User");
		u.setEmail("no-email@junodx.com");
		u.setUserType(UserType.STANDARD);
		u.setDateOfBirth("1997-12-15");
		u.setPatientDetails(DataBuilder.mockPatientDetails());
		u.setLimsContactId("78788");
		u.setId("1L");
		u.setPassword("Password&123");
		u.setClientId("12345");
		u.setXifinPatientId("1234");
		u.setStripeCustomerId("34677676");
		u.setActivated(true);
		u.setStatus(UserStatus.ACTIVATED);
		u.setActivationTs(Calendar.getInstance());
		u.setPrimaryPhone(DataBuilder.getMockPhone());
		u.setBillingAddress(DataBuilder.getMockAddress());
		u.setPreferences(DataBuilder.getMockPreferences());
		u.setMeta(DataBuilder.getMockMeta());
		return u;

	}

	public static EntityPayload mockEntityPayload() {
		EntityPayload msg = new EntityPayload();
		msg.setEntity(mockUser());
		msg.setEvent(EventType.CREATE);
		msg.setEventTs(Calendar.getInstance());
		msg.setStatus("STANDARD");
		return msg;
	}

	public static User mockUserPreferences() {
		User u = new User();
		u.setUsername("General_test_user");
		u.setFirstName("Juno_test");
		u.setLastName("User");
		u.setEmail("no-email@junodx.com");
		u.setUserType(UserType.STANDARD);
		u.setDateOfBirth("1997-12-15");
		u.setPatientDetails(DataBuilder.mockPatientDetails());
		u.setLimsContactId("78788");
		u.setId("1L");
		u.setPassword("Password&123");
		u.setClientId("12345");
		u.setXifinPatientId("1234");
		u.setStripeCustomerId("34677676");
		u.setActivated(true);
		u.setStatus(UserStatus.ACTIVATED);
		u.setActivationTs(Calendar.getInstance());
		u.setPrimaryPhone(DataBuilder.getMockPhone());
		u.setBillingAddress(DataBuilder.getMockAddress());
		u.setPreferences(DataBuilder.getMockPreference());
		u.setMeta(DataBuilder.getMockMeta());
		return u;

	}

	public static User mockUserFirst() {
		User u = new User();
		u.setUsername("General_test_user");
		u.setFirstName(null);
		u.setLastName(null);
		u.setEmail("no-email@junodx.com");
		u.setUserType(UserType.STANDARD);
		u.setDateOfBirth("1997-12-15");
		u.setPatientDetails(DataBuilder.mockPatientDetails());
		u.setLimsContactId("78788");
		u.setId("1L");
		u.setPassword("Password&123");
		u.setClientId("12345");
		u.setXifinPatientId("1234");
		u.setStripeCustomerId("34677676");
		u.setActivated(false);
		u.setStatus(UserStatus.ACTIVATED);
		u.setActivationTs(Calendar.getInstance());
		u.setPrimaryPhone(DataBuilder.getMockPhone());
		u.setBillingAddress(DataBuilder.getMockAddress());
		u.setPreferences(DataBuilder.getMockPreferences());
		u.setMeta(DataBuilder.getMockMeta());
		return u;

	}

	public static User mockUserForUpdate() {
		User u = new User();
		u.setUsername("General_test_user");
		u.setFirstName("Juno_tests");
		u.setLastName("Users");
		u.setEmail("no-email@junodx.com");
		u.setUserType(UserType.STANDARD);
		u.setDateOfBirth("1997-12-16");
		u.setPatientDetails(DataBuilder.mockPatientDetails());
		u.setLimsContactId("78788");
		u.setId("1L");
		u.setPassword("Password&123");
		u.setClientId("9348-8892-9342-01");
		u.setXifinPatientId("12345");
		u.setStripeCustomerId("346776766");
		u.setActivated(false);
		u.setStatus(UserStatus.ACTIVATED);
		u.setActivationTs(Calendar.getInstance());
		u.setPrimaryPhone(DataBuilder.getMockPhone());
		u.setBillingAddress(DataBuilder.getMockAddress());
		u.setPreferences(DataBuilder.getMockPreferences());
		u.setMeta(DataBuilder.getMockMeta());
		return u;

	}

	public static User mockUserForUpdatePatientPortal() {
		User u = new User();
		u.setUsername("General_test_user");
		u.setFirstName("Juno_tests");
		u.setLastName("Users");
		u.setEmail("no-email@junodx.com");
		u.setUserType(UserType.STANDARD);
		u.setDateOfBirth("1997-12-16");
		u.setPatientDetails(DataBuilder.mockPatientDetails());
		u.setLimsContactId("78788");
		u.setId("1L");
		u.setPassword("Password&123");
		u.setClientId("9348-8892-9342-01");
		u.setXifinPatientId("12345");
		u.setStripeCustomerId("346776766");
		u.setActivated(false);
		u.setStatus(UserStatus.ACTIVATED);
		u.setActivationTs(Calendar.getInstance());
		u.setPrimaryPhone(DataBuilder.getMockPhone());
		u.setBillingAddress(DataBuilder.getMockAddress());
		u.setPreferences(DataBuilder.getMockPreferences());
		u.setMeta(DataBuilder.getMockMeta());
		return u;

	}

	public static User mockUserForUpdatePatientPortalException() {
		User u = new User();
		u.setUsername("General_test_user");
		u.setFirstName("Juno_tests");
		u.setLastName("Users");
		u.setEmail("no-email@junodx.com");
		u.setUserType(UserType.STANDARD);
		u.setDateOfBirth("1997-12-16");
		u.setPatientDetails(DataBuilder.mockPatientDetails());
		u.setLimsContactId("78788");
		u.setId("1L");
		u.setPassword("Password&123");
		u.setClientId("9348");
		u.setXifinPatientId("12345");
		u.setStripeCustomerId("346776766");
		u.setActivated(false);
		u.setStatus(UserStatus.ACTIVATED);
		u.setActivationTs(Calendar.getInstance());
		u.setPrimaryPhone(DataBuilder.getMockPhone());
		u.setBillingAddress(DataBuilder.getMockAddress());
		u.setPreferences(DataBuilder.getMockPreferences());
		u.setMeta(DataBuilder.getMockMeta());
		return u;

	}

	public static User mockUserForUpdateExceptions() {
		User u = new User();
		u.setUsername("General_test_user");
		u.setFirstName("Juno_tests");
		u.setLastName("Users");
		u.setEmail("no-email@junodx.com");
		u.setUserType(UserType.STANDARD);
		u.setDateOfBirth("1997-12-16");
		u.setPatientDetails(DataBuilder.mockPatientDetails());
		u.setLimsContactId("78788");
		u.setId("1L");
		u.setPassword("Password&123");
		u.setClientId("12345");
		u.setXifinPatientId("12345");
		u.setStripeCustomerId("346776766");
		u.setActivated(false);
		u.setStatus(UserStatus.ACTIVATED);
		u.setActivationTs(Calendar.getInstance());
		u.setPrimaryPhone(DataBuilder.getMockPhone());
		u.setBillingAddress(DataBuilder.getMockAddress());
		u.setPreferences(DataBuilder.getMockPreferences());
		u.setMeta(DataBuilder.getMockMeta());
		return u;

	}

	public static User mockUserForUpdateLabPortal() {
		User u = new User();
		u.setUsername("General_test_user");
		u.setFirstName("Juno_tests");
		u.setLastName("Users");
		u.setEmail("no-email@junodx.com");
		u.setUserType(UserType.STANDARD);
		u.setDateOfBirth("1997-12-16");
		u.setPatientDetails(DataBuilder.mockPatientDetails());
		u.setLimsContactId("78788");
		u.setId("1L");
		u.setPassword("Password&123");
		u.setClientId("9358-4292-7682-02");
		u.setXifinPatientId("12345");
		u.setStripeCustomerId("346776766");
		u.setActivated(false);
		u.setStatus(UserStatus.ACTIVATED);
		u.setActivationTs(Calendar.getInstance());
		u.setPrimaryPhone(DataBuilder.getMockPhone());
		u.setBillingAddress(DataBuilder.getMockAddress());
		u.setPreferences(DataBuilder.getMockPreferences());
		u.setMeta(DataBuilder.getMockMeta());
		return u;

	}

	public static User mockUserForUpdateproviderPortal() {
		User u = new User();
		u.setUsername("General_test_user");
		u.setFirstName("Juno_tests");
		u.setLastName("Users");
		u.setEmail("no-email@junodx.com");
		u.setUserType(UserType.STANDARD);
		u.setDateOfBirth("1997-12-16");
		u.setPatientDetails(DataBuilder.mockPatientDetails());
		u.setLimsContactId("78788");
		u.setId("1L");
		u.setPassword("Password&123");
		u.setClientId("0352-8232-9622-03");
		u.setXifinPatientId("12345");
		u.setStripeCustomerId("346776766");
		u.setActivated(false);
		u.setStatus(UserStatus.ACTIVATED);
		u.setActivationTs(Calendar.getInstance());
		u.setPrimaryPhone(DataBuilder.getMockPhone());
		u.setBillingAddress(DataBuilder.getMockAddress());
		u.setPreferences(DataBuilder.getMockPreferences());
		u.setMeta(DataBuilder.getMockMeta());
		return u;

	}

	public static User mockUserForUpdates() {
		User u = new User();
		u.setUsername("General_test_user");
		u.setFirstName("Juno_tests");
		u.setLastName("Users");
		u.setEmail("no-email@junodx.com");
		u.setUserType(UserType.STANDARD);
		u.setDateOfBirth("1997-12-16");
		u.setPatientDetails(DataBuilder.mockPatientDetails());
		u.setLimsContactId("78788");
		u.setId("1L");
		u.setPassword("Password&123");
		u.setClientId("12345");
		u.setXifinPatientId("12345");
		u.setStripeCustomerId("346776766");
		u.setActivated(true);
		u.setStatus(UserStatus.ACTIVATED);
		u.setActivationTs(Calendar.getInstance());
		u.setPrimaryPhone(DataBuilder.getMockPhone());
		u.setBillingAddress(DataBuilder.getMockAddress());
		u.setPreferences(DataBuilder.getMockPreferences());
		u.setMeta(DataBuilder.getMockMeta());
		return u;

	}

	public static User mockUsers() {
		User u = new User();
		u.setUsername("General_test_user");
		u.setFirstName("Juno_test");
		u.setLastName("User");
		u.setEmail("jon+41@junodx.com");
		u.setUserType(UserType.TEST);
		u.setDateOfBirth("1997-12-15");
		u.setClientId("9348-8892-9342-01");
		u.setPatientDetails(DataBuilder.mockPatientDetails());
		u.setLimsContactId("78788");
		u.setId("1L");
		u.setActivated(true);
		u.setStatus(UserStatus.ACTIVATED);
		u.setActivationTs(Calendar.getInstance());
		u.setPrimaryPhone(DataBuilder.getMockPhone());
		u.setBillingAddress(DataBuilder.getMockAddress());
		u.setPreferences(DataBuilder.getMockPreferences());
		u.setMeta(DataBuilder.getMockMeta());
		return u;

	}

	public static Location getMockLocation() {
		Location location = new Location();
		location.setId("1");
		location.setName("Juno Headquarters");
		location.setPhone(getMockPhone());
		location.setAddress(getMockAddress());
		// location.setPractice(getMockPractice());
		return location;
	}

	public static Phone getMockPhone() {
		Phone phone = new Phone();
		phone.setAreaCode("93023");
		phone.setCountry("United States");
		phone.setCountryCode("93023");
		phone.setDigits(0);
		phone.setPhoneNumber("8586472374");
		phone.setPhoneType(PhoneType.MOBILE);
		phone.setPrimaryPhone(false);
		return phone;
	}

	public static Practice getMockPractice() {
		Practice practice = new Practice();
		practice.setActive(true);
		practice.setBillingEmail("billing@junodx.com");
		practice.setContactAddress(getMockAddress());
		practice.setDefaultPractice(true);
		practice.setId("2L");
		practice.setLimsId("78788");
		// List<Location> locations = new ArrayList<Location>();
		// locations.add(getMockLocation());
		// practice.setLocations(locations);
//		practice.setMeta(getMockMeta());
		practice.setName("Juno Diagnostics Medical");
		practice.setPatientEmail("support@junodx.com");
		practice.setPrimaryEmail("support@junodx.com");
		// practice.setPrimaryPhone(getMockPhone());
//		List<Provider> provide = new ArrayList<Provider>();
//		provide.add(getMockProvider());
//		practice.setProviders(provide);
//		practice.setSalesforceId(null);
//		practice.setXifinId(null);
		return practice;
	}
	
	public static Practice getMockPracticeLims() {
		Practice practice = new Practice();
		practice.setActive(true);
		practice.setBillingEmail("billing@junodx.com");
		practice.setContactAddress(getMockAddress());
		practice.setDefaultPractice(true);
		practice.setId("2L");
		practice.setLimsId(null);
		// List<Location> locations = new ArrayList<Location>();
		// locations.add(getMockLocation());
		// practice.setLocations(locations);
//		practice.setMeta(getMockMeta());
		practice.setName("Juno Diagnostics Medical");
		practice.setPatientEmail("support@junodx.com");
		practice.setPrimaryEmail("support@junodx.com");
		// practice.setPrimaryPhone(getMockPhone());
//		List<Provider> provide = new ArrayList<Provider>();
//		provide.add(getMockProvider());
//		practice.setProviders(provide);
//		practice.setSalesforceId(null);
//		practice.setXifinId(null);
		return practice;
	}

	public static Meta getMockMeta() {
		Meta meta = new Meta();
		meta.setCreatedAt(Calendar.getInstance());
		meta.setCreatedBy(getMockUserDetailsImpl().getEmail());
		meta.setCreatedById(getMockUserDetailsImpl().getUserId());
		meta.setLastModifiedAt(Calendar.getInstance());
		meta.setLastModifiedBy(getMockUserDetailsImpl().getEmail());
		meta.setLastModifiedById(getMockUserDetailsImpl().getUserId());
		return meta;
	}

	public static Address getMockAddress() {
		Address address = new Address();
		address.setCity("San Diego");
		address.setCountry("United States");
		address.setName("Juno Headquarters");
		address.setPostalCode("98077");
		address.setPrimaryAddress(true);
		address.setState("CA");
		address.setStreet("11511 Sorrento Valley Rd");
		address.setPrimaryMailingAddress(true);
		return address;
	}

	public static User user = User.createDummyUser();

	public static VerificationCode getVerificationCode() {
		VerificationCode verificationCode = new VerificationCode();
		verificationCode.setCode(verificationCode.generateCode());
		verificationCode.setCreatedAt(Calendar.getInstance());
		verificationCode.setUser(user);
		verificationCode.setId(1L);
		verificationCode.setExpiresAt(Calendar.getInstance());
		Calendar expiresAt = Calendar.getInstance();
		expiresAt = Calendar.getInstance();
		return verificationCode;
	}

	public static VerificationCode getVerificationCodes() {
		VerificationCode verificationCode = new VerificationCode();
		// verificationCode.setCode(verificationCode.generateCode());
		// verificationCode.setCreatedAt(Calendar.getInstance());
		// verificationCode.setUser(user);
		verificationCode.setId(1L);
		verificationCode.setExpiresAt(Calendar.getInstance());
		Calendar expiresAt = Calendar.getInstance();
		expiresAt = Calendar.getInstance();
		return verificationCode;
	}

	public static UserUpdateMedicalDetails mockUserUpdateMedicalDetails() {
		UserUpdateMedicalDetails userUpdateMedicalDetails = new UserUpdateMedicalDetails();
		userUpdateMedicalDetails.setPregnant(true);
		userUpdateMedicalDetails.setGestationalAge(30.00f);
		userUpdateMedicalDetails.setNumberOfFetuses(1);
		userUpdateMedicalDetails.setThreeOrMoreFetuses(true);
		userUpdateMedicalDetails.setNoBloodTransfusion(true);
		userUpdateMedicalDetails.setNoOrganTransplant(true);
		userUpdateMedicalDetails.setVitals(vitals);
		return userUpdateMedicalDetails;

	}

	public static UserUpdatePatientDetailsPayload mockUserUpdatePatientDetailsPayloads() {
		UserUpdatePatientDetailsPayload userUpdatePatientDetailsPayload = new UserUpdatePatientDetailsPayload();
		userUpdatePatientDetailsPayload.setMedicalDetails(mockUserUpdateMedicalDetailss());
		return userUpdatePatientDetailsPayload;

	}

	public static List<Vital> vitalss = new ArrayList<>();

	public static UserUpdateMedicalDetails mockUserUpdateMedicalDetailss() {
		UserUpdateMedicalDetails userUpdateMedicalDetails = new UserUpdateMedicalDetails();
		userUpdateMedicalDetails.setPregnant(true);
		userUpdateMedicalDetails.setGestationalAge(30.00f);
		userUpdateMedicalDetails.setNumberOfFetuses(1);
		userUpdateMedicalDetails.setThreeOrMoreFetuses(true);
		userUpdateMedicalDetails.setNoBloodTransfusion(true);
		userUpdateMedicalDetails.setNoOrganTransplant(true);
		userUpdateMedicalDetails.setVitals(vitalss);
		return userUpdateMedicalDetails;

	}

	public static UserUpdatePatientDetailsPayload mockUserUpdatePatientDetailsPayload() {
		UserUpdatePatientDetailsPayload userUpdatePatientDetailsPayload = new UserUpdatePatientDetailsPayload();
		userUpdatePatientDetailsPayload.setMedicalDetails(mockUserUpdateMedicalDetails());
		return userUpdatePatientDetailsPayload;

	}

	public static PostRegistrationPayload mockPostRegistrationPayload() {
		PostRegistrationPayload response = new PostRegistrationPayload();
		response.setAlreadyRegistered(false);
		response.setCreatedAt(getVerificationCode().getCreatedAt());
		response.setEmail("no-email@junodx.com");
		response.setExpiration(getVerificationCode().getExpiresAt());
		response.setIdToken("passwordneww");
		response.setRequiresVerification(true);
		response.setVerificationCodeSent(true);
		return response;

	}
	public static List<OrderLineItem> OrderlineItems = new ArrayList<>();
	public static Order mockOrderlineitems() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		
		// orderStatusHistory.add(mockOrderStatus());
		// lineItems.add(mockOrderLineItem());
		order.setLineItems(OrderlineItems);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(false);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		
		return order;
	}

	public static PostRegistrationPayload mockPostRegistrationPayloads() {
		PostRegistrationPayload response = new PostRegistrationPayload();
		response.setAlreadyRegistered(false);
		response.setCreatedAt(getVerificationCode().getCreatedAt());
		response.setEmail("no-email@junodx.com");
		response.setExpiration(getVerificationCode().getExpiresAt());
		response.setIdToken("passwordneww");
		response.setRequiresVerification(false);
		response.setVerificationCodeSent(false);
		return response;

	}
//	UserBatchDto userBatchDto = new UserBatchDto("1L", "Juno_test", "User", "no-email@junodx.com",
//			"General_test_user", true, DataBuilder.getMockAddress(), UserType.STANDARD, 5, Calendar.getInstance(),
//			UserStatus.ACTIVATED);

	public static UserBatchDto mockUserBatchDto() {
		UserBatchDto userBatchDto = new UserBatchDto();
		userBatchDto.setActivated(true);
		userBatchDto.setAge(5);
		userBatchDto.setFirstName("Juno_test");
		userBatchDto.setEmail("no-email@junodx.com");
		userBatchDto.setLastName("User");
		userBatchDto.setId("1L");
		userBatchDto.setLastOrderedAt(Calendar.getInstance());
		userBatchDto.setPrimaryAddress(getMockAddress());
		userBatchDto.setStatus(UserStatus.ACTIVATED);
		userBatchDto.setUsername("General_test_user");
		userBatchDto.setUserType(UserType.STANDARD);
		return userBatchDto;

	}

	public static ForgotPasswordCode mockForgotPasswordCode() {
		ForgotPasswordCode forgotPasswordCode = new ForgotPasswordCode();
		forgotPasswordCode.setClientId("9348-8892-9342-01");
		forgotPasswordCode.setCode(
				"am9uKzQxQGp1bm9keC5jb206OTM0OC04ODkyLTkzNDItMDE6OWEzOTA4MDktMGY5My00MmY2LTljMjAtMzEwOWZhYWU4Zjk1");
		forgotPasswordCode.setCreatedAt(Calendar.getInstance());
		Calendar expiresAt = Calendar.getInstance();
		boolean before = !expiresAt.before(Calendar.getInstance());
		forgotPasswordCode.setExpiresAt(expiresAt.getInstance().getInstance());
		forgotPasswordCode.setUser(mockUsers());
		forgotPasswordCode.setId(5L);
		return forgotPasswordCode;

	}

	public static ForgotPasswordCode mockForgotPasswordCodes() {
		ForgotPasswordCode forgotPasswordCode = new ForgotPasswordCode();
		forgotPasswordCode.setClientId("9348-8892-9342-01");
		forgotPasswordCode.setCode("toke");
		forgotPasswordCode.setCreatedAt(Calendar.getInstance());
		Calendar expiresAt = Calendar.getInstance();
		expiresAt.before(Calendar.getInstance());
		forgotPasswordCode.setExpiresAt(expiresAt);
		forgotPasswordCode.setUser(mockUsers());
		forgotPasswordCode.setId(5L);
		return forgotPasswordCode;

	}

	public static ChangePasswordPayload mockChangePasswordPayload() {
		ChangePasswordPayload changePasswordPayload = new ChangePasswordPayload();
		changePasswordPayload.setExistingPassword("Password&123");
		changePasswordPayload.setNewPassword("Password&123");
		changePasswordPayload.setUserId("12345");
		return changePasswordPayload;

	}

	public static ChangePasswordPayload mockChangePasswordPayloads() {
		ChangePasswordPayload changePasswordPayload = new ChangePasswordPayload();
		// changePasswordPayload.setExistingPassword("Password&123");
		// changePasswordPayload.setNewPassword("Password&123");
		// changePasswordPayload.setUserId("12345");
		return changePasswordPayload;

	}

	public static ChangePasswordPayload mockChangePasswordPayloadPassword() {
		ChangePasswordPayload changePasswordPayload = new ChangePasswordPayload();
		changePasswordPayload.setExistingPassword("Password&343");
		changePasswordPayload.setNewPassword("Password&123");
		changePasswordPayload.setUserId("12345");
		return changePasswordPayload;

	}

	public static EmailChangePayload getMockEmailChangePayload() {
		EmailChangePayload response = new EmailChangePayload();
		response.setExistingEmail("no-email@junodx.com");
		response.setChangeToEmail("anjus@gmail.com");
		response.setUserId("1L");
		response.setVerificationCodeExpiresAt(getVerificationCode().getExpiresAt());
		response.setVerificationCodeSent(true);
		return response;
	}

	public static EmailChange getMockEmailChange() {
		EmailChange emailChange = new EmailChange();
		emailChange.setExistingEmail("no-email@junodx.com");
		emailChange.setNewEmail("anjus@gmail.com");
		emailChange.setUserId(DataBuilder.getVerificationCode().getUser().getId());
		emailChange.setCreatedAt(Calendar.getInstance());
		Calendar expires = Calendar.getInstance();
		int verificationCodeExpirationDuration = 60 * 60;
		expires.add(Calendar.HOUR_OF_DAY, verificationCodeExpirationDuration);
		emailChange.setExpiresAt(expires);
		return emailChange;
	}

	public static EmailChange getMockEmailChanges() {
		EmailChange emailChange = new EmailChange();
		emailChange.setExistingEmail("no-email@junodx.com");
		emailChange.setNewEmail("anjus@gmail.com");
		emailChange.setUserId(DataBuilder.getVerificationCode().getUser().getId());
		emailChange.setCreatedAt(Calendar.getInstance());
		Calendar expires = Calendar.getInstance();
//		int verificationCodeExpirationDuration = 60 * 60;
//		expires.add(Calendar.HOUR_OF_DAY, verificationCodeExpirationDuration);
		emailChange.setExpiresAt(expires);
		return emailChange;
	}

	public static EmailChange getMockEmailChangeEmail() {
		EmailChange emailChange = new EmailChange();
		emailChange.setExistingEmail("email@junodx.com");
		emailChange.setNewEmail("anjus@gmail.com");
		emailChange.setUserId(DataBuilder.getVerificationCode().getUser().getId());
		emailChange.setCreatedAt(Calendar.getInstance());
		Calendar expires = Calendar.getInstance();
		int verificationCodeExpirationDuration = 60 * 60;
		expires.add(Calendar.HOUR_OF_DAY, verificationCodeExpirationDuration);
		emailChange.setExpiresAt(expires);
		return emailChange;
	}

	public static EmailChange getMockEmailChangeEmails() {
		EmailChange emailChange = new EmailChange();
		emailChange.setExistingEmail("no-email@junodx.com");
		emailChange.setNewEmail("junos@gmail.com");
		emailChange.setUserId(DataBuilder.getVerificationCode().getUser().getId());
		emailChange.setCreatedAt(Calendar.getInstance());
		Calendar expires = Calendar.getInstance();
		int verificationCodeExpirationDuration = 60 * 60;
		expires.add(Calendar.HOUR_OF_DAY, verificationCodeExpirationDuration);
		emailChange.setExpiresAt(expires);
		return emailChange;
	}

	public static VerificationCodeResponsePayload mockVerificationCodeResponsePayload() {
		VerificationCodeResponsePayload verificationCodeResponsePayload = new VerificationCodeResponsePayload();
		verificationCodeResponsePayload.setEmail("no-email@junodx.com");
		verificationCodeResponsePayload.setSuccess(true);
		verificationCodeResponsePayload.setUserId("1L");
		return verificationCodeResponsePayload;
	}

	public static RegisterUserPayload mockRegisterUserPayload() {
		RegisterUserPayload registerUserPayload = new RegisterUserPayload();
		registerUserPayload.setClientId("9348-8892-9342-01");
		registerUserPayload.setEmail("no-email@junodx.com");
		registerUserPayload.setFirstName("Juno_test");
		registerUserPayload.setIdToken("12345");
		registerUserPayload.setLastName("User");
		registerUserPayload.setPassword("Password&123");
		registerUserPayload.setUserType(UserType.STANDARD);
		return registerUserPayload;

	}

	public static RegisterUserPayload mockRegisterUserPayloadEmails() {
		RegisterUserPayload registerUserPayload = new RegisterUserPayload();
		registerUserPayload.setClientId("9348-8892-9342-01");
		registerUserPayload.setEmail("junodx.com");
		registerUserPayload.setFirstName("Juno_test");
		registerUserPayload.setIdToken("12345");
		registerUserPayload.setLastName("User");
		registerUserPayload.setPassword("Password&123");
		registerUserPayload.setUserType(UserType.STANDARD);
		return registerUserPayload;

	}

	public static RegisterUserPayload mockRegisterUserPayloadFirst() {
		RegisterUserPayload registerUserPayload = new RegisterUserPayload();
		registerUserPayload.setClientId("12345");
		registerUserPayload.setEmail("no-email@junodx.com");
		registerUserPayload.setFirstName(null);
		registerUserPayload.setIdToken("12345");
		registerUserPayload.setLastName("User");
		registerUserPayload.setPassword("Password&123");
		registerUserPayload.setUserType(UserType.STANDARD);
		return registerUserPayload;

	}

	public static RegisterUserPayload mockRegisterUserPayloads() {
		RegisterUserPayload registerUserPayload = new RegisterUserPayload();
		registerUserPayload.setClientId("12345");
		registerUserPayload.setEmail("no-email@junodx.com");
		registerUserPayload.setFirstName("Juno_test");
		registerUserPayload.setIdToken("12345");
		registerUserPayload.setLastName("User");
		registerUserPayload.setPassword("passwr");
		registerUserPayload.setUserType(UserType.STANDARD);
		return registerUserPayload;
	}

	public static UserVerificationPayload payload() {
		UserVerificationPayload payload = new UserVerificationPayload();
		payload.setClientId("12345");
		payload.setCode(getVerificationCode().generateCode());
		payload.setEmail("no-email@junodx.com");
		return payload;
	}

	public static UserVerificationPayload payloadexc() {

		UserVerificationPayload payload = new UserVerificationPayload();
		// payload.setClientId("12345");
		// payload.setCode(getVerificationCode().generateCode());
		// payload.setEmail("no-email@junodx.com");
		return payload;

	}

	public static UserVerificationPayload payloadDifferent() {
		UserVerificationPayload payload = new UserVerificationPayload();
		payload.setClientId("34343");
		payload.setCode(getVerificationCode().generateCode());
		payload.setEmail("no-email@junodx.com");
		return payload;

	}

	public static UserVerificationPayload payloads() {
		UserVerificationPayload payload = new UserVerificationPayload();
		payload.setClientId("12345");
		// payload.setCode(getVerificationCode().generateCode());
		// payload.setEmail("no-email@junodx.com");
		return payload;

	}

	public static UserVerificationPayload payloadClient() {
		UserVerificationPayload payload = new UserVerificationPayload();
		// payload.setClientId("12345");
		payload.setCode(getVerificationCode().generateCode());
		payload.setEmail("no-email@junodx.com");
		return payload;

	}

	public static UserVerificationPayload payloadss() {
		UserVerificationPayload payload = new UserVerificationPayload();
		payload.setClientId("12345");
		payload.setCode(getVerificationCode().generateCode());
		payload.setEmail("juno@junodx.com");
		return payload;

	}

	public static Provider getMockProvider() {
		Provider provider = new Provider();
		provider.setContactAddress(getMockAddress());
		provider.setContactPhone(getMockPhone());
		provider.setDefaultProvider(false);
		provider.setEmail("no-email@junodx.com");
		provider.setFirstName("Juno_test");
		// UUID productId = UUID.fromString("ac358df7-4a38-4ad0-b070-59adcd57dde0");
		provider.setId("12345");
		provider.setLastName("User");
//		List<MedicalLicense> license=new ArrayList<MedicalLicense>();
//		license.add(getMockMedicalLicense() );
		// provider.setLicenses(license);
		provider.setLimsId("1234");
		// provider.setMeta(getMockMeta());
		provider.setNpi("1224");
		// provider.setPractice(getMockPractice());
		provider.setXifinId("1234");
		return provider;
	}

	public static UserUpdatePayload mockuserUpdatePayload = new UserUpdatePayload("1L", "Juno_test", "User",
			"no-email@junodx.com", getMockPhone(), "15-12-1997", getMockAddress(), getMockAddress(), "123456657",
			"34677676", UserType.STANDARD);

	public static UserUpdatePayload mockuserUpdatePayload() {
		UserUpdatePayload userUpdatePayload = new UserUpdatePayload();
		userUpdatePayload.setBillingAddress(getMockAddress());
		userUpdatePayload.setDateOfBirth("15-12-1997");
		userUpdatePayload.setEmail("no-email@junodx.com");
		userUpdatePayload.setFirstName("Juno_test");
		userUpdatePayload.setId("1L");
		userUpdatePayload.setLastName("User");
		userUpdatePayload.setLimsContactId("123456657");
		// userUpdatePayload.setPatientDetails(mockUserUpdatePatientDetailsPayload());
		// userUpdatePayload.setPreferences(userUpdatePreferencesPayload);
		userUpdatePayload.setPrimaryAddress(getMockAddress());
		userUpdatePayload.setPrimaryPhone(getMockPhone());
		userUpdatePayload.setStripeCustomerId("34677676");
		userUpdatePayload.setUserType(UserType.STANDARD);
		return userUpdatePayload;

	}

	public static UserUpdatePayload mockuserUpdatePayloadId() {
		UserUpdatePayload userUpdatePayload = new UserUpdatePayload();
		userUpdatePayload.setBillingAddress(getMockAddress());
		userUpdatePayload.setDateOfBirth("15-12-1997");
		userUpdatePayload.setEmail("no-email@junodx.com");
		userUpdatePayload.setFirstName("Juno_test");
		// userUpdatePayload.setId("8L");
		userUpdatePayload.setLastName("User");
		userUpdatePayload.setLimsContactId("123456657");
		// userUpdatePayload.setPatientDetails(mockUserUpdatePatientDetailsPayload());
		// userUpdatePayload.setPreferences(userUpdatePreferencesPayload);
		userUpdatePayload.setPrimaryAddress(getMockAddress());
		userUpdatePayload.setPrimaryPhone(getMockPhone());
		userUpdatePayload.setStripeCustomerId("34677676");
		userUpdatePayload.setUserType(UserType.STANDARD);
		return userUpdatePayload;

	}

	public static UserUpdatePayload mockuserUpdatePayloadAdress() {
		UserUpdatePayload userUpdatePayload = new UserUpdatePayload();
		userUpdatePayload.setBillingAddress(getMockAddress());
		userUpdatePayload.setDateOfBirth("15-12-1997");
		userUpdatePayload.setEmail("juno@junodx.com");
		userUpdatePayload.setFirstName("Juno_test");
		userUpdatePayload.setId("12345");
		userUpdatePayload.setLastName("User");
		userUpdatePayload.setLimsContactId("123456657");
		userUpdatePayload.setPatientDetails(mockUserUpdatePatientDetailsPayload());
		userUpdatePayload.setPreferences(mockUserUpdatePreferencesPayload());
		userUpdatePayload.setPrimaryAddress(getMockAddress());
		userUpdatePayload.setPrimaryPhone(getMockPhone());
		userUpdatePayload.setStripeCustomerId("34677676");
		userUpdatePayload.setUserType(UserType.STANDARD);
		return userUpdatePayload;
	}

	public static UserUpdatePayload mockuserUpdatePayloadAddress() {
		UserUpdatePayload userUpdatePayload = new UserUpdatePayload();
		userUpdatePayload.setBillingAddress(getMockAddress());
		userUpdatePayload.setDateOfBirth("15-12-1997");
		userUpdatePayload.setEmail("juno@junodx.com");
		userUpdatePayload.setFirstName("Juno_test");
		userUpdatePayload.setId("12345");
		userUpdatePayload.setLastName("User");
		userUpdatePayload.setLimsContactId("123456657");
		userUpdatePayload.setPatientDetails(mockUserUpdatePatientDetailsPayload());
		userUpdatePayload.setPreferences(mockUserUpdatePreferencesPayload());
		userUpdatePayload.setPrimaryAddress(getMockAddress());
		userUpdatePayload.setPrimaryPhone(getMockPhone());
		userUpdatePayload.setStripeCustomerId("34677676");
		userUpdatePayload.setUserType(UserType.STANDARD);
		return userUpdatePayload;

	}

	public static UserUpdatePayload mockuserUpdatePayloadAddresselse() {
		UserUpdatePayload userUpdatePayload = new UserUpdatePayload();
		userUpdatePayload.setBillingAddress(getMockAddress());
		userUpdatePayload.setDateOfBirth("15-12-1997");
		userUpdatePayload.setEmail("juno@junodx.com");
		userUpdatePayload.setFirstName("Juno_test");
		userUpdatePayload.setId("12345");
		userUpdatePayload.setLastName("User");
		userUpdatePayload.setLimsContactId("123456657");
		userUpdatePayload.setPatientDetails(mockUserUpdatePatientDetailsPayloads());
		userUpdatePayload.setPreferences(mockUserUpdatePreferencesPayload());
		userUpdatePayload.setPrimaryAddress(getMockAddress());
		userUpdatePayload.setPrimaryPhone(getMockPhone());
		userUpdatePayload.setStripeCustomerId("34677676");
		userUpdatePayload.setUserType(UserType.STANDARD);
		return userUpdatePayload;

	}

	public static UserUpdatePayload mockuserUpdatePayloads() {
		UserUpdatePayload userUpdatePayload = new UserUpdatePayload();
		userUpdatePayload.setBillingAddress(getMockAddress());
		userUpdatePayload.setDateOfBirth("15-12-1997");
		userUpdatePayload.setEmail("juno@junodx.com");
		userUpdatePayload.setFirstName("Juno_test");
		userUpdatePayload.setId("12345");
		userUpdatePayload.setLastName("User");
		userUpdatePayload.setLimsContactId("123456657");
		userUpdatePayload.setPatientDetails(mockUserUpdatePatientDetailsPayload());
		userUpdatePayload.setPreferences(mockUserUpdatePreferencesPayload());
		userUpdatePayload.setPrimaryAddress(getMockAddress());
		userUpdatePayload.setPrimaryPhone(getMockPhone());
		userUpdatePayload.setStripeCustomerId("34677676");
		userUpdatePayload.setUserType(UserType.STANDARD);
		return userUpdatePayload;

	}

	public static List<GrantedAuthority> authorities = user.getAuthorities().stream()
			.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	public static UsernamePasswordAuthenticationToken usernamePasswordAuthenticationTokens = new UsernamePasswordAuthenticationToken(
			getMockUserDetailsImpls(), "Password&124");

	public static UserDetailsImpl getMockUserDetailsImpls() {
		UserDetailsImpl userDetailsImpls = new UserDetailsImpl();
		// userDetailsImpls.setClientId("12345");
		// userDetailsImpls.setEmail("no-email@junodx.com");
		// userDetailsImpls.setId(1L);
		// userDetailsImpls.setPassword("Password&124");
		// userDetailsImpls.setUserId("1L");
		// userDetailsImpls.setUsername(user.getUsername());
		userDetailsImpls.setAuthorities(authorities);
		return userDetailsImpls;
	}

	public static UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
			getMockUserDetailsImpl(), "Password&123");

	public static UserDetailsImpl getMockUserDetailsImpl() {
		UserDetailsImpl userDetailsImpls = new UserDetailsImpl();
		userDetailsImpls.setClientId("12345");
		userDetailsImpls.setEmail("no-email@junodx.com");
		userDetailsImpls.setId(1L);
		userDetailsImpls.setPassword("Password&123");
		userDetailsImpls.setUserId("1L");
		userDetailsImpls.setUsername(user.getUsername());
		userDetailsImpls.setAuthorities(authorities);
		return userDetailsImpls;
	}

	public static MedicalLicense getMockMedicalLicense() {
		MedicalLicense medicalLicense = new MedicalLicense();
		medicalLicense.setId(3L);
		medicalLicense.setLicenseNumber("1234567");
		medicalLicense.setProvider(getMockProvider());
		medicalLicense.setState(State.INDIANA);
		return medicalLicense;
	}

	public static Preferences getMockPreference() {
		Preferences preferences = new Preferences();
		preferences.setId(UUID.randomUUID().toString());
		preferences.setOptOut(false);
		preferences.setSmsMessages(false);
		preferences.setFstPreferences(getMockFetalSexResultsPreferences());
		// preferences.setUser(user);
		return preferences;
	}

	public static Preferences getMockPreferences() {
		Preferences preferences = new Preferences();
		preferences.setId(UUID.randomUUID().toString());
		preferences.setOptOut(false);
		preferences.setSmsMessages(false);
		// preferences.setFstPreferences(getMockFetalSexResultsPreferences());
		preferences.setUser(user);
		return preferences;
	}

	public static UserUpdateFetalSexResultsPreferences mockUserUpdateFetalSexResultsPreferences() {
		UserUpdateFetalSexResultsPreferences userUpdateFetalSexResultsPreferences = new UserUpdateFetalSexResultsPreferences();
		userUpdateFetalSexResultsPreferences.setFstResultsDelegatedEmail("");
		userUpdateFetalSexResultsPreferences.setGenderDelegated(false);
		userUpdateFetalSexResultsPreferences.setGenderFanfare(false);
		userUpdateFetalSexResultsPreferences.setGenderTerms(GenderTerms.BOY_GIRL);
		return userUpdateFetalSexResultsPreferences;

	}

	public static UserUpdatePreferencesPayload mockUserUpdatePreferencesPayload() {
		UserUpdatePreferencesPayload userUpdatePreferencesPayload = new UserUpdatePreferencesPayload();
		userUpdatePreferencesPayload.setFstPreferences(mockUserUpdateFetalSexResultsPreferences());
		userUpdatePreferencesPayload.setOptOut(true);
		userUpdatePreferencesPayload.setSmsMessages(true);
		return userUpdatePreferencesPayload;
	}

	public static FetalSexResultsPreferences getMockFetalSexResultsPreferences() {
		FetalSexResultsPreferences fetalSexResultsPreferences = new FetalSexResultsPreferences();
		fetalSexResultsPreferences.setFstResultsDelegatedEmail("");
		fetalSexResultsPreferences.setGenderDelegated(true);
		fetalSexResultsPreferences.setGenderFanfare(true);
		fetalSexResultsPreferences.setGenderTerms(GenderTerms.BOY_GIRL);
		return fetalSexResultsPreferences;
	}

	public static UserForgotPasswordPayload mockUserForgotPasswordPayload() {
		UserForgotPasswordPayload userForgotPasswordPayload = new UserForgotPasswordPayload();
		userForgotPasswordPayload.setEmail("no-email@junodx.com");
		userForgotPasswordPayload.setClientId("89766");
		userForgotPasswordPayload.setDestinationUrl("url");
		return userForgotPasswordPayload;
	}

	public static UserForgotPasswordPayload mockUserForgotPasswordPayloadEmpty() {
//		UserForgotPasswordPayload userForgotPasswordPayload = new UserForgotPasswordPayload();
//		userForgotPasswordPayload.setEmail("no-email@junodx.com");
//		userForgotPasswordPayload.setClientId("89766");
//		userForgotPasswordPayload.setDestinationUrl("url");
//		return userForgotPasswordPayload;
		return null;
	}

	public static UserUpdatePasswordPayload mockUserUpdatePasswordPayload() {
		UserUpdatePasswordPayload userUpdatePasswordPayload = new UserUpdatePasswordPayload();
		userUpdatePasswordPayload.setClientId("9348-8892-9342-01");
		userUpdatePasswordPayload.setPassword("password321&");
		userUpdatePasswordPayload.setToken(
				"am9uKzQxQGp1bm9keC5jb206OTM0OC04ODkyLTkzNDItMDE6OWEzOTA4MDktMGY5My00MmY2LTljMjAtMzEwOWZhYWU4Zjk1");
		return userUpdatePasswordPayload;

	}

	public static UserUpdatePasswordPayload mockUserUpdatePasswordPayloads() {
		UserUpdatePasswordPayload userUpdatePasswordPayload = new UserUpdatePasswordPayload();
		userUpdatePasswordPayload.setClientId("9348-8892-9342-01");
		userUpdatePasswordPayload.setPassword("password321&");
		userUpdatePasswordPayload.setToken("toke");
		return userUpdatePasswordPayload;

	}

	public static Vital mockVital() {
		Vital vital = new Vital();
		vital.setId(2L);
		vital.setMedicalDetails(mockMedicalDetails());
		vital.setRecordedAt(Calendar.getInstance());
		vital.setRecordedBy("anjali");
		vital.setType(VitalType.HEIGHT);
		vital.setValue("67");
		vital.setValueType(DataType.INT);
		return vital;

	}
	
	

	public static List<Vital> vitals = new ArrayList<>();

	public static MedicalDetails mockMedicalDetails() {
		MedicalDetails medicalDetails = new MedicalDetails();
		medicalDetails.setConceptionDate(Calendar.getInstance());
		medicalDetails.setGestationalAge(9.8f);
		medicalDetails.setId(8L);
		medicalDetails.setLastUpdatedAt(Calendar.getInstance());
		medicalDetails.setNoBloodTransfusion(false);
		medicalDetails.setNoOrganTransplant(false);
		medicalDetails.setNumberOfFetuses(5);
		// medicalDetails.setPatientDetails(mockPatientDetails());
		medicalDetails.setPregnant(false);
		medicalDetails.setThreeOrMoreFetuses(false);
		medicalDetails.setVitals(vitals);
		return medicalDetails;

	}

	public static Medication mockMedication() {
		Medication medication = new Medication();
		medication.setId(6L);
		medication.setName("medicine");
		medication.setPatientDetails(mockPatientDetails());
		medication.setType(MedicationType.PAIN_KILLER);
		return medication;

	}

	public static List<Medication> medications = new ArrayList<>();
	public static List<Provider> providers = new ArrayList<>();

	public static PatientDetails mockPatientDetails() {
		PatientDetails patientDetails = new PatientDetails();
		patientDetails.setChart(mockPatientChart());
		patientDetails.setId("8L");
		patientDetails.setMedicalDetails(mockMedicalDetails());
		patientDetails.setMedications(medications);
		patientDetails.setProviders(providers);
		patientDetails.setUser(user);
		return patientDetails;

	}

	public static PatientDetails mockPatientDetailsEmptyChart() {
		PatientDetails patientDetails = new PatientDetails();
		// patientDetails.setChart(mockPatientChart());
		patientDetails.setId("8L");
		patientDetails.setMedicalDetails(mockMedicalDetails());
		patientDetails.setMedications(medications);
		patientDetails.setProviders(providers);
		patientDetails.setUser(user);
		return patientDetails;

	}

	public static PatientChartEntry mockPatientChartEntry() {
		PatientChartEntry patientChartEntry = new PatientChartEntry();
		patientChartEntry.setAuthorEmail("author@gmail.com");
		patientChartEntry.setAuthorName("anjali");
		patientChartEntry.setAuthorSalesforceId("78999888");
		patientChartEntry.setId("2L");
		patientChartEntry.setNote("book");
		patientChartEntry.setPatientChart(mockPatientChart());
		patientChartEntry.setRelatedTestReportId("67890");
		patientChartEntry.setRelatedTestReportSalesforceId("78889999");
		patientChartEntry.setSalesforceId("89878");
		patientChartEntry.setTitle("authors");
		patientChartEntry.setTimestamp(Calendar.getInstance());
		return patientChartEntry;

	}

	public static Vital mockVitals() {
		Vital vital = new Vital();
		vital.setId(2L);
		vital.setMedicalDetails(mockMedicalDetails());
		vital.setRecordedAt(Calendar.getInstance());
		vital.setRecordedBy("anjali");
		vital.setType(VitalType.WEIGHT);
		vital.setValue("67");
		vital.setValueType(DataType.INT);
		return vital;

	}

	public static List<PatientChartEntry> patientChartEntrys = new ArrayList<>();

	public static PatientChart mockPatientChart() {
		PatientChart patientChart = new PatientChart();
		patientChart.setChartEntries(patientChartEntrys);
		patientChart.setId("6L");
		return patientChart;

	}

	public static PatientChart mockPatientChartException() {
		PatientChart patientChart = new PatientChart();
		// patientChart.setChartEntries(patientChartEntrys);
		patientChart.setId("6L");
		return patientChart;

	}

	public static PatientDetails mockPatientDetailException() {
		PatientDetails patientDetails = new PatientDetails();
		patientDetails.setChart(mockPatientChartException());
		patientDetails.setId("8L");
		patientDetails.setMedicalDetails(mockMedicalDetails());
		patientDetails.setMedications(medications);
		patientDetails.setProviders(providers);
		patientDetails.setUser(user);
		return patientDetails;

	}

	public static PatientChart mockPatientCharts() {
		PatientChart patientChart = new PatientChart();
		patientChart.setChartEntries(patientChartEntrys);
		patientChart.setId("6L");
		patientChart.setPatientDetails(mockPatientDetails());
		return patientChart;

	}

	public static PatientChartEntry mockPatientChartEntrys() {
		PatientChartEntry patientChartEntry = new PatientChartEntry();
		patientChartEntry.setAuthorEmail("");
		patientChartEntry.setPatientChart(mockPatientCharts());
		patientChartEntry.setAuthorName("");
		patientChartEntry.setAuthorSalesforceId("78999888");
		patientChartEntry.setId("2L");
		patientChartEntry.setNote("book");
		patientChartEntry.setRelatedTestReportId("67890");
		patientChartEntry.setRelatedTestReportSalesforceId("78889999");
		patientChartEntry.setSalesforceId("3L");
		patientChartEntry.setTitle("");
		patientChartEntry.setTimestamp(Calendar.getInstance());
		return patientChartEntry;

	}

	public static TestResult testResult;

	public static SFBillingAddress mockSFBillingAddress() {
		SFBillingAddress sfBillingAddress = new SFBillingAddress();
		sfBillingAddress.setBillingAddress(true);
		sfBillingAddress.setBillingCity("San Diego");
		sfBillingAddress.setBillingCountry("United States");
		sfBillingAddress.setBillingName("Juno Headquarters");
		sfBillingAddress.setBillingPostalCode("98077");
		sfBillingAddress.setBillingPrimaryAddress(true);
		sfBillingAddress.setBillingState("CA");
		sfBillingAddress.setBillingStreet("11511 Sorrento Valley Rd");
		sfBillingAddress.setResidential(true);
		return sfBillingAddress;

	}

	public static SalesforceChangedDataMap mockSalesforceChangedDataMap() {
		SalesforceChangedDataMap salesforceChangedDataMap = new SalesforceChangedDataMap();
		salesforceChangedDataMap.setActive(true);
		salesforceChangedDataMap.setBillingAddress(mockSFBillingAddress());
		salesforceChangedDataMap.setContactName("deepu");
		salesforceChangedDataMap.setCreatedById("anjali");
		salesforceChangedDataMap.setDateOfBirth("15-12-1997");
		salesforceChangedDataMap.setDescription("descr");
		salesforceChangedDataMap.setEmail("no-email@junodx.com");
		salesforceChangedDataMap.setFirstName("Juno_test");
		salesforceChangedDataMap.setId("3L");
		salesforceChangedDataMap.setJunoUserId("3L");
		salesforceChangedDataMap.setLastModifiedDate(Calendar.getInstance());
		salesforceChangedDataMap.setLastModifiedById("12345");
		salesforceChangedDataMap.setLastName("User");
		salesforceChangedDataMap.setName("General_test_user");
		salesforceChangedDataMap.setNpi("12345");
		salesforceChangedDataMap.setOptOut(true);
		salesforceChangedDataMap.setPhone("LocationTest.getMockPhone()");
		salesforceChangedDataMap.setPracticing(true);
		salesforceChangedDataMap.setPrimaryAddress(getMockAddress());
		salesforceChangedDataMap.setSalesforceAccountId("112");
		salesforceChangedDataMap.setSystemModStamp(Calendar.getInstance());
		salesforceChangedDataMap.setTestResultId("112123");
		salesforceChangedDataMap.setUserStatus("all");
		salesforceChangedDataMap.setUserType("Standard");
		return salesforceChangedDataMap;

	}

	public static SalesforceRecordChanged mockSalesforceRecordChanged() {
		SalesforceRecordChanged salesforceRecordChanged = new SalesforceRecordChanged();
		salesforceRecordChanged.setChangedDataMap(mockSalesforceChangedDataMap());
		salesforceRecordChanged.setId("3L");
		return salesforceRecordChanged;
	}

	public static SalesforceWhoRecord mockSalesforceWhoRecord() {
		SalesforceWhoRecord salesforceWhoRecord = new SalesforceWhoRecord();
		salesforceWhoRecord.setJunoUserId("12345");
		salesforceWhoRecord.setName("General_test_user");
		salesforceWhoRecord.setSalesforceUserId("3L");
		return salesforceWhoRecord;

	}

	public static SalesforceChartUpdateInfo mockSalesforceChartUpdateInfo() {
		SalesforceChartUpdateInfo salesforceChartUpdateInfo = new SalesforceChartUpdateInfo();
		salesforceChartUpdateInfo.setAuthorId("112");
		salesforceChartUpdateInfo.setDescription("des");
		salesforceChartUpdateInfo.setId("3L");
		salesforceChartUpdateInfo.setTestResult(testResult);
		salesforceChartUpdateInfo.setWho(mockSalesforceWhoRecord());
		return salesforceChartUpdateInfo;

	}

	public static SalesforceChartUpdateInfo mockSalesforceChartUpdateInfoException() {
		SalesforceChartUpdateInfo salesforceChartUpdateInfo = new SalesforceChartUpdateInfo();
		salesforceChartUpdateInfo.setAuthorId("112");
		salesforceChartUpdateInfo.setDescription("des");
		salesforceChartUpdateInfo.setId("3L");
		salesforceChartUpdateInfo.setTestResult(testResult);
		salesforceChartUpdateInfo.setWho(mockSalesforceWhoRecords());
		return salesforceChartUpdateInfo;

	}

	public static SalesforceWhoRecord mockSalesforceWhoRecords() {
		SalesforceWhoRecord salesforceWhoRecord = new SalesforceWhoRecord();
		// salesforceWhoRecord.setJunoUserId("12345");
		salesforceWhoRecord.setName("General_test_user");
		salesforceWhoRecord.setSalesforceUserId("3L");
		return salesforceWhoRecord;

	}

	public static SalesforceAttributes mockSalesforceAttributes() {
		SalesforceAttributes salesforceAttributes = new SalesforceAttributes();
		salesforceAttributes.setType("Standard");
		salesforceAttributes.setUrl("url");
		return salesforceAttributes;
	}

	public static SalesforceUserUpdateAccountInfo mockSalesforceUserUpdateAccountInfo() {
		SalesforceUserUpdateAccountInfo salesforceUserUpdateAccountInfo = new SalesforceUserUpdateAccountInfo();
		salesforceUserUpdateAccountInfo.setAttributes(mockSalesforceAttributes());
		salesforceUserUpdateAccountInfo.setId("3L");
		salesforceUserUpdateAccountInfo.setJunoUserId("12345");
		return salesforceUserUpdateAccountInfo;

	}

	// public static OrderLineItemInfo orderLineItemInfo = new OrderLineItemInfo(1,
	// true);

	public static OrderLineItemInfo mockOrderLineItemInfo() {
		OrderLineItemInfo orderLineItemInfo = new OrderLineItemInfo();
		orderLineItemInfo.setDone(true);
		orderLineItemInfo.setRecords(salesforceLineItemUpdateInfos);
		orderLineItemInfo.setTotalSize(1);
		return orderLineItemInfo;

	}

	public static List<SalesforceLineItemUpdateInfo> salesforceLineItemUpdateInfos = new ArrayList<>();

	public static SalesforceOrderUpdateAccountInfo mockSalesforceOrderUpdateAccountInfo() {
		SalesforceOrderUpdateAccountInfo salesforceOrderUpdateAccountInfo = new SalesforceOrderUpdateAccountInfo();
		salesforceOrderUpdateAccountInfo.setAccountId("Juno_testss");
		salesforceOrderUpdateAccountInfo.setActivatedById("General_test_users");
		salesforceOrderUpdateAccountInfo.setAttributes(mockSalesforceAttributes());
		salesforceOrderUpdateAccountInfo.setCarrierAddress1("Juno Headquarterss");
		salesforceOrderUpdateAccountInfo.setCarrierAddress2("11511 Sorrento Valley Rds");
		salesforceOrderUpdateAccountInfo.setCarrierCity("San Diego");
		salesforceOrderUpdateAccountInfo.setCarrierCountry("United Statess");
		salesforceOrderUpdateAccountInfo.setCarrierName("Juno Headquarters");
		salesforceOrderUpdateAccountInfo.setCarrierPhone("85864723745");
		salesforceOrderUpdateAccountInfo.setCarrierState("CA");
		salesforceOrderUpdateAccountInfo.setCarrierZip("98078");
		salesforceOrderUpdateAccountInfo.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd8");
		salesforceOrderUpdateAccountInfo.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef8");
		salesforceOrderUpdateAccountInfo.setClinicianNotes("notes");
		salesforceOrderUpdateAccountInfo.setCreatedById("3L");
		salesforceOrderUpdateAccountInfo.setCurrency("USD");
		salesforceOrderUpdateAccountInfo.setDescription("descr");
		salesforceOrderUpdateAccountInfo.setDiscountAmount("2f");
		salesforceOrderUpdateAccountInfo.setDiscountApplied("true");
		salesforceOrderUpdateAccountInfo.setDiscountMode("PERCENT");
		salesforceOrderUpdateAccountInfo.setDiscountType("PROMO_CODE");
		salesforceOrderUpdateAccountInfo.setGestationalAge("9.88f");
		salesforceOrderUpdateAccountInfo.setId("3L");
		salesforceOrderUpdateAccountInfo.setInsuranceBillingOrderId("898788");
		salesforceOrderUpdateAccountInfo.setIsPregnant("true");
		salesforceOrderUpdateAccountInfo.setKitId("c06071ba-bf8e-414c-8622-2aa57d5b6a7f");
		salesforceOrderUpdateAccountInfo.setLabId("f450237e-20a3-4bf1-b64d-9ecaab16be7a");
		salesforceOrderUpdateAccountInfo.setLastModifiedById("12345");
		salesforceOrderUpdateAccountInfo.setLineItems(mockOrderLineItemInfo());
		salesforceOrderUpdateAccountInfo.setOrderedAt("1997-12-15 00:00:00");
		salesforceOrderUpdateAccountInfo.setOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		salesforceOrderUpdateAccountInfo.setPriceBook2Id("9898778");
		salesforceOrderUpdateAccountInfo.setSampleNumber("123455");
		salesforceOrderUpdateAccountInfo.setShipping("349.99f");
		salesforceOrderUpdateAccountInfo.setShippingAddress("Dharpally");
		salesforceOrderUpdateAccountInfo.setShippingOrderId("8L");
		salesforceOrderUpdateAccountInfo.setStatus("CREATED");
		salesforceOrderUpdateAccountInfo.setSubTotal("349.98f");
		salesforceOrderUpdateAccountInfo.setTax("0.08f");
		salesforceOrderUpdateAccountInfo.setThreeOrMore("true");
		salesforceOrderUpdateAccountInfo.setTotalAmount("349.998f");
		salesforceOrderUpdateAccountInfo.setType("Standard");
		salesforceOrderUpdateAccountInfo.setWeight("");
		return salesforceOrderUpdateAccountInfo;

	}

	public static SalesforceLineItemUpdateInfo mockSalesforceLineItemUpdateInfo() {
		SalesforceLineItemUpdateInfo salesforceLineItemUpdateInfo = new SalesforceLineItemUpdateInfo();
		salesforceLineItemUpdateInfo.setAmount("349.98f");
		salesforceLineItemUpdateInfo.setAmountDiscounted("24f");
		salesforceLineItemUpdateInfo.setAvailableQuantity("8");
		salesforceLineItemUpdateInfo.setClinicianNotes("notes");
		salesforceLineItemUpdateInfo.setConcentFormName("anjali");
		salesforceLineItemUpdateInfo.setConcentType("false");
		salesforceLineItemUpdateInfo.setConsentApproval("false");
		salesforceLineItemUpdateInfo.setConsentApprovalDate("false");
		salesforceLineItemUpdateInfo.setCreatedById("3L");
		salesforceLineItemUpdateInfo.setDescription(" ");
		salesforceLineItemUpdateInfo.setInsurenceEstimatedCoveredAmount("8f");
		salesforceLineItemUpdateInfo.setIsDirectlyProvided("true");
		salesforceLineItemUpdateInfo.setIsInOfficeCollected("true");
		salesforceLineItemUpdateInfo.setLabId("f450237e-20a3-4bf1-b64d-9ecaab16be7a");
		salesforceLineItemUpdateInfo.setLineItemId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		salesforceLineItemUpdateInfo.setLabOrderId("76c97d43-347f-4132-ba18-ddf3b313c226");
		salesforceLineItemUpdateInfo.setLastModifiedById("123455");
		salesforceLineItemUpdateInfo.setOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		salesforceLineItemUpdateInfo.setOrderItemNumber("123458677");
		salesforceLineItemUpdateInfo.setPatientConsentId("6L");
		salesforceLineItemUpdateInfo.setProduct2Id("786518f5-4fa9-4576-a173-6318556ccb0b");
		salesforceLineItemUpdateInfo.setProductName("Juno Early Fetal Sex Test");
		salesforceLineItemUpdateInfo.setQuantity("88");
		salesforceLineItemUpdateInfo.setReferralType("SELF_CREATED");
		salesforceLineItemUpdateInfo.setRequiresShipping("false");
		salesforceLineItemUpdateInfo.setSKU("KIT238349");
		salesforceLineItemUpdateInfo.setTaxable("876f");
		salesforceLineItemUpdateInfo.setTaxAmount("876f");
		salesforceLineItemUpdateInfo.setTaxJurisdiction("jurisdiction");
		salesforceLineItemUpdateInfo.setTaxRate("876f");
		salesforceLineItemUpdateInfo.setTaxType("STATE");
		salesforceLineItemUpdateInfo.setTotalPrice("349.99f");
		salesforceLineItemUpdateInfo.setType("CREATED");
		salesforceLineItemUpdateInfo.setUnitPrice("1");
		return salesforceLineItemUpdateInfo;

	}

}
