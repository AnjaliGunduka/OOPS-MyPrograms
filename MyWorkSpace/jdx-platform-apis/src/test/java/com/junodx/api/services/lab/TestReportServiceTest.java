package com.junodx.api.services.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.NonUniqueResultException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.SortType;
import com.junodx.api.controllers.lab.types.TestReportSortBy;
import com.junodx.api.dto.mappers.LaboratoryMapStructMapper;
import com.junodx.api.dto.models.laboratory.TestReportsReviewResultsDto;
import com.junodx.api.models.auth.Authority;
import com.junodx.api.models.commerce.DataBuilderReport;
import com.junodx.api.models.commerce.DataBuilderCheckOut;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.commerce.DataBuilderorderExample;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.commerce.Product;
import com.junodx.api.models.commerce.types.OrderStatusType;
import com.junodx.api.models.core.types.IntervalType;
import com.junodx.api.models.laboratory.BatchRun;
import com.junodx.api.models.laboratory.TestReport;
import com.junodx.api.models.laboratory.TestRun;
import com.junodx.api.models.laboratory.reports.types.SignedOutType;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.laboratory.types.ReportType;
import com.junodx.api.models.laboratory.types.RetestActionType;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.repositories.commerce.OrderLineItemRepository;
import com.junodx.api.repositories.commerce.OrderRepository;
import com.junodx.api.repositories.lab.KitRepository;
import com.junodx.api.repositories.lab.TestReportRepository;
import com.junodx.api.repositories.lab.TestRunRepository;
import com.junodx.api.services.auth.UserService;
import com.junodx.api.services.commerce.OrderService;
import com.junodx.api.services.exceptions.JdxServiceException;
import com.junodx.api.services.lab.BatchRunService;
import com.junodx.api.services.lab.LaboratoryOrderService;
import com.junodx.api.services.lab.LaboratoryService;
import com.junodx.api.services.lab.TestReportService;
import com.junodx.api.services.lab.TestRunService;
import com.junodx.api.services.mail.MailService;

@ExtendWith(MockitoExtension.class)
 
public class TestReportServiceTest {

	@Mock 
	private TestReportRepository testReportRepository;

	@Mock
	private LaboratoryOrderService laboratoryOrderService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private UserService userService;

	@Mock
	private OrderService orderService;

	@Mock
	private TestRunRepository testRunRepository;

	@Mock
	private TestRunService testRunService;

	@Mock
	private KitRepository kitRepository;

	@Mock
	private LaboratoryService laboratoryService;

	@Mock
	private MailService mailService;

	@Mock
	private OrderLineItemRepository orderLineItemRepository;

