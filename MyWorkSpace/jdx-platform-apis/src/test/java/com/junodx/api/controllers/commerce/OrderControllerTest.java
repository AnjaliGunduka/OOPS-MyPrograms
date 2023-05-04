package com.junodx.api.controllers.commerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amazonaws.util.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.ControllerUtils;
import com.junodx.api.controllers.SortType;
import com.junodx.api.controllers.commerce.types.OrderSortType;
import com.junodx.api.controllers.lab.TestReportControllerTest;
import com.junodx.api.controllers.payloads.MessageResponse;
import com.junodx.api.controllers.users.payloads.RolePayload;
import com.junodx.api.dto.mappers.CommerceMapStructMapper;
import com.junodx.api.dto.models.commerce.OrderBatchDto;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.auth.Authority;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.commerce.types.OrderStatusType;
import com.junodx.api.models.core.types.IntervalType;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.commerce.OrderService;
import com.junodx.api.services.exceptions.JdxServiceException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.UsageRecordCreateOnSubscriptionItemParams.Timestamp;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = { OrderControllerTest.class })
public class OrderControllerTest {

	@InjectMocks
	private OrdersController ordersController;

	@Mock
	private OrderService orderService;
	@Mock
	private ObjectMapper mapper;

	@Mock
	private CommerceMapStructMapper commerceMapStructMapper;
	@Mock
	private Authentication authentication;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private SecurityContext securityContext;
	@Mock
	private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationTokens;

