package com.junodx.api.controllers.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.models.laboratory.Laboratory;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.ServiceBase.ServiceResponse;
import com.junodx.api.services.lab.LaboratoryService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = { LaboratoryControllerTest.class })
public class LaboratoryControllerTest {
 	@Mock
	private LaboratoryService laboratoryService;
	@InjectMocks
	private LaboratoryController laboratoryController;

	@Mock
	private Authentication authentication;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private SecurityContext securityContext;
	@Mock
	private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationTokens;

	@Mock
	ServiceBase.ServiceResponse serviceBase;
	@Mock
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;
 
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(laboratoryController).build();
		HttpServletRequest mockRequest = new MockHttpServletRequest();
		ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(servletRequestAttributes);
	}
 
	@Test
	public void testgetLab() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(laboratoryService.getLaboratory(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		this.mockMvc.perform(get("/api/labs/laboratory/{labId}", labId).contentType(MediaType.APPLICATION_JSON))
		 		.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetLabException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(laboratoryService.getLaboratory(Mockito.anyString())).thenReturn(Optional.empty());
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		this.mockMvc.perform(get("/api/labs/laboratory/{labId}", labId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testgetAllLabs() throws Exception {
		List<Laboratory> labs = new ArrayList<>();
		labs.add(DataBuilderOrder.mockLaboratory());
		SecurityContextHolder.setContext(securityContext);
		serviceBase = Mockito.mock(ServiceBase.ServiceResponse.class, Mockito.CALLS_REAL_METHODS);
		serviceBase.setResponseValue(labs);
		serviceBase.setSucceeded(true);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(laboratoryService.getAllLaboratories()).thenReturn(serviceBase);
		this.mockMvc.perform(get("/api/labs/laboratory").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetAllLabsException() throws Exception {
		List<Laboratory> labs = new ArrayList<>();
		labs.add(DataBuilderOrder.mockLaboratory());
		SecurityContextHolder.setContext(securityContext);
		serviceBase = Mockito.mock(ServiceBase.ServiceResponse.class, Mockito.CALLS_REAL_METHODS);
		serviceBase.setResponseValue(labs);
		serviceBase.setSucceeded(false);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(laboratoryService.getAllLaboratories()).thenReturn(serviceBase);
		this.mockMvc.perform(get("/api/labs/laboratory").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testcreateLab() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(laboratoryService.saveLaboratory(Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockLaboratory());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockLaboratory());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/laboratory").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}
 
	@Test
	public void testcreateLabException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(laboratoryService.saveLaboratory(Mockito.any(), Mockito.any())).thenReturn(null);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockLaboratory());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/laboratory").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testupdateLab() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(laboratoryService.updateLaboratory(Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockLaboratory());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockLaboratory());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/laboratory").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testupdateLabException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(laboratoryService.updateLaboratory(Mockito.any(), Mockito.any())).thenReturn(null);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockLaboratory());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/laboratory").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}
  
	@Test
	public void testdeleteLab() throws Exception {
		List<Laboratory> labs = new ArrayList<>();
		labs.add(DataBuilderOrder.mockLaboratory());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		serviceBase = Mockito.mock(ServiceBase.ServiceResponse.class, Mockito.CALLS_REAL_METHODS);
		serviceBase.setResponseValue(labs);
		serviceBase.setSucceeded(false);
		serviceBase.setCode(LogCode.SUCCESS);
		when(laboratoryService.deleteLaboratory(Mockito.any(), Mockito.any())).thenReturn(serviceBase);
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		this.mockMvc.perform(delete("/api/labs/laboratory/{labId}", labId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	} 
	@Test
	public void testdeleteLabException() throws Exception {
		List<Laboratory> labs = new ArrayList<>();
		labs.add(DataBuilderOrder.mockLaboratory());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		serviceBase = Mockito.mock(ServiceBase.ServiceResponse.class, Mockito.CALLS_REAL_METHODS);
		serviceBase.setResponseValue(labs);
		serviceBase.setSucceeded(false);
		serviceBase.setCode(LogCode.RESOURCE_DELETE_ERROR);
		when(laboratoryService.deleteLaboratory(Mockito.any(), Mockito.any())).thenReturn(serviceBase);
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		this.mockMvc.perform(delete("/api/labs/laboratory/{labId}", labId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}
} 