	@Mock
	private BatchRunService batchRunService;
	@Mock
	private LaboratoryMapStructMapper laboratoryMapStructMapper;
	@Mock
	private ObjectMapper mapper;
	@InjectMocks
	private TestReportService testReportService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testgetTestReport() {
		String[] includes = { "anjali", "gunduka" };
		String id = "2L";
		assertEquals(id, DataBuilderOrder.mockTestReport().getId());
		when(testReportRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestReport()));
		assertEquals(testReportService.getTestReport(id, includes, DataBuilder.getMockUserDetailsImpl()).getClass(),
				Optional.of(DataBuilderOrder.mockTestReport()).getClass());
	}

	@Test
	void testgetTestReportEmpty() {
		String[] includes = { "anjali", "gunduka" };
		testReportService.getTestReport(null, includes, DataBuilder.getMockUserDetailsImpl());
	}

	@Test
	void testgetAllTestReports() {
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
		when(testReportRepository.findAll(paging)).thenReturn(pages);
		assertEquals(testReportService.getAllTestReports(paging), pages);
	}

	@Test
	void testgetAllTestReportsAnother() {
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
		// when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(testReportRepository.findTestReportsByLabId(labId, paging)).thenReturn(pages);
		assertEquals(testReportService.getAllTestReports(Optional.of(labId), paging), pages);
	}

	@Test
	void testgetAllTestReportsAnotherDefault() {
		List<TestReport> testReport = new ArrayList<>();
		// testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 1);
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		assertEquals(testReportService.getAllTestReports(Optional.empty(), paging), null);
	}

	@Test
	public void testgetAllTestReportsException() throws JdxServiceException {
		Pageable paging = PageRequest.of(0, 2);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.getAllTestReports(Optional.empty(), paging));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testgetAllTestReportsByPatientId() {
		String id = "1L";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean isAvailable = true;
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
		assertEquals(id, DataBuilderOrder.mockTestReport().getPatient().getId());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		when(testReportRepository.findTestReportsByPatient_IdAndSignedOutTypeAndReportConfigurationAndAvailable(id,
				type, config, isAvailable, paging)).thenReturn(pages);
		assertEquals(testReportService.getAllTestReportsByPatientId(id, type, config, isAvailable, paging), pages);
	}

	@Test
	void testgetAllTestReportsByLabOrderId() {
		String id = "76c97d43-347f-4132-ba18-ddf3b313c226";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean isAvailable = true;
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
		assertEquals(id, DataBuilderOrder.mockTestReport().getLaboratoryOrderId());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		when(testReportRepository.findTestReportsByLaboratoryOrderIdAndSignedOutTypeAndReportConfigurationAndAvailable(
				id, type, config, isAvailable, paging)).thenReturn(pages);
		assertEquals(testReportService.getAllTestReportsByLabOrderId(id, type, config, isAvailable, paging), pages);
	}

	@Test
	void testgetAllTestReportsBySampleNumber() {
		String sampleNumber = "123455";
		SignedOutType type = SignedOutType.MANUAL;
		ReportConfiguration config = ReportConfiguration.NIPS_PLUS;
		boolean isAvailable = true;
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
		assertEquals(sampleNumber, DataBuilderOrder.mockTestReport().getSampleNumber());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		assertEquals(config, DataBuilderOrder.mockTestReport().getReportConfiguration());
		assertEquals(isAvailable, DataBuilderOrder.mockTestReport().isAvailable());
		when(testReportRepository.findTestReportsBySampleNumberAndSignedOutTypeAndReportConfigurationAndAvailable(
				"67898", type, config, isAvailable, paging)).thenReturn(pages);
		assertEquals(testReportService.getAllTestReportsBySampleNumber("67898", type, config, isAvailable, paging),
				pages);
	}

	@Test
	void testgetAllTestReportsBySignout() {
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		SignedOutType type = SignedOutType.MANUAL;
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		when(testReportRepository.findTestReportsBySignedOutType(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.anyBoolean(), Mockito.any())).thenReturn(pages);
		assertEquals(testReportService.getAllTestReportsBySignout(Optional.of(labId), type, 1, IntervalType.HOURS, true,
				paging), pages);
	}

	@Test
	void testgetAllTestReportsBySignoutDefault() {
		String labId = "f450237e-20a3-4bf1-b64d-9ecaab16be7a";
		SignedOutType type = SignedOutType.MANUAL;
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
		assertEquals(labId, DataBuilderOrder.mockLaboratory().getId());
		assertEquals(type, DataBuilderOrder.mockTestReport().getSignedOutType());
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(testReportRepository.findTestReportsBySignedOutType(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.anyBoolean(), Mockito.any())).thenReturn(pages);
		assertEquals(testReportService.getAllTestReportsBySignout(Optional.empty(), type, 1, IntervalType.HOURS, true,
				paging), pages);
	}

	@Test
	void testgetAllTestReportsBySignoutFForIntervalDays() {
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
		when(testReportRepository.findTestReportsBySignedOutType(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.anyBoolean(), Mockito.any())).thenReturn(pages);
		assertEquals(testReportService.getAllTestReportsBySignout(Optional.of("f450237e-20a3-4bf1-b64d-9ecaab16be7a"),
				SignedOutType.AUTOMATIC, 1, IntervalType.DAYS, true, paging), pages);
	}

	@Test
	void testgetAllTestReportsBySignoutFForIntervalWeeks() {
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
		when(testReportRepository.findTestReportsBySignedOutType(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.anyBoolean(), Mockito.any())).thenReturn(pages);
		assertEquals(testReportService.getAllTestReportsBySignout(Optional.of("f450237e-20a3-4bf1-b64d-9ecaab16be7a"),
				SignedOutType.AUTOMATIC, 1, IntervalType.WEEKS, true, paging), pages);
	}

	@Test
	void testgetAllTestReportsBySignoutFForIntervalMinutes() {
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
		when(testReportRepository.findTestReportsBySignedOutType(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.anyBoolean(), Mockito.any())).thenReturn(pages);
		assertEquals(testReportService.getAllTestReportsBySignout(Optional.of("f450237e-20a3-4bf1-b64d-9ecaab16be7a"),
				SignedOutType.AUTOMATIC, 1, IntervalType.MINUTES, true, paging), pages);
	}

	@Test
	public void testgetAllTestReportsBySignoutException() throws JdxServiceException {
		Pageable paging = PageRequest.of(0, 2);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.getAllTestReportsBySignout(Optional.empty(), SignedOutType.AUTOMATIC, 1,
						IntervalType.HOURS, true, paging));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testfindAllAvailableTestReportsByPatient() {
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
		when(testReportRepository.findTestReportsByPatient_IdAndSignedOut("6L", true, paging)).thenReturn(pages);
		assertEquals(testReportService.findAllAvailableTestReportsByPatient("6L", paging), pages);
	}

	@Test
	public void testfindAllAvailableTestReportsByPatientException() throws JdxServiceException {
		Pageable paging = PageRequest.of(0, 2);
		when(testReportRepository.findTestReportsByPatient_IdAndSignedOut("6L", true, paging))
				.thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.findAllAvailableTestReportsByPatient("6L", paging));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testsearchByAge() {
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
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
		when(testReportRepository.searchOrderByCompletedAtASC(Mockito.any(), Mockito.anyBoolean(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean(), Mockito.any(), Mockito.any(), Mockito.anyBoolean(), Mockito.any(), Mockito.any()))
				.thenReturn(pages); 
		assertEquals(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging), pages);
	}

	@Test
	void testsearchByAgeDesc() {
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
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
		SortType sortDirection = SortType.DESC;
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
		when(testReportRepository.searchOrderByCompletedAtDSC(Mockito.any(), Mockito.anyBoolean(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any(), Mockito.anyBoolean(), Mockito.any(),
				Mockito.any())).thenReturn(pages);
		assertEquals(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging), pages);
	}

	@Test
	void testsearchestimatedToBeAvailableAt() {
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
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
		TestReportSortBy sortBy = TestReportSortBy.estimatedToBeAvailableAt;
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
		when(testReportRepository.searchOrderByEstimatedToBeAvailableAtASC(Mockito.any(), Mockito.anyBoolean(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any(), Mockito.any(), Mockito.anyBoolean(),
				Mockito.any(), Mockito.any())).thenReturn(pages);
		assertEquals(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging), pages);
	}

	@Test
	void testsearchestimatedToBeAvailableAtDes() {
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
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
		TestReportSortBy sortBy = TestReportSortBy.estimatedToBeAvailableAt;
		SortType sortDirection = SortType.DESC;
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
		when(testReportRepository.searchOrderByEstimatedToBeAvailableAtDSC(Mockito.any(), Mockito.anyBoolean(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
	 			Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any(), Mockito.any(), Mockito.anyBoolean(),
				Mockito.any(), Mockito.any())).thenReturn(pages);
		assertEquals(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging), pages);
	}

	@Test
	void testsearchfirstAvailableAt() {
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
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
		TestReportSortBy sortBy = TestReportSortBy.firstAvailableAt;
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
		when(testReportRepository.searchOrderByFirstAvailableAtASC(Mockito.any(), Mockito.anyBoolean(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any(), Mockito.any(), Mockito.anyBoolean(),
				Mockito.any(), Mockito.any())).thenReturn(pages);
		assertEquals(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging), pages);
	}

	@Test
	void testsearchfirstAvailableAtDes() {
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
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
		TestReportSortBy sortBy = TestReportSortBy.firstAvailableAt;
		SortType sortDirection = SortType.DESC;
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
		when(testReportRepository.searchOrderByFirstAvailableAtDSC(Mockito.any(), Mockito.anyBoolean(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any(), Mockito.any(), Mockito.anyBoolean(),
				Mockito.any(), Mockito.any())).thenReturn(pages);
		assertEquals(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.of(sortBy), Optional.of(sortDirection),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging), pages);
	}

	@Test
	void testsearchdefault() {
		List<TestReport> testReport = new ArrayList<>();
		testReport.add(DataBuilderOrder.mockTestReport());
		Pageable paging = PageRequest.of(0, 2);
		final Page<TestReport> pages = new PageImpl<>(testReport, paging, 5);
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
//		TestReportSortBy sortBy = TestReportSortBy.estimatedToBeAvailableAt;
//		SortType sortDirection = SortType.ASC;
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
		when(testReportRepository.searchOrderByCompletedAtDSC(Mockito.any(), Mockito.anyBoolean(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any(), Mockito.anyBoolean(), Mockito.any(),
				Mockito.any())).thenReturn(pages);
		assertEquals(testReportService.search(Optional.of(labId), Optional.of(type), Optional.of(config),
				Optional.of(reportType), Optional.of(signedOut), Optional.of(patientId), Optional.of(firstName),
				Optional.of(lastName), Optional.of(email), Optional.of(sampleNumber), Optional.of(batchRunId),
				Optional.of(labOrderId), Optional.of(orderNumber), Optional.of(isAvailable), Optional.of(upcomingOnly),
				Optional.of(reportable), Optional.of(approved), Optional.empty(), Optional.empty(),
				Optional.of(condensed), Optional.of(Calendar.getInstance()), paging), pages);
	}

	@Test
	void testsignOut() {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilderReport.mockUser().setAuthorities(authority);
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderReport.mockTestReports());
		List<Order> ordersToSave = new ArrayList<>();
		ordersToSave.add(DataBuilderReport.mockOrders());
		DataBuilderReport.lineItems.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderReport.testRuns.add(DataBuilderReport.mockTestRuns());
		when(userService.findOne("1L")).thenReturn(Optional.of(DataBuilderReport.mockUser()));
		when(testReportRepository.findAllById(Mockito.anyList())).thenReturn(testReport);
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderReport.mockTestRuns()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRun(Mockito.any()))
				.thenReturn(Optional.of(DataBuilderReport.mockLaboratoryOrders()));
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderReport.mockOrders()));
		when(testReportRepository.saveAll(testReport)).thenReturn(testReport);
		when(testRunRepository.saveAll(Mockito.anyList())).thenReturn(DataBuilderReport.testRuns);
		when(orderRepository.saveAll(Mockito.anyList())).thenReturn(ordersToSave);
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		testReportService.sendTestReportAvaialbleEmail(DataBuilderReport.mockOrders());
		assertEquals(testReportService.signOutTestReports(testReport, "1L", DataBuilderReport.userDetailsImpl),
				testReport);

	}

	@Test
	public void testsignOutTestReportsUserException() throws JdxServiceException {
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		when(userService.findOne("1L")).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.signOutTestReports(testReport, "1L", DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsignOutTestReportsTestException() throws JdxServiceException {
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		List<TestReport> testReports = new LinkedList<>();
		testReports.add(DataBuilderOrder.mockTestReports());
		testReports.add(DataBuilderOrder.mockTestReports());
		when(userService.findOne("1L")).thenReturn(Optional.of(DataBuilderOrder.mockUser()));
		when(testReportRepository.findAllById(Mockito.anyList())).thenReturn(testReports);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.signOutTestReports(testReport, "1L", DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsignOutTestReportsTestReportableTestReportIdException() throws JdxServiceException {
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		DataBuilderReport.testRuns.add(DataBuilderReport.mockTestRuns());
		when(userService.findOne("1L")).thenReturn(Optional.of(DataBuilderReport.mockUser()));
		when(testReportRepository.findAllById(Mockito.anyList())).thenReturn(testReport);
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderReport.mockTestRuns()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRun(Mockito.any()))
				.thenReturn(Optional.of(DataBuilderReport.mockLaboratoryOrdersign()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.signOutTestReports(testReport, "1L", DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsignOutTestReportsLabException() throws JdxServiceException {
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		when(userService.findOne("1L")).thenReturn(Optional.of(DataBuilderOrder.mockUser()));
		when(testReportRepository.findAllById(Mockito.anyList())).thenReturn(testReport);
		when(testRunService.getTestRunForKitId(DataBuilderOrder.mockTestReports().getTestRun().getKit().getId()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRun(Mockito.any())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.signOutTestReports(testReport, "1L", DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsignOutTestReportsLabReportsException() throws JdxServiceException {
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		DataBuilderOrder.mockLaboratoryOrder().setReportableTestReportId(null);
		when(userService.findOne("1L")).thenReturn(Optional.of(DataBuilderOrder.mockUser()));
		when(testReportRepository.findAllById(Mockito.anyList())).thenReturn(testReport);
		when(testRunService.getTestRunForKitId(DataBuilderOrder.mockTestReports().getTestRun().getKit().getId()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRun(Mockito.any()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.signOutTestReports(testReport, "1L", DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsendTestReportAvaialbleEmail() {
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderOrder.mockOrder().setLineItems(DataBuilderOrder.lineItems);
		when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(true);
		testReportService.sendTestReportAvaialbleEmail(DataBuilderOrder.mockOrder());
	}

	@Test
	void testrequestReTestForTestReports() {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilderOrder.mockUser().setAuthorities(authority);
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		List<TestRun> runsToSave = new ArrayList<>();
		runsToSave.add(DataBuilderOrder.mockTestRun());
		List<Order> ordersToSave = new ArrayList<>();
		ordersToSave.add(DataBuilderOrder.mockOrder());
		List<BatchRun> batchesUpdated = new ArrayList<>();
		batchesUpdated.add(DataBuilderOrder.mockBatchRun());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRun());
		when(userService.findOne("1L")).thenReturn(Optional.of(DataBuilderOrder.mockUser()));
		when(testReportRepository.findAllById(Mockito.anyList())).thenReturn(testReport);
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(testReportRepository.saveAll(testReport)).thenReturn(testReport);
		when(testRunRepository.saveAll(Mockito.anyList())).thenReturn(runsToSave);
		// when(orderRepository.saveAll(Mockito.anyList())).thenReturn(ordersToSave);
		when(testReportRepository.countOfUnapprovedResultsInBatch(Mockito.anyString())).thenReturn(0L);
		when(batchRunService.updateAll(Mockito.any(), Mockito.any())).thenReturn(batchesUpdated);
		assertEquals(testReportService.requestReTestForTestReports(testReport, RetestActionType.REFLEX, true, "notes",
				"1L", DataBuilderOrder.userDetailsImpl), testReport);
	}

	@Test
	void testrequestReTestForTestReportBatchElse() {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilderOrder.mockUser().setAuthorities(authority);
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		List<TestRun> runsToSave = new ArrayList<>();
		runsToSave.add(DataBuilderOrder.mockTestRun());
		List<Order> ordersToSave = new ArrayList<>();
		ordersToSave.add(DataBuilderOrder.mockOrder());
		List<BatchRun> batchesUpdated = new ArrayList<>();
		batchesUpdated.add(DataBuilderOrder.mockBatchRun());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRun());
		when(userService.findOne("1L")).thenReturn(Optional.of(DataBuilderOrder.mockUser()));
		when(testReportRepository.findAllById(Mockito.anyList())).thenReturn(testReport);
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(testReportRepository.saveAll(testReport)).thenReturn(testReport);
		when(testRunRepository.saveAll(Mockito.anyList())).thenReturn(runsToSave);
		// when(orderRepository.saveAll(Mockito.anyList())).thenReturn(ordersToSave);
		when(testReportRepository.countOfUnapprovedResultsInBatch(Mockito.anyString())).thenReturn(1L);

		assertEquals(testReportService.requestReTestForTestReports(testReport, RetestActionType.REFLEX, true, "notes",
				"1L", DataBuilderOrder.userDetailsImpl), testReport);
	}

	@Test
	public void testrequestReTestForTestReportsException() throws JdxServiceException {
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		when(userService.findOne("1L")).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.requestReTestForTestReports(testReport, RetestActionType.REFLEX, true, "notes",
						"1L", DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testrequestReTestForTestReportsTestOrder() throws JdxServiceException {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilderOrder.mockUser().setAuthorities(authority);
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		List<TestRun> runsToSave = new ArrayList<>();
		runsToSave.add(DataBuilderOrder.mockTestRun());
		List<Order> ordersToSave = new ArrayList<>();
		ordersToSave.add(DataBuilderOrder.mockOrder());
		ordersToSave.add(DataBuilderOrder.mockOrder());
		ordersToSave.add(DataBuilderOrder.mockOrder());
		List<BatchRun> batchesUpdated = new ArrayList<>();
		batchesUpdated.add(DataBuilderOrder.mockBatchRun());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRun());
		when(userService.findOne("1L")).thenReturn(Optional.of(DataBuilderOrder.mockUser()));
		when(testReportRepository.findAllById(Mockito.anyList())).thenReturn(testReport);
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(testReportRepository.saveAll(testReport)).thenReturn(testReport);
		when(testRunRepository.saveAll(Mockito.anyList())).thenReturn(runsToSave);
		// when(orderRepository.saveAll(Mockito.anyList())).thenReturn(ordersToSave);
		when(testReportRepository.countOfUnapprovedResultsInBatch(Mockito.anyString())).thenReturn(0L);
		when(batchRunService.updateAll(Mockito.any(), Mockito.any())).thenReturn(batchesUpdated);
		assertEquals(testReportService.requestReTestForTestReports(testReport, RetestActionType.REFLEX, true, "notes",
				"1L", DataBuilderOrder.userDetailsImpl), testReport);
	}

	@Test
	public void testrequestReTestForTestReportsTestException() throws JdxServiceException {
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		List<TestReport> testReports = new LinkedList<>();
		testReports.add(DataBuilderOrder.mockTestReports());
		testReports.add(DataBuilderOrder.mockTestReports());
		when(userService.findOne("1L")).thenReturn(Optional.of(DataBuilderOrder.mockUser()));
		when(testReportRepository.findAllById(Mockito.anyList())).thenReturn(testReports);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.requestReTestForTestReports(testReport, RetestActionType.REFLEX, true, "notes",
						"1L", DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	void testapproveTestReports() {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilderOrder.mockUser().setAuthorities(authority);
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		List<TestRun> runsToSave = new ArrayList<>();
		runsToSave.add(DataBuilderOrder.mockTestRun());
		List<Order> ordersToSave = new ArrayList<>();
		ordersToSave.add(DataBuilderOrder.mockOrder());
		List<BatchRun> batchesUpdated = new ArrayList<>();
		batchesUpdated.add(DataBuilderOrder.mockBatchRun());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRun());
		when(userService.findOne("1L")).thenReturn(Optional.of(DataBuilderOrder.mockUser()));
		when(testReportRepository.findAllById(Mockito.anyList())).thenReturn(testReport);
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(testReportRepository.saveAll(testReport)).thenReturn(testReport);
		when(testRunRepository.saveAll(Mockito.anyList())).thenReturn(runsToSave);
		when(testReportRepository.countOfUnapprovedResultsInBatch(Mockito.anyString())).thenReturn(0L);
		when(batchRunService.updateAll(Mockito.any(), Mockito.any())).thenReturn(batchesUpdated);
		assertEquals(testReportService.approveTestReports(testReport, "1L", DataBuilderOrder.userDetailsImpl),
				testReport);
	}

	@Test
	void testapproveTestReportsFullyReviewed() {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilderOrder.mockUser().setAuthorities(authority);
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		List<TestRun> runsToSave = new ArrayList<>();
		runsToSave.add(DataBuilderOrder.mockTestRun());
		List<Order> ordersToSave = new ArrayList<>();
		ordersToSave.add(DataBuilderOrder.mockOrder());
		List<BatchRun> batchesUpdated = new ArrayList<>();
		batchesUpdated.add(DataBuilderOrder.mockBatchRun());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRun());
		when(userService.findOne("1L")).thenReturn(Optional.of(DataBuilderOrder.mockUser()));
		when(testReportRepository.findAllById(Mockito.anyList())).thenReturn(testReport);
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRun()));
		when(testReportRepository.saveAll(testReport)).thenReturn(testReport);
		when(testRunRepository.saveAll(Mockito.anyList())).thenReturn(runsToSave);
		when(testReportRepository.countOfUnapprovedResultsInBatch(Mockito.anyString())).thenReturn(1L);
		// when(batchRunService.updateAll(Mockito.any(),
		// Mockito.any())).thenReturn(batchesUpdated);
		assertEquals(testReportService.approveTestReports(testReport, "1L", DataBuilderOrder.userDetailsImpl),
				testReport);
	}

	@Test
	public void testapproveTestReportsException() throws JdxServiceException {
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		when(userService.findOne("1L")).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.approveTestReports(testReport, "1L", DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testapproveTestReportsTestException() throws JdxServiceException {
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		List<TestReport> testReports = new LinkedList<>();
		testReports.add(DataBuilderOrder.mockTestReports());
		testReports.add(DataBuilderOrder.mockTestReports());
		when(userService.findOne("1L")).thenReturn(Optional.of(DataBuilderOrder.mockUser()));
		when(testReportRepository.findAllById(Mockito.anyList())).thenReturn(testReports);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.approveTestReports(testReport, "1L", DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

//	@Test
//	void testcreateTestReport() {
//		List<TestReport> testReport = new LinkedList<>();
//		testReport.add(DataBuilderReport.mockTestReport());
//		DataBuilderReport.fulfillments.add(DataBuilderReport.mockFulfillments());
//		DataBuilderReport.lineItems.add(DataBuilderReport.mockOrderLineItemss());
//		// DataBuilderCreate.mockOrderLineItems().setOrder(DataBuilderCreate.mockOrder());
//		DataBuilderReport.fulfillments.add(DataBuilderReport.mockFulfillments());
//		DataBuilderReport.testRuns.add(DataBuilderReport.mockTestRuns());
//		DataBuilderReport.testRuns.add(DataBuilderReport.mockTestRuns());
//		testReportService.validateNIPSReport(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
//		testReportService.validateResultData(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
//		when(batchRunService.findByPipelineRunId(Mockito.anyString()))
//				.thenReturn(Optional.of(DataBuilderReport.mockBatchRun()));
//		when(kitRepository.findKitBySampleNumber(Mockito.anyString()))
//				.thenReturn(Optional.of(DataBuilderReport.mockKitss()));
//		when(testRunService.getTestRunForKitId(Mockito.anyString()))
//				.thenReturn(Optional.of(DataBuilderReport.mockTestRuns()));
//		when(laboratoryOrderService.findLaboratoryOrderByTestRun(Mockito.any()))
//				.thenReturn(Optional.of(DataBuilderReport.mockLaboratoryOrders()));
//
//		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderReport.mockOrders()));
//
//		when(testReportRepository.save(Mockito.any())).thenReturn(DataBuilderReport.mockTestReport());
//		when(testReportService.finalizeAndSaveReport(DataBuilderReport.mockTestReport(),
//				DataBuilderReport.mockBatchRun(), DataBuilderReport.mockTestRuns(), DataBuilderReport.mockKitss(),
//				DataBuilderReport.mockLaboratory(), Optional.of(DataBuilderReport.mockUser()),
//				Optional.of(DataBuilderReport.mockLaboratoryOrders()), Optional.of(DataBuilderReport.mockOrders()),
//				DataBuilderReport.userDetailsImpl, false, false)).thenReturn(DataBuilderOrder.mockTestReports());
//
//		when(testReportService.saveSingleTestReportWithOrder(DataBuilderReport.mockBatchRun(),
//				DataBuilderReport.mockTestRuns(), DataBuilderReport.mockKitss(), DataBuilderReport.mockTestReport(),
//				Optional.of(DataBuilderReport.mockLaboratoryOrders()), DataBuilderReport.userDetailsImpl))
//				.thenReturn(DataBuilderOrder.mockTestReports());
//
//		assertEquals(testReportService.createTestReport("12345", DataBuilderReport.mockTestReport(),
//				DataBuilderReport.userDetailsImpl), DataBuilderOrder.mockTestReports());
//
//	}
	@Test
	void testcreateTestReport() {
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderReport.mockTestReport());
		DataBuilderReport.fulfillments.add(DataBuilderReport.mockFulfillments());
		DataBuilderReport.lineItems.add(DataBuilderReport.mockOrderLineItemss());
		// DataBuilderCreate.mockOrderLineItems().setOrder(DataBuilderCreate.mockOrder());
		DataBuilderReport.fulfillments.add(DataBuilderReport.mockFulfillments());
		DataBuilderReport.testRuns.add(DataBuilderReport.mockTestRuns());
		DataBuilderReport.testRuns.add(DataBuilderReport.mockTestRuns());
		testReportService.validateNIPSReport(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		testReportService.validateResultData(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		when(batchRunService.findByPipelineRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderReport.mockBatchRun()));
		when(kitRepository.findKitBySampleNumber(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderReport.mockKitss()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderReport.mockTestRuns()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRun(Mockito.any()))
				.thenReturn(Optional.of(DataBuilderReport.mockLaboratoryOrders()));

		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilderReport.mockOrders()));

		when(testReportRepository.save(Mockito.any())).thenReturn(DataBuilderReport.mockTestReport());
		when(testReportService.finalizeAndSaveReport(DataBuilderReport.mockTestReport(),
				DataBuilderReport.mockBatchRun(), DataBuilderReport.mockTestRuns(), DataBuilderReport.mockKitss(),
				DataBuilderReport.mockLaboratory(), Optional.of(DataBuilderReport.mockUser()),
				Optional.of(DataBuilderReport.mockLaboratoryOrders()), Optional.of(DataBuilderReport.mockOrders()),
				DataBuilderReport.userDetailsImpl, false, false)).thenReturn(DataBuilderOrder.mockTestReports());

		when(testReportService.saveSingleTestReportWithOrder(DataBuilderReport.mockBatchRun(),
				DataBuilderReport.mockTestRuns(), DataBuilderReport.mockKitss(), DataBuilderReport.mockTestReport(),
				Optional.of(DataBuilderReport.mockLaboratoryOrders()), DataBuilderReport.userDetailsImpl))
				.thenReturn(DataBuilderOrder.mockTestReports());

		when(testReportService.createTestReport("12345", DataBuilderReport.mockTestReport(),
				DataBuilderReport.userDetailsImpl)).thenReturn(DataBuilderOrder.mockTestReports());
		assertEquals(testReportService.createTestReports("12345", testReport, DataBuilderReport.userDetailsImpl),
				testReport);

	}

	@Test
	void testcreateTestReportException() {
		testReportService.validateNIPSReport(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		testReportService.validateResultData(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.createTestReport("12345", DataBuilderReport.mockTestReportException(),
						DataBuilderReport.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	void testcreateTestReportBatchException() {
		testReportService.validateNIPSReport(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		testReportService.validateResultData(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		when(batchRunService.findByPipelineRunId(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.createTestReport("12345", DataBuilderReport.mockTestReport(), DataBuilderReport.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	void testcreateTestReportTestReport() {
		testReportService.validateNIPSReport(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		testReportService.validateResultData(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		when(batchRunService.findByPipelineRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderReport.mockBatchRun()));
		when(testReportRepository.findTestReportBySampleNumberAndPipelineRunId(Mockito.anyString(),
				Mockito.anyString())).thenReturn(Optional.of(DataBuilderReport.mockTestReport()));
		assertEquals(testReportService.createTestReport("12345", DataBuilderReport.mockTestReport(),
				DataBuilderReport.userDetailsImpl), DataBuilderOrder.mockTestReports());
	}

	@Test
	void testcreateTestReportTestReportException() {
		testReportService.validateNIPSReport(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		testReportService.validateResultData(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		when(batchRunService.findByPipelineRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderReport.mockBatchRun()));
		doThrow(new NonUniqueResultException()).when(testReportRepository)
				.findTestReportBySampleNumberAndPipelineRunId(Mockito.anyString(), Mockito.anyString());
		assertThrows(JdxServiceException.class, () -> testReportService.createTestReport("12345",
				DataBuilderReport.mockTestReport(), DataBuilderReport.userDetailsImpl));

	}

	@Test
	void testcreateTestReportKitException() {
		testReportService.validateNIPSReport(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		testReportService.validateResultData(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		when(batchRunService.findByPipelineRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderReport.mockBatchRun()));
		when(kitRepository.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class, () -> testReportService.createTestReport("12345",
				DataBuilderReport.mockTestReport(), DataBuilderReport.userDetailsImpl));

	}

	@Test
	void testcreateTestReportTestRunForKitException() {
		testReportService.validateNIPSReport(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		testReportService.validateResultData(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		when(batchRunService.findByPipelineRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderReport.mockBatchRun()));
		when(kitRepository.findKitBySampleNumber(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderReport.mockKitss()));
		when(testRunService.getTestRunForKitId(Mockito.anyString())).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class, () -> testReportService.createTestReport("12345",
				DataBuilderReport.mockTestReport(), DataBuilderReport.userDetailsImpl));

	}

	@Test
	void testcreateTestReportLaboratoryOrderByTestRunException() {
		testReportService.validateNIPSReport(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		testReportService.validateResultData(ReportConfiguration.NIPS_PLUS, DataBuilderReport.mockReport());
		when(batchRunService.findByPipelineRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderReport.mockBatchRun()));
		when(kitRepository.findKitBySampleNumber(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderReport.mockKitss()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderReport.mockTestRuns()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRun(Mockito.any())).thenReturn(Optional.empty());
		assertThrows(JdxServiceException.class, () -> testReportService.createTestReport("12345",
				DataBuilderReport.mockTestReport(), DataBuilderReport.userDetailsImpl));

	}

	@Test
	void testsaveSingleTestReportAsControl() {
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(testReportRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockTestReports());
		assertEquals(
				testReportService.saveSingleTestReportAsControl(DataBuilderOrder.mockBatchRun(),
						Optional.of(DataBuilderOrder.mockTestRuns()), Optional.of(DataBuilderOrder.mockKit()),
						DataBuilderOrder.mockTestReports(), DataBuilderOrder.userDetailsImpl),
				DataBuilderOrder.mockTestReports());

	}

	@Test
	void testsaveSingleTestReportAsResearch() {
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(testReportRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockTestReports());
		assertEquals(
				testReportService.saveSingleTestReportAsResearch(DataBuilderOrder.mockBatchRun(),
						Optional.of(DataBuilderOrder.mockTestRuns()), Optional.of(DataBuilderOrder.mockKit()),
						DataBuilderOrder.mockTestReports(), DataBuilderOrder.userDetailsImpl),
				DataBuilderOrder.mockTestReports());
	}

	@Test
	void testSingleTestReportAsResearchEmptyKit() {
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(testReportRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockTestReports());
		assertEquals(
				testReportService.saveSingleTestReportAsResearch(DataBuilderOrder.mockBatchRun(),
						Optional.of(DataBuilderOrder.mockTestRuns()), Optional.empty(),
						DataBuilderOrder.mockTestReports(), DataBuilderOrder.userDetailsImpl),
				DataBuilderOrder.mockTestReports());
	}

	@Test
	void testsaveSingleTestReportAsControlEmptyKit() {
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(testReportRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockTestReports());
		assertEquals(
				testReportService.saveSingleTestReportAsControl(DataBuilderOrder.mockBatchRun(),
						Optional.of(DataBuilderOrder.mockTestRuns()), Optional.empty(),
						DataBuilderOrder.mockTestReports(), DataBuilderOrder.userDetailsImpl),
				DataBuilderOrder.mockTestReports());
	}

	@Test
	void testsaveSingleTestReportAsCatchException() {
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.saveSingleTestReportAsControl(null,
						Optional.of(DataBuilderOrder.mockTestRuns()), Optional.of(DataBuilderOrder.mockKit()),
						DataBuilderOrder.mockTestReports(), DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	void testSingleTestReportAsResearchCatchException() {
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.saveSingleTestReportAsResearch(null,
						Optional.of(DataBuilderOrder.mockTestRuns()), Optional.of(DataBuilderOrder.mockKit()),
						DataBuilderOrder.mockTestReports(), DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	void testsaveSingleTestReportAsControlEmptyRuns() {
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(testReportRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockTestReports());
		assertEquals(testReportService.saveSingleTestReportAsControl(DataBuilderOrder.mockBatchRun(), Optional.empty(),
				Optional.of(DataBuilderOrder.mockKit()), DataBuilderOrder.mockTestReports(),
				DataBuilderOrder.userDetailsImpl), DataBuilderOrder.mockTestReports());

	}

	@Test
	void testsaveSingleTestReportAsResearchEmptyRuns() {
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		when(testReportRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockTestReports());
		assertEquals(testReportService.saveSingleTestReportAsResearch(DataBuilderOrder.mockBatchRun(), Optional.empty(),
				Optional.of(DataBuilderOrder.mockKit()), DataBuilderOrder.mockTestReports(),
				DataBuilderOrder.userDetailsImpl), DataBuilderOrder.mockTestReports());

	}

	@Test
	void testsaveSingleTestReportAsControlException() {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.saveSingleTestReportAsControl(DataBuilderOrder.mockBatchRun(),
						Optional.of(DataBuilderOrder.mockTestRuns()), Optional.of(DataBuilderOrder.mockKit()), null,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	void testsaveSingleTestReportAsResearchException() {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.saveSingleTestReportAsResearch(DataBuilderOrder.mockBatchRun(),
						Optional.of(DataBuilderOrder.mockTestRuns()), Optional.of(DataBuilderOrder.mockKit()), null,
						DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());

	}

	@Test
	void testcreateTestReports() {
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		testReport.add(DataBuilderOrder.mockTestReports());
		when(batchRunService.findByPipelineRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockBatchRun()));
		testReportService.createTestReports("8L", testReport, DataBuilderOrder.userDetailsImpl);
	}

	@Test
	void testcreateTestReportBatchRunEmpty() {
		List<TestReport> testReport = new LinkedList<>();
		testReport.add(DataBuilderOrder.mockTestReports());
		testReport.add(DataBuilderOrder.mockTestReports());
		when(batchRunService.findByPipelineRunId(Mockito.anyString())).thenReturn(Optional.empty());
		testReportService.createTestReports("8L", testReport, DataBuilderOrder.userDetailsImpl);
	}

	@Test
	public void testvalidateResultDataException() throws JdxServiceException {

		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.validateResultData(null, DataBuilderOrder.mockReport()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateResultDataReportException() throws JdxServiceException {

		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.validateResultData(ReportConfiguration.UCS, DataBuilderOrder.mockReport()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportFstNullException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportFstNull()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportC13SnrException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportC13Snr()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetChr13SensException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportChr13Sens()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetC18SnrException() throws JdxServiceException {
		DataBuilderOrder.mockTestQC().setC18Snr(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportC18Snr()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetChr18SensException() throws JdxServiceException {
		DataBuilderOrder.mockTestQC().setChr18Sens(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportChr18Sens()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetC21SnrException() throws JdxServiceException {
		DataBuilderOrder.mockTestQC().setC21Snr(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportC21Snr()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetChr21SensException() throws JdxServiceException {
		DataBuilderOrder.mockTestQC().setChr21Sens(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportChr21Sens()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetLowReadsException() throws JdxServiceException {
		DataBuilderOrder.mockTestQC().setLowReads(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportLowReads()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetRawCountsException() throws JdxServiceException {
		DataBuilderOrder.mockTestQC().setRawCounts(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportRawCounts()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetSnpIdentityException() throws JdxServiceException {
		DataBuilderOrder.mockTestQC().setSnpIdentity(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportSnpIdentity()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetFetalFractionException() throws JdxServiceException {
		DataBuilderOrder.mockNIPSBasicRawData().setFetalFraction(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportFetalFraction()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetT13Exception() throws JdxServiceException {
		DataBuilderOrder.mockNIPSBasicRawData().setT13(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportT13()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetT13CallException() throws JdxServiceException {
		DataBuilderOrder.mockT13Test().setCall(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportT13Call()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetT13ScoreException() throws JdxServiceException {
		DataBuilderOrder.mockT13Test().setzScore(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportT13Scores()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetT13ConfidenceLowerException() throws JdxServiceException {
		DataBuilderOrder.mockT13Test().setConfidenceLower(null);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED,
						DataBuilderOrder.mockReportT13ConfidenceLower()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetT21Exception() throws JdxServiceException {
		DataBuilderOrder.mockNIPSBasicRawData().setT21(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportT21()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetT21CallException() throws JdxServiceException {
		DataBuilderOrder.mockT21Test().setCall(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportT21Call()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetT21ScoreException() throws JdxServiceException {
		DataBuilderOrder.mockT21Test().setzScore(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportT21Score()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetT21ConfidenceLowerException() throws JdxServiceException {
		DataBuilderOrder.mockT21Test().setConfidenceLower(null);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED,
						DataBuilderOrder.mockReportT21ConfidenceLower()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetT18Exception() throws JdxServiceException {
		DataBuilderOrder.mockNIPSBasicRawData().setT18(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportT18()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetT18CallException() throws JdxServiceException {
		DataBuilderOrder.mockT18Test().setCall(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportT18Call()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetT18ScoreException() throws JdxServiceException {
		DataBuilderOrder.mockT18Test().setzScore(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportT18Score()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetT18ConfidenceLowerException() throws JdxServiceException {
		DataBuilderOrder.mockT18Test().setConfidenceLower(null);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED,
						DataBuilderOrder.mockReportT18ConfidenceLower()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetEuploidException() throws JdxServiceException {
		DataBuilderOrder.mockNIPSBasicRawData().setEuploid(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportEuploid()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetscaException() throws JdxServiceException {
		DataBuilderOrder.mockNIPSBasicRawData().setSca(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportSca()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetscaGenderResultException() throws JdxServiceException {
		DataBuilderOrder.mockSCATest().setGenderResult(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportScaGenderResult()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetscaScaResultException() throws JdxServiceException {
		DataBuilderOrder.mockSCATest().setScaResult(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportScaResult()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetGenderConfidenceException() throws JdxServiceException {
		DataBuilderOrder.mockSCATest().setGenderConfidence(null);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED,
						DataBuilderOrder.mockReportScaGenderConfidence()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetScaConfidenceException() throws JdxServiceException {
		DataBuilderOrder.mockSCATest().setScaConfidence(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportScaConfidence()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetxVecException() throws JdxServiceException {
		DataBuilderOrder.mockSCATest().setxVec(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportScaxVec()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetyVecException() throws JdxServiceException {
		DataBuilderOrder.mockSCATest().setyVec(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportScayVec()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetyVec2Exception() throws JdxServiceException {
		DataBuilderOrder.mockSCATest().setyVec2(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportScayVec2()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateNIPSReportgetXzScoresException() throws JdxServiceException {
		DataBuilderOrder.mockSCATest().setXzScores(null);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportScaXzScores()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateException() throws JdxServiceException {

		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderOrder.mockReportData()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testvalidateScaException() throws JdxServiceException {

		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.validateNIPSReport(ReportConfiguration.FST, DataBuilderOrder.mockReportDataSca()));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testfinalizeAndSaveReportException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService.finalizeAndSaveReport(
				DataBuilderReport.mockTestReport(), DataBuilderReport.mockBatchRun(), DataBuilderReport.mockTestRuns(),
				DataBuilderReport.mockKitss(), DataBuilderReport.mockLaboratory(),
				Optional.of(DataBuilderReport.mockUser()), Optional.of(DataBuilderReport.mockLaboratoryOrders()),
				Optional.of(DataBuilderReport.mockOrdersempty()), DataBuilderReport.userDetailsImpl, false, false));

		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testfinalizeAndSaveReportBatchRun() throws JdxServiceException {
		DataBuilderCheckOut.lineItems.add(DataBuilderCheckOut.mockOrderLineItems());
		testReportService.finalizeAndSaveReport(DataBuilderReport.mockTestReport(),
				DataBuilderReport.mockBatchRunSequence(), DataBuilderReport.mockTestRuns(),
				DataBuilderReport.mockKitss(), DataBuilderReport.mockLaboratory(),
				Optional.of(DataBuilderReport.mockUser()), Optional.of(DataBuilderReport.mockLaboratoryOrders()),
				Optional.of(DataBuilderCheckOut.mockOrder()), DataBuilderReport.userDetailsImpl, false, false);

	}

	@Test
	public void testfinalizeAndSaveReportBatchException() throws JdxServiceException {
		when(batchRunService.findByPipelineRunId(Mockito.anyString())).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.createTestReport("12345", DataBuilderOrder.mockTestReports(), DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testfinalizeAndSaveReportkitException() throws JdxServiceException {
		when(batchRunService.findByPipelineRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockBatchRun()));
		when(kitRepository.findKitBySampleNumber(Mockito.anyString())).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.createTestReport("12345", DataBuilderOrder.mockTestReports(), DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testfinalizeAndSaveReportTestException() throws JdxServiceException {
		when(batchRunService.findByPipelineRunId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockBatchRun()));
		when(kitRepository.findKitBySampleNumber(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRuns()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRun(Mockito.any())).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.createTestReport("12345", DataBuilderOrder.mockTestReports(), DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveSingleTestReportWithlabException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.saveSingleTestReportWithOrder(DataBuilderOrder.mockBatchRun(),
						DataBuilderOrder.mockTestRuns(), DataBuilderOrder.mockKit(), DataBuilderOrder.mockTestReports(),
						Optional.empty(), DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveSingleTestReportWithLineItemsException() throws JdxServiceException {
		when(orderRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderReport.mockOrdersempty()));
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.saveSingleTestReportWithOrder(DataBuilderOrder.mockBatchRun(),
						DataBuilderOrder.mockTestRuns(), DataBuilderOrder.mockKit(), DataBuilderOrder.mockTestReports(),
						Optional.of(DataBuilderOrder.mockLaboratoryOrder()), DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveSingleTestReportException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.saveSingleTestReportWithOrder(DataBuilderOrder.mockBatchRun(),
						DataBuilderOrder.mockTestRuns(), DataBuilderOrder.mockKit(), null,
						Optional.of(DataBuilderOrder.mockLaboratoryOrder()), DataBuilderOrder.userDetailsImpl));
		assertEquals("The system experienced an error processing the client's request.", e.getMessage());
	}

	@Test
	public void testsaveSingleTestReportWithOrdersIdException() throws JdxServiceException {
		when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.saveSingleTestReportWithOrder(DataBuilderOrder.mockBatchRun(),
						DataBuilderOrder.mockTestRuns(), DataBuilderOrder.mockKit(), DataBuilderOrder.mockTestReports(),
						Optional.of(DataBuilderOrder.mockLaboratoryOrder()), DataBuilderOrder.userDetailsImpl));
	}

	@Test
	void testsaveSingleTestReportWithOrders() {
		DataBuilderOrder.lineItems.add(DataBuilderOrder.mockOrderLineItem());
		DataBuilderOrder.testRuns.add(DataBuilderOrder.mockTestRuns());
		when(kitRepository.findKitBySampleNumber(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRuns()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRun(Mockito.any()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderLineItemRepository.findOrderLineItemByLaboratoryOrderDetails_Id(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderLineItem()));
		when(orderRepository.findOrderByLineItems(Mockito.any())).thenReturn(Optional.of(DataBuilderOrder.mockOrder()));
		when(testReportRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockTestReports());
		assertEquals(testReportService.saveSingleTestReportWithOrder(DataBuilderOrder.mockTestReports(),
				DataBuilderOrder.userDetailsImpl), DataBuilderOrder.mockTestReports());
	}

	@Test
	public void testsaveSingleTestReportWithOrdersAnotherIdskitException() throws JdxServiceException {
		when(kitRepository.findKitBySampleNumber(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.saveSingleTestReportWithOrder(DataBuilderOrder.mockTestReports(), DataBuilderOrder.userDetailsImpl));
	}

	@Test
	public void testsaveSingleTestReportWithOrdersAnotherTestException() throws JdxServiceException {
		when(kitRepository.findKitBySampleNumber(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString())).thenReturn(Optional.empty());
//		when(laboratoryOrderService.findLaboratoryOrderByTestRun(Mockito.any())).thenThrow(JdxServiceException.class);
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.saveSingleTestReportWithOrder(DataBuilderOrder.mockTestReports(), DataBuilderOrder.userDetailsImpl));
	}

	@Test
	public void testsaveSingleTestReportWithOrdersAnotherlabException() throws JdxServiceException {
		when(kitRepository.findKitBySampleNumber(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRuns()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRun(Mockito.any())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.saveSingleTestReportWithOrder(DataBuilderOrder.mockTestReports(), DataBuilderOrder.userDetailsImpl));
	}

	@Test
	public void testsaveSingleTestReportWithOrdersAnotherOrderLineItemsException() throws JdxServiceException {
		when(kitRepository.findKitBySampleNumber(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRuns()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRun(Mockito.any()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderLineItemRepository.findOrderLineItemByLaboratoryOrderDetails_Id(Mockito.anyString()))
				.thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.saveSingleTestReportWithOrder(DataBuilderOrder.mockTestReports(), DataBuilderOrder.userDetailsImpl));
	}

	@Test
	public void testsaveSingleTestReportWithOrdersException() throws JdxServiceException {
		when(kitRepository.findKitBySampleNumber(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockKit()));
		when(testRunService.getTestRunForKitId(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestRuns()));
		when(laboratoryOrderService.findLaboratoryOrderByTestRun(Mockito.any()))
				.thenReturn(Optional.of(DataBuilderOrder.mockLaboratoryOrder()));
		when(orderLineItemRepository.findOrderLineItemByLaboratoryOrderDetails_Id(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockOrderLineItem()));
		when(orderRepository.findOrderByLineItems(Mockito.any())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.saveSingleTestReportWithOrder(DataBuilderOrder.mockTestReports(), DataBuilderOrder.userDetailsImpl));
	}

	@Test
	public void testsaveSingleTestReportWithTestException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.saveSingleTestReportWithOrder(null, DataBuilderOrder.userDetailsImpl));
	}

	@Test
	void testdelete() {
		verify(testReportRepository, times(0)).deleteById(Mockito.anyString());
		testReportService.delete("2L", DataBuilderOrder.userDetailsImpl);
	}

	@Test
	void testdeleteException() {
		doThrow(new NoSuchElementException()).when(testReportRepository).deleteById(Mockito.anyString());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.delete(null, DataBuilderOrder.userDetailsImpl));
	}

	@Test
	void testvalidateFSTReport() {
		testReportService.validateFSTReport(DataBuilderOrder.mockReports());
	}

	@Test
	void testvalidateResultDataFst() {
		testReportService.validateFSTReport(DataBuilderOrder.mockReports());
		testReportService.validateResultData(ReportConfiguration.FST, DataBuilderOrder.mockReports());

	}

	@Test
	void testvalidateResultDataNIPS_ADVANCED() {
		testReportService.validateNIPSReport(ReportConfiguration.NIPS_ADVANCED, DataBuilderReport.mockReport());
		testReportService.validateResultData(ReportConfiguration.NIPS_ADVANCED, DataBuilderReport.mockReport());

	}

	@Test
	void testvalidateResultDataNIPS_BASIC() {
		testReportService.validateNIPSReport(ReportConfiguration.NIPS_BASIC, DataBuilderReport.mockReports());
		testReportService.validateResultData(ReportConfiguration.NIPS_BASIC, DataBuilderReport.mockReports());

	}

	@Test
	public void testvalidateFSTReportException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.validateFSTReport(DataBuilderOrder.mockReportFstEuploid()));
	}

	@Test
	public void testvalidateFSTReportEuploidException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.validateFSTReport(DataBuilderOrder.mockReportT13Fst()));
	}

	@Test
	public void testvalidateFSTReportT18Exception() throws JdxServiceException {
		DataBuilderOrder.mockNIPSBasicRawData().setT18(null);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.validateFSTReport(DataBuilderOrder.mockReportT18Fst()));
	}

	@Test
	public void testvalidateFSTReportT21Exception() throws JdxServiceException {
		DataBuilderOrder.mockNIPSBasicRawData().setT21(null);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.validateFSTReport(DataBuilderOrder.mockReportT21Fst()));
	}

	@Test
	public void testvalidateFSTReportScaException() throws JdxServiceException {
		DataBuilderOrder.mockNIPSBasicRawData().setSca(null);
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.validateFSTReport(DataBuilderOrder.mockReportScaFst()));
	}

	@Test
	void testgetAllLabReviewStats() {
		List<TestReportsReviewResultsDto> resultsDto = new ArrayList<>();
		resultsDto.add(DataBuilderOrder.mockTestReportsReviewResultsDto());
		assertEquals(testReportService.getAllLabReviewStats("1997-12-15 00:00:00").getClass(), resultsDto.getClass());
	}

	@Test
	void testgetAllLabReviewStatsElse() {
		List<TestReportsReviewResultsDto> resultsDto = new ArrayList<>();
		resultsDto.add(DataBuilderOrder.mockTestReportsReviewResultsDto());
		assertEquals(testReportService.getAllLabReviewStats(" ").getClass(), resultsDto.getClass());
	}

	@Test
	void testgetTestReportResults() {
		List<TestReportsReviewResultsDto> resultsDto = new ArrayList<>();
		resultsDto.add(DataBuilderOrder.mockTestReportsReviewResultsDto());
		assertEquals(testReportService.getTestReportResults(Optional.of("lab"), "1997-12-15 00:00:00").getClass(),
				resultsDto.getClass());
	}

	@Test
	void testgetTestReportResultsLabEmpty() {
		List<TestReportsReviewResultsDto> resultsDto = new ArrayList<>();
		resultsDto.add(DataBuilderOrder.mockTestReportsReviewResultsDto());
		testReportService.getTestReportResults(Optional.empty(), "1997-12-15 00:00:00");
	}

	@Test
	void testgetTestReportResultsElse() {
		List<TestReportsReviewResultsDto> resultsDto = new ArrayList<>();
		resultsDto.add(DataBuilderOrder.mockTestReportsReviewResultsDto());
		assertEquals(testReportService.getTestReportResults(Optional.of("lab"), " ").getClass(), resultsDto.getClass());
	}

	@Test
	void testgetAwaitingReports() {
		assertEquals(testReportService.getAwaitingReports("f450237e-20a3-4bf1-b64d-9ecaab16be7a", "FST").getClass(),
				DataBuilderOrder.mockTestReportsAwaitingReviewDto().getClass());
	}

	@Test
	public void testAwaitingReportsException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.getAwaitingReports(null, "FST"));
	}

	@Test
	void testgetAwaitingReportLabs() {
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		assertEquals(testReportService.getAwaitingReports(null, "FST").getClass(),
				DataBuilderOrder.mockTestReportsAwaitingReviewDto().getClass());
	}

	@Test
	void testgetAllAwaitingReports() {
		DataBuilderOrder.counts.add(DataBuilderOrder.mockTestReportsAwaitingReviewDto());
		// when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		assertEquals(testReportService.getAllAwaitingReports("f450237e-20a3-4bf1-b64d-9ecaab16be7a").getClass(),
				DataBuilderOrder.mockLaboratoryReviewStatisticsDto().getClass());
	}

	@Test
	void testgetAllAwaitingReportsAnother() {
		DataBuilderOrder.counts.add(DataBuilderOrder.mockTestReportsAwaitingReviewDto());
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.of(DataBuilderOrder.mockLaboratory()));
		assertEquals(testReportService.getAllAwaitingReports().getClass(),
				DataBuilderOrder.mockLaboratoryReviewStatisticsDto().getClass());
	}

	@Test
	public void testgetAllAwaitingReportsAnotherException() throws JdxServiceException {
		when(laboratoryService.getDefaultLaboratory()).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.getAllAwaitingReports());
	}

	@Test
	public void testsendTestReportAvaialbleEmailException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class,
				() -> testReportService.sendTestReportAvaialbleEmail(DataBuilderOrder.mockOrder()));
	}

	@Test
	void testcountOfUnapprovedResultsInBatch() {
		when(testReportRepository.countOfUnapprovedResultsInBatch(Mockito.anyString())).thenReturn(8L);
		assertEquals(testReportService.countOfUnapprovedResultsInBatch("8L"), 8L);
	}

	@Test
	void testupdateTestReportPatientViewedResult() {
		List<Product> products = new LinkedList<>();
		products.add(DataBuilderOrder.mockProduct());
		DataBuilderOrder.orderStatusHistory.add(DataBuilderOrder.mockOrderStatus());
		DataBuilderOrder.mockOrderStatus().setStatusType(OrderStatusType.KIT_ASSIGNED);
		DataBuilderOrder.mockOrder().setOrderStatusHistory(DataBuilderOrder.orderStatusHistory);
		when(testReportRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestReports()));

		orderService.updateOrderWithNewStatus("aa86f0ec-9ea1-487c-a814-b1c6001be7e8",
				DataBuilderOrder.mockOrderStatus(), DataBuilderOrder.userDetailsImpl);
		when(testReportRepository.save(Mockito.any())).thenReturn(DataBuilderOrder.mockTestReports());

		assertEquals(
				testReportService.updateTestReportPatientViewedResult("2L", "1L", DataBuilderOrder.userDetailsImpl),
				DataBuilderOrder.mockTestReports());
	}

	@Test
	public void testupdateTestReportPatientViewedResultTestException() throws JdxServiceException {
		when(testReportRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.updateTestReportPatientViewedResult("2L", "1L", DataBuilderOrder.userDetailsImpl));
	}
	@Test
	public void testupdateTestReportPatientViewedException() throws JdxServiceException {
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.updateTestReportPatientViewedResult(null, "1L", DataBuilderOrder.userDetailsImpl));
	}
	
	@Test
	public void testupdateTestReportPatientViewedResultException() throws JdxServiceException {
		when(testReportRepository.findById(Mockito.anyString()))
				.thenReturn(Optional.of(DataBuilderOrder.mockTestReportEmptyPatient()));
		JdxServiceException e = assertThrows(JdxServiceException.class, () -> testReportService
				.updateTestReportPatientViewedResult("2L", "1L", DataBuilderOrder.userDetailsImpl));
	}
}
