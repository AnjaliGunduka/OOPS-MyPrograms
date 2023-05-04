package com.junodx.api.controllers.fulfillment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.lab.TestReportControllerTest;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.fulfillment.FulfillmentProvider;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.services.fulfillment.FulfillmentProviderService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = { FulfillmentProviderControllerTest.class })
 
public class FulfillmentProviderControllerTest {

	@Mock 
	private FulfillmentProviderService fulfillmentProviderService;
	@Mock
	private ObjectMapper mapper;

	@Mock
	private Authentication authentication;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private SecurityContext securityContext;
	@Mock 
	private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationTokens;
	@InjectMocks
	private FulfillmentProviderController fulfillmentProviderController;
	private static UserDetails usedetails;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(fulfillmentProviderController).build();
		HttpServletRequest mockRequest = new MockHttpServletRequest();
		ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(servletRequestAttributes);
	}

	@Test 
	public void testgetProvider() throws Exception {
		String[] includes = { "anjali" };
		String id = "1L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(fulfillmentProviderService.getProvider(id, includes))
				.thenReturn(Optional.of(DataBuilderOrder.mockFulfillmentProvider()));
		assertEquals(id, DataBuilderOrder.mockFulfillmentProvider().getId());
		this.mockMvc.perform(get("/api/fulfillment/providers/{id}" + "?id=1L&include=anjali", id)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testgetProviderException() throws Exception {
		String[] includes = { "anjali" };
		String id = "1L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(fulfillmentProviderService.getProvider(id, includes)).thenReturn(Optional.empty());
		assertEquals(id, DataBuilderOrder.mockFulfillmentProvider().getId());
		this.mockMvc.perform(get("/api/fulfillment/providers/{id}" + "?id=1L&include=anjali", id)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	public void testgetAllProviders() throws Exception {
		String[] includes = { "anjali" };
		List<FulfillmentProvider> providers = new ArrayList<>();
		providers.add(DataBuilderOrder.mockFulfillmentProvider());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(fulfillmentProviderService.getAllProviders(includes)).thenReturn(providers);
		this.mockMvc
				.perform(get("/api/fulfillment/providers" + "?include=anjali").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testgetAllProvidersException() throws Exception {
		String[] includes = { "anjali" };
		List<FulfillmentProvider> providers = new ArrayList<>();
		providers.add(DataBuilderOrder.mockFulfillmentProvider());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(fulfillmentProviderService.getAllProviders(includes)).thenReturn(null);
		this.mockMvc
				.perform(get("/api/fulfillment/providers" + "?include=anjali").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	public void testgetAllProvidersString() throws Exception {
		List<FulfillmentProvider> providers = new ArrayList<>();
		providers.add(DataBuilderOrder.mockFulfillmentProvider());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		this.mockMvc.perform(get("/api/fulfillment/providers").contentType(MediaType.APPLICATION_JSON)).andDo(print());

	}

	@Test 
	public void testcreateNewProvider() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockFulfillmentProvider());
		System.out.println(inputJson);
		this.mockMvc
				.perform(post("/api/fulfillment/providers").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}
 
	@Test
	public void testcreateNewProviderException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockFulfillmentProvider());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(fulfillmentProviderService).saveProvider(Mockito.any(),Mockito.any());
		this.mockMvc
				.perform(post("/api/fulfillment/providers").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testdeleteProvider() throws Exception {

		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		verify(fulfillmentProviderService, times(0)).deleteProviderById("1L", DataBuilderOrder.userDetailsImpl);
		String id = "1L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		assertEquals(id, DataBuilderOrder.mockFulfillmentProvider().getId());
		this.mockMvc.perform(
				delete("/api/fulfillment/providers/{id}" + "?id=1L", id).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	} 

}
