package com.junodx.api.controllers.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

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
import com.junodx.api.controllers.payloads.ReportConfigurationPayload;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.laboratory.TestReport;
import com.junodx.api.models.laboratory.TestRun;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.services.lab.TestRunService;

@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = { TestRunControllerTest.class })
public class TestRunControllerTest {

	@Mock
	private TestRunService testRunService;
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
	private TestRunController testRunController;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(testRunController).build();
		HttpServletRequest mockRequest = new MockHttpServletRequest();
		ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(servletRequestAttributes);
	}

	@Test
	public void testgetTestRun() throws Exception {
		String runId = "1L";
		String id = "1L"; 
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(testRunService.getTestRun(runId)).thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		assertEquals(runId, DataBuilderOrder.mockTestRun().getId());
		assertEquals(id, DataBuilderOrder.mockTestRun().getId());
		this.mockMvc
				.perform(get("/api/labs/runs/{runId}", runId).param(id, runId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}
 
	@Test
	public void testgetTestRunException() throws Exception {
		String runId = "1L";
		String id = "1L";
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		// when(testRunService.getTestRun(runId)).thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		assertEquals(runId, DataBuilderOrder.mockTestRun().getId());
		assertEquals(id, DataBuilderOrder.mockTestRun().getId());
		this.mockMvc
				.perform(get("/api/labs/runs/{runId}", runId).param(id, runId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	public void testgetTestRunsLabOrderId() throws Exception {
		String laboratoryOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		List<TestRun> testRuns = new ArrayList<>();
		testRuns.add(DataBuilderOrder.mockTestRun());
		Pageable paging = PageRequest.of(5, 5);
		final Page<TestRun> pages = new PageImpl<>(testRuns, paging, 5);
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testRunController, "maxPageSize", 20);
		assertEquals(laboratoryOrderId, DataBuilderOrder.mockTestRun().getLaboratoryOrder().getId());
		this.mockMvc
				.perform(get("/api/labs/runs" + "?page=5&size=5&laboratoryOrderId=76c97d43-347f-4132-ba18-ddf3b313c226",
						laboratoryOrderId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
 
	}

	@Test
	public void testgetTestRunsLabKitId() throws Exception {
		String kitId = "c06071ba-bf8e-414c-8622-2aa57d5b6a7f";
		List<TestRun> testRuns = new ArrayList<>();
		testRuns.add(DataBuilderOrder.mockTestRun());
		Pageable paging = PageRequest.of(5, 5);
		final Page<TestRun> pages = new PageImpl<>(testRuns, paging, 5);
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testRunController, "maxPageSize", 20);
		assertEquals(kitId, DataBuilderOrder.mockTestRun().getKit().getId());
		this.mockMvc
				.perform(get("/api/labs/runs" + "?page=5&size=5&byKitId=c06071ba-bf8e-414c-8622-2aa57d5b6a7f", kitId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testgetTestRunsPages() throws Exception {
		String kitId = "c06071ba-bf8e-414c-8622-2aa57d5b6a7f";
		List<TestRun> testRuns = new ArrayList<>();
		testRuns.add(DataBuilderOrder.mockTestRun());
		Pageable paging = PageRequest.of(5, 5);
		final Page<TestRun> pages = new PageImpl<>(testRuns, paging, 5);
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testRunController, "maxPageSize", 20);
		when(testRunService.getAllTestRuns(paging)).thenReturn(pages);
		assertEquals(kitId, DataBuilderOrder.mockTestRun().getKit().getId());
		this.mockMvc.perform(get("/api/labs/runs" + "?page=5&size=5").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	} 

	@Test
	public void testcreateRun() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(testRunService.saveTestRun(Mockito.any(), Mockito.anyString(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockTestRun());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestRunUpsertRequest());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/runs").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testcreateRunCatchException() throws Exception {
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		doThrow(new NoSuchElementException()).when(testRunService).saveTestRun(Mockito.any(), Mockito.anyString(),
				Mockito.any());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestRunUpsertRequest());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/runs").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	} 
 
	@Test
	public void testcreateRetest() throws Exception {
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(testRunService.createRetestRequest(Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockTestRun());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestRunRetestPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/runs/retest").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testcreateRetestCatchException() throws Exception {
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(testRunService.createRetestRequest(Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.mockTestRun());
		doThrow(new NoSuchElementException()).when(testRunService).createRetestRequest(Mockito.any(), Mockito.any());

		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestRunRetestPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/runs/retest").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdateRun() throws Exception {
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(testRunService.update(Mockito.any(), Mockito.any())).thenReturn(DataBuilderOrder.mockTestRun());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestRun());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/runs").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testupdateRunException() throws Exception {
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		doThrow(new NoSuchElementException()).when(testRunService).update(Mockito.any(), Mockito.any());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestRun());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/runs").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testsetCompletedOverride() throws Exception {
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestRun());
		System.out.println(inputJson);
		this.mockMvc
				.perform(patch("/api/labs/runs/override").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testsetCompletedOverrideException() throws Exception {
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		doThrow(new NoSuchElementException()).when(testRunService).overrideCompletedState(Mockito.anyString());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestRun());
		System.out.println(inputJson);
		this.mockMvc
				.perform(patch("/api/labs/runs/override").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testgetReportConfigurationsForSampleIds() throws Exception {
		List<String> sampleIds = new ArrayList<>();
		sampleIds.add("1L");
		List<ReportConfigurationPayload> reportConfigs = new ArrayList<>();
		reportConfigs.add(DataBuilderOrder.mockReportConfigurationPayload());
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(testRunService.getReportConfigurationsForSampleIds(sampleIds)).thenReturn(reportConfigs);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(sampleIds);
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/runs/report-configurations", sampleIds).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}
	
	@Test
	public void testgetReportConfigurationsForSampleIdsException() throws Exception {
		List<String> sampleIds = new ArrayList<>();
		sampleIds.add("1L");
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(testRunService.getReportConfigurationsForSampleIds(sampleIds)).thenReturn(null);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(sampleIds);
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/runs/report-configurations", sampleIds).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());
	}
	
	
	
	@Test
	public void testgetAssociatedSampleInformation() throws Exception {
		String sampleId= "1L";
		List<TestRun> testRuns = new ArrayList<>();
		testRuns.add(DataBuilderOrder.mockTestRun());
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testRunController, "maxPageSize", 20);
		when(testRunService.getTestRunsForLabOrder(Mockito.anyString())).thenReturn(testRuns);
		assertEquals(sampleId, DataBuilderOrder.mockReportConfigurationPayload().getSampleId());
		this.mockMvc.perform(get("/api/labs/runs/sample-info/{sampleid}" + "?sampleid=1L",sampleId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}
	
	@Test
	public void testgetAssociatedSampleInformationException() throws Exception {
		String sampleId= "1L";
		List<TestRun> testRuns = new ArrayList<>();
		testRuns.add(DataBuilderOrder.mockTestRun());
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testRunController, "maxPageSize", 20);
//		when(testRunService.getTestRunsForLabOrder(Mockito.anyString())).thenReturn(testRuns);
		doThrow(new NoSuchElementException()).when(testRunService).getTestRunsForLabOrder(Mockito.anyString());
		assertEquals(sampleId, DataBuilderOrder.mockReportConfigurationPayload().getSampleId());
		this.mockMvc.perform(get("/api/labs/runs/sample-info/{sampleid}" + "?sampleid=1L",sampleId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}
	
 
	@Test
	public void testgetAssociatedSampleInformationExceptionNull() throws Exception {
		String sampleId= "1L";
		List<TestRun> testRuns = new ArrayList<>();
		testRuns.add(DataBuilderOrder.mockTestRun());
		DataBuilderOrder.status.add(DataBuilderOrder.mockLaboratoryStatus());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testRunController, "maxPageSize", 20);
		assertEquals(sampleId, DataBuilderOrder.mockReportConfigurationPayload().getSampleId());
		this.mockMvc.perform(get("/api/labs/runs/sample-info/{sampleid}" ,sampleId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

}
