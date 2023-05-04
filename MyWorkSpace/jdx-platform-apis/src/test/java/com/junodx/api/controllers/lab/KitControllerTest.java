package com.junodx.api.controllers.lab;

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
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.payloads.MessageResponse;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.models.laboratory.TestReport;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.services.lab.KitService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@AutoConfigureMockMvc
@ContextConfiguration 
@SpringBootTest(classes = { KitControllerTest.class })
public class KitControllerTest {
	@Mock 
	private KitService kitService;
	@InjectMocks
	private KitController kitController;
	@Mock
	private Authentication authentication;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private SecurityContext securityContext;
	@Mock
	private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationTokens;
	@Autowired
	private MockMvc mockMvc;
 
	@BeforeEach 
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(kitController).build();
		HttpServletRequest mockRequest = new MockHttpServletRequest();
		ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(servletRequestAttributes);
	}
 
	@Test
	public void testgetKit() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(kitService.getKit(Mockito.anyString())).thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		String kitId = "c06071ba-bf8e-414c-8622-2aa57d5b6a7f";
		assertEquals(kitId, DataBuilderOrder.mockKit().getId());
		this.mockMvc.perform(get("/api/labs/kits/{kitId}", kitId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}
  
	@Test
	public void testgetKitException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(kitService.getKit(Mockito.anyString())).thenReturn(Optional.empty());
		String kitId = "c06071ba-bf8e-414c-8622-2aa57d5b6a7f";
		assertEquals(kitId, DataBuilderOrder.mockKit().getId());
		this.mockMvc.perform(get("/api/labs/kits/{kitId}", kitId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testgetAllKitException() throws Exception {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		Pageable paging = PageRequest.of(5, 5);
		final Page<Kit> pages = new PageImpl<>(kit, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(kitController, "maxPageSize", 20);
		when(kitService.findKitsBySampleNumber(Mockito.anyString())).thenReturn(kit);
		when(kitService.getAllKits(paging)).thenReturn(pages);
		this.mockMvc.perform(get("/api/labs/kits")).andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testGetKitsExample() throws Exception {
		String bySampleNumbers = "123455";
		String bycodes = "JO135wwa33jj789";
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		Pageable paging = PageRequest.of(5, 5);
		final Page<Kit> pages = new PageImpl<>(kit, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(kitController, "maxPageSize", 20);
		when(kitService.findKitsBySampleNumber(bySampleNumbers)).thenReturn(kit);
		when(kitService.getAllKits(paging)).thenReturn(pages);
		assertEquals(bySampleNumbers, DataBuilderOrder.mockKit().getSampleNumber());
		assertEquals(bycodes, DataBuilderOrder.mockKit().getCode());
		mockMvc.perform(get("/api/labs/kits" + "?page=5&size=5&bySampleNumber=123455&bycode=bycodes"))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testGetKitTotalElements() throws Exception {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		Pageable paging = PageRequest.of(5, 5);
		final Page<Kit> pages = new PageImpl<>(kit, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(kitController, "maxPageSize", 20);
		when(kitService.getAllKits(paging)).thenReturn(pages);
		mockMvc.perform(get("/api/labs/kits" + "?page=5&size=5&bySampleNumber=1234&bycode=JO135wwa33jj789"))
				.andExpect(status().isOk());

	} 

	@Test
	public void testGetKitTotalElementsgreatherThanOne() throws Exception {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		Pageable paging = PageRequest.of(0, 20);
		final Page<Kit> pages = new PageImpl<>(kit, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(kitController, "maxPageSize", 20);
		when(kitService.getAllKits(paging)).thenReturn(pages);
		mockMvc.perform(get("/api/labs/kits" + "?page=0&size=20&bySampleNumber=1234&bycode=JO135wwa33jj789"))
				.andExpect(status().isOk());

	} 

	@Test
	public void testGetKitMax() throws Exception {
		List<Kit> kit = new ArrayList<>();
		kit.add(DataBuilderOrder.mockKit());
		Pageable paging = PageRequest.of(0, 21);
		final Page<Kit> pages = new PageImpl<>(kit, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(kitController, "maxPageSize", 20);
		when(kitService.getAllKits(paging)).thenReturn(pages);
		mockMvc.perform(get("/api/labs/kits" + "?page=0&size=21&bySampleNumber=1234&bycode=JO135wwa33jj789"));
	}
 
	@Test
	public void testcreateKit() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(kitService.saveKit(Mockito.any(), Mockito.any())).thenReturn(DataBuilderOrder.mockKit());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockKit());
		this.mockMvc.perform(post("/api/labs/kits").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testcreateKitException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockKit());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/kits").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}
	@Test
	public void testcreateKitCatchException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockKit());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(kitService).saveKit(Mockito.any(),Mockito.any());
		this.mockMvc.perform(post("/api/labs/kits").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}
	@Test
	public void testupdateKit() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(kitService.updateKit(Mockito.any(), Mockito.any())).thenReturn(DataBuilderOrder.mockKit());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockKit());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/kits").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testupdateKitException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(kitService.updateKit(Mockito.any(), Mockito.any())).thenReturn(null);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockKit());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/kits").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testdeleteKit() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		verify(kitService, times(0)).deleteKit(DataBuilderOrder.mockKit().getId(), DataBuilderOrder.userDetailsImpl);
		String kitId = "c06071ba-bf8e-414c-8622-2aa57d5b6a7f";
		assertEquals(kitId, DataBuilderOrder.mockKit().getId());
		this.mockMvc.perform(delete("/api/labs/kits/{kitId}", kitId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	
	@Test
	public void testudeleteKitCatchException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		String kitId = "c06071ba-bf8e-414c-8622-2aa57d5b6a7f";
		assertEquals(kitId, DataBuilderOrder.mockKit().getId());
		doThrow(new NoSuchElementException()).when(kitService).deleteKit(Mockito.anyString(),Mockito.any());
		this.mockMvc.perform(delete("/api/labs/kits/{kitId}", kitId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	} 
}