	private static UserDetails usedetails;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(ordersController).build();
		HttpServletRequest mockRequest = new MockHttpServletRequest();
		ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(servletRequestAttributes);
	}

	@Test
	public void testcreateNewOrder() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrder());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/orders").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testcreateNewNull() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrder());
		System.out.println(inputJson);
		doThrow(JdxServiceException.class).when(orderService).saveOne(Mockito.any(), Mockito.any());
		this.mockMvc.perform(post("/api/orders").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testcreateNewOrderException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrder());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(orderService).saveOne(Mockito.any(), Mockito.any());
		this.mockMvc.perform(post("/api/orders").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testgetOrder() throws Exception {
		String[] includes = { "anjali" };
		String id = "aa86f0ec-9ea1-487c-a814-b1c6001be7e8";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(orderService.findOneByOrderId(id, includes, DataBuilder.getMockUserDetailsImpl()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		assertEquals(id, DataBuilderOrder.mockOrder().getId());
		this.mockMvc.perform(get("/api/orders/{id}" + "?id=aa86f0ec-9ea1-487c-a814-b1c6001be7e8&include=anjali", id)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetOrderAnotherExample() throws Exception {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUser().setAuthorities(authority);
		RolePayload roles = new RolePayload(auth);
		roles.setRole(auth);
		SimpleGrantedAuthority authoritys = new SimpleGrantedAuthority(Authority.ROLE_PATIENT);
		List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		updatedAuthorities.add(authoritys);
		DataBuilder.authorities.add(authoritys);
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
		String customerId = "1L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(orderService.findOrdersByCustomer(customerId)).thenReturn(orders);
		assertEquals(customerId, DataBuilderOrder.mockOrder().getCustomer().getId());
		this.mockMvc.perform(get("/api/orders/customer/{customerId}" + "?customerId=1L", customerId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetOrderAnotherExamples() throws Exception {
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
		String customerId = "1L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(orderService.findOrdersByCustomer(customerId)).thenReturn(null);
		assertEquals(customerId, DataBuilderOrder.mockOrder().getCustomer().getId());
		this.mockMvc.perform(get("/api/orders/customer/{customerId}" + "?customerId=1L", customerId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}
 
	@Test
	public void testgetOrderAnotherException() throws Exception {
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
		String customerId = "1L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		assertEquals(customerId, DataBuilderOrder.mockOrder().getCustomer().getId());
		doThrow(new NoSuchElementException()).when(orderService).findOrdersByCustomer(Mockito.anyString());
		this.mockMvc.perform(get("/api/orders/customer/{customerId}" + "?customerId=1L", customerId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testgetOrderException() throws Exception {
		String[] includes = { "anjali" };
		String id = "aa86f0ec-9ea1-487c-a814-b1c6001be7e8";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(orderService.findOneByOrderId(id, includes, DataBuilder.getMockUserDetailsImpl()))
				.thenReturn(Optional.empty());
		assertEquals(id, DataBuilderOrder.mockOrder().getId());
		this.mockMvc.perform(get("/api/orders/{id}" + "?id=aa86f0ec-9ea1-487c-a814-b1c6001be7e8&include=anjali", id)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testgetOrderExceptions() throws Exception {
		String[] includes = { "anjali" };
		String id = "aa86f0ec-9ea1-487c-a814-b1c6001be7e8";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(orderService.findOneByOrderId(id, includes, DataBuilder.getMockUserDetailsImpl()))
				.thenReturn(Optional.empty());
		assertEquals(id, DataBuilderOrder.mockOrder().getId());
		this.mockMvc.perform(get("/api/orders/{id}" + "?id=aa86f0ec-9ea1-487c-a814-b1c6001be7e8&include=anjali", id)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testgetOrders() throws Exception {
		Pageable paging = PageRequest.of(1, 5);
		String[] includes = { "anjali" };
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
		final Page<Order> pages = new PageImpl<>(orders, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.findAll(includes, DataBuilder.getMockUserDetailsImpl(), paging)).thenReturn(pages);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);
		this.mockMvc
				.perform(get("/api/orders" + "?page=1&size=5&include=anjali").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetOrderCatchException() throws Exception {
		Pageable paging = PageRequest.of(1, 5);
		String[] includes = { "anjali" };
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		doThrow(new NoSuchElementException()).when(orderService).findAll(includes, DataBuilder.getMockUserDetailsImpl(),
				paging);
		this.mockMvc
				.perform(get("/api/orders" + "?page=1&size=5&include=anjali").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testgetOrdersIncludes() throws Exception {
		Pageable paging = PageRequest.of(1, 5);
		String[] includes = { "anjali" };
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
		final Page<Order> pages = new PageImpl<>(orders, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.findAll(includes, DataBuilder.getMockUserDetailsImpl(), paging)).thenReturn(pages);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);
		this.mockMvc.perform(get("/api/orders" + "?page=1&size=5").contentType(MediaType.APPLICATION_JSON))
				.andDo(print());
	}

	@Test
	public void testgetOrdersMaxSize() throws Exception {
		Pageable paging = PageRequest.of(1, 5);
		String[] includes = { "anjali" };
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
		final Page<Order> pages = new PageImpl<>(orders, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.findAll(includes, DataBuilder.getMockUserDetailsImpl(), paging)).thenReturn(pages);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);
		this.mockMvc.perform(get("/api/orders" + "?page=1&size=21").contentType(MediaType.APPLICATION_JSON))
				.andDo(print());
	}

	@Test
	public void testgetOrdersException() throws Exception {
		Pageable paging = PageRequest.of(1, 5);
		String[] includes = { "anjali" };
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
		final Page<Order> pages = new PageImpl<>(orders, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.findAll(includes, DataBuilder.getMockUserDetailsImpl(), paging)).thenReturn(null);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);
		this.mockMvc
				.perform(get("/api/orders" + "?page=1&size=5&include=anjali").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testfindCondensed() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
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
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.search(Optional.of(requiresShipment), Optional.of(requiresRedraw),
				Optional.of(resultsAvailable), Optional.of(isOpen), Optional.of(status), Optional.of(withInsurance),
				Optional.of(patientId), Optional.of(firstName), Optional.of(lastName), Optional.of(email),
				Optional.of(xifinId), Optional.of(stripeId), Optional.of(customerActivated),
				Optional.of(containsProductId), Optional.of(true), Optional.of(orderNumber), Optional.of(orderId),
				Optional.of(kitCode), Optional.of(sampleNumber), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(false), Optional.empty(), paging)).thenReturn(pages);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);
		this.mockMvc.perform(get("/api/orders/search" + "?page=5" + "&size=5" +
		// "&interval=1"
				"&units=SECONDS" + "&byordernumber=1234" + "&byorderid=aa86f0ec-9ea1-487c-a814-b1c6001be7e8"
				+ "&bykitcode=JO135wwa33jj789" + "&bysamplenumber=123455" + "&byrequireshipment=false"
				+ "&byrequiresredraw=false" + "&byresultsavailable=false" + "&byopen=false" + "&bystatus=CREATED"
				+ "&bywithinsurance=true" + "&bypatient=1L" + "&byfirstname=Juno_test" + "&bylastname=User"
				+ "&byemail=no-email@junodx.com" + "&byxifinid=BSX-FST-0001" + "&bystripeid=BSX-FST-0001"
				+ "&bycustomeractivated=true" + "&bycontainsproduct=786518f5-4fa9-4576-a173-6318556ccb0b"
				+ "&bycontainstests=true" + "&sortby=ORDERED_AT" + "&sorttype=ASC" + "&condensed=false")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testfinds() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
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
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.search(Optional.of(requiresShipment), Optional.of(requiresRedraw),
				Optional.of(resultsAvailable), Optional.of(isOpen), Optional.of(status), Optional.of(withInsurance),
				Optional.of(patientId), Optional.of(firstName), Optional.of(lastName), Optional.of(email),
				Optional.of(xifinId), Optional.of(stripeId), Optional.of(customerActivated),
				Optional.of(containsProductId), Optional.of(true), Optional.of(orderNumber), Optional.of(orderId),
				Optional.of(kitCode), Optional.of(sampleNumber), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(false), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);

		this.mockMvc.perform(get("/api/orders/search" + "?page=5" + "&size=5" + "&interval=1" + "&units=SECONDS"
				+ "&byordernumber=1234" + "&byorderid=aa86f0ec-9ea1-487c-a814-b1c6001be7e8"
				+ "&bykitcode=JO135wwa33jj789" + "&bysamplenumber=123455" + "&byrequireshipment=false"
				+ "&byrequiresredraw=false" + "&byresultsavailable=false" + "&byopen=false" + "&bystatus=CREATED"
				+ "&bywithinsurance=true" + "&bypatient=1L" + "&byfirstname=Juno_test" + "&bylastname=User"
				+ "&byemail=no-email@junodx.com" + "&byxifinid=BSX-FST-0001" + "&bystripeid=BSX-FST-0001&"
				+ "bycustomeractivated=true" + "&bycontainsproduct=786518f5-4fa9-4576-a173-6318556ccb0b"
				+ "&bycontainstests=true" + "&sortby=ORDERED_AT" + "&sorttype=ASC" + "&condensed=false&")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print());
	}

	@Test
	public void testfindPagesException() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
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
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.search(Optional.of(requiresShipment), Optional.of(requiresRedraw),
				Optional.of(resultsAvailable), Optional.of(isOpen), Optional.of(status), Optional.of(withInsurance),
				Optional.of(patientId), Optional.of(firstName), Optional.of(lastName), Optional.of(email),
				Optional.of(xifinId), Optional.of(stripeId), Optional.of(customerActivated),
				Optional.of(containsProductId), Optional.of(true), Optional.of(orderNumber), Optional.of(orderId),
				Optional.of(kitCode), Optional.of(sampleNumber), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(true), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);
		this.mockMvc.perform(
				get("/api/orders/search" + "?page=5&size=5&condensed=true").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testfindPagesMaxPagesException() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
		final Page<Order> pages = new PageImpl<>(orders, paging, 5);
		SecurityContextHolder.setContext(securityContext);
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
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.search(Optional.of(requiresShipment), Optional.of(requiresRedraw),
				Optional.of(resultsAvailable), Optional.of(isOpen), Optional.of(status), Optional.of(withInsurance),
				Optional.of(patientId), Optional.of(firstName), Optional.of(lastName), Optional.of(email),
				Optional.of(xifinId), Optional.of(stripeId), Optional.of(customerActivated),
				Optional.of(containsProductId), Optional.of(true), Optional.of(orderNumber), Optional.of(orderId),
				Optional.of(kitCode), Optional.of(sampleNumber), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(true), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);
		this.mockMvc.perform(
				get("/api/orders/search" + "?page=5&size=21&condensed=true").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testfindDays() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
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
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.search(Optional.of(requiresShipment), Optional.of(requiresRedraw),
				Optional.of(resultsAvailable), Optional.of(isOpen), Optional.of(status), Optional.of(withInsurance),
				Optional.of(patientId), Optional.of(firstName), Optional.of(lastName), Optional.of(email),
				Optional.of(xifinId), Optional.of(stripeId), Optional.of(customerActivated),
				Optional.of(containsProductId), Optional.of(true), Optional.of(orderNumber), Optional.of(orderId),
				Optional.of(kitCode), Optional.of(sampleNumber), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(true), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);

		this.mockMvc.perform(get("/api/orders/search" + "?page=5" + "&size=5" + "&interval=1" + "&units=DAYS"
				+ "&byordernumber=1234" + "&byorderid=aa86f0ec-9ea1-487c-a814-b1c6001be7e8"
				+ "&bykitcode=JO135wwa33jj789" + "&bysamplenumber=123455" + "&byrequireshipment=false"
				+ "&byrequiresredraw=false" + "&byresultsavailable=false" + "&byopen=false" + "&bystatus=CREATED"
				+ "&bywithinsurance=true" + "&bypatient=1L" + "&byfirstname=Juno_test" + "&bylastname=User"
				+ "&byemail=no-email@junodx.com" + "&byxifinid=BSX-FST-0001" + "&bystripeid=BSX-FST-0001&"
				+ "bycustomeractivated=true" + "&bycontainsproduct=786518f5-4fa9-4576-a173-6318556ccb0b"
				+ "&bycontainstests=true" + "&sortby=ORDERED_AT" + "&sorttype=ASC" + "&condensed=true&")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testfindWeeks() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
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
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.search(Optional.of(requiresShipment), Optional.of(requiresRedraw),
				Optional.of(resultsAvailable), Optional.of(isOpen), Optional.of(status), Optional.of(withInsurance),
				Optional.of(patientId), Optional.of(firstName), Optional.of(lastName), Optional.of(email),
				Optional.of(xifinId), Optional.of(stripeId), Optional.of(customerActivated),
				Optional.of(containsProductId), Optional.of(true), Optional.of(orderNumber), Optional.of(orderId),
				Optional.of(kitCode), Optional.of(sampleNumber), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(true), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);
		this.mockMvc.perform(get("/api/orders/search" + "?page=5" + "&size=5" + "&interval=1" + "&units=WEEKS"
				+ "&byordernumber=1234" + "&byorderid=aa86f0ec-9ea1-487c-a814-b1c6001be7e8"
				+ "&bykitcode=JO135wwa33jj789" + "&bysamplenumber=123455" + "&byrequireshipment=false"
				+ "&byrequiresredraw=false" + "&byresultsavailable=false" + "&byopen=false" + "&bystatus=CREATED"
				+ "&bywithinsurance=true" + "&bypatient=1L" + "&byfirstname=Juno_test" + "&bylastname=User"
				+ "&byemail=no-email@junodx.com" + "&byxifinid=BSX-FST-0001" + "&bystripeid=BSX-FST-0001&"
				+ "bycustomeractivated=true" + "&bycontainsproduct=786518f5-4fa9-4576-a173-6318556ccb0b"
				+ "&bycontainstests=true" + "&sortby=ORDERED_AT" + "&sorttype=ASC" + "&condensed=true&")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print());

	}

	@Test
	public void testfindHours() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
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
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.search(Optional.of(requiresShipment), Optional.of(requiresRedraw),
				Optional.of(resultsAvailable), Optional.of(isOpen), Optional.of(status), Optional.of(withInsurance),
				Optional.of(patientId), Optional.of(firstName), Optional.of(lastName), Optional.of(email),
				Optional.of(xifinId), Optional.of(stripeId), Optional.of(customerActivated),
				Optional.of(containsProductId), Optional.of(true), Optional.of(orderNumber), Optional.of(orderId),
				Optional.of(kitCode), Optional.of(sampleNumber), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(true), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);

		this.mockMvc.perform(get("/api/orders/search" + "?page=5" + "&size=5" + "&interval=1" + "&units=HOURS"
				+ "&byordernumber=1234" + "&byorderid=aa86f0ec-9ea1-487c-a814-b1c6001be7e8"
				+ "&bykitcode=JO135wwa33jj789" + "&bysamplenumber=123455" + "&byrequireshipment=false"
				+ "&byrequiresredraw=false" + "&byresultsavailable=false" + "&byopen=false" + "&bystatus=CREATED"
				+ "&bywithinsurance=true" + "&bypatient=1L" + "&byfirstname=Juno_test" + "&bylastname=User"
				+ "&byemail=no-email@junodx.com" + "&byxifinid=BSX-FST-0001" + "&bystripeid=BSX-FST-0001&"
				+ "bycustomeractivated=true" + "&bycontainsproduct=786518f5-4fa9-4576-a173-6318556ccb0b"
				+ "&bycontainstests=true" + "&sortby=ORDERED_AT" + "&sorttype=ASC" + "&condensed=true&")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print());
	}

	@Test
	public void testfindMinutes() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
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
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.search(Optional.of(requiresShipment), Optional.of(requiresRedraw),
				Optional.of(resultsAvailable), Optional.of(isOpen), Optional.of(status), Optional.of(withInsurance),
				Optional.of(patientId), Optional.of(firstName), Optional.of(lastName), Optional.of(email),
				Optional.of(xifinId), Optional.of(stripeId), Optional.of(customerActivated),
				Optional.of(containsProductId), Optional.of(true), Optional.of(orderNumber), Optional.of(orderId),
				Optional.of(kitCode), Optional.of(sampleNumber), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(true), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);
		this.mockMvc.perform(get("/api/orders/search" + "?page=5" + "&size=5" + "&interval=1" + "&units=MINUTES"
				+ "&byordernumber=1234" + "&byorderid=aa86f0ec-9ea1-487c-a814-b1c6001be7e8"
				+ "&bykitcode=JO135wwa33jj789" + "&bysamplenumber=123455" + "&byrequireshipment=false"
				+ "&byrequiresredraw=false" + "&byresultsavailable=false" + "&byopen=false" + "&bystatus=CREATED"
				+ "&bywithinsurance=true" + "&bypatient=1L" + "&byfirstname=Juno_test" + "&bylastname=User"
				+ "&byemail=no-email@junodx.com" + "&byxifinid=BSX-FST-0001" + "&bystripeid=BSX-FST-0001&"
				+ "bycustomeractivated=true" + "&bycontainsproduct=786518f5-4fa9-4576-a173-6318556ccb0b"
				+ "&bycontainstests=true" + "&sortby=ORDERED_AT" + "&sorttype=ASC" + "&condensed=true&")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print());
	}

	@Test
	public void testfindSeconds() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
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
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.search(Optional.of(requiresShipment), Optional.of(requiresRedraw),
				Optional.of(resultsAvailable), Optional.of(isOpen), Optional.of(status), Optional.of(withInsurance),
				Optional.of(patientId), Optional.of(firstName), Optional.of(lastName), Optional.of(email),
				Optional.of(xifinId), Optional.of(stripeId), Optional.of(customerActivated),
				Optional.of(containsProductId), Optional.of(true), Optional.of(orderNumber), Optional.of(orderId),
				Optional.of(kitCode), Optional.of(sampleNumber), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(true), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);

		this.mockMvc.perform(get("/api/orders/search" + "?page=5" + "&size=5" + "&interval=1" + "&units=SECONDS"
				+ "&byordernumber=1234" + "&byorderid=aa86f0ec-9ea1-487c-a814-b1c6001be7e8"
				+ "&bykitcode=JO135wwa33jj789" + "&bysamplenumber=123455" + "&byrequireshipment=false"
				+ "&byrequiresredraw=false" + "&byresultsavailable=false" + "&byopen=false" + "&bystatus=CREATED"
				+ "&bywithinsurance=true" + "&bypatient=1L" + "&byfirstname=Juno_test" + "&bylastname=User"
				+ "&byemail=no-email@junodx.com" + "&byxifinid=BSX-FST-0001" + "&bystripeid=BSX-FST-0001&"
				+ "bycustomeractivated=true" + "&bycontainsproduct=786518f5-4fa9-4576-a173-6318556ccb0b"
				+ "&bycontainstests=true" + "&sortby=ORDERED_AT" + "&sorttype=ASC" + "&condensed=true&")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print());
	}

	@Test
	public void testfindDefault() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
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
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.search(Optional.of(requiresShipment), Optional.of(requiresRedraw),
				Optional.of(resultsAvailable), Optional.of(isOpen), Optional.of(status), Optional.of(withInsurance),
				Optional.of(patientId), Optional.of(firstName), Optional.of(lastName), Optional.of(email),
				Optional.of(xifinId), Optional.of(stripeId), Optional.of(customerActivated),
				Optional.of(containsProductId), Optional.of(true), Optional.of(orderNumber), Optional.of(orderId),
				Optional.of(kitCode), Optional.of(sampleNumber), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(true), Optional.empty(), paging)).thenReturn(pages);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);

		this.mockMvc.perform(get("/api/orders/search" + "?page=5" + "&size=5"
		// + "&interval=1"
				+ "&units=SECONDS" + "&byordernumber=1234" + "&byorderid=aa86f0ec-9ea1-487c-a814-b1c6001be7e8"
				+ "&bykitcode=JO135wwa33jj789" + "&bysamplenumber=123455" + "&byrequireshipment=false"
				+ "&byrequiresredraw=false" + "&byresultsavailable=false" + "&byopen=false" + "&bystatus=CREATED"
				+ "&bywithinsurance=true" + "&bypatient=1L" + "&byfirstname=Juno_test" + "&bylastname=User"
				+ "&byemail=no-email@junodx.com" + "&byxifinid=BSX-FST-0001" + "&bystripeid=BSX-FST-0001&"
				+ "bycustomeractivated=true" + "&bycontainsproduct=786518f5-4fa9-4576-a173-6318556ccb0b"
				+ "&bycontainstests=true" + "&sortby=ORDERED_AT" + "&sorttype=ASC" + "&condensed=true&")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print());
	}

	@Test
	public void testfindExample() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<Order> orders = new ArrayList<>();
		orders.add(DataBuilderOrder.mockOrder());
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
		final Page<Order> pages = new PageImpl<>(orders, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.search(Optional.of(requiresShipment), Optional.of(requiresRedraw),
				Optional.of(resultsAvailable), Optional.of(isOpen), Optional.of(status), Optional.of(withInsurance),
				Optional.of(patientId), Optional.of(firstName), Optional.of(lastName), Optional.of(email),
				Optional.of(xifinId), Optional.of(stripeId), Optional.of(customerActivated),
				Optional.of(containsProductId), Optional.of(true), Optional.of(orderNumber), Optional.of(orderId),
				Optional.of(kitCode), Optional.of(sampleNumber), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(true), Optional.empty(), paging)).thenReturn(pages);
		when(commerceMapStructMapper.orderToOrderBatchDtos(pages.getContent())).thenReturn(transitionOrders);
		this.mockMvc.perform(get("/api/orders/search" + "?page=5" + "&size=5"
		// + "&interval=1"
				+ "&units=SECONDS" + "&byordernumber=1234" + "&byorderid=aa86f0ec-9ea1-487c-a814-b1c6001be7e8"
				+ "&bykitcode=JO135wwa33jj789" + "&bysamplenumber=123455" + "&byrequireshipment=false"
				+ "&byrequiresredraw=false" + "&byresultsavailable=false" + "&byopen=false" + "&bystatus=CREATED"
				+ "&bywithinsurance=true" + "&bypatient=1L" + "&byfirstname=Juno_test" + "&bylastname=User"
				+ "&byemail=no-email@junodx.com" + "&byxifinid=BSX-FST-0001" + "&bystripeid=BSX-FST-0001&"
				+ "bycustomeractivated=true" + "&bycontainsproduct=786518f5-4fa9-4576-a173-6318556ccb0b"
				+ "&bycontainstests=true" + "&sortby=ORDERED_AT" + "&sorttype=ASC" + "&condensed=true&")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testfindWithoutCondensed() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
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
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(ordersController, "maxPageSize", 20);
		when(orderService.search(Optional.of(requiresShipment), Optional.of(requiresRedraw),
				Optional.of(resultsAvailable), Optional.of(isOpen), Optional.of(status), Optional.of(withInsurance),
				Optional.of(patientId), Optional.of(firstName), Optional.of(lastName), Optional.of(email),
				Optional.of(xifinId), Optional.of(stripeId), Optional.of(customerActivated),
				Optional.of(containsProductId), Optional.of(true), Optional.of(orderNumber), Optional.of(orderId),
				Optional.of(kitCode), Optional.of(sampleNumber), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.empty(), Optional.empty(), paging)).thenReturn(pages);

		this.mockMvc.perform(get("/api/orders/search" + "?page=5" + "&size=5"
		// + "&interval=1"
				+ "&units=SECONDS" + "&byordernumber=1234" + "&byorderid=aa86f0ec-9ea1-487c-a814-b1c6001be7e8"
				+ "&bykitcode=JO135wwa33jj789" + "&bysamplenumber=123455" + "&byrequireshipment=false"
				+ "&byrequiresredraw=false" + "&byresultsavailable=false" + "&byopen=false" + "&bystatus=CREATED"
				+ "&bywithinsurance=true" + "&bypatient=1L" + "&byfirstname=Juno_test" + "&bylastname=User"
				+ "&byemail=no-email@junodx.com" + "&byxifinid=BSX-FST-0001" + "&bystripeid=BSX-FST-0001&"
				+ "bycustomeractivated=true" + "&bycontainsproduct=786518f5-4fa9-4576-a173-6318556ccb0b"
				+ "&bycontainstests=true" + "&sortby=ORDERED_AT" + "&sorttype=ASC")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testupdateRequest() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrderUpdateRequest());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/orders").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdateRequestCatchException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrderUpdateRequestException());
		System.out.println(inputJson);
//		doThrow(new NoSuchElementException()).when(ordersController)
//		.update(DataBuilderOrder.mockOrderUpdateRequestException());
//		ResponseEntity<?> res = ordersController.update(DataBuilderOrder.mockOrderUpdateRequestException());
//		assertEquals(HttpStatus.SC_BAD_REQUEST, res.getStatusCode().value());
		this.mockMvc.perform(patch("/api/orders").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdateRequestActionsLab() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrderUpdateRequests());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/orders").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdateRequestActions() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrderUpdateRequestShipping());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/orders").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdateRequestAction() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrderUpdateRequestAction());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/orders").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdateRequestOrders() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrderUpdateRequestOrders());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/orders").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

//	@Test
//	public void testupdate() {
//		SecurityContextHolder.setContext(securityContext);
//		DataBuilderOrder.mockOrderUpdateRequest().setOrder(null);
//		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
//				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
//		when(orderService.updateWithKitDetails(Mockito.anyString(), Mockito.anyString(), Mockito.any(),
//				Mockito.anyString(), Mockito.any())).thenReturn(DataBuilderOrder.mockOrder());
//		ResponseEntity<?> response = ordersController.update(DataBuilderOrder.mockOrderKitAssignPayload());
//		assertEquals(DataBuilderOrder.mockOrder().getClass(), response.getBody().getClass());
//
//	}
	@Test
	public void testupdate() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(orderService.updateWithKitDetails(Mockito.anyString(), Mockito.anyString(), Mockito.any(),
				Mockito.anyString(), Mockito.any())).thenReturn(DataBuilderOrder.mockOrder());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrderKitAssignPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/orders/kit").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testupdatekitException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		DataBuilderOrder.mockOrderKitAssignPayload().setOrderId(null);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrderKitAssignPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/orders/kit").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testmodifyPackageCodesForOrder() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		DataBuilderOrder.mockOrderUpdateRequest().setOrder(null);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(orderService.updateWithKitDetails(Mockito.anyString(), Mockito.anyString(), Mockito.any(),
				Mockito.anyString(), Mockito.any())).thenReturn(DataBuilderOrder.mockOrder());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrderKitAssignPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/orders/kit/modify").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testmodifyPackageCodesForOrderException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		DataBuilderOrder.mockOrderUpdateRequest().setOrder(null);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrderKitAssignPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/orders/kit/modify").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testmodifyPackageCodesForOrderCatchException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		DataBuilderOrder.mockOrderUpdateRequest().setOrder(null);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrderKitAssignPayload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(orderService).updateWithKitDetails(Mockito.anyString(),
				Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.any());
		this.mockMvc.perform(post("/api/orders/kit/modify").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testremovePackageCodesFromOrder() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		DataBuilderOrder.mockOrderUpdateRequest().setOrder(null);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(orderService.updateWithKitDetails(Mockito.anyString(), Mockito.anyString(), Mockito.any(),
				Mockito.anyString(), Mockito.any())).thenReturn(DataBuilderOrder.mockOrder());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrderKitAssignPayload());
		System.out.println(inputJson);
		this.mockMvc
				.perform(post("/api/orders/kit/unassign").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testremovePackageCodesFromOrderException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		DataBuilderOrder.mockOrderUpdateRequest().setOrder(null);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrderKitAssignPayload());
		System.out.println(inputJson);
		this.mockMvc
				.perform(post("/api/orders/kit/unassign").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testremovePackageCodesFromOrderCatchException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		DataBuilderOrder.mockOrderUpdateRequest().setOrder(null);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrderKitAssignPayload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(orderService).updateWithKitDetails(Mockito.anyString(),
				Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.any());
		this.mockMvc
				.perform(post("/api/orders/kit/unassign").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testcreateRedrawRequest() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(orderService.updateWithRedraw("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
				"845dba6b-4c0f-4cc9-ba77-62e7216bd692", "1L", true, false, DataBuilder.getMockUserDetailsImpl()))
				.thenReturn(DataBuilderOrder.mockOrder());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockRedrawRequestPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/orders/redraw").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testcreateRedrawRequestException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		DataBuilderOrder.mockRedrawRequestPayload().setOrderId(null);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockRedrawRequestPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/orders/redraw").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testcreateRedrawRequestCatchException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		DataBuilderOrder.mockRedrawRequestPayload().setOrderId(null);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockRedrawRequestPayload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(orderService).updateWithRedraw(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		this.mockMvc.perform(post("/api/orders/redraw").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testcreateRefundRequest() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(orderService.updateWithRefund("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
				"845dba6b-4c0f-4cc9-ba77-62e7216bd692", true, false, DataBuilder.getMockUserDetailsImpl()))
				.thenReturn(DataBuilderOrder.mockOrder());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockRefundRequestPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/orders/refund").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testcreateRefundRequestException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		DataBuilderOrder.mockRedrawRequestPayload().setOrderId(null);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockRefundRequestPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/orders/refund").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testcreateRefundRequestCatchException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		DataBuilderOrder.mockRedrawRequestPayload().setOrderId(null);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockRefundRequestPayload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(orderService).updateWithRefund(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		this.mockMvc.perform(post("/api/orders/refund").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testdeleteOrder() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		String id = "aa86f0ec-9ea1-487c-a814-b1c6001be7e8";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		assertEquals(id, DataBuilderOrder.mockOrder().getId());
		this.mockMvc.perform(delete("/api/orders/{id}" + "?id=aa86f0ec-9ea1-487c-a814-b1c6001be7e8", id)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testcreateOrderFromCheckout() throws Exception {
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		PaymentIntent payments = new PaymentIntent();
		payments.setCurrency("USD");
		payments.setAmount(2L);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(orderService.saveOneFromCheckout(token, payments, DataBuilder.getMockUserDetailsImpl()))
				.thenReturn(DataBuilderOrder.mockOrder());
		this.mockMvc.perform(post("/api/orders/tmp/checkoutOrder/{token}" + "?token"
				+ "=bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5&",
				token).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testcreateOrderFromCheckoutCatchException() throws Exception {
		String token = "bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		doThrow(new NoSuchElementException()).when(orderService).saveOneFromCheckout(Mockito.anyString(), Mockito.any(),
				Mockito.any());
		this.mockMvc.perform(post("/api/orders/tmp/checkoutOrder/{token}" + "?token"
				+ "=bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5&",
				token).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testactivateKit() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockActivationPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/orders/activate").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testactivateKitException() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockActivationPayload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(orderService).activateKit(Mockito.any());
		this.mockMvc.perform(post("/api/orders/activate").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testreceiveInLab() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockLabReceiptPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/orders/receive").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testreceiveInLabException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockLabReceiptPayload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(orderService).updateOrderFromLabReceipt(Mockito.anyString(),
				Mockito.any());
		this.mockMvc.perform(post("/api/orders/receive").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testgetShipAndRedrawCounts() throws Exception {
		this.mockMvc.perform(get("/api/orders/counts").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetShipAndRedrawCountsCatchException() throws Exception {
		doThrow(new NoSuchElementException()).when(orderService).getCounts(Mockito.any());
		this.mockMvc.perform(get("/api/orders/counts").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testgetShipAndRedrawCountDays() throws Exception {
		this.mockMvc
				.perform(get("/api/orders/counts" + "?interval=1&units=DAYS").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetShipAndRedrawCountSeconds() throws Exception {
		this.mockMvc
				.perform(
						get("/api/orders/counts" + "?interval=1&units=SECONDS").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetShipAndRedrawCountMinutes() throws Exception {
		this.mockMvc
				.perform(
						get("/api/orders/counts" + "?interval=1&units=MINUTES").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetShipAndRedrawCountHours() throws Exception {
		this.mockMvc
				.perform(get("/api/orders/counts" + "?interval=1&units=HOURS").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetShipAndRedrawCountWeeks() throws Exception {
		this.mockMvc
				.perform(get("/api/orders/counts" + "?interval=1&units=WEEKS").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testgetShipAndRedrawCountDefault() throws Exception {
		this.mockMvc.perform(get("/api/orders/counts" + "?interval=1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}
}
