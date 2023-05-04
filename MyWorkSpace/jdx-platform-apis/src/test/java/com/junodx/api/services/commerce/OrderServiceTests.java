package com.junodx.api.services.commerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.util.VisibleForTesting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.connectors.aws.configuration.AwsConfiguration;
import com.junodx.api.connectors.aws.sns.SnsMessageResponse;
import com.junodx.api.connectors.lims.services.ElementsService;
import com.junodx.api.connectors.messaging.SnsMessageHandler;
import com.junodx.api.connectors.stripe.services.StripeService;
import com.junodx.api.controllers.SortType;
import com.junodx.api.controllers.commerce.types.OrderSortType;
import com.junodx.api.controllers.payloads.SalesforceLineItemUpdateInfo;
import com.junodx.api.controllers.payloads.SalesforceOrderUpdateAccountInfo;
import com.junodx.api.controllers.payloads.SalesforceRecordChanged;
import com.junodx.api.controllers.payloads.SalesforceUserUpdateAccountInfo;
import com.junodx.api.dto.mappers.CommerceMapStructMapper;
import com.junodx.api.dto.mappers.UserMapStructMapper;
import com.junodx.api.models.auth.Authority;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.commerce.DataBuilderorderExample;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.commerce.Product;
import com.junodx.api.models.commerce.types.OrderStatusType;
import com.junodx.api.models.commerce.types.ProductType;
import com.junodx.api.models.fulfillment.Fulfillment;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.models.laboratory.types.CustomerActionRequestType;
import com.junodx.api.models.laboratory.types.LaboratoryStatusType;
import com.junodx.api.models.laboratory.types.TestRunType;
import com.junodx.api.models.payment.Transaction;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.repositories.commerce.OrderLineItemRepository;
import com.junodx.api.repositories.commerce.OrderRepository;
import com.junodx.api.repositories.commerce.TransactionRepository;
import com.junodx.api.repositories.fulfillment.FulfillmentProviderRepository;
import com.junodx.api.repositories.fulfillment.ShippingCarrierRepository;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.auth.UserService;
import com.junodx.api.services.auth.UserServiceImpl;
import com.junodx.api.services.exceptions.JdxServiceException;
import com.junodx.api.services.fulfillment.FulfillmentProviderService;
import com.junodx.api.services.inventory.InventoryService;
import com.junodx.api.services.lab.KitService;
import com.junodx.api.services.lab.LaboratoryOrderService;
import com.junodx.api.services.lab.LaboratoryService;
import com.junodx.api.services.lab.TestRunService;
import com.junodx.api.services.mail.MailService;
import com.junodx.api.services.providers.PracticeService;
import com.junodx.api.services.providers.ProviderService;
import com.stripe.net.Webhook;

