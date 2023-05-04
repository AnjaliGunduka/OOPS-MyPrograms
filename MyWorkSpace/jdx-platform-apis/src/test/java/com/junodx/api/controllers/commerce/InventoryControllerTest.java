package com.junodx.api.controllers.commerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.lab.TestRunController;
import com.junodx.api.controllers.lab.TestRunControllerTest;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.services.inventory.InventoryService;

@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = { InventoryControllerTest.class })
public class InventoryControllerTest {
	@Mock
	private InventoryService inventoryService;
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
	private InventoryController inventoryController;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
		HttpServletRequest mockRequest = new MockHttpServletRequest();
		ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(servletRequestAttributes);
	}

	@Test 
	public void testgetInventoryByProductId() throws Exception {
		String productId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		String id = "786518f5-4fa9-4576-a173-6318556ccb0b";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(inventoryService.get(productId)).thenReturn(Optional.of(DataBuilderOrder.mockInventoryItem()));
		assertEquals(productId, DataBuilderOrder.mockProduct().getId());
		assertEquals(id, DataBuilderOrder.mockProduct().getId());
		this.mockMvc.perform(get("/api/inventory/{productId}", productId).param(id, productId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testgetInventoryByProductIdCatchException() throws Exception {
		String productId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		String id = "786518f5-4fa9-4576-a173-6318556ccb0b";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		doThrow(new NoSuchElementException()).when(inventoryService).get(Mockito.anyString());
		assertEquals(productId, DataBuilderOrder.mockProduct().getId());
		assertEquals(id, DataBuilderOrder.mockProduct().getId());
		this.mockMvc.perform(get("/api/inventory/{productId}", productId).param(id, productId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testgetInventoryByProductIdException() throws Exception {
		String productId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		String id = "786518f5-4fa9-4576-a173-6318556ccb0b";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		assertEquals(productId, DataBuilderOrder.mockProduct().getId());
		assertEquals(id, DataBuilderOrder.mockProduct().getId());
		this.mockMvc.perform(get("/api/inventory/{productId}", productId).param(id, productId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	public void testupsertInventoryItem() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		// inventoryService.upsert(Mockito.any(),Mockito.any()))
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockInventoryUpdatePayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/inventory").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}
	
	@Test
	public void testupsertInventoryItemException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		// inventoryService.upsert(Mockito.any(),Mockito.any()))
		doThrow(new NoSuchElementException()).when(inventoryService).upsert(Mockito.any(), Mockito.any());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockInventoryUpdatePayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/inventory").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}
	
	
	@Test
	public void testdeleteInventoryItem() throws Exception {
		String inventoryId = "3L";
		String id = "3L";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		verify(inventoryService, times(0)).delete(inventoryId);
		assertEquals(inventoryId, DataBuilderOrder.mockInventoryItem().getId());
		assertEquals(id, DataBuilderOrder.mockInventoryItem().getId());
		this.mockMvc.perform(delete("/api/inventory/{inventoryId}", inventoryId).param(id, inventoryId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());

	}
	
	@Test
	public void testdeleteInventoryItemException() throws Exception {
		String inventoryId = "3L";
		String id = "3L";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		doThrow(new NoSuchElementException()).when(inventoryService).delete(inventoryId);
		assertEquals(inventoryId, DataBuilderOrder.mockInventoryItem().getId());
		assertEquals(id, DataBuilderOrder.mockInventoryItem().getId());
		this.mockMvc.perform(delete("/api/inventory/{inventoryId}", inventoryId).param(id, inventoryId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());

	}
}
