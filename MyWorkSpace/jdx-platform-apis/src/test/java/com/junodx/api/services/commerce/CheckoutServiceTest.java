package com.junodx.api.services.commerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.connectors.aws.configuration.AwsConfiguration;
import com.junodx.api.connectors.commerce.stripe.StripeClient;
import com.junodx.api.dto.mappers.UserMapStructMapper;
import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.models.auth.Authority;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.commerce.Checkout;
import com.junodx.api.models.commerce.DataBuilderCheckOut;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.commerce.types.PaymentMethodType;
import com.junodx.api.models.commerce.types.PaymentProcessingType;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.repositories.commerce.CheckoutRepository;
import com.junodx.api.repositories.commerce.OrderRepository;
import com.junodx.api.services.auth.UserService;
import com.junodx.api.services.exceptions.JdxServiceException;
import com.junodx.api.services.fulfillment.FulfillmentProviderService;
import com.junodx.api.services.inventory.InventoryService;
import com.junodx.api.services.providers.PracticeService;
import com.junodx.api.services.providers.ProviderService;
import com.stripe.exception.StripeException;
import com.stripe.model.AutomaticPaymentMethodsPaymentIntent;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.PaymentIntent;
import com.stripe.net.StripeResponse;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceTest {

	@Mock
	private AwsConfiguration awsConfigurationProperties;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private CheckoutRepository checkoutRepository;

	@Mock
	private ProviderService providerService;

	@Mock
	private InventoryService inventoryService;

	@Mock
	private ProductService productService;

	@Mock
	private PracticeService practiceService;

	@Mock
	private UserService userService;
	@Mock
	private UserMapStructMapper userMapStructMapper;

	@Mock
	private ObjectMapper mapper;
	@Spy
	@InjectMocks
	private CheckoutService checkoutService;
	@Mock
	private StripeClient stripe = new StripeClient();
	@Mock
	private PaymentIntent paymentIntents;

	@Mock
	private PaymentProcessorProviderService paymentProcessorProviderService;
	@Mock
	private OrderService orderService;
	@Mock
	private FulfillmentProviderService fulfillmentProviderService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	User user = User.createDummyUser();

	@Test
	void testcreatePayment() throws StripeException {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		user.setAuthorities(authority);
		user.setDateOfBirth("1997-12-15");
		String[] includes = {};
		DataBuilderOrder.items.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		when(userService.findOneByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(user));
		when(userMapStructMapper.userToUserCheckout(Mockito.any())).thenReturn(DataBuilderOrder.mockCustomers());
		when(productService.get(Mockito.any())).thenReturn(Optional.of(DataBuilderOrder.mockProduct()));
		when(fulfillmentProviderService.findProviderFromAddress(Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockFulfillmentProvider());
		when(providerService.getProvider(DataBuilderOrder.mockCheckoutRequestPayloads().getProvider().getId(),
				includes)).thenReturn(Optional.of(DataBuilder.getMockProvider()));
		when(checkoutRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockCheckoutAnother());
		assertEquals(checkoutService
				.create(DataBuilderOrder.mockCheckoutRequestPayloads(), false, DataBuilderOrder.userDetailsImpl)
				.getClass(), DataBuilderOrder.mockCheckoutAnother().getClass());

	}

	@Test
	public void testcreateException() throws JdxServiceException {
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(null, false, DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	public void testcreateFirstnameCustomerException() throws JdxServiceException, StripeException {
		DataBuilderOrder.itemCustomerFirstName.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());

		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadCustomerFirstName(), false,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("Insufficient information provided about the customer.", exception.getMessage());
	}

	@Test
	public void testcreateLastNameCustomerException() throws JdxServiceException, StripeException {
		DataBuilderOrder.itemCustomerLastName.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadCustomerLastName(), false,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("Insufficient information provided about the customer.", exception.getMessage());
	}

	@Test
	public void testcreateDateOfBirthCustomerException() throws JdxServiceException, StripeException {
		DataBuilderOrder.itemCustomerDateOfBirth.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadCustomerDateOfBirth(), false,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("Insufficient information provided about the customer.", exception.getMessage());
	}

	@Test
	public void testcreateShippingAddressCustomerException() throws JdxServiceException, StripeException {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		user.setAuthorities(authority);
		user.setDateOfBirth("1997-12-15");
		String[] includes = {};
		DataBuilderOrder.itemCustomerStripeCustomer.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		when(providerService.getProvider(
				DataBuilderOrder.mockCheckoutRequestPayloadCustomerStripeCustomer().getProvider().getId(), includes))
				.thenReturn(Optional.of(DataBuilder.getMockProvider()));
		when(checkoutRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockCheckoutAnother());
		assertEquals(
				checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadCustomerStripeCustomer(), false,
						DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilderOrder.mockCheckoutAnother().getClass());
	}

	@Test
	public void testcreateDefaultProviderCustomerException() throws JdxServiceException, StripeException {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		user.setAuthorities(authority);
		user.setDateOfBirth("1997-12-15");
		String[] includes = {};
		DataBuilderOrder.itemCustomerDefaultProvider.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		when(providerService.getProvider(
				DataBuilderOrder.mockCheckoutRequestPayloadCustomerDefaultProvider().getProvider().getId(), includes))
				.thenReturn(Optional.empty());
		when(checkoutRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockCheckoutAnother());
		assertEquals(
				checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadCustomerDefaultProvider(), false,
						DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilderOrder.mockCheckoutAnother().getClass());
	}

	@Test
	public void testcreateApprovingProviderCustomerException() throws JdxServiceException, StripeException {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		user.setAuthorities(authority);
		user.setDateOfBirth("1997-12-15");
		String[] includes = {};
		DataBuilderOrder.itemCustomerApprovingProvider.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		when(checkoutRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockCheckoutAnother());
		assertEquals(
				checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadCustomerApprovingProvider(), false,
						DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilderOrder.mockCheckoutAnother().getClass());
	}

	@Test
	public void testcreateItemsOverTenWeeksException() throws JdxServiceException, StripeException {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		user.setAuthorities(authority);
		user.setDateOfBirth("1997-12-15");
		DataBuilderOrder.itemOverTenWeeks.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		when(checkoutRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockCheckoutAnother());
		assertEquals(
				checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadOverTenWeeks(), false,
						DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilderOrder.mockCheckoutAnother().getClass());

	}

	@Test
	public void testcreateItemsconceptionDateNullException() throws JdxServiceException, StripeException {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		user.setAuthorities(authority);
		user.setDateOfBirth("1997-12-15");
		DataBuilderOrder.itemconceptionDate.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());

		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadconceptionDate(), true,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("Insufficient information provided about the customer.", exception.getMessage());

	}

	@Test
	public void testcreateItemsMedicalDetailsNullException() throws JdxServiceException, StripeException {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		user.setAuthorities(authority);
		user.setDateOfBirth("1997-12-15");
		DataBuilderOrder.itemsetconceptionDate.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadsetconceptionDate(), true,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("Insufficient information provided about the customer.", exception.getMessage());

	}

	@Test
	public void testcreateItemsCheckoutNullException() throws JdxServiceException, StripeException {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		user.setAuthorities(authority);
		user.setDateOfBirth("1997-12-15");
		String[] includes = {};
		DataBuilderOrder.itemCheckout.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		when(userService.findOneByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(user));
		when(userMapStructMapper.userToUserCheckout(Mockito.any())).thenReturn(DataBuilderOrder.mockCustomers());
		when(providerService.getProvider(DataBuilderOrder.mockCheckoutRequestPayloadCheckout().getProvider().getId(),
				includes)).thenReturn(Optional.of(DataBuilder.getMockProvider()));
		doThrow(new NoSuchElementException()).when(checkoutRepository).save(Mockito.any());
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadCheckout(), false,
						DataBuilderOrder.userDetailsImpl));

	}

	@Test
	public void testcreateItemsSevenWeeksException() throws JdxServiceException, StripeException {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		user.setAuthorities(authority);
		user.setDateOfBirth("1997-12-15");
		DataBuilderOrder.itemOverSevenWeeks.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		when(checkoutRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockCheckoutAnother());
		assertEquals(
				checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadOverSevenWeeks(), false,
						DataBuilderOrder.userDetailsImpl).getClass(),
				DataBuilderOrder.mockCheckoutAnother().getClass());

	}

	@Test
	public void testcreateFirstnameanonymousException() throws JdxServiceException {
		DataBuilderOrder.itemFirstName.add(DataBuilderOrder.mockCheckoutLineItem());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadFirstName(), true,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("Insufficient information provided about the customer.", exception.getMessage());
	}

	@Test
	public void testcreateLastNameanonymousException() throws JdxServiceException {
		DataBuilderOrder.itemLastName.add(DataBuilderOrder.mockCheckoutLineItem());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		JdxServiceException exception = assertThrows(JdxServiceException.class, () -> checkoutService
				.create(DataBuilderOrder.mockCheckoutRequestPayloadLastName(), true, DataBuilderOrder.userDetailsImpl));
		assertEquals("Insufficient information provided about the customer.", exception.getMessage());
	}

	@Test
	public void testcreateDateOfBirthanonymousException() throws JdxServiceException {
		DataBuilderOrder.itemDateOfBirth.add(DataBuilderOrder.mockCheckoutLineItem());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadDateOfBirth(), true,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("Insufficient information provided about the customer.", exception.getMessage());
	}

	@Test
	public void testcreateEmailanonymousException() throws JdxServiceException {
		DataBuilderOrder.itemEmail.add(DataBuilderOrder.mockCheckoutLineItem());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		JdxServiceException exception = assertThrows(JdxServiceException.class, () -> checkoutService
				.create(DataBuilderOrder.mockCheckoutRequestPayloadEmail(), true, DataBuilderOrder.userDetailsImpl));
		assertEquals("Insufficient information provided about the customer.", exception.getMessage());
	}

	@Test
	public void testcreateClientIdanonymousException() throws JdxServiceException {
		DataBuilderOrder.itemClientId.add(DataBuilderOrder.mockCheckoutLineItem());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		JdxServiceException exception = assertThrows(JdxServiceException.class, () -> checkoutService
				.create(DataBuilderOrder.mockCheckoutRequestPayloadClientId(), true, DataBuilderOrder.userDetailsImpl));
		assertEquals("Client ID was not provided and is required to perform this function.", exception.getMessage());
	}

	@Test
	public void testcreateClientIdanonymous() throws JdxServiceException, StripeException {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		user.setAuthorities(authority);
		user.setDateOfBirth("1997-12-15");
		DataBuilderOrder.itemAnonymous.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablity());
		when(userService.findOneByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(user));
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadAnonymous(), true,
						DataBuilderOrder.userDetailsImpl));
		assertEquals(
				"The email provided in an anonymous checkout is already in-use and cannot be used here without first authenticating.",
				exception.getMessage());
	}

	@Test
	public void testcreateCustomerException() throws JdxServiceException {
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadException(), false,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("Cannot find customer to associate with checkout request.", exception.getMessage());
	}

	@Test
	public void testcreateShippingException() throws JdxServiceException {
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadExceptionshipping(), false,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("There was an issue with shipping address information for checkout request.",
				exception.getMessage());
	}

	@Test
	public void testcreateShippingStateException() throws JdxServiceException {
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadExceptions(), false,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("There was an issue with shipping address information for checkout request.",
				exception.getMessage());
	}

	@Test
	public void testcreateItemsException() throws JdxServiceException {
		DataBuilderOrder.items.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.items.add(DataBuilderOrder.mockCheckoutLineItem());
		JdxServiceException exception = assertThrows(JdxServiceException.class, () -> checkoutService
				.create(DataBuilderOrder.mockCheckoutRequestPayload(), false, DataBuilderOrder.userDetailsImpl));
		assertEquals("There was an issue with the items provided for checkout request.", exception.getMessage());
	}

	@Test
	public void testcreateItemsProductIdException() throws JdxServiceException {
		DataBuilderOrder.itemsss.add(DataBuilderOrder.mockCheckoutLineItemItemEmptyProduct());
		JdxServiceException exception = assertThrows(JdxServiceException.class, () -> checkoutService
				.create(DataBuilderOrder.mockCheckoutRequestPayloadItem(), false, DataBuilderOrder.userDetailsImpl));
		assertEquals("There was an issue with the items provided for checkout request.", exception.getMessage());
	}

	@Test
	public void testcreateItemsProductException() throws JdxServiceException {
		DataBuilderOrder.itemsEmpty.add(DataBuilderOrder.mockCheckoutLineItem());
		doThrow(new NoSuchElementException()).when(productService).getProductAvailabilityWithRegion(Mockito.anyString(),
				Mockito.any(), Mockito.any());
		JdxServiceException exception = assertThrows(JdxServiceException.class, () -> checkoutService
				.create(DataBuilderOrder.mockCheckoutRequestPayloadItems(), false, DataBuilderOrder.userDetailsImpl));
		assertEquals("There is an issue determining inventory for this product", exception.getMessage());
	}

	@Test
	public void testcreateUserProductAvailabilityOutOfRegionException() throws JdxServiceException, StripeException {
		DataBuilderOrder.itemsss.add(DataBuilderOrder.mockCheckoutLineItem());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablityException());
		JdxServiceException exception = assertThrows(JdxServiceException.class, () -> checkoutService
				.create(DataBuilderOrder.mockCheckoutRequestPayloadItem(), true, DataBuilderOrder.userDetailsImpl));
		assertEquals("The product requested is not yet available in the provided region.", exception.getMessage());
	}

	@Test
	public void testcreateUserProductAvailabilitySoldOutException() throws JdxServiceException, StripeException {
		DataBuilderOrder.itemssss.add(DataBuilderOrder.mockCheckoutLineItem());
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockProductAvailablitySoldOut());
		JdxServiceException exception = assertThrows(JdxServiceException.class, () -> checkoutService
				.create(DataBuilderOrder.mockCheckoutRequestPayloadItemSold(), true, DataBuilderOrder.userDetailsImpl));
		assertEquals("The product requested is sold out.", exception.getMessage());
	}

	@Test
	public void testcreateUsersException() throws JdxServiceException, StripeException {
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadExceptions(), true,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("There was an issue with shipping address information for checkout request.",
				exception.getMessage());
	}

	@Test
	public void testcreateUsersItemsException() throws JdxServiceException, StripeException {
		DataBuilderOrder.items.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.items.add(DataBuilderOrder.mockCheckoutLineItem());
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadExceptions(), true,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("There was an issue with shipping address information for checkout request.",
				exception.getMessage());
	}

	@Test
	public void testcreateUsersMedicalException() throws JdxServiceException, StripeException {
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadExceptions(), true,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("There was an issue with shipping address information for checkout request.",
				exception.getMessage());
	}

	@Test
	void testcreateAnother() {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		user.setAuthorities(authority);
		user.setDateOfBirth("1997-12-15");
		String[] includes = {};
		DataBuilderCheckOut.items.add(DataBuilderCheckOut.mockCheckoutLineItem());
		DataBuilderCheckOut.allowedDMAs.add(DataBuilderCheckOut.mockDMA());

		DataBuilderCheckOut.allowedStates.add(DataBuilderCheckOut.mockState());
		doReturn(user).when(userService).getSystemUser();
		when(productService.getProductAvailabilityWithRegion(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderCheckOut.mockProductAvailablity());
		when(userService.findOneByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(user));
		when(userMapStructMapper.userToUserCheckout(Mockito.any())).thenReturn(DataBuilderCheckOut.mockCustomer());
		when(providerService.getProvider(DataBuilderCheckOut.mockCheckoutRequestPayload().getProvider().getId(),
				includes)).thenReturn(Optional.of(DataBuilder.getMockProvider()));
		when(checkoutRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockCheckout());
		assertEquals(checkoutService.create(DataBuilderCheckOut.mockCheckoutRequestPayload(), false).getClass(),
				DataBuilderCheckOut.mockCheckout().getClass());
	}

	@Test
	void testcreatePaymentIntent() throws StripeException {
		assertEquals(checkoutService.createPaymentIntent(DataBuilderOrder.mockCheckoutAnother()).getClass(),
				DataBuilderOrder.mockPaymentIntentDto().getClass());

	}

	@Test
	void testcreatePaymentIntentstripeCustomerAsnull() throws StripeException {
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.createPaymentIntent(DataBuilderOrder.mockCheckoutAnotherstripeCustomerAs()));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());

	}

	@Test
	public void testcreatePaymentIntenCheckOutException() throws JdxServiceException, StripeException {
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.createPaymentIntent(null));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	public void testcreatePaymentIntentCustomerException() throws JdxServiceException, StripeException {
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.createPaymentIntent(DataBuilderOrder.mockCheckoutException()));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	public void testcreatePaymentIntentCustomerStripeException() throws JdxServiceException, StripeException {
		JdxServiceException exception = assertThrows(JdxServiceException.class,
				() -> checkoutService.createPaymentIntent(DataBuilderOrder.mockCheckoutExceptions()));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	void testget() {
		when(checkoutRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockCheckout()));
		assertEquals(checkoutService.get("1L").getClass(), Optional.of(DataBuilderOrder.mockCheckout()).getClass());
	}

	@Test
	void testfind() {
		when(checkoutRepository.findCheckoutByToken(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockCheckout()));
		assertEquals(checkoutService.find(Optional.of(
				"bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5"))
				.getClass(), Optional.of(DataBuilderOrder.mockCheckout()).getClass());
	}

	@Test
	void testfindEmpty() {
		checkoutService.find(Optional.empty());

	}

	
	@Test
	void testdelete() {
		when(checkoutRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockCheckout()));
		verify(checkoutService, times(0)).delete(Mockito.anyString());
		checkoutService.delete("1L");
	}

	@Test
	void testdeleteException() {
		doThrow(JdxServiceException.class).when(checkoutService).delete(Mockito.anyString());
		JdxServiceException exception = assertThrows(JdxServiceException.class, () -> checkoutService.delete("1L"));
	}

	@Test
	void testgetDefaultProvider() {
		when(practiceService.getDefaultPractice()).thenReturn(Optional.of(DataBuilder.getMockPractice()));
		when(providerService.getDefaultProviderForPractice(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilder.getMockProvider()));
		assertEquals(checkoutService.getDefaultProvider().getClass(), DataBuilder.getMockProvider().getClass());
	}

}
