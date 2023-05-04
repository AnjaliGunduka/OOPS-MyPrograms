package com.junodx.api.controllers.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.laboratory.BatchRunMetaProjection;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.services.lab.BatchRunService;
import com.junodx.api.services.lab.KitService;


@AutoConfigureMockMvc
@ContextConfiguration 
@SpringBootTest(classes = {BatchRunControllerTest.class })
public class BatchRunControllerTest {
	
	
	@Mock 
	private BatchRunService batchRunService;
	@InjectMocks
	private BatchRunController batchRunController;
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
	@Mock
	private BatchRunMetaProjection batchRunMetaProjection;
	@BeforeEach 
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(batchRunController).build();
		HttpServletRequest mockRequest = new MockHttpServletRequest();
		ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(servletRequestAttributes);
	}
	
	@Test
	public void testgetBatchRun() throws Exception {
		String batchId = "8L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(batchRunService.getBatchRun(batchId)).thenReturn(Optional.of(DataBuilderOrder.mockBatchRun()));
		assertEquals(batchId, DataBuilderOrder.mockBatchRun().getId());
		this.mockMvc.perform(get("/api/labs/batches/{batchId}", batchId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}
	
	@Test
	public void testgetBatchRunException() throws Exception {
		String batchId = "8L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(batchRunService.getBatchRun(batchId)).thenReturn(Optional.empty());
		//ResponseEntity<?> batchRun = batchRunController.getBatchRun(null);
		assertEquals(batchId, DataBuilderOrder.mockBatchRun().getId());
		this.mockMvc.perform(get("/api/labs/batches/{batchId}", batchId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());
	}
	
//	@Test
//	public void testgetBatchRunMeta() throws Exception {
//		String batchId = "8L";
//		SecurityContextHolder.setContext(securityContext);
//		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
//				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
//		when(batchRunService.getBatchRunMeta(batchId)).thenReturn(Optional.of(batchRunMetaProjection));
//		assertEquals(batchId, DataBuilderOrder.mockBatchRun().getId());
//		ResponseEntity<?> batchRun = batchRunController.getBatchRunMeta(batchId);
//		this.mockMvc.perform(get("/api/labs/batches/meta/{batchId}", batchId).contentType(MediaType.APPLICATION_JSON))
//		.andExpect(status().isOk()).andDo(print());
//	}
//	
	
	
	

}
