package com.junodx.api.controllers.commerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.commerce.payloads.ProductAvailabilityResponsePayload;
import com.junodx.api.controllers.lab.TestRunController;
import com.junodx.api.dto.mappers.CommerceMapStructMapper;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.commerce.Product;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.services.commerce.ProductService;
import com.junodx.api.services.inventory.InventoryService;

@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = { ProductControllerTest.class })
public class ProductControllerTest {
	@Mock
	private ProductService productService;
	@Mock
	private InventoryService inventoryService;
	@Mock
	private CommerceMapStructMapper commerceMapStructMapper;
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
	private ProductController productController;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
		HttpServletRequest mockRequest = new MockHttpServletRequest();
		ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(servletRequestAttributes);
	}

	@Test
	public void testcreate() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(productService.create(Mockito.any())).thenReturn(DataBuilderOrder.mockProduct());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockProduct());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/products").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testcreateException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
//		when(productService.create(Mockito.any()))
//				.thenReturn(DataBuilderOrder.mockProduct());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockProduct());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/products").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testgetOne() throws Exception {
		String productId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		String id = "786518f5-4fa9-4576-a173-6318556ccb0b";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(productService.get(productId)).thenReturn(Optional.of(DataBuilderOrder.mockProduct()));
		assertEquals(productId, DataBuilderOrder.mockProduct().getId());
		assertEquals(id, DataBuilderOrder.mockProduct().getId());
		this.mockMvc
				.perform(get(
						"/api/products/id/{productId}"
								+ "?productId =786518f5-4fa9-4576-a173-6318556ccb0b&available=true&withzip=503165",
						productId)

						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testgetOneException() throws Exception {
		String productId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		String id = "786518f5-4fa9-4576-a173-6318556ccb0b";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		// when(productService.get(productId)).thenReturn(Optional.of(DataBuilderOrder.mockProduct()));
		assertEquals(productId, DataBuilderOrder.mockProduct().getId());
		assertEquals(id, DataBuilderOrder.mockProduct().getId());
		this.mockMvc
				.perform(get(
						"/api/products/id/{productId}"
								+ "?productId =786518f5-4fa9-4576-a173-6318556ccb0b&available=true&withzip=503165",
						productId)

						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	public void testgetAll() throws Exception {
		List<Product> products = new ArrayList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(productService.getAll()).thenReturn(products);

		this.mockMvc.perform(get("/api/products/all")

				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testgetAllException() throws Exception {
		List<Product> products = new ArrayList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		this.mockMvc.perform(get("/api/products/all").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	public void testgetOneAuthorized() throws Exception {
		String productId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		String id = "786518f5-4fa9-4576-a173-6318556ccb0b";
		List<Product> products = new ArrayList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(productService.get(productId)).thenReturn(Optional.of(DataBuilderOrder.mockProduct()));
		assertEquals(productId, DataBuilderOrder.mockProduct().getId());
		assertEquals(id, DataBuilderOrder.mockProduct().getId());
		this.mockMvc.perform(
				get("/api/products/{productId}" + "?productId =786518f5-4fa9-4576-a173-6318556ccb0b", productId)

						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testgetOneAuthorizedException() throws Exception {
		String productId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		String id = "786518f5-4fa9-4576-a173-6318556ccb0b";
		List<Product> products = new ArrayList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		// when(productService.get(productId)).thenReturn(Optional.of(DataBuilderOrder.mockProduct()));
		assertEquals(productId, DataBuilderOrder.mockProduct().getId());
		assertEquals(id, DataBuilderOrder.mockProduct().getId());
		this.mockMvc
				.perform(
						get("/api/products/{productId}" + "?productId =786518f5-4fa9-4576-a173-6318556ccb0b", productId)

								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	public void testgetAllAuthorized() throws Exception {
		List<Product> products = new ArrayList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(productService.getAll()).thenReturn(products);
		this.mockMvc.perform(get("/api/products").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print());

	}

	@Test
	public void testgetAllAuthorizedException() throws Exception {
		List<Product> products = new ArrayList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		// when(productService.getAll()).thenReturn(products);
		this.mockMvc.perform(get("/api/products").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testupdate() throws Exception {
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(productService.update(Mockito.any())).thenReturn(DataBuilderOrder.mockProduct());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockProduct());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/products").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testupdateException() throws Exception {
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		// when(productService.update(Mockito.any())).thenReturn(DataBuilderOrder.mockProduct());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(null);
		System.out.println(inputJson);
		ResponseEntity<?> response = productController.update(null);
		assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode().value());
		this.mockMvc.perform(patch("/api/products").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testdelete() throws Exception {
		String productId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		verify(productService, times(0)).delete(productId);
		assertEquals(productId, DataBuilderOrder.mockProduct().getId());
		this.mockMvc.perform(
				delete("/api/products/{productId}" + "?productId =786518f5-4fa9-4576-a173-6318556ccb0b", productId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testdeleteException() throws Exception {
		String productId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		doThrow(new NoSuchElementException()).when(productService).delete(productId);
		assertEquals(productId, DataBuilderOrder.mockProduct().getId());
		this.mockMvc
				.perform(delete("/api/products/{productId}" + "?productId =786518f5-4fa9-4576-a173-6318556ccb0b",
						productId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupsertProductAvailability() throws Exception {
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		DataBuilderOrder.allowedZipCodes.add(DataBuilderOrder.mockZipCode());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockProductAvailabilityDto());
		System.out.println(inputJson);
		this.mockMvc
				.perform(post("/api/products/availability").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testupsertProductAvailabilityException() throws Exception {
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		DataBuilderOrder.allowedDMAs.add(DataBuilderOrder.mockDMA());
		DataBuilderOrder.allowedStates.add(DataBuilderOrder.mockState());
		DataBuilderOrder.allowedZipCodes.add(DataBuilderOrder.mockZipCode());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		doThrow(new NoSuchElementException()).when(productService).updateProductAvailability(Mockito.any(),
				Mockito.any());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockProductAvailabilityDto());
		System.out.println(inputJson);
		this.mockMvc
				.perform(post("/api/products/availability").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testgetProductAvailability() throws Exception {
		String productId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		String id = "786518f5-4fa9-4576-a173-6318556ccb0b";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		assertEquals(productId, DataBuilderOrder.mockProduct().getId());
		assertEquals(id, DataBuilderOrder.mockProduct().getId());
		this.mockMvc.perform(get("/api/products/id/{productId}/availability/region"
				+ "?productId =786518f5-4fa9-4576-a173-6318556ccb0b&state=CA&zip=503165", productId)

				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testgetProductAvailabilityException() throws Exception {
		String productId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		String id = "786518f5-4fa9-4576-a173-6318556ccb0b";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		assertEquals(productId, DataBuilderOrder.mockProduct().getId());
		assertEquals(id, DataBuilderOrder.mockProduct().getId());
		doThrow(new NoSuchElementException()).when(productService).getProductAvailabilityWithRegion(Mockito.anyString(),
				Mockito.any(), Mockito.any());
		this.mockMvc.perform(get("/api/products/id/{productId}/availability/region"
				+ "?productId =786518f5-4fa9-4576-a173-6318556ccb0b&state=CA&zip=503165", productId)

				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testgetAvailabilityForProduct() throws Exception {
		String productId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		String zipCode = "503165";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		assertEquals(productId, DataBuilderOrder.mockProduct().getId());
		assertEquals(zipCode, DataBuilderOrder.mockZipCode().getZip());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ProductAvailabilityResponsePayload availability = productService.getAvailability(productId,
				Optional.of(zipCode));
		when(availability).thenReturn(DataBuilderOrder.mockProductAvailabilityResponsePayload());
		this.mockMvc.perform(get(
				"/api/products/id/{productId}/availability"
						+ "?productId =786518f5-4fa9-4576-a173-6318556ccb0b&withzip=503165",
				productId, Optional.of(zipCode))

				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testgetAvailabilityForProductCatchException() throws Exception {
		String productId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		String zipCode = "503165";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		assertEquals(productId, DataBuilderOrder.mockProduct().getId());
		assertEquals(zipCode, DataBuilderOrder.mockZipCode().getZip());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
//		ProductAvailabilityResponsePayload availability = productService.getAvailability(productId,
//				Optional.of(zipCode));
//		when(availability).thenReturn(DataBuilderOrder.mockProductAvailabilityResponsePayload());
		doThrow(new NoSuchElementException()).when(productService).getAvailability(productId, Optional.of(zipCode));
		this.mockMvc.perform(get(
				"/api/products/id/{productId}/availability"
						+ "?productId =786518f5-4fa9-4576-a173-6318556ccb0b&withzip=503165",
				productId, Optional.of(zipCode))

				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testgetAvailabilityForProductException() throws Exception {
		String productId = "786518f5-4fa9-4576-a173-6318556ccb0b";
		String zipCode = "503165";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		assertEquals(productId, DataBuilderOrder.mockProduct().getId());
		assertEquals(zipCode, DataBuilderOrder.mockZipCode().getZip());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(productService.getAvailability(Mockito.anyString(), Optional.of(Mockito.anyString())))
				.thenReturn(DataBuilderOrder.mockProductAvailabilityResponsePayload());
		this.mockMvc.perform(get("/api/products/id/{productId}/availability"
				+ "?productId =786518f5-4fa9-4576-a173-6318556ccb0b&withzip=503165", productId)

				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());

	}

}
