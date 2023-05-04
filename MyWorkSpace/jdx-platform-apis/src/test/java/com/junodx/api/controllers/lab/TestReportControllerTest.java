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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.junodx.api.models.laboratory.types.ReportType;
import com.junodx.api.models.laboratory.types.RetestActionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.SortType;
import com.junodx.api.controllers.lab.types.TestReportSortBy;
import com.junodx.api.controllers.payloads.MessageResponse;
import com.junodx.api.dto.mappers.CommerceMapStructMapper;
import com.junodx.api.dto.mappers.LaboratoryMapStructMapper;
import com.junodx.api.dto.models.commerce.OrderBatchDto;
import com.junodx.api.dto.models.laboratory.reports.TestReportBatchDto;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.core.types.IntervalType;
import com.junodx.api.models.laboratory.TestReport;
import com.junodx.api.models.laboratory.reports.types.SignedOutType;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.services.lab.TestReportService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = { TestReportControllerTest.class })
public class TestReportControllerTest {

	@Mock
	private TestReportService testReportService;
	@Mock
	private ObjectMapper mapper;
	@Mock
	private LaboratoryMapStructMapper laboratoryMapStructMapper;

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
	@InjectMocks
	private TestReportController testReportController;
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(testReportController).build();
		HttpServletRequest mockRequest = new MockHttpServletRequest();
		ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(servletRequestAttributes);
	}

	@Test
	public void testgetTestReport() throws Exception {
		String[] includes = { "anjali" };
		String reportId = "6L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(testReportService.getTestReport(reportId, includes, DataBuilder.getMockUserDetailsImpl()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestReport()));
		assertEquals(reportId, DataBuilderOrder.mockReport().getId());
		this.mockMvc.perform(get("/api/labs/reports/{reportId}" + "?reportId=6L&include=anjali", reportId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetTestReportException() throws Exception {
		String[] includes = { "anjali" };
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(testReportService.getTestReport("6L", includes, DataBuilder.getMockUserDetailsImpl()))
				.thenReturn(Optional.empty());
		String reportId = "6L";
		assertEquals(reportId, DataBuilderOrder.mockReport().getId());
		this.mockMvc.perform(get("/api/labs/reports/{reportId}" + "?reportId=6L&include=anjali", reportId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testgetTestReportsForPatient() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		String userId = "1L";
		when(testReportService.findAllAvailableTestReportsByPatient(userId, paging)).thenReturn(pages);

		mockMvc.perform(get("/api/labs/reports/patient/{userId}" + "?userId=1L&page=5&size=5&", userId))
				.andExpect(status().isOk()).andDo(print());
	}
 
	@Test
	public void testgetTestReportsForPatientException() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		String userId = "1L";
		when(testReportService.findAllAvailableTestReportsByPatient(userId, paging)).thenReturn(null);
		mockMvc.perform(get("/api/labs/reports/patient/{userId}" + "?userId=1L&page=5&size=5&", userId))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test 
	public void testgetTestReportsForPatientMaxmimum() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		String userId = "1L";
		when(testReportService.findAllAvailableTestReportsByPatient(userId, paging)).thenReturn(pages);
		mockMvc.perform(get("/api/labs/reports/patient/{userId}" + "?userId=1L&page=5&size=21&", userId));

	}

	@Test
	public void testgetfindcondensed() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean signedOut = true;
		ReportType reportType = ReportType.STANDARD;
		String patientId = "1L";
		String firstName = "Juno_test";
		String lastName = "User";
		String email = "no-email@junodx.com";
		String sampleNumber = "123455";
		String batchRunId = "8L";
		String labOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		String orderNumber = "1234";
		boolean isAvailable = true;
		boolean upcomingOnly = true;
		boolean reportable = true;
		boolean approved = true;
		boolean condensed = false;
		TestReportSortBy sortBy = TestReportSortBy.age;
		SortType sortDirection = SortType.ASC;
		assertEquals(approved, DataBuilderOrder.mockTestReport().isApproved());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		assertEquals(reportable, DataBuilderOrder.mockTestReport().isReportable());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(orderNumber, DataBuilderOrder.mockTestReport().getOrderNumber());
		assertEquals(sampleNumber, DataBuilderOrder.mockTestReport().getSampleNumber());
		assertEquals(batchRunId, DataBuilderOrder.mockTestReport().getBatchRunId());
		assertEquals(labOrderId, DataBuilderOrder.mockTestReport().getLaboratoryOrderId());
		assertEquals(patientId, DataBuilderOrder.mockTestReport().getPatient().getId());
		assertEquals(firstName, DataBuilderOrder.mockTestReport().getPatient().getFirstName());
		assertEquals(lastName, DataBuilderOrder.mockTestReport().getPatient().getLastName());
		assertEquals(email, DataBuilderOrder.mockTestReport().getPatient().getEmail());
		assertEquals(signedOut, DataBuilderOrder.mockTestReport().isSignedOut());
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		assertEquals(reportType, DataBuilderOrder.mockTestReport().getReportType());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		when(testReportService.getAllTestReports(paging)).thenReturn(pages);
		when(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);

		mockMvc.perform(get("/api/labs/reports" + "?page=5&size=5&condensed=false")).andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void testgetfindcondensedTrue() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean signedOut = true;
		ReportType reportType = ReportType.STANDARD;
		String patientId = "1L";
		String firstName = "Juno_test";
		String lastName = "User";
		String email = "no-email@junodx.com";
		String sampleNumber = "123455";
		String batchRunId = "8L";
		String labOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		String orderNumber = "1234";
		boolean isAvailable = true;
		boolean upcomingOnly = true;
		boolean reportable = true;
		boolean approved = true;
		boolean condensed = true;
		TestReportSortBy sortBy = TestReportSortBy.age;
		SortType sortDirection = SortType.ASC;
		assertEquals(approved, DataBuilderOrder.mockTestReport().isApproved());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		assertEquals(reportable, DataBuilderOrder.mockTestReport().isReportable());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(orderNumber, DataBuilderOrder.mockTestReport().getOrderNumber());
		assertEquals(sampleNumber, DataBuilderOrder.mockTestReport().getSampleNumber());
		assertEquals(batchRunId, DataBuilderOrder.mockTestReport().getBatchRunId());
		assertEquals(labOrderId, DataBuilderOrder.mockTestReport().getLaboratoryOrderId());
		assertEquals(patientId, DataBuilderOrder.mockTestReport().getPatient().getId());
		assertEquals(firstName, DataBuilderOrder.mockTestReport().getPatient().getFirstName());
		assertEquals(lastName, DataBuilderOrder.mockTestReport().getPatient().getLastName());
		assertEquals(email, DataBuilderOrder.mockTestReport().getPatient().getEmail());
		assertEquals(signedOut, DataBuilderOrder.mockTestReport().isSignedOut());
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		assertEquals(reportType, DataBuilderOrder.mockTestReport().getReportType());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		when(testReportService.getAllTestReports(paging)).thenReturn(pages);
		when(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		mockMvc.perform(get("/api/labs/reports" + "?page=5&size=5&condensed=true")).andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void testgetfindgetAllTestReports() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean signedOut = true;
		ReportType reportType = ReportType.STANDARD;
		String patientId = "1L";
		String firstName = "Juno_test";
		String lastName = "User";
		String email = "no-email@junodx.com";
		String sampleNumber = "123455";
		String batchRunId = "8L";
		String labOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		String orderNumber = "1234";
		boolean isAvailable = true;
		boolean upcomingOnly = true;
		boolean reportable = true;
		boolean approved = true;
		boolean condensed = true;
		TestReportSortBy sortBy = TestReportSortBy.age;
		SortType sortDirection = SortType.ASC;
		assertEquals(approved, DataBuilderOrder.mockTestReport().isApproved());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		assertEquals(reportable, DataBuilderOrder.mockTestReport().isReportable());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(orderNumber, DataBuilderOrder.mockTestReport().getOrderNumber());
		assertEquals(sampleNumber, DataBuilderOrder.mockTestReport().getSampleNumber());
		assertEquals(batchRunId, DataBuilderOrder.mockTestReport().getBatchRunId());
		assertEquals(labOrderId, DataBuilderOrder.mockTestReport().getLaboratoryOrderId());
		assertEquals(patientId, DataBuilderOrder.mockTestReport().getPatient().getId());
		assertEquals(firstName, DataBuilderOrder.mockTestReport().getPatient().getFirstName());
		assertEquals(lastName, DataBuilderOrder.mockTestReport().getPatient().getLastName());
		assertEquals(email, DataBuilderOrder.mockTestReport().getPatient().getEmail());
		assertEquals(signedOut, DataBuilderOrder.mockTestReport().isSignedOut());
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		assertEquals(reportType, DataBuilderOrder.mockTestReport().getReportType());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		when(testReportService.getAllTestReports(paging)).thenReturn(pages);
		when(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		mockMvc.perform(get("/api/labs/reports" + "?page=5&size=5")).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetfindgetAllTestReportss() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReportBatchDto> transitionReports = new ArrayList<>();
		transitionReports.add(DataBuilderOrder.mockTestReportBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean signedOut = true;
		ReportType reportType = ReportType.STANDARD;
		String patientId = "1L";
		String firstName = "Juno_test";
		String lastName = "User";
		String email = "no-email@junodx.com";
		String sampleNumber = "123455";
		String batchRunId = "8L";
		String labOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		String orderNumber = "1234";
		boolean isAvailable = true;
		boolean upcomingOnly = true;
		boolean reportable = true;
		boolean approved = true;
		boolean condensed = true;
		TestReportSortBy sortBy = TestReportSortBy.age;
		SortType sortDirection = SortType.ASC;
		assertEquals(approved, DataBuilderOrder.mockTestReport().isApproved());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		assertEquals(reportable, DataBuilderOrder.mockTestReport().isReportable());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(orderNumber, DataBuilderOrder.mockTestReport().getOrderNumber());
		assertEquals(sampleNumber, DataBuilderOrder.mockTestReport().getSampleNumber());
		assertEquals(batchRunId, DataBuilderOrder.mockTestReport().getBatchRunId());
		assertEquals(labOrderId, DataBuilderOrder.mockTestReport().getLaboratoryOrderId());
		assertEquals(patientId, DataBuilderOrder.mockTestReport().getPatient().getId());
		assertEquals(firstName, DataBuilderOrder.mockTestReport().getPatient().getFirstName());
		assertEquals(lastName, DataBuilderOrder.mockTestReport().getPatient().getLastName());
		assertEquals(email, DataBuilderOrder.mockTestReport().getPatient().getEmail());
		assertEquals(signedOut, DataBuilderOrder.mockTestReport().isSignedOut());
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		assertEquals(reportType, DataBuilderOrder.mockTestReport().getReportType());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		when(testReportService.getAllTestReports(paging)).thenReturn(pages);
		when(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		when(laboratoryMapStructMapper.testReportToTestReportBatchDtos(pages.getContent()))
				.thenReturn(transitionReports);
		mockMvc.perform(get("/api/labs/reports" + "?page=5&size=5&")).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetfindIntervaldefault() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean signedOut = true;
		ReportType reportType = ReportType.STANDARD;
		String patientId = "1L";
		String firstName = "Juno_test";
		String lastName = "User";
		String email = "no-email@junodx.com";
		String sampleNumber = "123455";
		String batchRunId = "8L";
		String labOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		String orderNumber = "1234";
		boolean isAvailable = true;
		boolean upcomingOnly = true;
		boolean reportable = true;
		boolean approved = true;
		boolean condensed = true;
		TestReportSortBy sortBy = TestReportSortBy.age;
		SortType sortDirection = SortType.ASC;
		assertEquals(approved, DataBuilderOrder.mockTestReport().isApproved());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		assertEquals(reportable, DataBuilderOrder.mockTestReport().isReportable());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(orderNumber, DataBuilderOrder.mockTestReport().getOrderNumber());
		assertEquals(sampleNumber, DataBuilderOrder.mockTestReport().getSampleNumber());
		assertEquals(batchRunId, DataBuilderOrder.mockTestReport().getBatchRunId());
		assertEquals(labOrderId, DataBuilderOrder.mockTestReport().getLaboratoryOrderId());
		assertEquals(patientId, DataBuilderOrder.mockTestReport().getPatient().getId());
		assertEquals(firstName, DataBuilderOrder.mockTestReport().getPatient().getFirstName());
		assertEquals(lastName, DataBuilderOrder.mockTestReport().getPatient().getLastName());
		assertEquals(email, DataBuilderOrder.mockTestReport().getPatient().getEmail());
		assertEquals(signedOut, DataBuilderOrder.mockTestReport().isSignedOut());
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		assertEquals(reportType, DataBuilderOrder.mockTestReport().getReportType());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		// when(testReportService.getAllTestReports(paging)).thenReturn(pages);
		when(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.empty(), paging)).thenReturn(pages);
		mockMvc.perform(get("/api/labs/reports" + "?page=5&size=5" + "&labid=f450237e-20a3-4bf1-b64d-9ecaab16be7a"
		// + "&interval=1"
				+ "&units=WEEKS" + "&bytype=MANUAL" + "&byreport=NIPS_PLUS" + "&byreporttype=STANDARD"
				+ "&bysignedout=true" + "&byapproved=true" + "&bypatient=1L" + "&byfirstname=Juno_test"
				+ "&bylastname=User" + "&byemail=no-email@junodx.com" + "&bysamplenumber=123455" + "&bybatchrunid=8L"
				+ "&bylaborder=76c97d43-347f-4132-ba18-ddf3b313c226" + "&byordernumber=1234" + "&isavailable=true"
				+ "&upcomingonly=true" + "&sortby=age" + "&sorttype=ASC&" + "condensed=true" + "&byreportable=true&"))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetfindunits() throws Exception {
		List<TestReportBatchDto> transitionReports = new ArrayList<>();
		transitionReports.add(DataBuilderOrder.mockTestReportBatchDto());
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean signedOut = true;
		ReportType reportType = ReportType.STANDARD;
		String patientId = "1L";
		String firstName = "Juno_test";
		String lastName = "User";
		String email = "no-email@junodx.com";
		String sampleNumber = "123455";
		String batchRunId = "8L";
		String labOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		String orderNumber = "1234";
		boolean isAvailable = true;
		boolean upcomingOnly = true;
		boolean reportable = true;
		boolean approved = true;
		boolean condensed = true;
		TestReportSortBy sortBy = TestReportSortBy.age;
		SortType sortDirection = SortType.ASC;
		assertEquals(approved, DataBuilderOrder.mockTestReport().isApproved());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		assertEquals(reportable, DataBuilderOrder.mockTestReport().isReportable());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(orderNumber, DataBuilderOrder.mockTestReport().getOrderNumber());
		assertEquals(sampleNumber, DataBuilderOrder.mockTestReport().getSampleNumber());
		assertEquals(batchRunId, DataBuilderOrder.mockTestReport().getBatchRunId());
		assertEquals(labOrderId, DataBuilderOrder.mockTestReport().getLaboratoryOrderId());
		assertEquals(patientId, DataBuilderOrder.mockTestReport().getPatient().getId());
		assertEquals(firstName, DataBuilderOrder.mockTestReport().getPatient().getFirstName());
		assertEquals(lastName, DataBuilderOrder.mockTestReport().getPatient().getLastName());
		assertEquals(email, DataBuilderOrder.mockTestReport().getPatient().getEmail());
		assertEquals(signedOut, DataBuilderOrder.mockTestReport().isSignedOut());
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		assertEquals(reportType, DataBuilderOrder.mockTestReport().getReportType());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		when(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.empty(), paging)).thenReturn(pages);
		when(laboratoryMapStructMapper.testReportToTestReportBatchDtos(Mockito.anyList()))
				.thenReturn(transitionReports);
		mockMvc.perform(get("/api/labs/reports" + "?page=5&size=5" + "&labid=f450237e-20a3-4bf1-b64d-9ecaab16be7a"
		// + "&interval=1"
				+ "&units=DAYS" + "&bytype=MANUAL" + "&byreport=NIPS_PLUS" + "&byreporttype=STANDARD"
				+ "&bysignedout=true" + "&byapproved=true" + "&bypatient=1L" + "&byfirstname=Juno_test"
				+ "&bylastname=User" + "&byemail=no-email@junodx.com" + "&bysamplenumber=123455" + "&bybatchrunid=8L"
				+ "&bylaborder=76c97d43-347f-4132-ba18-ddf3b313c226" + "&byordernumber=1234" + "&isavailable=true"
				+ "&upcomingonly=true" + "&sortby=age" + "&sorttype=ASC&" + "condensed=true" + "&byreportable=true&"))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetfindgetAllTestReportsMaximumNotFound() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean signedOut = true;
		ReportType reportType = ReportType.STANDARD;
		String patientId = "1L";
		String firstName = "Juno_test";
		String lastName = "User";
		String email = "no-email@junodx.com";
		String sampleNumber = "123455";
		String batchRunId = "8L";
		String labOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		String orderNumber = "1234";
		boolean isAvailable = true;
		boolean upcomingOnly = true;
		boolean reportable = true;
		boolean approved = true;
		boolean condensed = true;
		TestReportSortBy sortBy = TestReportSortBy.age;
		SortType sortDirection = SortType.ASC;
		assertEquals(approved, DataBuilderOrder.mockTestReport().isApproved());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		assertEquals(reportable, DataBuilderOrder.mockTestReport().isReportable());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(orderNumber, DataBuilderOrder.mockTestReport().getOrderNumber());
		assertEquals(sampleNumber, DataBuilderOrder.mockTestReport().getSampleNumber());
		assertEquals(batchRunId, DataBuilderOrder.mockTestReport().getBatchRunId());
		assertEquals(labOrderId, DataBuilderOrder.mockTestReport().getLaboratoryOrderId());
		assertEquals(patientId, DataBuilderOrder.mockTestReport().getPatient().getId());
		assertEquals(firstName, DataBuilderOrder.mockTestReport().getPatient().getFirstName());
		assertEquals(lastName, DataBuilderOrder.mockTestReport().getPatient().getLastName());
		assertEquals(email, DataBuilderOrder.mockTestReport().getPatient().getEmail());
		assertEquals(signedOut, DataBuilderOrder.mockTestReport().isSignedOut());
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		assertEquals(reportType, DataBuilderOrder.mockTestReport().getReportType());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		when(testReportService.getAllTestReports(paging)).thenReturn(pages);
		when(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		mockMvc.perform(get("/api/labs/reports" + "?page=5&size=21")).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testgetfindInterval() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean signedOut = true;
		ReportType reportType = ReportType.STANDARD;
		String patientId = "1L";
		String firstName = "Juno_test";
		String lastName = "User";
		String email = "no-email@junodx.com";
		String sampleNumber = "123455";
		String batchRunId = "8L";
		String labOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		String orderNumber = "1234";
		boolean isAvailable = true;
		boolean upcomingOnly = true;
		boolean reportable = true;
		boolean approved = true;
		boolean condensed = true;
		TestReportSortBy sortBy = TestReportSortBy.age;
		SortType sortDirection = SortType.ASC;
		assertEquals(approved, DataBuilderOrder.mockTestReport().isApproved());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		assertEquals(reportable, DataBuilderOrder.mockTestReport().isReportable());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(orderNumber, DataBuilderOrder.mockTestReport().getOrderNumber());
		assertEquals(sampleNumber, DataBuilderOrder.mockTestReport().getSampleNumber());
		assertEquals(batchRunId, DataBuilderOrder.mockTestReport().getBatchRunId());
		assertEquals(labOrderId, DataBuilderOrder.mockTestReport().getLaboratoryOrderId());
		assertEquals(patientId, DataBuilderOrder.mockTestReport().getPatient().getId());
		assertEquals(firstName, DataBuilderOrder.mockTestReport().getPatient().getFirstName());
		assertEquals(lastName, DataBuilderOrder.mockTestReport().getPatient().getLastName());
		assertEquals(email, DataBuilderOrder.mockTestReport().getPatient().getEmail());
		assertEquals(signedOut, DataBuilderOrder.mockTestReport().isSignedOut());
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		assertEquals(reportType, DataBuilderOrder.mockTestReport().getReportType());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		// when(testReportService.getAllTestReports(paging)).thenReturn(pages);
		when(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		mockMvc.perform(get("/api/labs/reports" + "?page=5&size=5" + "&labid=f450237e-20a3-4bf1-b64d-9ecaab16be7a"
				+ "&interval=1" + "&units=MINUTES" + "&bytype=MANUAL" + "&byreport=NIPS_PLUS" + "&byreporttype=STANDARD"
				+ "&bysignedout=true" + "&byapproved=true" + "&bypatient=1L" + "&byfirstname=Juno_test"
				+ "&bylastname=User" + "&byemail=no-email@junodx.com" + "&bysamplenumber=123455" + "&bybatchrunid=8L"
				+ "&bylaborder=76c97d43-347f-4132-ba18-ddf3b313c226" + "&byordernumber=1234" + "&isavailable=true"
				+ "&upcomingonly=true" + "&sortby=age" + "&sorttype=ASC&" + "condensed=true" + "&byreportable=true&"))
				.andDo(print());
	}

	@Test
	public void testgetfindIntervalSeconds() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean signedOut = true;
		ReportType reportType = ReportType.STANDARD;
		String patientId = "1L";
		String firstName = "Juno_test";
		String lastName = "User";
		String email = "no-email@junodx.com";
		String sampleNumber = "123455";
		String batchRunId = "8L";
		String labOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		String orderNumber = "1234";
		boolean isAvailable = true;
		boolean upcomingOnly = true;
		boolean reportable = true;
		boolean approved = true;
		boolean condensed = true;
		TestReportSortBy sortBy = TestReportSortBy.age;
		SortType sortDirection = SortType.ASC;
		assertEquals(approved, DataBuilderOrder.mockTestReport().isApproved());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		assertEquals(reportable, DataBuilderOrder.mockTestReport().isReportable());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(orderNumber, DataBuilderOrder.mockTestReport().getOrderNumber());
		assertEquals(sampleNumber, DataBuilderOrder.mockTestReport().getSampleNumber());
		assertEquals(batchRunId, DataBuilderOrder.mockTestReport().getBatchRunId());
		assertEquals(labOrderId, DataBuilderOrder.mockTestReport().getLaboratoryOrderId());
		assertEquals(patientId, DataBuilderOrder.mockTestReport().getPatient().getId());
		assertEquals(firstName, DataBuilderOrder.mockTestReport().getPatient().getFirstName());
		assertEquals(lastName, DataBuilderOrder.mockTestReport().getPatient().getLastName());
		assertEquals(email, DataBuilderOrder.mockTestReport().getPatient().getEmail());
		assertEquals(signedOut, DataBuilderOrder.mockTestReport().isSignedOut());
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		assertEquals(reportType, DataBuilderOrder.mockTestReport().getReportType());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		// when(testReportService.getAllTestReports(paging)).thenReturn(pages);
		when(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		mockMvc.perform(get("/api/labs/reports" + "?page=5&size=5" + "&labid=f450237e-20a3-4bf1-b64d-9ecaab16be7a"
				+ "&interval=1" + "&units=SECONDS" + "&bytype=MANUAL" + "&byreport=NIPS_PLUS" + "&byreporttype=STANDARD"
				+ "&bysignedout=true" + "&byapproved=true" + "&bypatient=1L" + "&byfirstname=Juno_test"
				+ "&bylastname=User" + "&byemail=no-email@junodx.com" + "&bysamplenumber=123455" + "&bybatchrunid=8L"
				+ "&bylaborder=76c97d43-347f-4132-ba18-ddf3b313c226" + "&byordernumber=1234" + "&isavailable=true"
				+ "&upcomingonly=true" + "&sortby=age" + "&sorttype=ASC&" + "condensed=true" + "&byreportable=true&"))
				.andDo(print());
	}

	@Test
	public void testgetfindIntervalHours() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean signedOut = true;
		ReportType reportType = ReportType.STANDARD;
		String patientId = "1L";
		String firstName = "Juno_test";
		String lastName = "User";
		String email = "no-email@junodx.com";
		String sampleNumber = "123455";
		String batchRunId = "8L";
		String labOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		String orderNumber = "1234";
		boolean isAvailable = true;
		boolean upcomingOnly = true;
		boolean reportable = true;
		boolean approved = true;
		boolean condensed = true;
		TestReportSortBy sortBy = TestReportSortBy.age;
		SortType sortDirection = SortType.ASC;
		assertEquals(approved, DataBuilderOrder.mockTestReport().isApproved());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		assertEquals(reportable, DataBuilderOrder.mockTestReport().isReportable());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(orderNumber, DataBuilderOrder.mockTestReport().getOrderNumber());
		assertEquals(sampleNumber, DataBuilderOrder.mockTestReport().getSampleNumber());
		assertEquals(batchRunId, DataBuilderOrder.mockTestReport().getBatchRunId());
		assertEquals(labOrderId, DataBuilderOrder.mockTestReport().getLaboratoryOrderId());
		assertEquals(patientId, DataBuilderOrder.mockTestReport().getPatient().getId());
		assertEquals(firstName, DataBuilderOrder.mockTestReport().getPatient().getFirstName());
		assertEquals(lastName, DataBuilderOrder.mockTestReport().getPatient().getLastName());
		assertEquals(email, DataBuilderOrder.mockTestReport().getPatient().getEmail());
		assertEquals(signedOut, DataBuilderOrder.mockTestReport().isSignedOut());
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		assertEquals(reportType, DataBuilderOrder.mockTestReport().getReportType());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		// when(testReportService.getAllTestReports(paging)).thenReturn(pages);
		when(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);

		mockMvc.perform(get("/api/labs/reports" + "?page=5&size=5" + "&labid=f450237e-20a3-4bf1-b64d-9ecaab16be7a"
				+ "&interval=1" + "&units=HOURS" + "&bytype=MANUAL" + "&byreport=NIPS_PLUS" + "&byreporttype=STANDARD"
				+ "&bysignedout=true" + "&byapproved=true" + "&bypatient=1L" + "&byfirstname=Juno_test"
				+ "&bylastname=User" + "&byemail=no-email@junodx.com" + "&bysamplenumber=123455" + "&bybatchrunid=8L"
				+ "&bylaborder=76c97d43-347f-4132-ba18-ddf3b313c226" + "&byordernumber=1234" + "&isavailable=true"
				+ "&upcomingonly=true" + "&sortby=age" + "&sorttype=ASC&" + "condensed=true" + "&byreportable=true&"))
				.andDo(print());
	}

	@Test
	public void testgetfindIntervalDays() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean signedOut = true;
		ReportType reportType = ReportType.STANDARD;
		String patientId = "1L";
		String firstName = "Juno_test";
		String lastName = "User";
		String email = "no-email@junodx.com";
		String sampleNumber = "123455";
		String batchRunId = "8L";
		String labOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		String orderNumber = "1234";
		boolean isAvailable = true;
		boolean upcomingOnly = true;
		boolean reportable = true;
		boolean approved = true;
		boolean condensed = true;
		TestReportSortBy sortBy = TestReportSortBy.age;
		SortType sortDirection = SortType.ASC;
		assertEquals(approved, DataBuilderOrder.mockTestReport().isApproved());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		assertEquals(reportable, DataBuilderOrder.mockTestReport().isReportable());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(orderNumber, DataBuilderOrder.mockTestReport().getOrderNumber());
		assertEquals(sampleNumber, DataBuilderOrder.mockTestReport().getSampleNumber());
		assertEquals(batchRunId, DataBuilderOrder.mockTestReport().getBatchRunId());
		assertEquals(labOrderId, DataBuilderOrder.mockTestReport().getLaboratoryOrderId());
		assertEquals(patientId, DataBuilderOrder.mockTestReport().getPatient().getId());
		assertEquals(firstName, DataBuilderOrder.mockTestReport().getPatient().getFirstName());
		assertEquals(lastName, DataBuilderOrder.mockTestReport().getPatient().getLastName());
		assertEquals(email, DataBuilderOrder.mockTestReport().getPatient().getEmail());
		assertEquals(signedOut, DataBuilderOrder.mockTestReport().isSignedOut());
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		assertEquals(reportType, DataBuilderOrder.mockTestReport().getReportType());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		// when(testReportService.getAllTestReports(paging)).thenReturn(pages);
		when(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		mockMvc.perform(get("/api/labs/reports" + "?page=5&size=5" + "&labid=f450237e-20a3-4bf1-b64d-9ecaab16be7a"
				+ "&interval=1" + "&units=DAYS" + "&bytype=MANUAL" + "&byreport=NIPS_PLUS" + "&byreporttype=STANDARD"
				+ "&bysignedout=true" + "&byapproved=true" + "&bypatient=1L" + "&byfirstname=Juno_test"
				+ "&bylastname=User" + "&byemail=no-email@junodx.com" + "&bysamplenumber=123455" + "&bybatchrunid=8L"
				+ "&bylaborder=76c97d43-347f-4132-ba18-ddf3b313c226" + "&byordernumber=1234" + "&isavailable=true"
				+ "&upcomingonly=true" + "&sortby=age" + "&sorttype=ASC&" + "condensed=true" + "&byreportable=true&"))
				.andDo(print());
	}

	@Test
	public void testgetfindIntervalWeeks() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean signedOut = true;
		ReportType reportType = ReportType.STANDARD;
		String patientId = "1L";
		String firstName = "Juno_test";
		String lastName = "User";
		String email = "no-email@junodx.com";
		String sampleNumber = "123455";
		String batchRunId = "8L";
		String labOrderId = "76c97d43-347f-4132-ba18-ddf3b313c226";
		String orderNumber = "1234";
		boolean isAvailable = true;
		boolean upcomingOnly = true;
		boolean reportable = true;
		boolean approved = true;
		boolean condensed = true;
		TestReportSortBy sortBy = TestReportSortBy.age;
		SortType sortDirection = SortType.ASC;
		assertEquals(approved, DataBuilderOrder.mockTestReport().isApproved());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		assertEquals(reportable, DataBuilderOrder.mockTestReport().isReportable());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(orderNumber, DataBuilderOrder.mockTestReport().getOrderNumber());
		assertEquals(sampleNumber, DataBuilderOrder.mockTestReport().getSampleNumber());
		assertEquals(batchRunId, DataBuilderOrder.mockTestReport().getBatchRunId());
		assertEquals(labOrderId, DataBuilderOrder.mockTestReport().getLaboratoryOrderId());
		assertEquals(patientId, DataBuilderOrder.mockTestReport().getPatient().getId());
		assertEquals(firstName, DataBuilderOrder.mockTestReport().getPatient().getFirstName());
		assertEquals(lastName, DataBuilderOrder.mockTestReport().getPatient().getLastName());
		assertEquals(email, DataBuilderOrder.mockTestReport().getPatient().getEmail());
		assertEquals(signedOut, DataBuilderOrder.mockTestReport().isSignedOut());
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		assertEquals(reportType, DataBuilderOrder.mockTestReport().getReportType());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		// when(testReportService.getAllTestReports(paging)).thenReturn(pages);
		when(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging)).thenReturn(pages);
		mockMvc.perform(get("/api/labs/reports" + "?page=5&size=5" + "&labid=f450237e-20a3-4bf1-b64d-9ecaab16be7a"
				+ "&interval=1" + "&units=WEEKS" + "&bytype=MANUAL" + "&byreport=NIPS_PLUS" + "&byreporttype=STANDARD"
				+ "&bysignedout=true" + "&byapproved=true" + "&bypatient=1L" + "&byfirstname=Juno_test"
				+ "&bylastname=User" + "&byemail=no-email@junodx.com" + "&bysamplenumber=123455" + "&bybatchrunid=8L"
				+ "&bylaborder=76c97d43-347f-4132-ba18-ddf3b313c226" + "&byordernumber=1234" + "&isavailable=true"
				+ "&upcomingonly=true" + "&sortby=age" + "&sorttype=ASC&" + "condensed=true" + "&byreportable=true&"))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testfindException() throws Exception {
		Pageable paging = PageRequest.of(5, 5);
		List<OrderBatchDto> transitionOrders = new ArrayList<>();
		transitionOrders.add(DataBuilderOrder.MockOrderBatchDto());
		List<TestReport> testReports = new ArrayList<>();
		testReports.add(DataBuilderOrder.mockTestReport());
		final Page<TestReport> pages = new PageImpl<>(testReports, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxPageSize", 20);
		when(testReportService.getAllTestReports(paging)).thenReturn(pages);
		mockMvc.perform(get("/api/labs/reports")).andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	public void testReportActionsApprovetest() throws Exception {
		DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		when(testReportService.approveTestReports(Mockito.any(), Mockito.anyString(), Mockito.any()))
				.thenReturn(DataBuilderOrder.testReports);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequest());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	} 

	@Test
	public void testReportActionsCatchException() throws Exception {
		DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
//		when(testReportService.approveTestReports(Mockito.any(), Mockito.anyString(), Mockito.any()))
//				.thenReturn(DataBuilderOrder.testReports);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequest());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(testReportService).approveTestReports(Mockito.any(),
				Mockito.anyString(), Mockito.any());
 
		this.mockMvc.perform(post("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testReportActionsException() throws Exception {
		List<TestReport> testReportEmptys = new ArrayList<>();
		DataBuilderOrder.testReportEmpty.add(DataBuilderOrder.mockTestReportEmpty());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
//		when(testReportService.approveTestReports(Mockito.anyList(), Mockito.anyString(), Mockito.any()))
//				.thenReturn(DataBuilderOrder.testReportEmpty);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequest());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testReportActionstestSignOut() throws Exception {
		DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		when(testReportService.signOutTestReports(Mockito.any(), Mockito.anyString(), Mockito.any()))
				.thenReturn(DataBuilderOrder.testReports);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestSignout());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testReportActionsCreatetest() throws Exception {
		DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		when(testReportService.createTestReports(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.testReports);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestss());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testReportActionsCreatetestException() throws Exception {
		DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		when(testReportService.createTestReports(Mockito.anyString(), Mockito.any(), Mockito.any()))
				.thenReturn(DataBuilderOrder.testReports);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequesttPipe());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testReportActionsCreatetestReportAException() throws Exception {
		// DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		when(testReportService.createTestReports(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(null);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestCreateAction());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testReportActionsretest() throws Exception {
		DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		when(testReportService.requestReTestForTestReports(DataBuilderOrder.testReports, RetestActionType.REDRAW, true,
				"notes", "12345", DataBuilder.getMockUserDetailsImpl())).thenReturn(DataBuilderOrder.testReports);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestsss());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testReportActionsretestException() throws Exception {
		DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		when(testReportService.requestReTestForTestReports(DataBuilderOrder.testReports, RetestActionType.REDRAW, true,
				"notes", "12345", DataBuilder.getMockUserDetailsImpl())).thenReturn(DataBuilderOrder.testReports);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestRetestException());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdateApprove() throws Exception {
		DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		when(testReportService.approveTestReports(Mockito.any(), Mockito.anyString(), Mockito.any()))
				.thenReturn(DataBuilderOrder.testReports);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequest());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testupdateApproveException() throws Exception {
		DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		when(testReportService.approveTestReports(Mockito.any(), Mockito.anyString(), Mockito.any())).thenReturn(null);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequest());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdateApproveExceptions() throws Exception {
		// DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		// ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize",
		// 20);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper
				.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestWithoutTestReportAction());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testReportActionsupdateApproveExceptions() throws Exception {
		// DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		// ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize",
		// 20);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestWithoutTestReport());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testReportActionsRetestAException() throws Exception {
		DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		when(testReportService.requestReTestForTestReports(DataBuilderOrder.testReports, RetestActionType.REDRAW, true,
				"notes", "12345", DataBuilder.getMockUserDetailsImpl())).thenReturn(DataBuilderOrder.testReports);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestRetestException());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testReportActionsRetestUserIdException() throws Exception {
		DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		when(testReportService.requestReTestForTestReports(DataBuilderOrder.testReports, RetestActionType.REDRAW, true,
				"notes", "12345", DataBuilder.getMockUserDetailsImpl())).thenReturn(DataBuilderOrder.testReports);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestRetestExceptions());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdateSignout() throws Exception {
		DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		when(testReportService.signOutTestReports(Mockito.any(), Mockito.anyString(), Mockito.any()))
				.thenReturn(DataBuilderOrder.testReports);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestSignout());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testupdateRestest() throws Exception {
		DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		when(testReportService.requestReTestForTestReports(DataBuilderOrder.testReports, RetestActionType.REDRAW, true,
				"notes", "12345", DataBuilder.getMockUserDetailsImpl())).thenReturn(DataBuilderOrder.testReports);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestsss());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testReportActionsReportsPageSizeActionExceptions() throws Exception {
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestWithoutTestReports());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testgetTestReportResults() throws Exception {
		this.mockMvc.perform(get("/api/labs/reports/results").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetTestReportResultslabId() throws Exception {
		this.mockMvc.perform(get("/api/labs/reports/results" + "?labId=f450237e-20a3-4bf1-b64d-9ecaab16be7a")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}


	@Test
	public void testgetAwaitingReports() throws Exception {
		this.mockMvc.perform(get("/api/labs/reports/results/awaiting").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetAwaitingReportsLabId() throws Exception {
		this.mockMvc.perform(get("/api/labs/reports/results/awaiting" + "?labId=f450237e-20a3-4bf1-b64d-9ecaab16be7a")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetAwaitingReportsTypes() throws Exception {
		this.mockMvc.perform(get("/api/labs/reports/results/awaiting" + "?reportType=STANDARD")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testdelete() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		verify(testReportService, times(0)).delete("2L", DataBuilderOrder.userDetailsImpl);
		String reportId = "2L";
		assertEquals(reportId, DataBuilderOrder.mockTestReport().getId());
		this.mockMvc.perform(delete("/api/labs/reports/{reportId}" + "?reportId=2L", reportId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testdeleteException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		// verify(testReportService, times(0)).delete(null,
		// DataBuilderOrder.userDetailsImpl);
		String reportId = "2L";
		doThrow(new NoSuchElementException()).when(testReportService).delete(Mockito.anyString(), Mockito.any());
		// assertEquals(reportId, DataBuilderOrder.mockTestReport().getId());
		this.mockMvc.perform(delete("/api/labs/reports/{reportId}", reportId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdateViewed() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		String reportId = "2L";
		String patientId = "1L";
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestss());
		System.out.println(inputJson);
		this.mockMvc
				.perform(post("/api/labs/reports/{reportId}/viewed" + "?reportId=2L&patientId=1L", reportId, patientId)
						.content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testupdateViewedException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		String reportId = "2L";
		String patientId = "1L";
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestss());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(testReportService)
				.updateTestReportPatientViewedResult(Mockito.anyString(), Mockito.anyString(), Mockito.any());
		this.mockMvc
				.perform(post("/api/labs/reports/{reportId}/viewed" + "?reportId=2L&patientId=5L", reportId, patientId)
						.content(inputJson).contentType(MediaType.APPLICATION_JSON))
			 	.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testReportActionsApprovetestException() throws Exception {
		DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		when(testReportService.approveTestReports(Mockito.any(), Mockito.anyString(), Mockito.any())).thenReturn(null);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequest());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testReportActionsReportsExceptions() throws Exception {
		// DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		// ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize",
		// 20);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestWithoutTestReport());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testReportActionsReportsPageSizeExceptions() throws Exception {
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		DataBuilderOrder.testReportss.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize", 20);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestWithoutTestReports());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	} 

	@Test
	public void testReportActionsReportGetActionExceptions() throws Exception {
		// DataBuilderOrder.testReports.add(DataBuilderOrder.mockTestReport());
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		// ReflectionTestUtils.setField(testReportController, "maxTestReportPostSize",
		// 20);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper
				.writeValueAsString(DataBuilderOrder.mockTestReportUpdateRequestWithoutTestReportAction());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/labs/reports").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}
}
