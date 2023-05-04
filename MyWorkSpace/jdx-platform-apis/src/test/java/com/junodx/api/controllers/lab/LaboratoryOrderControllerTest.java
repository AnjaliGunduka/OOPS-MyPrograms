package com.junodx.api.controllers.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.payloads.MessageResponse;
import com.junodx.api.dto.mappers.LaboratoryMapStructMapper;
import com.junodx.api.dto.models.laboratory.LabOrderBatchDto;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.models.laboratory.LaboratoryOrder;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.services.lab.LaboratoryOrderService;

@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = { LaboratoryControllerTest.class })
public class LaboratoryOrderControllerTest {
	@Mock
	 private LaboratoryOrderService laboratoryOrderService;
	@Mock 
	private LaboratoryMapStructMapper labMapper;
	@InjectMocks
	private LaboratoryOrderController laboratoryOrderController;
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
		mockMvc = MockMvcBuilders.standaloneSetup(laboratoryOrderController).build();
		HttpServletRequest mockRequest = new MockHttpServletRequest();
		ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(servletRequestAttributes);
	}

	@Test
	public void testget() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(laboratoryOrderService.getLaboratoryOrder(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		String labOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		assertEquals(labOrderId, DataBuilderOrder.mockLaboratoryOrder().getId());
		this.mockMvc.perform(get("/api/labs/orders/{labOrderId}", labOrderId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}
 
	@Test
	public void testgetException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(laboratoryOrderService.getLaboratoryOrder(Mockito.anyString())).thenReturn(Optional.empty());
		String labOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		assertEquals(labOrderId, DataBuilderOrder.mockLaboratoryOrder().getId());
		this.mockMvc.perform(get("/api/labs/orders/{labOrderId}", labOrderId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testfindOrderLineItemId() throws Exception {
		List<LaboratoryOrder> lab = new ArrayList<>();
		lab.add(DataBuilderOrder.mockLaboratoryOrder());
		List<LabOrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.mockLabOrderBatchDto());
		Pageable paging = PageRequest.of(5, 5);
		final Page<LaboratoryOrder> pages = new PageImpl<>(lab, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(laboratoryOrderController, "maxPageSize", 20);
		when(laboratoryOrderService.getAllLabOrders(paging)).thenReturn(pages);
		when(labMapper.orderToOrderLabBatchDtos(Mockito.anyList())).thenReturn(transitionOrders);
		mockMvc.perform(get("/api/labs/orders" + "?page=5&size=5&"
				+ "orderLineItemId=845dba6b-4c0f-4cc9-ba77-62e7216bd692&byTestRunId=1L")).andExpect(status().isOk())
				.andDo(print());

	}

	@Test
	public void testfindTestRunId() throws Exception {
		List<LaboratoryOrder> lab = new ArrayList<>();
		lab.add(DataBuilderOrder.mockLaboratoryOrder());
		List<LabOrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.mockLabOrderBatchDto());
		Pageable paging = PageRequest.of(5, 5);
		final Page<LaboratoryOrder> pages = new PageImpl<>(lab, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(laboratoryOrderController, "maxPageSize", 20);
		when(laboratoryOrderService.getAllLabOrders(paging)).thenReturn(pages);
		when(labMapper.orderToOrderLabBatchDtos(Mockito.anyList())).thenReturn(transitionOrders);
		mockMvc.perform(get("/api/labs/orders" + "?page=5&size=5" + "&byTestRunId=1L")).andExpect(status().isOk())
				.andDo(print());

	}

	@Test
	public void testfindLabOrders() throws Exception {
		List<LaboratoryOrder> lab = new ArrayList<>();
		lab.add(DataBuilderOrder.mockLaboratoryOrder());
		List<LabOrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.mockLabOrderBatchDto());
		Pageable paging = PageRequest.of(5, 5);
		final Page<LaboratoryOrder> pages = new PageImpl<>(lab, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(laboratoryOrderController, "maxPageSize", 20);
		when(laboratoryOrderService.getAllLabOrders(paging)).thenReturn(pages);
		when(labMapper.orderToOrderLabBatchDtos(Mockito.anyList())).thenReturn(transitionOrders);
		mockMvc.perform(get("/api/labs/orders" + "?page=5&size=5")).andExpect(status().isOk()).andDo(print());

	}
	@Test 
	public void testfindLabOrdersMax () throws Exception {
		List<LaboratoryOrder> lab = new ArrayList<>();
		lab.add(DataBuilderOrder.mockLaboratoryOrder());
		List<LabOrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.mockLabOrderBatchDto());
		Pageable paging = PageRequest.of(0, 21);
		final Page<LaboratoryOrder>  pages = new PageImpl<>(lab, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(laboratoryOrderController, "maxPageSize", 20);
		when(laboratoryOrderService.getAllLabOrders(paging)).thenReturn(pages);
		when(labMapper.orderToOrderLabBatchDtos(Mockito.anyList())).thenReturn(transitionOrders);
		mockMvc.perform(get("/api/labs/orders" + "?page=5&size=21"));
	}
	@Test
	public void testfindException() throws Exception {
		List<LaboratoryOrder> lab = new ArrayList<>();
		lab.add(DataBuilderOrder.mockLaboratoryOrder());
		List<LabOrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.mockLabOrderBatchDto());
		Pageable paging = PageRequest.of(5, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(laboratoryOrderController, "maxPageSize", 20);
		when(laboratoryOrderService.getAllLabOrders(paging)).thenReturn(null);
		mockMvc.perform(get("/api/labs/orders" + "?page=5&size=5")).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testupdate() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(laboratoryOrderService.update(Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockLaboratoryOrder());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockLaboratoryOrder());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/orders").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testupdateException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(laboratoryOrderService.update(Mockito.any(), Mockito.any())).thenReturn(null);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockLaboratoryOrder());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/orders").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}
	@Test 
	public void testupdateCatchException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockLaboratoryOrder());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(laboratoryOrderService).update(Mockito.any(),Mockito.any());
		this.mockMvc.perform(patch("/api/labs/orders").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}
	@Test
	public void testupdateTestRun() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestRunRemovalPayload());
		System.out.println(inputJson);
		this.mockMvc
				.perform(
						patch("/api/labs/orders/remove/run").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testupdateTestRunException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestRunRemovalPayloads());
		System.out.println(inputJson);
		this.mockMvc
				.perform(
						patch("/api/labs/orders/remove/run").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}
	@Test
	public void testupdateTestRunCatchException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestRunRemovalPayload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(laboratoryOrderService).removeTestRun(Mockito.anyString(),Mockito.anyString());
		this.mockMvc
				.perform(
						patch("/api/labs/orders/remove/run").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

}
