//package com.junodx.api.controllers.commerce;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.isA;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.ext.ExceptionMapper;
//
//import org.apache.http.HttpStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.Spy;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.junodx.api.controllers.users.payloads.RolePayload;
//import com.junodx.api.models.auth.Authority;
//import com.junodx.api.models.commerce.Checkout;
//import com.junodx.api.models.commerce.DataBuilderOrder;
//import com.junodx.api.models.providers.DataBuilder;
//import com.junodx.api.security.ResourceOwnerValidation;
//import com.junodx.api.services.auth.UserDetailsImpl;
//import com.junodx.api.services.commerce.CheckoutService;
//import com.junodx.api.services.exceptions.JdxServiceException;
//
//@AutoConfigureMockMvc
//@ContextConfiguration
//@SpringBootTest(classes = { CheckoutControllerTest.class })
//
//public class CheckoutControllerTest {
//
//	@Autowired
//	private MockMvc mockMvc;
//	@Mock
//	private CheckoutService checkoutService;
//	@Mock
//	private ObjectMapper mapper;
//
//	@Mock
//	private Authentication authentication;
//	@Mock
//	private AuthenticationManager authenticationManager;
//	@Mock
//	private SecurityContext securityContext;
//	@Mock
//	private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationTokens;
//	@Spy
//	@InjectMocks
//	private CheckoutController checkoutController;
//
//	@BeforeEach
//	public void setUp() {
//		mockMvc = MockMvcBuilders.standaloneSetup(checkoutController).build();
//	}
//
//	@Test
//	public void testcreate() throws Exception {
//		SecurityContextHolder.setContext(securityContext);
//		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
//				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
//		when(checkoutService.create(Mockito.any(), Mockito.anyBoolean(), Mockito.any()))
//				.thenReturn(DataBuilderOrder.mockCheckout());
//		ObjectMapper mapper = new ObjectMapper();
//		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockCheckout());
//		System.out.println(inputJson);
//		this.mockMvc.perform(post("/api/orders/checkout").content(inputJson).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk()).andDo(print());
//	}
//
//	@Test
//	public void testcreateException() throws Exception {
//		SecurityContextHolder.setContext(securityContext);
//		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
//				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
//		ObjectMapper mapper = new ObjectMapper();
//		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockCheckoutRequestPayloadExcep());
//		System.out.println(inputJson);
////		when(checkoutService.create(DataBuilderOrder.mockCheckoutRequestPayloadExcep(), true,
////				DataBuilder.getMockUserDetailsImpl())).thenReturn(DataBuilderOrder.mockCheckout());
//		this.mockMvc.perform(post("/api/orders/checkout").content(inputJson).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest()).andDo(print());
//	}
//
//	@Test
//	public void testcreateExceptionss() throws Exception {
//		Authority auth = new Authority();
//		auth.setName("ROLE_PATIENT");
//		List<Authority> authority = new ArrayList<>();
//		authority.add(auth);
//		DataBuilder.mockUser().setAuthorities(authority);
//		RolePayload roles = new RolePayload(auth);
//		roles.setRole(auth);
//		SimpleGrantedAuthority authoritys = new SimpleGrantedAuthority(Authority.ROLE_PATIENT);
//		List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
//		updatedAuthorities.add(authoritys);
//		DataBuilder.authorities.add(authoritys);
//		SecurityContextHolder.setContext(securityContext);
//		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
//				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationTokens);
//		ObjectMapper mapper = new ObjectMapper();
//		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockCheckoutExceptionss());
//		System.out.println(inputJson);
//		this.mockMvc.perform(post("/api/orders/checkout").content(inputJson).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest()).andDo(print());
//	}
//
//	@Test
//	public void testcreateAnonymous() throws Exception {
//		when(checkoutService.create(Mockito.any(), Mockito.anyBoolean())).thenReturn(DataBuilderOrder.mockCheckout());
//		ObjectMapper mapper = new ObjectMapper();
//		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockCheckout());
//		System.out.println(inputJson);
//		this.mockMvc.perform(
//				post("/api/orders/checkout/anonymous").content(inputJson).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk()).andDo(print());
//	}
//
//	@Test
//	public void testcreateAnonymousException() throws Exception {
//		when(checkoutService.create(Mockito.any(), Mockito.anyBoolean())).thenReturn(null);
//		ObjectMapper mapper = new ObjectMapper();
//		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockCheckout());
//		System.out.println(inputJson);
//
//		this.mockMvc.perform(
//				post("/api/orders/checkout/anonymous").content(inputJson).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest()).andDo(print());
//	}
//
//	@Test
//	public void testgetOne() throws Exception {
//		SecurityContextHolder.setContext(securityContext);
//		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
//				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
//		when(checkoutService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockCheckout()));
//		String checkoutId = "1L";
//		assertEquals(checkoutId, DataBuilderOrder.mockCheckout().getId());
//		this.mockMvc
//				.perform(get("/api/orders/checkout/{checkoutId}", checkoutId).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk()).andDo(print());
//	}
//
//	@Test
//	public void testgetOneException() throws Exception {
//		SecurityContextHolder.setContext(securityContext);
//		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
//				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
//		String checkoutId = "1L";
//		assertEquals(checkoutId, DataBuilderOrder.mockCheckout().getId());
//		this.mockMvc
//				.perform(get("/api/orders/checkout/{checkoutId}", checkoutId).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isNotFound()).andDo(print());
//
//	}
//
//	@Test
//	public void testgetOneAuthException() throws Exception {
//		Authority auth = new Authority();
//		auth.setName("ROLE_PATIENT");
//		List<Authority> authority = new ArrayList<>();
//		authority.add(auth);
//		DataBuilder.mockUser().setAuthorities(authority);
//		RolePayload roles = new RolePayload(auth);
//		roles.setRole(auth);
//		SimpleGrantedAuthority authoritys = new SimpleGrantedAuthority(Authority.ROLE_PATIENT);
//		List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
//		updatedAuthorities.add(authoritys);
//		DataBuilder.authorities.add(authoritys);
//		SecurityContextHolder.setContext(securityContext);
//		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
//				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationTokens);
//		when(checkoutService.get(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockCheckout()));
//		String checkoutId = "1L";
//		assertEquals(checkoutId, DataBuilderOrder.mockCheckout().getId());
//		this.mockMvc
//				.perform(get("/api/orders/checkout/{checkoutId}", checkoutId).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest()).andDo(print());
//
//	}
//
//	@Test
//	public void testfind() throws Exception {
//		SecurityContextHolder.setContext(securityContext);
//		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
//				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
//		String token = "pi_3KrtS7GkZuPExSA90C1FoWC3_secret_f5gBjmJLJINHXD6fBguACYZjV";
//		when(checkoutService.find(Optional.of(token))).thenReturn(Optional.of(DataBuilderOrder.mockCheckout()));
//		this.mockMvc
//				.perform(get("/api/orders/checkout?token=pi_3KrtS7GkZuPExSA90C1FoWC3_secret_f5gBjmJLJINHXD6fBguACYZjV")
//						.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk()).andDo(print());
//	}
//
//	@Test
//	public void testfindException() throws Exception {
//		SecurityContextHolder.setContext(securityContext);
//		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
//				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
//		String token = "pi_3KrtS7GkZuPExSA90C1FoWC3_secret_f5gBjmJLJINHXD6fBguACYZjV";
//		when(checkoutService.find(Optional.of(token))).thenReturn(Optional.empty());
//		this.mockMvc
//				.perform(get("/api/orders/checkout?token=pi_3KrtS7GkZuPExSA90C1FoWC3_secret_f5gBjmJLJINHXD6fBguACYZjV")
//						.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isNotFound()).andDo(print());
//	}
//
//	@Test
//	public void testupdate() throws Exception {
//		SecurityContextHolder.setContext(securityContext);
//		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
//				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
//		when(checkoutService.update(Mockito.any(), Mockito.any())).thenReturn(DataBuilderOrder.mockCheckout());
//		ObjectMapper mapper = new ObjectMapper();
//		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockCheckout());
//		System.out.println(inputJson);
//		this.mockMvc.perform(patch("/api/orders/checkout").content(inputJson).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk()).andDo(print());
//
//	}
//
//	
//	@Test
//	public void testupdateException() throws Exception {
//		SecurityContextHolder.setContext(securityContext);
//		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
//				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
//		when(checkoutService.update(Mockito.any(), Mockito.any())).thenReturn(DataBuilderOrder.mockCheckout());
//		ObjectMapper mapper = new ObjectMapper();
//		String inputJson = mapper.writeValueAsString(null);
//		System.out.println(inputJson);
//		ResponseEntity<?> res = checkoutController.update(null);
//		assertEquals(HttpStatus.SC_BAD_REQUEST, res.getStatusCode().value());
//		this.mockMvc.perform(patch("/api/orders/checkout").content(inputJson).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest()).andDo(print());
//
//	}
//
//	@Test
//	public void testdelete() throws Exception {
//		String checkoutId = "1L";
//		SecurityContextHolder.setContext(securityContext);
//		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
//				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
//		verify(checkoutService, times(0)).delete(Mockito.anyString());
//		this.mockMvc
//				.perform(
//						delete("/api/orders/checkout/{checkoutId}", checkoutId).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk()).andDo(print());
//	}
//
//	@Test
//	public void testdeleteException() throws Exception {
//		String checkoutId = "1L";
//		SecurityContextHolder.setContext(securityContext);
//		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
//				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
//		doThrow(new NoSuchElementException()).when(checkoutService).delete(Mockito.anyString());
//		this.mockMvc
//				.perform(
//						delete("/api/orders/checkout/{checkoutId}", checkoutId).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest()).andDo(print());
//
//	}
//
//}