import software.amazon.awssdk.services.sns.SnsClient;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import org.apache.commons.lang3.StringUtils;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {
	@Mock
	private AwsConfiguration awsConfigurationProperties;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private UserService userService;

	@Mock
	private InventoryService inventoryService;

	@Mock
	private ElementsService elementsService;

	@Mock
	private ProviderService providerService;

	@Mock
	private LaboratoryService laboratoryService;

	@Mock
	private TestRunService testRunService;

	@Mock
	private LaboratoryOrderService laboratoryOrderService;

	@Mock
	private ProductService productService;

	@Mock
	private CheckoutService checkoutService;

	@Mock
	private PracticeService practiceService;

	@Mock
	private MailService mailService;

	@Mock
	private StripeService stripeService;

	@Mock
	private PaymentProcessorProviderService paymentProcessorProviderService;

	@Mock
	private FulfillmentProviderService fulfillmentProviderService;
	@Mock
	private FulfillmentProviderRepository fulfillmentProviderRepository;
	@Mock
	private KitService kitService;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private ShippingCarrierRepository shippingCarrierRepository;

	@Mock
	private OrderLineItemRepository orderLineitemRepository;
	@Mock
	private ObjectMapper mapper;
	@Mock
	private CommerceMapStructMapper commerceMapStructMapper;
	@Mock
	private UserMapStructMapper userMapStructMapper;

	@Mock
	private PaymentIntent payment;
	@Spy
	@InjectMocks
	private OrderService orderServices;

	@Mock
	private Order order;

	@Mock
	private UserServiceImpl userServiceImpl;

	User user = User.createDummyUser();

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testfindAll() {
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
		Pageable paging = PageRequest.of(0, 2);
		final Page<Order> pages = new PageImpl<>(orders, paging, 5);
		String[] includes = { "anjali", "gunduka" };
		when(orderRepository.findAll(paging)).thenReturn(pages);
		assertEquals(orderServices.findAll(includes, DataBuilder.getMockUserDetailsImpl(), paging), pages);
	}
  
	@Test
	public void testfindAllException() throws JdxServiceException {
		Pageable paging = PageRequest.of(0, 2);
		when(orderRepository.findAll(paging)).thenThrow(JdxServiceException.class);
		Exception exception = assertThrows(JdxServiceException.class, () -> orderServices.findAll(null, null, null));
		assertEquals("The system experienced an error processing the client's request.", exception.getMessage());
	}

	@Test
	void testfindOneByOrderId() {
		String id = "aa86f0ec-9ea1-487c-a814-b1c6001be7e8";
		String[] includes = { "anajali", "gunduka" };
		assertEquals(id, DataBuilderOrder.mockOrder().getId());
		orderRepository.findById(Mockito.anyString());
		assertEquals(orderServices.findOneByOrderId(id, includes, DataBuilder.getMockUserDetailsImpl()).getClass(),
				Optional.of(DataBuilderOrder.mockOrder()).getClass());
	}

	@Test
	void testfindByLineItem() {
		orderRepository.findOrderByLineItems(Mockito.any());
		assertEquals(orderServices.findByLineItem(DataBuilderOrder.mockOrderLineItem()).getClass(),
				Optional.of(DataBuilderOrder.mockOrder()).getClass());
	}

	@Test
	public void testfindByLineItemException() throws JdxServiceException {
		when(orderRepository.findOrderByLineItems(null)).thenThrow(JdxServiceException.class);
		assertThrows(JdxServiceException.class, () -> orderServices.findByLineItem(null));
	}

	@Test
	void testfindOrdersByCustomer() {
		String customerId = "1L";
		List<Order> orderss = new LinkedList<>();
		orderss.add(DataBuilderOrder.mockOrder());
		when(orderRepository.findOrdersByCustomer_IdOrderByOrderedAt(customerId)).thenReturn(orderss);
		assertEquals(orderServices.findOrdersByCustomer(customerId).getClass(), orderss.getClass());
	}

	@Test
	void testprocessStripeWebhookEventException() {
		String signatureHeader = "";
		String payLoad = "";
		assertThrows(JdxServiceException.class,
				() -> orderServices.processStripeWebhookEvents(signatureHeader, payLoad));
	}

//	@Test
//	void testprocessStripeWebhookEvent() {
//		String signatureHeader = "anusha";
//		String payLoad = "anusha";
//		ReflectionTestUtils.setField(orderServices, "endPointSecret", "whsec_BT325r6wJtD9rSfzgjKd4VXsX4lYLQa4");
//		orderServices.processStripeWebhookEvents(signatureHeader, payLoad);
//	}

	@Test
	public void testfindOrdersByCustomerException() throws JdxServiceException {
		when(orderRepository.findOrdersByCustomer_IdOrderByOrderedAt(null)).thenThrow(JdxServiceException.class);
		assertThrows(JdxServiceException.class, () -> orderServices.findOrdersByCustomer(null));
	}

	@Test 
	void testsearch() {
		DataBuilderOrder.orderStatusHistory.add(DataBuilderOrder.mockOrderStatus());
		Pageable paging = PageRequest.of(0, 2);
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
		final Page<Order> pages = new PageImpl<>(orders, paging, 5);
		String patientId = "1L";
		String firstName = "Juno_test";
		String lastName = "User";
		String email = "no-email@junodx.com";
		String xifinId = "BSX-FST-0001";
		String stripeId = "BSX-FST-0001";
		String orderNumber = "1234";
		String orderId = "aa86f0ec-9ea1-487c-a814-b1c6001be7e8";
		String containsProductId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		boolean requiresShipment = false;
		boolean requiresRedraw = false;
		boolean resultsAvailable = false;
		boolean isOpen = false;
		boolean withInsurance = true;
		boolean customerActivated = true;
		OrderStatusType status = OrderStatusType.CREATED;
		OrderSortType sortBy = OrderSortType.ORDERED_AT;
		SortType sortDirection = SortType.ASC;
		String kitCode = "JO135wwa33jj789";
		String sampleNumber = "123455"; 
		assertEquals(kitCode, DataBuilderOrder.mockKit().getCode());
		// assertEquals(status, DataBuilderOrder.mockOrder().getCurrentStatus());
		assertEquals(orderNumber, DataBuilderOrder.mockOrder().getOrderNumber());
		assertEquals(orderId, DataBuilderOrder.mockOrder().getId());
		assertEquals(containsProductId, DataBuilderOrder.mockProduct().getId());
		assertEquals(requiresShipment, DataBuilderOrder.mockOrder().isRequiresShipment());
		assertEquals(requiresRedraw, DataBuilderOrder.mockOrder().isRequiresRedraw());
		assertEquals(resultsAvailable, DataBuilderOrder.mockOrder().isResultsAvailable());
		assertEquals(withInsurance, DataBuilderOrder.mockOrder().isWithInsurance());
		assertEquals(sampleNumber, DataBuilderOrder.mockKit().getSampleNumber());
		assertEquals(customerActivated, DataBuilderOrder.mockOrder().getCustomer().isActivated());
		assertEquals(patientId, DataBuilderOrder.mockOrder().getCustomer().getId());
		assertEquals(firstName, DataBuilderOrder.mockOrder().getCustomer().getFirstName());
		assertEquals(lastName, DataBuilderOrder.mockOrder().getCustomer().getLastName());
		assertEquals(email, DataBuilderOrder.mockOrder().getCustomer().getEmail());
		when(orderRepository.searchOnKitCodeOrSampleNumber(kitCode, sampleNumber, paging)).thenReturn(pages);
		assertEquals(orderServices.search(Optional.of(requiresShipment), Optional.of(requiresRedraw),
				Optional.of(resultsAvailable), Optional.of(isOpen), Optional.of(status), Optional.of(withInsurance),
				Optional.of(patientId), Optional.of(firstName), Optional.of(lastName), Optional.of(email),
				Optional.of(xifinId), Optional.of(stripeId), Optional.of(customerActivated),
				Optional.of(containsProductId), Optional.of(true), Optional.of(orderNumber), Optional.of(orderId),
				Optional.of(kitCode), Optional.of(sampleNumber), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(false), Optional.of(Calendar.getInstance()), paging), pages);
	}

	@Test
	void testsearchempty() {
		DataBuilderOrder.orderStatusHistory.add(DataBuilderOrder.mockOrderStatus());
		Pageable paging = PageRequest.of(0, 2);
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
		final Page<Order> pages = new PageImpl<>(orders, paging, 5);
		String patientId = "1L";
		String firstName = "Juno_test";
		String lastName = "User";
		String email = "no-email@junodx.com";
		String xifinId = "BSX-FST-0001";
		String stripeId = "BSX-FST-0001";
		String orderNumber = "1234";
		String orderId = "aa86f0ec-9ea1-487c-a814-b1c6001be7e8";
		String containsProductId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		boolean requiresShipment = false;
		boolean requiresRedraw = false;
		boolean resultsAvailable = false;
		boolean isOpen = false;
		boolean withInsurance = true;
		boolean customerActivated = true;
		OrderStatusType status = OrderStatusType.CREATED;
		OrderSortType sortBy = OrderSortType.ORDERED_AT;
		SortType sortDirection = SortType.ASC;
		assertEquals(orderNumber, DataBuilderOrder.mockOrder().getOrderNumber());
		assertEquals(orderId, DataBuilderOrder.mockOrder().getId());
		assertEquals(containsProductId, DataBuilderOrder.mockProduct().getId());
		assertEquals(requiresShipment, DataBuilderOrder.mockOrder().isRequiresShipment());
		assertEquals(requiresRedraw, DataBuilderOrder.mockOrder().isRequiresRedraw());
		assertEquals(resultsAvailable, DataBuilderOrder.mockOrder().isResultsAvailable());
		assertEquals(withInsurance, DataBuilderOrder.mockOrder().isWithInsurance());
		assertEquals(customerActivated, DataBuilderOrder.mockOrder().getCustomer().isActivated());
		assertEquals(patientId, DataBuilderOrder.mockOrder().getCustomer().getId());
		assertEquals(firstName, DataBuilderOrder.mockOrder().getCustomer().getFirstName());
		assertEquals(lastName, DataBuilderOrder.mockOrder().getCustomer().getLastName());
		assertEquals(email, DataBuilderOrder.mockOrder().getCustomer().getEmail());
		orderServices.search(Optional.of(requiresShipment), Optional.of(requiresRedraw), Optional.of(resultsAvailable),
				Optional.of(isOpen), Optional.of(status), Optional.of(withInsurance), Optional.of(patientId),
				Optional.of(firstName), Optional.of(lastName), Optional.of(email), Optional.of(xifinId),
				Optional.of(stripeId), Optional.of(customerActivated), Optional.of(containsProductId),
				Optional.of(true), Optional.of(orderNumber), Optional.of(orderId), Optional.empty(), Optional.empty(),
				Optional.of(sortBy), Optional.of(sortDirection), Optional.of(false),
				Optional.of(Calendar.getInstance()), paging);
	}

	@Test
	void testsaveOneFromCheckout() {
		PaymentIntent payments = new PaymentIntent();
		payments.setCurrency("USD");
		payments.setAmount(2L);
		order.setCurrency(Currency.getInstance(payments.getCurrency().toUpperCase(Locale.ROOT)));
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		DataBuilderOrder.items.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.fulfillmentProviders.add(DataBuilderOrder.mockFulfillmentProvider());
		DataBuilderOrder.laboratoryProviders.add(DataBuilderOrder.mockLaboratory());
		DataBuilderOrder.licenses.add(DataBuilder.getMockMedicalLicense());
		DataBuilderOrder.carriers.add(DataBuilderOrder.mockShippingCarrier());
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		List<OrderLineItem> orderLines = new ArrayList<>();
		orderLines.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderOrder.mockOrder().setLineItems(orderLines);
		when(checkoutService.find(Optional.of(token))).thenReturn(Optional.of(DataBuilderOrder.mockCheckout()));
		// when(inventoryService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockInventoryItem()));
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(paymentProcessorProviderService.getPaymentProcessorProviderByName(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockPaymentProcessorProvider()));
		when(userService.findOne(Mockito.anyString())).thenReturn(Optional.of(user));
		when(userService.update(Mockito.any(), Mockito.any())).thenReturn(user);
		when(productService.getAllByIds(Mockito.any())).thenReturn(products);
//		when(fulfillmentProviderService.getDefaultProvider())
//				.thenReturn(Optional.of(DataBuilderOrder.mockFulfillmentProvider()));
//		when(practiceService.getDefaultPractice()).thenReturn(Optional.of(DataBuilder.getMockPractice()));
//		when(providerService.getDefaultProviderForPractice(Mockito.anyString()))
//				.thenReturn(Optional.of(DataBuilderOrder.mockapprovingProvider()));
		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrder());
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		orderServices.sendOrderCreateEmail(DataBuilderOrder.mockOrder());
		verify(checkoutService, times(0)).delete(Mockito.anyString());
		assertEquals(
				orderServices.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl()).getClass(),
				DataBuilderOrder.mockOrder().getClass());
	}

	@Test
	public void testsaveOneFromCheckoutForInventoryItemException() throws JdxServiceException {
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		PaymentIntent payments = new PaymentIntent();
		payments.setCurrency("USD");
		payments.setAmount(2L);
		DataBuilderOrder.inventoryItems.add(DataBuilderOrder.mockCheckoutLineItem());
		order.setCurrency(Currency.getInstance(payments.getCurrency().toUpperCase(Locale.ROOT)));
		when(checkoutService.find(Optional.of(token)))
				.thenReturn(Optional.of(DataBuilderOrder.mockCheckoutInventoryItems()));
		when(inventoryService.get(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveOneFromCheckoutForInventoryItem() throws JdxServiceException {
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		List<OrderLineItem> orderLines = new ArrayList<>();
		orderLines.add(DataBuilderOrder.mockOrderLineItem());
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		PaymentIntent payments = new PaymentIntent();
		payments.setCurrency("USD");
		payments.setAmount(2L);
		DataBuilderOrder.fulfillmentProviders.add(DataBuilderOrder.mockFulfillmentProvider());
		DataBuilderOrder.laboratoryProviders.add(DataBuilderOrder.mockLaboratory());
		DataBuilderOrder.licenses.add(DataBuilder.getMockMedicalLicense());
		DataBuilderOrder.carriers.add(DataBuilderOrder.mockShippingCarrier());
		DataBuilderOrder.lineInventoryItem.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderOrder.inventoryItem.add(DataBuilderOrder.mockCheckoutLineItem());
		order.setCurrency(Currency.getInstance(payments.getCurrency().toUpperCase(Locale.ROOT)));
		DataBuilderOrder.mockOrder().setLineItems(orderLines);
		when(checkoutService.find(Optional.of(token)))
				.thenReturn(Optional.of(DataBuilderOrder.mockCheckoutInventoryItem()));
		when(inventoryService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockInventoryItem()));
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(paymentProcessorProviderService.getPaymentProcessorProviderByName(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockPaymentProcessorProvider()));
		when(userService.findOneByEmailAndClientId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(user));
		when(userService.update(Mockito.any(), Mockito.any())).thenReturn(user);
		when(productService.getAllByIds(Mockito.any())).thenReturn(products);
//		when(fulfillmentProviderService.getDefaultProvider())
//				.thenReturn(Optional.of(DataBuilderOrder.mockFulfillmentProvider()));
		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrderInventoryItem());
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		orderServices.sendOrderCreateEmail(DataBuilderOrder.mockOrderInventoryItem());
		verify(checkoutService, times(0)).delete(Mockito.anyString());
		orderServices.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl());
	}

	@Test 
	public void testsaveOneFromCheckoutForDefaultPracticeException() throws JdxServiceException, StripeException {
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		List<OrderLineItem> orderLines = new ArrayList<>();
		orderLines.add(DataBuilderOrder.mockOrderLineItem());
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		PaymentIntent payments = new PaymentIntent();
		payments.setCurrency("USD");
		payments.setAmount(2L);
		DataBuilderOrder.inventoryitems.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.fulfillmentProviders.add(DataBuilderOrder.mockFulfillmentProvider());
		DataBuilderOrder.laboratoryProviders.add(DataBuilderOrder.mockLaboratory());
		DataBuilderOrder.licenses.add(DataBuilder.getMockMedicalLicense());
		DataBuilderOrder.carriers.add(DataBuilderOrder.mockShippingCarrier());
		order.setCurrency(Currency.getInstance(payments.getCurrency().toUpperCase(Locale.ROOT)));
		DataBuilderOrder.mockOrder().setLineItems(orderLines);
		when(checkoutService.find(Optional.of(token)))
				.thenReturn(Optional.of(DataBuilderOrder.mockCheckoutInventoryUse()));
		when(inventoryService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockInventoryItem()));
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(paymentProcessorProviderService.getPaymentProcessorProviderByName(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockPaymentProcessorProvider()));
		when(productService.getAllByIds(Mockito.any())).thenReturn(products);
		when(practiceService.getDefaultPractice()).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveOneFromCheckoutForDefaultProviderForPracticeException()
			throws JdxServiceException, StripeException {
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		List<OrderLineItem> orderLines = new ArrayList<>();
		orderLines.add(DataBuilderOrder.mockOrderLineItem());
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		PaymentIntent payments = new PaymentIntent();
		payments.setCurrency("USD");
		payments.setAmount(2L);
		DataBuilderOrder.itemsDefaultProviderForPractice.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.fulfillmentProviders.add(DataBuilderOrder.mockFulfillmentProvider());
		DataBuilderOrder.laboratoryProviders.add(DataBuilderOrder.mockLaboratory());
		DataBuilderOrder.licenses.add(DataBuilder.getMockMedicalLicense());
		DataBuilderOrder.carriers.add(DataBuilderOrder.mockShippingCarrier());
		order.setCurrency(Currency.getInstance(payments.getCurrency().toUpperCase(Locale.ROOT)));
		DataBuilderOrder.mockOrder().setLineItems(orderLines);
		when(checkoutService.find(Optional.of(token)))
				.thenReturn(Optional.of(DataBuilderOrder.mockCheckoutDefaultProviderForPractice()));
		when(inventoryService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockInventoryItem()));
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(paymentProcessorProviderService.getPaymentProcessorProviderByName(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockPaymentProcessorProvider()));
		when(productService.getAllByIds(Mockito.any())).thenReturn(products);
		when(practiceService.getDefaultPractice()).thenReturn(Optional.of(DataBuilder.getMockPractice()));
		when(providerService.getDefaultProviderForPractice(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveOneFromCheckoutForRequiresProviderApproval() throws JdxServiceException, StripeException {
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProductRequiresProviderApproval());
		List<OrderLineItem> orderLines = new ArrayList<>();
		orderLines.add(DataBuilderOrder.mockOrderLineItem());
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		PaymentIntent payments = new PaymentIntent();
		payments.setCurrency("USS");
		payments.setAmount(2L);
		DataBuilderOrder.itemsproduct.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.lineproduct.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderOrder.fulfillmentProviders.add(DataBuilderOrder.mockFulfillmentProvider());
		DataBuilderOrder.laboratoryProviders.add(DataBuilderOrder.mockLaboratory());
		DataBuilderOrder.licenses.add(DataBuilder.getMockMedicalLicense());
		DataBuilderOrder.carriers.add(DataBuilderOrder.mockShippingCarrier());
		order.setCurrency(Currency.getInstance(payments.getCurrency().toUpperCase(Locale.ROOT)));
		DataBuilderOrder.mockOrder().setLineItems(orderLines);
		when(checkoutService.find(Optional.of(token))).thenReturn(Optional.of(DataBuilderOrder.mockCheckoutproduct()));
		when(inventoryService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockInventoryItem()));
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(paymentProcessorProviderService.getPaymentProcessorProviderByName(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockPaymentProcessorProvider()));
		when(productService.getAllByIds(Mockito.any())).thenReturn(products);
//		when(fulfillmentProviderService.getDefaultProvider())
//				.thenReturn(Optional.of(DataBuilderOrder.mockFulfillmentProvider()));
		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrderproduct());
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		orderServices.sendOrderCreateEmail(DataBuilderOrder.mockOrderproduct());
		verify(checkoutService, times(0)).delete(Mockito.anyString());
		orderServices.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl());
	}

	@Test
	public void testsaveOneFromCheckoutForProductException() throws JdxServiceException, StripeException {
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		PaymentIntent payments = new PaymentIntent();
		List<Product> products = new LinkedList<>();
		// products.add(DataBuilderOrder.mockProduct());
		payments.setCurrency("USD");
		payments.setAmount(2L);
		user.setPatientDetails(DataBuilder.mockPatientDetails());
		order.setCurrency(Currency.getInstance(payments.getCurrency().toUpperCase(Locale.ROOT)));
		when(checkoutService.find(Optional.of(token)))
				.thenReturn(Optional.of(DataBuilderOrder.mockCheckoutInventoryUsers()));
		// when(inventoryService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockInventoryItem()));
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(paymentProcessorProviderService.getPaymentProcessorProviderByName(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockPaymentProcessorProvider()));
		when(userService.findOne(Mockito.anyString())).thenReturn(Optional.of(user));
		when(productService.getAllByIds(Mockito.any())).thenReturn(products);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveOneFromCheckoutForTokenException() throws JdxServiceException {
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		PaymentIntent payments = new PaymentIntent();
		payments.setCurrency("USD");
		payments.setAmount(2L);
		order.setCurrency(Currency.getInstance(payments.getCurrency().toUpperCase(Locale.ROOT)));
		when(checkoutService.find(Optional.of(token))).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

//	@Test
//	public void testsaveOneFromCheckoutForCatchException() throws JdxServiceException, StripeException {
//		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
//		PaymentIntent payments = new PaymentIntent();
//		payments.setCurrency("USD");
//		payments.setAmount(2L);
//		DataBuilderOrder.itemsUser.add(DataBuilderOrder.mockCheckoutLineItem());
//		DataBuilderOrder.fulfillmentProviders.add(DataBuilderOrder.mockFulfillmentProvider());
//		DataBuilderOrder.laboratoryProviders.add(DataBuilderOrder.mockLaboratory());
//		DataBuilderOrder.licenses.add(DataBuilder.getMockMedicalLicense());
//		DataBuilderOrder.carriers.add(DataBuilderOrder.mockShippingCarrier());
//		order.setCurrency(Currency.getInstance(payments.getCurrency().toUpperCase(Locale.ROOT)));
//		when(checkoutService.find(Optional.of(token)))
//				.thenReturn(Optional.of(DataBuilderOrder.mockCheckoutUser()));
//		when(inventoryService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockInventoryItem()));
//		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
//		when(paymentProcessorProviderService.getPaymentProcessorProviderByName(Mockito.anyString()))
//				.thenReturn(Optional.of(DataBuilderOrder.mockPaymentProcessorProvider()));
//		doThrow(new NoSuchElementException()).when(orderServices).update(Mockito.any(), Mockito.any());
//		JdxServiceException e = assertThrows(JdxServiceException.class,
//				() -> orderServices.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl()));
//		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
//	}

	@Test
	public void testsaveOneFromCheckoutForlabEmptyException() throws JdxServiceException {
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		PaymentIntent payments = new PaymentIntent();
		payments.setCurrency("USD");
		payments.setAmount(2L);
		order.setCurrency(Currency.getInstance(payments.getCurrency().toUpperCase(Locale.ROOT)));
		// when(checkoutService.find(Optional.of(token))).thenReturn(Optional.of(DataBuilderOrder.mockCheckout()));
		// when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveOneFromCheckoutForpaymentEmptyException() throws JdxServiceException {
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		PaymentIntent payments = new PaymentIntent();
		payments.setCurrency("USD");
		payments.setAmount(2L);
		order.setCurrency(Currency.getInstance(payments.getCurrency().toUpperCase(Locale.ROOT)));
		when(checkoutService.find(Optional.of(token))).thenReturn(Optional.of(DataBuilderOrder.mockCheckout()));
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(paymentProcessorProviderService.getPaymentProcessorProviderByName(Mockito.anyString()))
				.thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveOneFromCheckoutForuserEmptyException() throws JdxServiceException {
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		PaymentIntent payments = new PaymentIntent();
		payments.setCurrency("USD");
		payments.setAmount(2L);
		order.setCurrency(Currency.getInstance(payments.getCurrency().toUpperCase(Locale.ROOT)));
		when(checkoutService.find(Optional.of(token))).thenReturn(Optional.of(DataBuilderOrder.mockCheckout()));
		// when(inventoryService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.InventoryItem));
		// when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		// when(paymentProcessorProviderService.getPaymentProcessorProviderByName(Mockito.anyString()))
		// .thenReturn(Optional.of(DataBuilderOrder.mockPaymentProcessorProvider()));
		// when(userService.findOne(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveOneFromCheckoutForinventoryEmptyException() throws JdxServiceException {
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		PaymentIntent payments = new PaymentIntent();
		payments.setCurrency("USD");
		payments.setAmount(2L);
		order.setCurrency(Currency.getInstance(payments.getCurrency().toUpperCase(Locale.ROOT)));
		when(checkoutService.find(Optional.of(token))).thenReturn(Optional.empty());
		// when(inventoryService.get(Mockito.anyString())).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveOneFromCheckoutForinventoryUnitsEmptyException() throws JdxServiceException {
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		PaymentIntent payments = new PaymentIntent();
		payments.setCurrency("USD");
		payments.setAmount(2L);
		DataBuilderOrder.items.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.mockInventoryItem().setAvailableUnits(0);
		order.setCurrency(Currency.getInstance(payments.getCurrency().toUpperCase(Locale.ROOT)));
		when(checkoutService.find(Optional.of(token))).thenThrow(JdxServiceException.class);
		// when(inventoryService.get(Mockito.anyString())).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

//	@Test
//	public void testupdateWithKitDetails() {
//
//		DataBuilderorderExample.status.add(DataBuilderorderExample.mockLaboratoryStatuss());
//
//		List<Kit> kitss = new ArrayList<>();
//		kitss.add(DataBuilderorderExample.mockKitss());
//		user.setPatientDetails(DataBuilder.mockPatientDetails());
//		user.setLimsContactId("78788");
//		DataBuilderorderExample.orderStatusHistory.add(DataBuilderorderExample.mockOrderStatuss());
//		DataBuilderorderExample.lineItems.add(DataBuilderorderExample.mockOrderLineItemss());
//		DataBuilderorderExample.testRuns.add(DataBuilderorderExample.mockTestRuns());
//		DataBuilderorderExample.reportConfigurations.add(DataBuilderorderExample.mockReportConfigurationCounts());
//		DataBuilderorderExample.mockTestRuns().setKit(DataBuilderorderExample.mockKitss());
//		DataBuilderorderExample.mockOrderLineItemss()
//				.setLaboratoryOrderDetails(DataBuilderorderExample.mockLaboratoryOrders());
//		DataBuilderorderExample.mockTestRuns().setLaboratoryOrder(DataBuilderorderExample.mockLaboratoryOrders());
//		DataBuilderorderExample.shippingStatuss.add(DataBuilderorderExample.mockShippingStatus());
//		DataBuilderorderExample.fulfillments.add(DataBuilderOrder.mockFulfillments());
//		DataBuilderorderExample.laboratoryProviders.add(DataBuilderorderExample.mockLaboratory());
//		// DataBuilderorderExample.mockLaboratoryOrders().setOrderLineItem(DataBuilderorderExample.mockOrderLineItemss());
//		DataBuilderorderExample.mockLaboratoryOrders().setPatient(user);
//		DataBuilderOrder.mockTestRun().setLaboratoryOrder(DataBuilderorderExample.mockLaboratoryOrders());
//		DataBuilderorderExample.mockOrderStatuss().setStatusType(OrderStatusType.KIT_ASSIGNED);
//
//		DataBuilderorderExample.shippingStatuss.add(DataBuilderorderExample.mockShippingStatus());
//		when(orderRepository.findById(Mockito.anyString()))
//				.thenReturn(Optional.of(DataBuilderorderExample.mockOrders()));
//		when(kitService.updateKit(Mockito.any(), Mockito.any())).thenReturn(DataBuilderorderExample.mockKitss());
//		when(testRunService.updateTestRunLaboratoryStatus(Mockito.any(), Mockito.any()))
//				.thenReturn(DataBuilderorderExample.mockTestRuns());
//		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderorderExample.mockOrders());
//
//		when(kitService.updateKit(Mockito.any(), Mockito.any())).thenReturn(DataBuilderorderExample.mockKitss());
//		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderorderExample.mockOrders());
//		assertEquals(
//				orderServices
//						.updateWithKitDetails("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
//								"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilderorderExample.mockKitss(),
//								"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl())
//						.getClass(),
//				DataBuilderorderExample.mockOrders().getClass());
//
//	}

	@Test
	public void testupdateWithKitDetailsException() throws JdxServiceException {
		DataBuilderorderExample.status.add(DataBuilderorderExample.mockLaboratoryStatuss());
		List<Kit> kitss = new ArrayList<>();
		kitss.add(DataBuilderorderExample.mockKitss());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithKitDetails(null, "845dba6b-4c0f-4cc9-ba77-62e7216bd692",
						DataBuilderorderExample.mockKitss(), "8fdf6101-f19a-4154-a36f-521528d02654",
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("Information provided in request was insufficient to process order update.", e.getMessage());
	}

	@Test
	public void testupdateWithKitDetailsOrderException() throws JdxServiceException {
		DataBuilderorderExample.status.add(DataBuilderorderExample.mockLaboratoryStatuss());
		List<Kit> kitss = new ArrayList<>();
		kitss.add(DataBuilderorderExample.mockKitss());
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithKitDetails("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilderorderExample.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("Client ID was not provided and is required to perform this function.", e.getMessage());
	}

	@Test
	public void testupdateWithKitDetailsOrderOpenException() throws JdxServiceException {
		DataBuilderorderExample.status.add(DataBuilderorderExample.mockLaboratoryStatuss());
		List<Kit> kitss = new ArrayList<>();
		kitss.add(DataBuilderorderExample.mockKitss());
		DataBuilderorderExample.mockOrders().setOpen(false);
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderorderExample.mockOrderOpen()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithKitDetails("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilderorderExample.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("An attempt to modify a closed order is now allowed", e.getMessage());
	}

	@Test
	public void testupdateWithKitDetailsOrderlineitemsException() throws JdxServiceException {
		DataBuilderorderExample.status.add(DataBuilderorderExample.mockLaboratoryStatuss());
		List<Kit> kitss = new ArrayList<>();
		kitss.add(DataBuilderorderExample.mockKitss());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderorderExample.mockOrderslineItemsEmpty()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithKitDetails("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilderorderExample.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("One or more resources requested from the server cannot be found", e.getMessage());
	}

	@Test
	public void testupdateWithKitDetailsOrderlabOrderException() throws JdxServiceException {
		DataBuilderorderExample.lineItemsEmptyLab.add(DataBuilderorderExample.mockOrderLineItemEmptyLabOrder());
		DataBuilderorderExample.status.add(DataBuilderorderExample.mockLaboratoryStatuss());
		List<Kit> kitss = new ArrayList<>();
		kitss.add(DataBuilderorderExample.mockKitss());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderorderExample.mockOrdersEmptyLab()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithKitDetails("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilderorderExample.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("One or more resources requested from the server cannot be found", e.getMessage());
	}

	@Test
	public void testupdateWithKitDetailsTestRunException() throws JdxServiceException {
		DataBuilderorderExample.lineItemsKitCompleted.add(DataBuilderorderExample.mockOrderLineItemKitCompleted());
		DataBuilderorderExample.testRunKit.add(DataBuilderorderExample.mockTestRunCompleted());
		List<Kit> kitss = new ArrayList<>();
		kitss.add(DataBuilderorderExample.mockKitss());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderorderExample.mockOrderKitCompleted()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithKitDetails("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilderorderExample.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("Order or Test Run is in a state that disallows the requested status change", e.getMessage());
	}

	@Test
	public void testupdateWithKitDetailsTestRunBeyondKitAssignmentException() throws JdxServiceException {
		DataBuilderorderExample.statusBeyondKitAssignment
				.add(DataBuilderorderExample.mockLaboratoryStatuBeyondKitAssignment());
		DataBuilderorderExample.lineItemsBeyondKitAssignment
				.add(DataBuilderorderExample.mockOrderLineItemKitBeyondKitAssignment());
		DataBuilderorderExample.testRunBeyondKitAssignment
				.add(DataBuilderorderExample.mockTestRunBeyondKitAssignment());
		List<Kit> kitss = new ArrayList<>();
		kitss.add(DataBuilderorderExample.mockKitss());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderorderExample.mockOrderBeyondKitAssignment()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithKitDetails("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilderorderExample.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("Order or Test Run is in a state that disallows the requested status change", e.getMessage());
	}

	@Test
	public void testupdateWithKitDetailsfoundRunCatchException() throws JdxServiceException {
		DataBuilderorderExample.lineItemsTestRunEmpty.add(DataBuilderorderExample.mockOrderLineItemTesRunsEmpty());
		List<Kit> kitss = new ArrayList<>();
		kitss.add(DataBuilderorderExample.mockKitss());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderorderExample.mockOrderTestRunEmpty()));
//		when(kitService.updateKit(Mockito.any(), Mockito.any())).thenReturn(DataBuilderorderExample.mockKitss());
//		when(testRunService.updateTestRunLaboratoryStatus(Mockito.any(), Mockito.any()))
//				.thenReturn(DataBuilderorderExample.mockTestRuns());
//		when(orderRepository.save(Mockito.any())).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithKitDetails("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilderorderExample.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	public void testupdateWithKitDetailsfoundRunEmptyException() throws JdxServiceException {
		DataBuilderorderExample.lineItemsTestRunEmpty.add(DataBuilderorderExample.mockOrderLineItemTesRunsEmptys());
		List<Kit> kitss = new ArrayList<>();
		kitss.add(DataBuilderorderExample.mockKitss());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderorderExample.mockOrderTestRunEmpty()));
		when(kitService.getKit(Mockito.anyString())).thenReturn(Optional.of(DataBuilderorderExample.mockKitss()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithKitDetails("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilderorderExample.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	public void testupdateWithKitDetailsfoundRunEmptyCompletedException() throws JdxServiceException {
		DataBuilderorderExample.lineItemsTestRunEmptys.add(DataBuilderorderExample.mockOrderLineItemTesRunsEmpty());
		DataBuilderorderExample.testRunempty.add(DataBuilderorderExample.mockTestRunCompltedEmpty());
		List<Kit> kitss = new ArrayList<>();
		kitss.add(DataBuilderorderExample.mockKitss());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderorderExample.mockOrderTestRunEmptys()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithKitDetails("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilderorderExample.mockKitComplted(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	public void testupdateWithKitDetailsOrderUpdateException() throws JdxServiceException {
		DataBuilderorderExample.lineItems.add(DataBuilderorderExample.mockOrderLineItemss());
		DataBuilderorderExample.testRuns.add(DataBuilderorderExample.mockTestRuns());
		DataBuilderorderExample.status.add(DataBuilderorderExample.mockLaboratoryStatuss());
		List<Kit> kitss = new ArrayList<>();
		kitss.add(DataBuilderorderExample.mockKitss());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderorderExample.mockOrders()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithKitDetails("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilderorderExample.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("An attempt to update the order failed", e.getMessage());
	}

	@Test
	public void testcreateNewFulfillment() {
		// DataBuilderOrder.fulfillments.add(DataBuilderorderExample.mockFulfillmentss());
		DataBuilderOrder.mockCheckoutLineItem().setCheckout(DataBuilderOrder.mockCheckout());
		DataBuilderOrder.items.add(DataBuilderOrder.mockCheckoutLineItem());
		DataBuilderOrder.fulfillmentProviders.add(DataBuilderOrder.mockFulfillmentProvider());
		DataBuilderOrder.mockProduct().setInventoryItem(DataBuilderOrder.mockInventoryItem());
		DataBuilderOrder.mockProduct().setFulfillmentProviders(DataBuilderOrder.fulfillmentProviders);
		DataBuilderOrder.laboratoryProviders.add(DataBuilderOrder.mockLaboratory());
		DataBuilderOrder.licenses.add(DataBuilder.getMockMedicalLicense());
		DataBuilderOrder.carriers.add(DataBuilderOrder.mockShippingCarrier());
		DataBuilderOrder.mockFulfillment().setFulfillmentProvider(DataBuilderOrder.mockFulfillmentProvider());
		DataBuilderOrder.mockFulfillment().setOrderLineItem(DataBuilderOrder.mockOrderLineItem());
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
//		when(fulfillmentProviderRepository.findFulfillmentProviderByDefaultProviderIsTrue())
//				.thenReturn(Optional.empty());
//		when(fulfillmentProviderService.getDefaultProvider())
//				.thenReturn(Optional.of(DataBuilderOrder.mockFulfillmentProvider()));
		assertEquals(orderServices.createNewFulfillment(DataBuilderOrder.mockProduct(), user,
				DataBuilder.getMockAddress(), DataBuilderOrder.mockLaboratory()).getClass(),
				DataBuilderOrder.mockFulfillment().getClass());
	}

	@Test
	public void testsendOrderCreateEmail() {
//		List<OrderLineItem> orderLines = new ArrayList<>();
//		orderLines.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderOrder.mockOrder().setLineItems(DataBuilderOrder.lineItems);
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		orderServices.sendOrderCreateEmail(DataBuilderOrder.mockOrder());
	}

	@Test
	public void testsendOrderHasShippedEmail() {
//		List<OrderLineItem> orderLines = new ArrayList<>();
//		orderLines.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderOrder.OrderlineItemsKitPreparedForShipping.add(DataBuilderOrder.mockOrderLineItem());
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		orderServices.sendOrderHasShippedEmail(DataBuilderOrder.mockOrderKitPreparedForShipping(),
				DataBuilderOrder.mockFulfillment());
	}

	@Test
	public void testsendOrderHasShippedEmailException() {
		DataBuilderOrder.OrderlineItemsKitPreparedForShipping.add(DataBuilderOrder.mockOrderLineItem());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.sendOrderHasShippedEmail(DataBuilderOrder.mockOrderKitPreparedForShipping(),
						DataBuilderOrder.mockFulfillment()));
	}

	@Test
	public void testsendOrderCreateEmailException() {
		DataBuilderOrder.OrderlineItemsCreateEmail.add(DataBuilderOrder.mockOrderLineItem());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.sendOrderCreateEmail(DataBuilderOrder.mockOrderCreateEmail()));
	}

	@Test
	public void testsendKitPreparedForShippingException() {
		DataBuilderOrder.OrderlineItemsKitPreparedForShipping.add(DataBuilderOrder.mockOrderLineItem());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.sendKitPreparedForShipping(DataBuilderOrder.mockOrderKitPreparedForShipping(),
						DataBuilderOrder.mockFulfillment()));
	}

	@Test
	public void testsendKitActivatedEmailException() {
		DataBuilderOrder.OrderlineItemsCreateEmail.add(DataBuilderOrder.mockOrderLineItem());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.sendKitActivatedEmail(DataBuilderOrder.mockOrder()));
	}

	@Test
	public void testOps_sendOrderCreateEmail() {
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.Ops_sendOrderCreateEmail(DataBuilderOrder.mockEntityPayload()));
	}

	@Test
	public void testOps_sendOrderCreateEmailException() {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.Ops_sendOrderCreateEmail(DataBuilderOrder.mockEntityPayload()));
	}

	@Test
	public void testgetEstimatedShipmentTime() {
		assertEquals(orderServices.getEstimatedShipmentTime().getClass(),
				Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles").getDefault()).getClass());
	}

	@Test
	public void testgetEstimatedShipmentTimeElse() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(0);
		orderServices.getEstimatedShipmentTime();
	}

	@Test
	void testsaveOne() {

//		List<OrderLineItem> orderLines = new ArrayList<>();
//		orderLines.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());

		DataBuilderOrder.mockOrder().setLineItems(DataBuilderOrder.lineItems);
		String[] includes = {};
		List<Kit> kitss = new ArrayList<>();
		kitss.add(DataBuilderOrder.mockKit());
		DataBuilderOrder.mockTestRun().setLaboratoryOrder(DataBuilderOrder.mockLaboratoryOrder());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRun());
		DataBuilderOrder.mockLaboratoryOrder().setTestRuns(DataBuilderOrder.testRuns);
		DataBuilderOrder.fulfillments.add(DataBuilderOrder.mockFulfillment());
		// DataBuilderOrder.orderLineItem.setFulfillments(DataBuilderOrder.fulfillments);
		when(userService.findOne(Mockito.anyString())).thenReturn(Optional.of(user));
		when(laboratoryService.getLaboratory(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(providerService.getProvider(DataBuilderOrder.mockapprovingProvider().getId(), includes))
				.thenReturn(Optional.of(DataBuilderOrder.mockapprovingProvider()));
		when(kitService.findKitsByIds(Mockito.any())).thenReturn(kitss);
		// when(kitService.getKit(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrder());
		assertEquals(
				orderServices.saveOne(DataBuilderOrder.mockOrder(), DataBuilder.getMockUserDetailsImpl()).getClass(),
				DataBuilderOrder.mockOrder().getClass());
	}

	@Test
	void testsaveOneKit() {

//		List<OrderLineItem> orderLines = new ArrayList<>();
//		orderLines.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderOrder.OrderlineItemsKit.add(DataBuilderOrder.mockOrderLineItemkit());
		DataBuilderOrder.mockOrder().setLineItems(DataBuilderOrder.lineItems);
		String[] includes = {};
		List<Kit> kitss = new ArrayList<>();
		kitss.add(DataBuilderOrder.mockKit());
		DataBuilderOrder.mockTestRun().setLaboratoryOrder(DataBuilderOrder.mockLaboratoryOrder());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRun());
		DataBuilderOrder.mockLaboratoryOrder().setTestRuns(DataBuilderOrder.testRuns);
		DataBuilderOrder.fulfillments.add(DataBuilderOrder.mockFulfillment());
		// DataBuilderOrder.orderLineItem.setFulfillments(DataBuilderOrder.fulfillments);
		when(userService.findOne(Mockito.anyString())).thenReturn(Optional.of(user));
		when(laboratoryService.getLaboratory(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(providerService.getProvider(DataBuilderOrder.mockapprovingProvider().getId(), includes))
				.thenReturn(Optional.of(DataBuilderOrder.mockapprovingProvider()));
		when(kitService.findKitsByIds(Mockito.any())).thenReturn(kitss);
		when(kitService.getKit(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrderKit());
		assertEquals(
				orderServices.saveOne(DataBuilderOrder.mockOrderKit(), DataBuilder.getMockUserDetailsImpl()).getClass(),
				DataBuilderOrder.mockOrderKit().getClass());
	}

	@Test
	public void testsaveOneUserException() throws JdxServiceException {
		when(userService.findOne(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.saveOne(DataBuilderOrder.mockOrder(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveOnenull() throws JdxServiceException {
		orderServices.saveOne(null, DataBuilder.getMockUserDetailsImpl());
	}

	@Test
	public void testsaveOnelabOrderException() throws JdxServiceException {
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItemWithoutLab());
		when(userService.findOne(Mockito.anyString())).thenReturn(Optional.of(user));
		when(laboratoryService.getLaboratory(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.saveOne(DataBuilderOrder.mockOrder(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveOneProviderException() throws JdxServiceException {
		String[] includes = {};
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());
		when(userService.findOne(Mockito.anyString())).thenReturn(Optional.of(user));
		when(laboratoryService.getLaboratory(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(providerService.getProvider(DataBuilderOrder.mockapprovingProvider().getId(), includes))
				.thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.saveOne(DataBuilderOrder.mockOrder(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveOneKitUnsuableException() throws JdxServiceException {
		List<Kit> kitss = new ArrayList<>();
		kitss.add(DataBuilderOrder.mockKitUnusable());
		String[] includes = {};
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItemss());
		when(userService.findOne(Mockito.anyString())).thenReturn(Optional.of(user));
		when(laboratoryService.getLaboratory(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(providerService.getProvider(DataBuilderOrder.mockapprovingProvider().getId(), includes))
				.thenReturn(Optional.of(DataBuilderOrder.mockapprovingProvider()));
		when(kitService.findKitsByIds(Mockito.any())).thenReturn(kitss);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.saveOne(DataBuilderOrder.mockOrder(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testupdate() {
		assertEquals(
				orderServices.update(DataBuilderOrder.mockOrder(), DataBuilder.getMockUserDetailsImpl()).getClass(),
				DataBuilderOrder.mockOrder().getClass());
	}

	@Test
	public void testupdateException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.update(null, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testdeleteOrder() {
		verify(orderRepository, times(0)).delete(DataBuilderOrder.mockOrder());
		orderServices.deleteOrder(DataBuilderOrder.mockOrder());
	}

	@Test
	void testupdateWithLimsDetails() {
		assertEquals(orderServices.updateWithLimsDetails("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
				"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilderOrder.mockElementsOrder(),
				DataBuilder.getMockUserDetailsImpl()), null);
	}

	@Test
	public void testassignNewKitToOrder() {
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		String[] includes = {};
		user.setPatientDetails(DataBuilder.mockPatientDetails());
		user.setLimsContactId("78788");
		DataBuilderorderExample.lineItems.add(DataBuilderorderExample.mockOrderLineItemss());

		DataBuilderOrder.fulfillments.add(DataBuilderorderExample.mockFulfillments());

		DataBuilderorderExample.lineItems.add(DataBuilderorderExample.mockOrderLineItemss());
		// DataBuilderOrder.fulfillments.add(DataBuilderOrder.mockFulfillment());

		DataBuilderorderExample.laboratoryProviders.add(DataBuilderorderExample.mockLaboratory());
		// DataBuilderOrder.mockProduct().setLaboratoryProviders(DataBuilderOrder.laboratoryProviders);
		// DataBuilderOrder.mockOrderLineItem().setLaboratoryOrderDetails(DataBuilderOrder.laboratoryOrder);
		DataBuilderorderExample.mockLaboratoryOrders().setOrderLineItem(DataBuilderorderExample.mockOrderLineItemss());
		// DataBuilderOrder.mockOrderLineItem().setFulfillments(DataBuilderOrder.fulfillments);
		DataBuilderorderExample.mockFulfillments()
				.setFulfillmentProvider(DataBuilderorderExample.mockFulfillmentProvider());
		DataBuilderorderExample.mockFulfillments().setOrderLineItem(DataBuilderorderExample.mockOrderLineItemss());
		DataBuilderorderExample.mockLaboratoryOrders()
				.setProviderApproval(DataBuilderorderExample.mockProviderApproval());
		DataBuilderOrder.mockElementsPractitioner().setId("78788");
		DataBuilder.getMockPractice().setLimsId("78788");
		DataBuilderOrder.mockapprovingProvider().setLimsId("78788");
		DataBuilderOrder.mockLaboratoryOrder().setPatient(user);

		DataBuilder.getMockPractice().setLimsId("78788");
		DataBuilderOrder.mockProduct().setLimsReportId("78788");
		DataBuilderOrder.mockElementsReport().setReportTypeId(DataBuilderOrder.mockProduct().getLimsReportId());
		// DataBuilderOrder.mockElementsClinic().setId(Integer.valueOf(DataBuilder.getMockPractice().getLimsId()));
		DataBuilderorderExample.mockTestRuns().setLimsReportId("78788");
		DataBuilderOrder.mockElementsReport().setExternalId(DataBuilderOrder.mockTestRun().getLimsReportId());

		when(productService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockProduct()));
		when(fulfillmentProviderService.getDefaultProvider())
				.thenReturn(Optional.of(DataBuilderOrder.mockFulfillmentProvider()));

		when(productService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockProduct()));
		when(practiceService.getPractice(DataBuilderOrder.mockapprovingProvider().getPracticeId(), includes))
				.thenReturn(DataBuilder.getMockPractice());

		when(elementsService.createOrder(Mockito.any())).thenReturn(DataBuilderOrder.mockElementsOrder());
		when(kitService.updateKit(Mockito.any(), Mockito.any())).thenReturn(DataBuilderOrder.mockKitss());

		orderServices.assignNewKitToOrder(DataBuilderorderExample.mockOrders(),
				DataBuilderorderExample.mockOrderLineItemss(), DataBuilderOrder.mockKitss(),
				"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl());
	}

	@Test
	public void testassignNewKitToOrderException() throws JdxServiceException {
		DataBuilderorderExample.lineItems.add(DataBuilderorderExample.mockOrderLineItemsss());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.assignNewKitToOrder(DataBuilderorderExample.mockOrders(),
						DataBuilderorderExample.mockOrderLineItemss(), DataBuilderOrder.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testassignNewKitToOrderLabOrderException() throws JdxServiceException {
		DataBuilderorderExample.lineItemOrderLabEmpty.add(DataBuilderorderExample.mockOrderLineItemOrderEmpty());
		// when(productService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockProduct()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.assignNewKitToOrder(DataBuilderorderExample.mockOrderLabEmpty(),
						DataBuilderorderExample.mockOrderLineItemOrderEmpty(), DataBuilderOrder.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testassignNewKitToOrderlabProvidersException() throws JdxServiceException {
		DataBuilderorderExample.lineItemProduct.add(DataBuilderorderExample.mockOrderLineItemOrderProduct());
		when(productService.get(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockProductEmptyLabProvider()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.assignNewKitToOrder(DataBuilderorderExample.mockOrdermProduct(),
						DataBuilderorderExample.mockOrderLineItemOrderProduct(), DataBuilderOrder.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testassignNewKitToOrderlimsOrderException() throws JdxServiceException {
		String[] includes = {};
		DataBuilderorderExample.lineItemlimOrder.add(DataBuilderorderExample.mockOrderLineItemOrderlim());
		DataBuilderorderExample.fulfillmentempty.add(DataBuilderorderExample.mockFulfillmentKit());
		when(productService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockProductlimsOrder()));
//		when(fulfillmentProviderService.getDefaultProvider())
//				.thenReturn(Optional.of(DataBuilderOrder.mockFulfillmentProvider()));
		when(practiceService.getPractice(DataBuilderOrder.mockapprovingProvider().getPracticeId(), includes))
				.thenReturn(DataBuilder.getMockPractice());
		when(elementsService.createOrder(Mockito.any())).thenReturn(null);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.assignNewKitToOrder(DataBuilderorderExample.mockOrderlimOrder(),
						DataBuilderorderExample.mockOrderLineItemOrderlim(), DataBuilderOrder.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testassignNewKitToOrderElementException() throws JdxServiceException {
		DataBuilderorderExample.lineItems.add(DataBuilderorderExample.mockOrderLineItemsss());
		when(productService.get(Mockito.anyString())).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.assignNewKitToOrder(DataBuilderorderExample.mockOrders(),
						DataBuilderorderExample.mockOrderLineItemss(), DataBuilderOrder.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	public void testassignExistingKitToOrder() {
		DataBuilderorderExample.lineItemassignExistingKit
				.add(DataBuilderorderExample.mockOrderLineItemOrderassignExistingKit());
		DataBuilderorderExample.fulfillmentassignExistingKit.add(DataBuilderorderExample.mockFulfillmentKit());
		when(kitService.updateKit(Mockito.any(), Mockito.any())).thenReturn(DataBuilderOrder.mockKits());
		orderServices.assignExistingKitToOrder(DataBuilderorderExample.mockOrderassignExistingKit(),
				DataBuilderorderExample.mockOrderLineItemOrderassignExistingKit(), DataBuilderOrder.mockKits(),
				"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl());
	}

	@Test
	public void testassignExistingKitToOrders() {
		DataBuilderorderExample.lineItemassignExistingKits
				.add(DataBuilderorderExample.mockOrderLineItemOrderassignExistingKits());
		// DataBuilderorderExample.fulfillmentassignExistingKits.add(DataBuilderorderExample.mockFulfillmentKit());
		when(productService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockProduct()));
		when(fulfillmentProviderService.getDefaultProvider())
				.thenReturn(Optional.of(DataBuilderOrder.mockFulfillmentProvider()));
		when(kitService.updateKit(Mockito.any(), Mockito.any())).thenReturn(DataBuilderOrder.mockKits());
		orderServices.assignExistingKitToOrder(DataBuilderorderExample.mockOrderassignExistingKits(),
				DataBuilderorderExample.mockOrderLineItemOrderassignExistingKits(), DataBuilderOrder.mockKits(),
				"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl());
	}

	@Test
	public void testassignExistingKitToOrderException() throws JdxServiceException {
		DataBuilderorderExample.lineItems.add(DataBuilderorderExample.mockOrderLineItemsss());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.assignExistingKitToOrder(DataBuilderorderExample.mockOrders(),
						DataBuilderorderExample.mockOrderLineItemss(), DataBuilderOrder.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testassignExistingKitToOrderlabProvidersException() throws JdxServiceException {
		DataBuilderorderExample.lineItemProducts.add(DataBuilderorderExample.mockOrderLineItemOrderProducts());
		// DataBuilderorderExample.fulfillmentProducts.add(DataBuilderorderExample.mockFulfillmentKit());
		when(productService.get(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockProductEmptyLabProviders()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.assignExistingKitToOrder(DataBuilderorderExample.mockOrdermProducts(),
						DataBuilderorderExample.mockOrderLineItemOrderProducts(), DataBuilderOrder.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testassignExistingKitToOrderProductException() throws JdxServiceException {
		DataBuilderorderExample.lineItemOrderLabEmpty.add(DataBuilderorderExample.mockOrderLineItemOrderEmpty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.assignExistingKitToOrder(DataBuilderorderExample.mockOrderLabEmpty(),
						DataBuilderorderExample.mockOrderLineItemOrderEmpty(), DataBuilderOrder.mockKitss(),
						"8fdf6101-f19a-4154-a36f-521528d02654", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateLimsWithOrder() {

		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());

		DataBuilderOrder.fulfillments.add(DataBuilderOrder.mockFulfillment());
		DataBuilderOrder.laboratoryProviders.add(DataBuilderOrder.mockLaboratory());
		DataBuilderOrder.mockProduct().setLaboratoryProviders(DataBuilderOrder.laboratoryProviders);
		DataBuilderOrder.mockFulfillment().setFulfillmentProvider(DataBuilderOrder.mockFulfillmentProvider());
		DataBuilderOrder.mockLaboratoryOrder().setProviderApproval(DataBuilderOrder.mockProviderApproval());
		DataBuilderOrder.mockElementsPractitioner().setId("78788");
		DataBuilder.getMockPractice().setLimsId("78788");
		DataBuilderOrder.mockapprovingProvider().setLimsId("78788");
		DataBuilderOrder.mockLaboratoryOrder().setPatient(user);
		String[] includes = {};
		DataBuilder.getMockPractice().setLimsId("78788");
		DataBuilderOrder.mockProduct().setLimsReportId("78788");
		DataBuilderOrder.mockElementsReport().setReportTypeId(DataBuilderOrder.mockProduct().getLimsReportId());
		// DataBuilderOrder.mockElementsClinic().setId(Integer.valueOf(DataBuilder.getMockPractice().getLimsId()));
		DataBuilderOrder.mockTestRun().setLimsReportId("78788");
		DataBuilderOrder.mockElementsReport().setExternalId(DataBuilderOrder.mockTestRun().getLimsReportId());

		when(productService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockProduct()));
		when(practiceService.getPractice(DataBuilderOrder.mockapprovingProvider().getPracticeId(), includes))
				.thenReturn(DataBuilder.getMockPractice());

		when(elementsService.createOrder(Mockito.any())).thenReturn(DataBuilderOrder.mockElementsOrder());

		assertEquals(
				orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrder(), DataBuilderOrder.mockOrderLineItem(),
						DataBuilderOrder.mockTestRun(), DataBuilderOrder.mockKit(),
						DataBuilder.getMockUserDetailsImpl()).getClass(),
				DataBuilderOrder.mockElementsOrder().getClass());

	}

	@Test
	public void testupdateLimsWithOrderapprovingProvider() {
		String[] includes = {};
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.OrderlineItemsapprovingProviders.add(DataBuilderOrder.mockOrderLineItemapprovingProviders());
		when(providerService.getDefaultProvider()).thenReturn(Optional.of(DataBuilder.getMockProvider()));
		when(productService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockProduct()));
		when(elementsService.createOrder(Mockito.any())).thenReturn(DataBuilderOrder.mockElementsOrder());
		orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrderapprovingProviders(),
				DataBuilderOrder.mockOrderLineItemapprovingProviders(), DataBuilderOrder.mockTestRun(),
				DataBuilderOrder.mockKit(), DataBuilder.getMockUserDetailsImpl());

	}

	@Test
	public void testupdateLimsWithOrderapprovingProviderEmptyException() throws JdxServiceException {
		String[] includes = {};
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.OrderlineItemsapprovingProvider.add(DataBuilderOrder.mockOrderLineItemapprovingProvider());
		when(practiceService.getPractice(DataBuilderOrder.mockapprovingProvider().getPracticeId(), includes))
				.thenReturn(DataBuilder.getMockPractice());

		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrderapprovingProvider(),
						DataBuilderOrder.mockOrderLineItemapprovingProvider(), DataBuilderOrder.mockTestRun(),
						DataBuilderOrder.mockKit(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	public void testupdateLimsWithOrderupdateLimsEmptyException() throws JdxServiceException {

		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.OrderlineItemsDefaultPractice.add(DataBuilderOrder.mockOrderLineItemDefaultPractice());

		when(practiceService.getDefaultPractice()).thenReturn(Optional.of(DataBuilder.getMockPractice()));
		when(providerService.getDefaultProviderForPractice(Mockito.any()))
				.thenReturn(Optional.of(DataBuilder.getMockProvider()));
		when(productService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockProductEmptyLims()));

		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrderDefaultPractice(),
						DataBuilderOrder.mockOrderLineItemDefaultPractice(), DataBuilderOrder.mockTestRun(),
						DataBuilderOrder.mockKit(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	public void testupdateLimsWithOrderDefaultPracticeEmptyException() throws JdxServiceException {

		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.OrderlineItemsDefaultPractice.add(DataBuilderOrder.mockOrderLineItemDefaultPractice());

		when(practiceService.getDefaultPractice()).thenReturn(Optional.empty());

		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrderDefaultPractice(),
						DataBuilderOrder.mockOrderLineItemDefaultPractice(), DataBuilderOrder.mockTestRun(),
						DataBuilderOrder.mockKit(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	public void testupdateLimsWithOrderDefaultProviderForPracticeEmptyException() throws JdxServiceException {

		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.OrderlineItemsDefaultPractice.add(DataBuilderOrder.mockOrderLineItemDefaultPractice());

		when(practiceService.getDefaultPractice()).thenReturn(Optional.of(DataBuilder.getMockPractice()));
		when(providerService.getDefaultProviderForPractice(Mockito.any())).thenReturn(Optional.empty());

		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrderDefaultPractice(),
						DataBuilderOrder.mockOrderLineItemDefaultPractice(), DataBuilderOrder.mockTestRun(),
						DataBuilderOrder.mockKit(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	public void testupdateLimsWithOrdersException() throws JdxServiceException {
		DataBuilderOrder.OrderlineDefaultProvider.add(DataBuilderOrder.mockOrderLineItemRequiresApproval());
		when(providerService.getDefaultProvider()).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrderDefaultProvider(),
						DataBuilderOrder.mockOrderLineItemRequiresApproval(), DataBuilderOrder.mockTestRun(),
						DataBuilderOrder.mockKit(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateLimsWithOrderEmptyException() throws JdxServiceException {

		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateLimsWithOrder(null, DataBuilderOrder.mockOrderLineItem(),
						DataBuilderOrder.mockTestRun(), DataBuilderOrder.mockKit(),
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateLimsWithOrderLaboratoryOrderEmptyException() throws JdxServiceException {
		DataBuilderOrder.OrderlineItemsLab.add(DataBuilderOrder.mockOrderLineItemWithoutLabs());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrderActionLab(),
						DataBuilderOrder.mockOrderLineItemWithoutLabs(), DataBuilderOrder.mockTestRun(),
						DataBuilderOrder.mockKit(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateLimsWithOrderException() throws JdxServiceException {
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrder(),
						DataBuilderOrder.mockOrderLineItem(), DataBuilderOrder.mockTestRun(),
						DataBuilderOrder.mockKit(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateLimsWithOrderPatientException() throws JdxServiceException {
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItemWithoutPatient());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrder(),
						DataBuilderOrder.mockOrderLineItemWithoutPatient(), DataBuilderOrder.mockTestRun(),
						DataBuilderOrder.mockKit(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateLimsWithOrderPracticeException() throws JdxServiceException {
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItemss());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrder(),
						DataBuilderOrder.mockOrderLineItem(), DataBuilderOrder.mockTestRun(),
						DataBuilderOrder.mockKit(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateLimsWithOrderPracticeEmptyException() throws JdxServiceException {
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItemss());
		String[] includes = {};
		when(practiceService.getPractice(DataBuilderOrder.mockapprovingProvider().getPracticeId(), includes))
				.thenReturn(DataBuilder.getMockPracticeLims());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrder(),
						DataBuilderOrder.mockOrderLineItem(), DataBuilderOrder.mockTestRun(),
						DataBuilderOrder.mockKit(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateLimsWithOrderPracticeLimsIdEmptyException() throws JdxServiceException {
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItemss());
		String[] includes = {};
		DataBuilder.getMockPractice().setLimsId(null);
		when(practiceService.getPractice(DataBuilderOrder.mockapprovingProvider().getPracticeId(), includes))
				.thenReturn(DataBuilder.getMockPractice());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrder(),
						DataBuilderOrder.mockOrderLineItem(), DataBuilderOrder.mockTestRun(),
						DataBuilderOrder.mockKit(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateLimsWithOrderProductEmptyException() throws JdxServiceException {
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItemss());
		String[] includes = {};
		DataBuilder.getMockPractice().setLimsId(null);
		when(practiceService.getPractice(DataBuilderOrder.mockapprovingProvider().getPracticeId(), includes))
				.thenReturn(DataBuilder.getMockPractice());
		when(productService.get(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrder(),
						DataBuilderOrder.mockOrderLineItem(), DataBuilderOrder.mockTestRun(),
						DataBuilderOrder.mockKit(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateLimsWithOrderProductLimsIdEmptyException() throws JdxServiceException {
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItemss());
		String[] includes = {};
		DataBuilder.getMockPractice().setLimsId(null);
		DataBuilderOrder.mockProduct().setLimsReportId(null);
		when(practiceService.getPractice(DataBuilderOrder.mockapprovingProvider().getPracticeId(), includes))
				.thenReturn(DataBuilder.getMockPractice());
		when(productService.get(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateLimsWithOrder(DataBuilderOrder.mockOrder(),
						DataBuilderOrder.mockOrderLineItem(), DataBuilderOrder.mockTestRun(),
						DataBuilderOrder.mockKit(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderStatus() {

		assertEquals(
				orderServices.updateOrderStatus(DataBuilderOrder.mockOrder(), OrderStatusType.KIT_ASSIGNED).getClass(),
				DataBuilderOrder.mockOrder().getClass());

	}

	@Test
	public void testupdateOrderWithNewStatus() {
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.orderStatusHistory.add(DataBuilderOrder.mockOrderStatus());
		DataBuilderOrder.mockOrderStatus().setStatusType(OrderStatusType.KIT_ASSIGNED);
		DataBuilderOrder.mockOrder().setOrderStatusHistory(DataBuilderOrder.orderStatusHistory);
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		// when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.order);
		assertEquals(
				orderServices.updateOrderWithNewStatus("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						DataBuilderOrder.mockOrderStatus(), DataBuilder.getMockUserDetailsImpl()).getClass(),
				DataBuilderOrder.mockOrder().getClass());

	}

	@Test
	public void testupdateOrderWithNewStatusCase1() {
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.orderStatusHistory.add(DataBuilderOrder.mockOrderStatus());
		DataBuilderOrder.mockOrderStatus().setStatusType(OrderStatusType.KIT_ASSIGNED);
		DataBuilderOrder.mockOrder().setOrderStatusHistory(DataBuilderOrder.orderStatusHistory);
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrder());
		assertEquals(
				orderServices
						.updateOrderWithNewStatus("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
								DataBuilderOrder.mockOrderStatusRefunded(), DataBuilder.getMockUserDetailsImpl())
						.getClass(),
				DataBuilderOrder.mockOrder().getClass());

	}

	@Test
	public void testupdateOrderWithNewStatusCase2() {
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		assertEquals(orderServices
				.updateOrderWithNewStatus("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						DataBuilderOrder.mockOrderStatusResults_Viewed(), DataBuilder.getMockUserDetailsImpl())
				.getClass(), DataBuilderOrder.mockOrder().getClass());

	}

	@Test
	public void testupdateOrderWithNewStatusCase3() {
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		assertEquals(
				orderServices
						.updateOrderWithNewStatus("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
								DataBuilderOrder.mockOrderStatusClosed(), DataBuilder.getMockUserDetailsImpl())
						.getClass(),
				DataBuilderOrder.mockOrder().getClass());

	}

	@Test
	public void testupdateOrderWithNewStatusCase4() {
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		assertEquals(
				orderServices
						.updateOrderWithNewStatus("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
								DataBuilderOrder.mockOrderStatusCanceled(), DataBuilder.getMockUserDetailsImpl())
						.getClass(),
				DataBuilderOrder.mockOrder().getClass());

	}

	@Test
	public void testupdateOrderWithNewStatusOrderException() throws JdxServiceException {

		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderWithNewStatus(null, DataBuilderOrder.mockOrderStatus(),
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderWithNewStatusOrderStatusException() throws JdxServiceException {

		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderWithNewStatus("aa86f0ec-9ea1-487c-a814-b1c6001be7e8", null,
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderWithNewStatusOrderEmptyStatusException() throws JdxServiceException {
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderWithNewStatus("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						DataBuilderOrder.mockOrderStatus(), DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testcloseOrder() {
		assertEquals(
				orderServices.closeOrder("aa86f0ec-9ea1-487c-a814-b1c6001be7e8", DataBuilder.getMockUserDetailsImpl()),
				null);
	}

	@Test
	void testcancelOrder() {
		assertEquals(
				orderServices.cancelOrder("aa86f0ec-9ea1-487c-a814-b1c6001be7e8", DataBuilder.getMockUserDetailsImpl()),
				null);
	}

	@Test
	public void testupdateWithRedraw() {
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderOrder.customerActionRequests.add(DataBuilderOrder.mockCustomerActionRequest());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRun());
		DataBuilderOrder.mockLaboratoryOrder().setTestRuns(DataBuilderOrder.testRuns);
		DataBuilderOrder.fulfillments.add(DataBuilderOrder.mockFulfillment());
		DataBuilderOrder.mockOrderStatus().setStatusType(OrderStatusType.REDRAW_APPROVED);
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrder());
		assertEquals(orderServices.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
				"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "1L", false, false, DataBuilder.getMockUserDetailsImpl())
				.getClass(), DataBuilderOrder.mockOrder().getClass());
	}

	@Test
	public void testupdateWithRedrawActionRequest() {
		DataBuilderOrder.OrderlineItemsActionRequest.add(DataBuilderOrder.mockOrderLineItemActionRequest());
		DataBuilderOrder.customerActionRequestss.add(DataBuilderOrder.mockCustomerActionRequesOrdert());
		DataBuilderOrder.fulfillmentsActionRequest.add(DataBuilderOrder.mockFulfillment());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderActionRequest()));
		when(productService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockProduct()));
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(fulfillmentProviderService.getDefaultProvider())
				.thenReturn(Optional.of(DataBuilderOrder.mockFulfillmentProvider()));
		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrderActionRequest());
		assertEquals(orderServices.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
				"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "1L", false, true, DataBuilder.getMockUserDetailsImpl())
				.getClass(), DataBuilderOrder.mockOrderActionRequest().getClass());

	}

	@Test
	public void testupdateWithRedrawOrderCustomerActionRequest() {
		DataBuilderOrder.OrderlineItemsActionRequestApproves.add(DataBuilderOrder.mockOrderLineItemActionRequests());
		DataBuilderOrder.customerActionRequestApproves.add(DataBuilderOrder.mockCustomerActionRequesOrderts());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderActionRequestApproves()));
		orderServices.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8", "845dba6b-4c0f-4cc9-ba77-62e7216bd692",
				"1L", true, false, DataBuilder.getMockUserDetailsImpl());
	}

	@Test
	public void testupdateWithRedrawOrderCustomerActionRequestException() throws JdxServiceException {
		DataBuilderOrder.OrderlineItemsActionRequestApprove.add(DataBuilderOrder.mockOrderLineItemActionRequest());
		DataBuilderOrder.customerActionRequestApprove.add(DataBuilderOrder.mockCustomerActionRequest());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderActionRequestApprove()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "1L", true, false,
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRedrawOrderLineItemsEmptyException() throws JdxServiceException {
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderActionRequestApprovefalse()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "1L", true, false,
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRedrawLabEmptyException() throws JdxServiceException {
		DataBuilderOrder.OrderlineItemsLab.add(DataBuilderOrder.mockOrderLineItemWithoutLabs());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderActionLab()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "1L", true, false,
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRedrawTestRunEmptyException() throws JdxServiceException {
		DataBuilderOrder.OrderlineItemsTestRuns.add(DataBuilderOrder.mockOrderLineItemWithoutTestRun());
		DataBuilderOrder.tesRunEmpty.add(DataBuilderOrder.mockTestRunDiffeId());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderActionTestRuns()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "1L", true, false,
						DataBuilder.getMockUserDetailsImpl()));
	}

	@Test
	public void testupdateWithRedrawOrderIdException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> orderServices.updateWithRedraw(null,
				"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "1L", false, false, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRedrawActionRequestOrderLineItemEmpty() throws JdxServiceException {
		DataBuilderOrder.customerLineItemEmpty.add(DataBuilderOrder.mockCustomerActionRequesOrdert());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderLineItemEmpty()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "1L", false, true,
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRedrawActionRequestProductIdEmpty() throws JdxServiceException {
		DataBuilderOrder.OrderlineItemsActionRequestApproveProduct
				.add(DataBuilderOrder.mockOrderLineItemActionRequestProduct());
		DataBuilderOrder.customerActionRequestApproveProduct.add(DataBuilderOrder.mockCustomerActionRequesOrdert());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderActionRequestApproveProduct()));
		when(productService.get(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "1L", false, true,
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRedrawActionRequestlaboratoryServiceIdEmpty() throws JdxServiceException {
		DataBuilderOrder.OrderlineItemsActionRequestApproveProduct
				.add(DataBuilderOrder.mockOrderLineItemActionRequestProduct());
		DataBuilderOrder.customerActionRequestApproveProduct.add(DataBuilderOrder.mockCustomerActionRequesOrdert());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderActionRequestApproveProduct()));
		when(productService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockProduct()));
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "1L", false, true,
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRedrawrunIdException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", null, false, false,
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRedrawApproveException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "1L", true, true,
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRedrawApproveOrderIdException() throws JdxServiceException {
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "1L", false, false,
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRedrawCustomerAprroveException() throws JdxServiceException {
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());
		when(orderRepository.findById(Mockito.anyString())).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "1L", false, false,
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testupdateWithRetest() {
		assertEquals(orderServices.updateWithRetest("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
				"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "c06071ba-bf8e-414c-8622-2aa57d5b6a7f",
				DataBuilder.getMockUserDetailsImpl()), null);
	}

	@Test
	public void testupdateWithRefund() {
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderOrder.transactions.add(DataBuilderOrder.mockTransaction());
		DataBuilderOrder.mockOrder().setTransactions(DataBuilderOrder.transactions);
		DataBuilderOrder.mockTransaction().setOrder(DataBuilderOrder.mockOrder());
		DataBuilderOrder.mockOrderStatus().setStatusType(OrderStatusType.REFUND_PROCESSING);
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		when(stripeService.issueRefund(Mockito.anyString())).thenReturn(DataBuilderOrder.mockTransaction());
		DataBuilderOrder.mockOrderStatus().setStatusType(OrderStatusType.REFUNDED);
		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrder());
		assertEquals(orderServices.updateWithRefund("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
				"845dba6b-4c0f-4cc9-ba77-62e7216bd692", false, true, DataBuilder.getMockUserDetailsImpl()).getClass(),
				DataBuilderOrder.mockOrder().getClass());
	}

	@Test
	public void testupdateWithRefundIsCreate() {
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrder());
		assertEquals(orderServices.updateWithRefund("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
				"845dba6b-4c0f-4cc9-ba77-62e7216bd692", true, false, DataBuilder.getMockUserDetailsImpl()).getClass(),
				DataBuilderOrder.mockOrder().getClass());
	}

	@Test
	public void testupdateWithRefundIntentsException() throws JdxServiceException {
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderTransaction()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRefund("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", false, true, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRefundIntentstripeServiceException() throws JdxServiceException {
		DataBuilderOrder.transactions.add(DataBuilderOrder.mockTransaction());
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRefund("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", false, true, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRefundOrderIdException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> orderServices.updateWithRefund(null,
				"845dba6b-4c0f-4cc9-ba77-62e7216bd692", false, true, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRefundApproveException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRefund("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", true, true, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRefundApproveOrderIdException() throws JdxServiceException {
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRefund("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", false, false, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRefundIntentException() throws JdxServiceException {
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderTransaction()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRefund("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", false, false, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateWithRefundStripeException() throws JdxServiceException {
		when(orderRepository.findById(Mockito.anyString())).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateWithRefund("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", false, false, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}


//	@Test
//	public void testupdateOrderFromLabReceipt() {
//
//		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());
//
//		DataBuilderOrder.mockOrder().setLineItems(DataBuilderOrder.lineItems);
//		DataBuilderOrder.fulfillments.add(DataBuilderOrder.mockFulfillment());
//
//		DataBuilderOrder.mockOrderStatus().setStatusType(OrderStatusType.RECEIVED);
//
//		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
//		when(testRunService.getTestRunForKitId(Mockito.anyString()))
//				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
//		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString()))
//				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
//		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
//
//		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrder());
//		assertEquals(orderServices.updateOrderFromLabReceipt("123455", DataBuilder.getMockUserDetailsImpl()).getClass(),
//				DataBuilderOrder.mockOrder().getClass());
//	}
	
	@Test
	public void testupdateOrderFromLabReceipt() {

		DataBuilderOrder.OrderlineItemsFromLabReceipt.add(DataBuilderOrder.mockOrderLineItemFromLabReceipt());
		DataBuilderOrder.fulfillmentLabReceipt.add(DataBuilderOrder.mockFulfillment());
		//DataBuilderOrder.mockOrder().setLineItems(DataBuilderOrder.lineItems);
		DataBuilderOrder.fulfillments.add(DataBuilderOrder.mockFulfillment());

		DataBuilderOrder.mockOrderStatus().setStatusType(OrderStatusType.RECEIVED);

		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrderFromLabReceipt()));

		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrderFromLabReceipt());
		assertEquals(orderServices.updateOrderFromLabReceipt("123455", DataBuilder.getMockUserDetailsImpl()).getClass(),
				DataBuilderOrder.mockOrderFromLabReceipt().getClass());
	}

	@Test
	public void testupdateOrderFromLabReceiptSampleException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromLabReceipt(null, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabReceiptKitException() throws JdxServiceException {
		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromLabReceipt("123455", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabReceiptTestException() throws JdxServiceException {
		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromLabReceipt("123455", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabReceiptlabException() throws JdxServiceException {
		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromLabReceipt("123455", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabReceiptOrderException() throws JdxServiceException {
		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromLabReceipt("123455", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabReceiptParentOrderIdException() throws JdxServiceException {
		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrderParentid()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromLabReceipt("123455", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabReceiptlineItemsException() throws JdxServiceException {
		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderLineItemEmpty()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromLabReceipt("123455", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithReport() {
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());

		DataBuilderOrder.mockOrder().setLineItems(DataBuilderOrder.lineItems);
		DataBuilderOrder.mockOrderLineItem().setOrder(DataBuilderOrder.mockOrder());

		DataBuilderOrder.mockOrderStatus().setStatusType(OrderStatusType.LABORATORY_PROCESSING);

		when(testRunService.findTestRunByLimsReportId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));

		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrder());
		assertEquals(orderServices.updateOrderFromLabProcessingWithReport("7898", DataBuilder.getMockUserDetailsImpl())
				.getClass(), DataBuilderOrder.mockOrder().getClass());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithReportParentOrderIdException() throws JdxServiceException {
		when(testRunService.findTestRunByLimsReportId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrderParentid()));

		JdxServiceException e = assertThrows(JdxServiceException.class, () -> orderServices
				.updateOrderFromLabProcessingWithReport("7898", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithReportlineItemsException() throws JdxServiceException {
		when(testRunService.findTestRunByLimsReportId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderLineItemEmpty()));
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> orderServices
				.updateOrderFromLabProcessingWithReport("7898", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithReportIdException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromLabProcessingWithReport(null, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithReportTestException() throws JdxServiceException {
		when(testRunService.findTestRunByLimsReportId(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> orderServices
				.updateOrderFromLabProcessingWithReport("7898", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithReportLabOrderException() throws JdxServiceException {
		when(testRunService.findTestRunByLimsReportId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> orderServices
				.updateOrderFromLabProcessingWithReport("7898", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithReportOrderException() throws JdxServiceException {
		when(testRunService.findTestRunByLimsReportId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> orderServices
				.updateOrderFromLabProcessingWithReport("7898", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithSample() {
		DataBuilderOrder.OrderlineItemsLabProcessingWithSample
				.add(DataBuilderOrder.mockOrderLineItemLabProcessingWithSample());
		DataBuilderOrder.statuses.add(DataBuilderOrder.mockLaboratoryStatus());
		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRunStatus()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderLabProcessingWithSample()));
		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrderLabProcessingWithSample());
		assertEquals(orderServices
				.updateOrderFromLabProcessingWithSample("123455", DataBuilder.getMockUserDetailsImpl()).getClass(),
				DataBuilderOrder.mockOrder().getClass());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithSampleParentOrderIdException() throws JdxServiceException {
		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRunStatus()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrderParentid()));

		JdxServiceException e = assertThrows(JdxServiceException.class, () -> orderServices
				.updateOrderFromLabProcessingWithSample("123455", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithSamplelineItemsException() throws JdxServiceException {
		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRunStatus()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderLineItemEmpty()));
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> orderServices
				.updateOrderFromLabProcessingWithSample("123455", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithSampleReportIdException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromLabProcessingWithSample(null, DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithSampleKitException() throws JdxServiceException {
		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> orderServices
				.updateOrderFromLabProcessingWithSample("123455", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithSampleTestException() throws JdxServiceException {
		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> orderServices
				.updateOrderFromLabProcessingWithSample("123455", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithSamplelabException() throws JdxServiceException {
		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> orderServices
				.updateOrderFromLabProcessingWithSample("123455", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromLabProcessingWithSampleOrderException() throws JdxServiceException {
		when(kitService.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> orderServices
				.updateOrderFromLabProcessingWithSample("123455", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromShipping() {
		List<OrderLineItem> orderLines = new ArrayList<>();
		orderLines.add(DataBuilderOrder.mockOrderLineItems());
		DataBuilderorderExample.OrderlineItems.add(DataBuilderorderExample.mockOrderLineItemshipping());
		DataBuilderOrder.OrderlineItemsShipping.add(DataBuilderorderExample.mockOrderLineItemshippingsss());
		DataBuilderorderExample.fulfillmentShippingsss.add(DataBuilderOrder.mockFulfillmentd());
		DataBuilderorderExample.fulfillmentShippings.add(DataBuilderOrder.mockFulfillment());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderShipping()));
		when(kitService.getKit(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		orderServices.sendKitPreparedForShipping(DataBuilderorderExample.mockOrder(),
				DataBuilderorderExample.mockFulfillments());
		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderorderExample.mockOrder());

		assertEquals(
				orderServices
						.updateOrderFromShipping(DataBuilderorderExample.mockOrder(),
								"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilder.getMockUserDetailsImpl())
						.getClass(),
				DataBuilderorderExample.mockOrder().getClass());
	}

	@Test
	public void testupdateOrderFromShippingEmptyFulfillemnt() throws JdxServiceException {
		DataBuilderorderExample.orderStatusHistorys.add(DataBuilderOrder.mockOrderStatuskitAssaigned());
		DataBuilderOrder.OrderlineItemsIds.add(DataBuilderOrder.mockOrderLineItemFulfillment1());
		DataBuilderOrder.fulfillments1.add(DataBuilderOrder.mockFulfillment());
		List<OrderLineItem> orderLines = new ArrayList<>();
		orderLines.add(DataBuilderOrder.mockOrderLineItems());
		DataBuilderorderExample.OrderlineItemEmpty.add(DataBuilderorderExample.mockOrderLineItemshippingEmpty());
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderorderExample.mockOrderEmptyFulfillments()));

		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		orderServices.sendKitPreparedForShipping(DataBuilderorderExample.mockOrderEmptyFulfillments(),
				DataBuilderorderExample.mockFulfillments());

		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromShipping(DataBuilderOrder.mockOrderlineItemsIds(),
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	public void testupdateOrderFromShippingException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromShipping(null, "845dba6b-4c0f-4cc9-ba77-62e7216bd692",
						DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromShippingOrderException() throws JdxServiceException {
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromShipping(DataBuilderOrder.mockOrder(),
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromShippingOrderlineEmptyException() throws JdxServiceException {
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromShipping(DataBuilderOrder.mockOrder(),
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromShippingOrderLinesEmptyException() throws JdxServiceException {
		DataBuilderOrder.OrderlineItemsId.add(DataBuilderOrder.mockOrderLineItemId());
		DataBuilderOrder.Orderlins.add(DataBuilderOrder.mockOrderLineItemIds());
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrderlines()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromShipping(DataBuilderOrder.mockOrderlineItemsId(),
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testupdateOrderFromShippingOrderfulfillmentSizeException() throws JdxServiceException {
		DataBuilderOrder.OrderlineItemsIds.add(DataBuilderOrder.mockOrderLineItemFulfillment1());
		DataBuilderOrder.Orderlins.add(DataBuilderOrder.mockOrderLineItemIds());
		DataBuilderOrder.fulfillments1.add(DataBuilderOrder.mockFulfillment());
		DataBuilderOrder.fulfillments1.add(DataBuilderOrder.mockFulfillment());
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockOrderlines()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.updateOrderFromShipping(DataBuilderOrder.mockOrderlineItemsIds(),
						"845dba6b-4c0f-4cc9-ba77-62e7216bd692", DataBuilder.getMockUserDetailsImpl()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsendKitPreparedForShipping() {
//		List<OrderLineItem> orderLines = new ArrayList<>();
//		orderLines.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());

		DataBuilderOrder.mockOrder().setLineItems(DataBuilderOrder.lineItems);

		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);

		orderServices.sendKitPreparedForShipping(DataBuilderOrder.mockOrder(), DataBuilderOrder.mockFulfillment());
	}

	@Test
	public void testactivateKit() {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		user.setAuthorities(authority);
		user.setDateOfBirth("1997-12-15");
//		List<OrderLineItem> orderLines = new ArrayList<>();
//		orderLines.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());

		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
		DataBuilderOrder.mockOrder().setLineItems(DataBuilderOrder.lineItems);

		DataBuilderOrder.mockOrderLineItem().setOrder(DataBuilderOrder.mockOrder());
		DataBuilderOrder.mockOrderLineItem().setOrder(DataBuilderOrder.mockOrder());
		DataBuilderOrder.fulfillments.add(DataBuilderOrder.mockFulfillment());
		DataBuilderOrder.mockFulfillment().setShippingDetails(DataBuilderOrder.mockShippingDetail());
		DataBuilderOrder.mockFulfillment().setKit(DataBuilderOrder.mockKits());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRun());
		DataBuilderOrder.mockLaboratoryOrder().setTestRuns(DataBuilderOrder.testRuns);

		List<GrantedAuthority> authorities = user.getAuthorities().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) new UserDetailsImpl(1L, "General_test_user",
				"no-email@junodx.com", "12345", "Password&123", "123456", authorities);
		doReturn(user).when(userService).getSystemUser();

		when(orderRepository.findOrderByCustomer_LastNameOrderByOrderedAtDesc(Mockito.anyString())).thenReturn(orders);

		DataBuilderOrder.mockOrderStatus().setStatusType(OrderStatusType.KIT_ACTIVATED);

		when(orderRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockOrder());
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);

		orderServices.sendKitActivatedEmail(DataBuilderOrder.mockOrder());
		assertEquals(orderServices.activateKit(DataBuilderOrder.mockActivationPayload()).getClass(),
				DataBuilderOrder.mockActivationResponsePayload().getClass());
	}

	@Test
	public void testactivateKitExceptions() throws JdxServiceException {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		user.setAuthorities(authority);
		user.setDateOfBirth("1997-12-15");
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
		DataBuilderOrder.fulfillments.add(DataBuilderOrder.mockFulfillment());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRun());
		doReturn(user).when(userService).getSystemUser();
		when(orderRepository.findOrderByCustomer_LastNameOrderByOrderedAtDesc(Mockito.anyString())).thenReturn(orders);
		DataBuilderOrder.mockOrderStatus().setStatusType(OrderStatusType.KIT_ACTIVATED);
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		orderServices.sendKitActivatedEmail(DataBuilderOrder.mockOrder());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.activateKit(DataBuilderOrder.mockActivationPayload()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testactivateKitException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> orderServices.activateKit(null));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testactivateKitUserException() throws JdxServiceException {
		// doReturn(user).when(userService).getSystemUser();
		when(userService.getSystemUser()).thenReturn(null);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.activateKit(DataBuilderOrder.mockActivationPayload()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testactivateKitOrderException() throws JdxServiceException {
		doReturn(user).when(userService).getSystemUser();
		when(orderRepository.findOrderByCustomer_LastNameOrderByOrderedAtDesc(Mockito.anyString())).thenReturn(null);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.activateKit(DataBuilderOrder.mockActivationPayload()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testactivateKitOrderFulfillmentException() throws JdxServiceException {
		DataBuilderOrder.OrderlineItemsOrderedAtDesc.add(DataBuilderOrder.mockOrderLineItemsOrderedAtDesc());
		DataBuilderOrder.fulfillmentsOrderedAtDesc.add(DataBuilderOrder.mockFulfillmentOrderedAtDesc());
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrderOrderedAtDesc());
		doReturn(user).when(userService).getSystemUser();
		when(orderRepository.findOrderByCustomer_LastNameOrderByOrderedAtDesc(Mockito.anyString())).thenReturn(orders);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> orderServices.activateKit(DataBuilderOrder.mockActivationPayload()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsendKitActivatedEmail() {

		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());

		DataBuilderOrder.mockOrder().setLineItems(DataBuilderOrder.lineItems);
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);

		orderServices.sendKitActivatedEmail(DataBuilderOrder.mockOrder());
	}

	@Test
	public void testupdateOrderFromSalesforces() {
		DataBuilderOrder.lineItemSalesforces.add(DataBuilderOrder.mockOrderLineItemSalesforces());
		DataBuilderOrder.fulfillmentSalesforces.add(DataBuilderOrder.mockFulfillment());
		DataBuilderOrder.fulfillments.add(DataBuilderOrder.mockFulfillment());
		DataBuilderOrder.lineItems.add(DataBuilderorderExample.mockOrderLineItemss());
		DataBuilder.medications.add(DataBuilder.mockMedication());
		List<SalesforceRecordChanged> salesforceRecordChangeds = new ArrayList<>();
		salesforceRecordChangeds.add(DataBuilder.mockSalesforceRecordChanged());
		List<SalesforceOrderUpdateAccountInfo> salesforceUserUpdateAccountInfos = new ArrayList<>();
		salesforceUserUpdateAccountInfos.add(DataBuilder.mockSalesforceOrderUpdateAccountInfo());
		DataBuilderOrder.mockOrders().setLineItems(DataBuilderOrder.lineItems);
		DataBuilder.medications.add(DataBuilder.mockMedication());

		DataBuilder.vitals.add(DataBuilder.mockVital());

		DataBuilderOrder.orderStatusHistory.add(DataBuilderOrder.mockOrderStatus());
		DataBuilderOrder.mockOrders().setOrderStatusHistory(DataBuilderOrder.orderStatusHistory);
		DataBuilderorderExample.mockOrderLineItemss().setOrder(DataBuilderOrder.mockOrders());
		DataBuilderOrder.fulfillments.add(DataBuilderOrder.mockFulfillment());
		DataBuilder.salesforceLineItemUpdateInfos.add(DataBuilder.mockSalesforceLineItemUpdateInfo());

		DataBuilder.vitals.add(DataBuilder.mockVital());

		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderSalesforces()));
		when(shippingCarrierRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockShippingCarrier()));
		when(orderLineitemRepository.findOrderLineItemByIdAndOrder_id(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(DataBuilderorderExample.mockOrderLineItemss());
		orderServices.updateOrderFromSalesforce(salesforceRecordChangeds, salesforceUserUpdateAccountInfos,
				DataBuilderOrder.userDetailsImpl);

	}

}
