package com.junodx.api.services.lab;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.SortType;
import com.junodx.api.controllers.lab.types.BatchRunSortBy;
import com.junodx.api.controllers.lab.types.TestReportSortBy;
import com.junodx.api.controllers.payloads.ReportConfigurationPayload;
import com.junodx.api.controllers.payloads.SampleIdListPayload;
import com.junodx.api.dto.mappers.LaboratoryMapStructMapper;
import com.junodx.api.dto.models.laboratory.LaboratoryReviewStatisticsDto;
import com.junodx.api.dto.models.laboratory.TestReportsAwaitingReviewDto;
import com.junodx.api.dto.models.laboratory.TestReportsReviewResultsDto;
import com.junodx.api.models.auth.Authority;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.commerce.OrderStatus;
import com.junodx.api.models.commerce.types.OrderStatusType;
import com.junodx.api.models.core.types.IntervalType;
import com.junodx.api.models.fulfillment.Fulfillment;
import com.junodx.api.models.fulfillment.ShippingStatus;
import com.junodx.api.models.laboratory.BatchRun;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.models.laboratory.Laboratory;
import com.junodx.api.models.laboratory.LaboratoryOrder;
import com.junodx.api.models.laboratory.LaboratoryStatus;
import com.junodx.api.models.laboratory.Signout;
import com.junodx.api.models.laboratory.TestReport;
import com.junodx.api.models.laboratory.TestRun;
import com.junodx.api.models.laboratory.*;
import com.junodx.api.models.laboratory.reports.Report;
import com.junodx.api.models.laboratory.reports.types.SignedOutType;
import com.junodx.api.models.laboratory.tests.types.GenderResultType;
import com.junodx.api.models.laboratory.tests.types.SCAResultType;
import com.junodx.api.models.laboratory.types.*;
import com.junodx.api.repositories.commerce.OrderLineItemRepository;
import com.junodx.api.repositories.commerce.OrderRepository;
import com.junodx.api.repositories.lab.KitRepository;
import com.junodx.api.repositories.lab.TestReportRepository;
import com.junodx.api.repositories.lab.TestRunRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.auth.UserService;
import com.junodx.api.services.commerce.OrderService;
import com.junodx.api.services.exceptions.JdxServiceException;
import com.junodx.api.services.mail.MailService;
import com.junodx.api.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.NonUniqueResultException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TestReportService extends ServiceBase {

    @Autowired
    private TestReportRepository testReportRepository;

    @Autowired
    private LaboratoryOrderService laboratoryOrderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TestRunRepository testRunRepository;

    @Autowired
    private TestRunService testRunService;

    @Autowired
    private KitRepository kitRepository;

    @Autowired
    private LaboratoryService laboratoryService;

    @Autowired
    private MailService mailService;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private BatchRunService batchRunService;

    private LaboratoryMapStructMapper laboratoryMapStructMapper;

    private ObjectMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(TestReportService.class);

    public TestReportService(){
        mapper = new ObjectMapper();
    }

    @Autowired
    public TestReportService(LaboratoryMapStructMapper laboratoryMapStructMapper){
        this.laboratoryMapStructMapper = laboratoryMapStructMapper;
        this.mapper = new ObjectMapper();
    }

    public Optional<TestReport> getTestReport(String id, String[] includes, UserDetailsImpl updater){
        if(id != null) {
            return testReportRepository.findById(id);
        }

        return Optional.empty();
    }

    public Page<TestReport> getAllTestReports(Pageable pageable){
        String laboratoryId = null;

        return testReportRepository.findAll(pageable);
    }

    public Page<TestReport> getAllTestReports(Optional<String> labId, Pageable pageable){
        String laboratoryId = null;

        if(labId.isPresent())
            laboratoryId = labId.get();
        else{
            Optional<Laboratory> defaultLab = laboratoryService.getDefaultLaboratory();
            if(defaultLab.isPresent())
                laboratoryId = defaultLab.get().getId();
            else
                throw new JdxServiceException("Cannot get test reports, not laboratory specified and not default laboratory declared");
        }
            return testReportRepository.findTestReportsByLabId(laboratoryId, pageable);
    }

    public Page<TestReport> getAllTestReportsByPatientId(String id, SignedOutType type, ReportConfiguration config, boolean isAvailable, Pageable pageable){
        return testReportRepository.findTestReportsByPatient_IdAndSignedOutTypeAndReportConfigurationAndAvailable(id, type, config, isAvailable, pageable);
    }

    public Page<TestReport> getAllTestReportsByLabOrderId(String id, SignedOutType type, ReportConfiguration config, boolean isAvailable, Pageable pageable){
        return testReportRepository.findTestReportsByLaboratoryOrderIdAndSignedOutTypeAndReportConfigurationAndAvailable(id, type, config, isAvailable, pageable);
    }

    public Page<TestReport> getAllTestReportsBySampleNumber(String sampleNumber, SignedOutType type, ReportConfiguration config, boolean isAvailable, Pageable pageable){
        return testReportRepository.findTestReportsBySampleNumberAndSignedOutTypeAndReportConfigurationAndAvailable(sampleNumber, type, config, isAvailable, pageable);
    }

    public Page<TestReport> getAllTestReportsBySignout(Optional<String> labId, SignedOutType type, int interval, IntervalType intervaltype, boolean isSignedOut, Pageable pageable){
        if(intervaltype == IntervalType.DAYS)
            interval *= interval * 24;
        else if(intervaltype.equals(IntervalType.WEEKS))
            interval *= interval * 24 * 7;
        else if(intervaltype.equals(IntervalType.MINUTES))
            interval *= interval / 60;

        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, -interval);

        String laboratoryId = null;

        if(labId.isPresent())
            laboratoryId = labId.get();
        else{
            Optional<Laboratory> defaultLab = laboratoryService.getDefaultLaboratory();
            if(defaultLab.isPresent())
                laboratoryId = defaultLab.get().getId();
            else
                throw new JdxServiceException("Cannot get test reports, not laboratory specified and not default laboratory declared");
        }

        return testReportRepository.findTestReportsBySignedOutType(laboratoryId, type, now, isSignedOut, pageable);
    }

    public Page<TestReport> findAllAvailableTestReportsByPatient(String patientId, Pageable pageable) throws JdxServiceException {
        try {
            return testReportRepository.findTestReportsByPatient_IdAndSignedOut(patientId, true, pageable);
        } catch (Exception e){e.printStackTrace();
            throw new JdxServiceException("Cannot find reports for patient : " + patientId);
        }
    }

    public Page<TestReport> search(Optional<String> labId,
                                   Optional<SignedOutType> type,
                                   Optional<ReportConfiguration> report,
                                   Optional<ReportType> reportType,
                                   Optional<Boolean> signedOut,
                                   Optional<String> patientId,
                                   Optional<String> firstName,
                                   Optional<String> lastName,
                                   Optional<String> email,
                                   Optional<String> sampleNumber,
                                   Optional<String> batchRunId,
                                   Optional<String> labOrderId,
                                   Optional<String> orderNumber,
                                   Optional<Boolean> isAvailable,
                                   Optional<Boolean> upcomingOnly,
                                   Optional<Boolean> reportable,
                                   Optional<Boolean> approved,
                                   Optional<TestReportSortBy> sortBy,
                                   Optional<SortType> sortDirection,
                                   Optional<Boolean> condensed,
                                   Optional<Calendar> after,
                                   Pageable pageable) {
        SignedOutType oType = null;
        String oLabId = null;
        String oPatientId = null;
        String oFirstName = null;
        String oLastName = null;
        String oEmail = null;
        String oSampleNumber = null;
        String oBatchRunid = null;
        String oLabOrderId = null;
        String oOrderNumber = null;
        Boolean oSignedOut = null;
        Boolean oIsAvailable = null;
        Boolean oUpcomingOnly = null;
        Boolean oReportable = null;
        Boolean oApproved = null;
        ReportConfiguration oReport = null;
        ReportType oReportType = null;
        Calendar oAfter = null;
        TestReportSortBy sort = null;
        SortType sortType = null;

        if(type.isPresent()) oType = type.get();
        if(patientId.isPresent()) oPatientId = patientId.get();
        if(firstName.isPresent()) oFirstName = firstName.get();
        if(lastName.isPresent()) oLastName = lastName.get();
        if(email.isPresent()) oEmail = email.get();
        if(sampleNumber.isPresent()) oSampleNumber = sampleNumber.get();
        if(batchRunId.isPresent()) oBatchRunid = batchRunId.get();
        if(labId.isPresent()) oLabId = labId.get();
        if(labOrderId.isPresent()) oLabOrderId = labOrderId.get();
        if(orderNumber.isPresent()) oOrderNumber = orderNumber.get();
        if(signedOut.isPresent()) oSignedOut = signedOut.get();
        if(isAvailable.isPresent()) oIsAvailable = isAvailable.get();
        if(upcomingOnly.isPresent()) oUpcomingOnly = upcomingOnly.get();
        if(reportable.isPresent()) oReportable = reportable.get();
        if(approved.isPresent()) oApproved = approved.get();
        if(report.isPresent()) oReport = report.get();
        if(reportType.isPresent()) oReportType = reportType.get();
        if(after.isPresent()) oAfter = after.get();

        logger.info("Searching for orders");

        if(sortBy.isPresent() && sortDirection.isPresent()) {
            switch(sortBy.get()) {
                    case estimatedToBeAvailableAt:
                    if(sortDirection.get().equals(SortType.ASC))
                        return testReportRepository.searchOrderByEstimatedToBeAvailableAtASC(oType, oSignedOut, oPatientId, oFirstName, oLastName, oEmail, oSampleNumber, oBatchRunid, oLabId, oLabOrderId, oOrderNumber, oIsAvailable, oApproved, oReport, oReportType, oReportable, oAfter, pageable);
                    else
                        return testReportRepository.searchOrderByEstimatedToBeAvailableAtDSC(oType, oSignedOut, oPatientId, oFirstName, oLastName, oEmail, oSampleNumber, oBatchRunid, oLabId, oLabOrderId, oOrderNumber, oIsAvailable, oApproved, oReport, oReportType, oReportable, oAfter, pageable);
                case firstAvailableAt:
                    if(sortDirection.get().equals(SortType.ASC))
                        return testReportRepository.searchOrderByFirstAvailableAtASC(oType, oSignedOut, oPatientId, oFirstName, oLastName, oEmail, oSampleNumber, oBatchRunid, oLabId, oLabOrderId, oOrderNumber, oIsAvailable, oApproved, oReport, oReportType, oReportable, oAfter, pageable);
                    else
                        return testReportRepository.searchOrderByFirstAvailableAtDSC(oType, oSignedOut, oPatientId, oFirstName, oLastName, oEmail, oSampleNumber, oBatchRunid, oLabId, oLabOrderId, oOrderNumber, oIsAvailable, oApproved, oReport, oReportType,  oReportable, oAfter, pageable);
                default:
                    if(sortDirection.get().equals(SortType.ASC))
                        return testReportRepository.searchOrderByCompletedAtASC(oType, oSignedOut, oPatientId, oFirstName, oLastName, oEmail, oSampleNumber, oBatchRunid, oLabId, oLabOrderId, oOrderNumber, oIsAvailable, oApproved, oReport, oReportType, oReportable, oAfter, pageable);
                    else
                        return testReportRepository.searchOrderByCompletedAtDSC(oType, oSignedOut, oPatientId, /*oFirstName, oLastName, oEmail, */oSampleNumber, oBatchRunid, oLabId, oLabOrderId, /*oOrderNumber,*/ oIsAvailable, oApproved, oReport, /*oReportType,*/oReportable,  oAfter, pageable);
            }
        } else
            return testReportRepository.searchOrderByCompletedAtDSC(oType, oSignedOut, oPatientId, /*oFirstName, oLastName, oEmail, */oSampleNumber, oBatchRunid, oLabId, oLabOrderId, /*oOrderNumber,*/ oIsAvailable, oApproved, oReport, /*oReportType,*/ oReportable, oAfter, pageable);
    }

    public List<TestReport> signOutTestReports(List<TestReport> reports, String signingUserId, UserDetailsImpl user) throws JdxServiceException {

        int reportSize = reports.size();

        try {
            List<TestReport> reportsToSave = new ArrayList<>();
            List<Order> ordersToSave = new ArrayList<>();
            List<TestRun> runsToSave = new ArrayList<>();

            Optional<User> signatory = userService.findOne(signingUserId);
            if(signatory.isEmpty())
                throw new JdxServiceException("Cannot proceed with signout as the signatory is not found");

            if(!signatory.get().getAuthorities().stream().anyMatch(x->x.getName().equals(Authority.ROLE_LAB_DIRECTOR)))
            reports = testReportRepository.findAllById(reports.stream().map(x -> x.getId()).collect(Collectors.toList()));

            if(reportSize != reports.size())
                throw new JdxServiceException("Didn't find all test reports to sign out that was originally provided");

            for(TestReport report : reports) {
                //Sign out is an atomic action as of now, fail the entire batch if just one of the reports is not approved
           //TODO once approval is fully working, re-add this block of code.
           //     if(!report.isApproved())
           //         throw new JdxServiceException("Cannot sign out batch of results as report with ID " + report.getId() + " is not approved.");

                report.setMeta(updateMeta(report.getMeta(), user));
                report.setReportable(true);

                Optional<TestRun> testRun = testRunService.getTestRunForKitId(report.getTestRun().getKit().getId());
                if(testRun.isPresent()) {
                    testRun.get().addStatus(new LaboratoryStatus(LaboratoryStatusType.SIGNED_OUT, testRun.get(), true));
                    report.setTestRun(testRun.get());

                    runsToSave.add(testRun.get());

                    //Get attached objects
                    if (report.getReportType().equals(ReportType.STANDARD)) {

                        Optional<LaboratoryOrder> labOrder = laboratoryOrderService.findLaboratoryOrderByTestRun(testRun.get());

                        if (labOrder.isEmpty())
                            throw new JdxServiceException("Laboratory order " + report.getLaboratoryOrderId() + " cannot be found for report " + report.getId());

                        List<TestRun> allTestRunsInLabOrder = labOrder.get().getTestRuns();
                        for(TestRun r : allTestRunsInLabOrder){
                            if(r.getReport().isReportable() && !r.getReport().getId().equals(report.getId()))
                                throw new JdxServiceException("Cannot sign out test report " + report.getId() + " since it has other reportable reports for this lab order");
                        }

                        if(labOrder.get().getReportableTestReportId() != null && !labOrder.get().getReportableTestReportId().equals(report.getId()))
                            throw new JdxServiceException("Cannot sign out test report " + report.getId() + " since another report has been set as the reportable for this lab order. Resend signout with override set to true");

                        labOrder.get().setReportableTestReportId(report.getId());
                        labOrder.get().setReportableTestRunId(testRun.get().getId());
                        labOrder.get().setReportableTestRunId(testRun.get().getId());

                        report.setLaboratoryOrderId(labOrder.get().getId());
                        report.setLabId(labOrder.get().getLab().getId());
                        report.setOrderId(labOrder.get().getParentOrderId());
                        report.setDeliveredToPatient(true);
                        report.setDeliveredToPatientAt(Calendar.getInstance());
                        report.setDeliveredToProvider(true);



                        Optional<Order> order = orderRepository.findById(report.getOrderId());
                        if (order.isEmpty())
                            throw new JdxServiceException("Cannot find associated order for this report");

                        report.setOrderNumber(order.get().getOrderNumber());

                        order.get().addOrderStatus(new OrderStatus(OrderStatusType.RESULTS_AVAILABLE, true, order.get()));
                        order.get().setMeta(updateMeta(order.get().getMeta(), user));
                        ordersToSave.add(order.get());
                    }

                    report.setSignedOut(true);
                    Signout signoutDetails = new Signout();
                    signoutDetails.setSignatory(signatory.get());
                    signoutDetails.setSignedOutAt(Calendar.getInstance());

                    report.setSignoutDetails(signoutDetails);
                    report.setAvailable(true);
                    report.setReportable(true);
                    report.setEstimatedToBeAvailableAt(Calendar.getInstance());
                    report.setFirstAvailableAt(Calendar.getInstance());

                    reportsToSave.add(report);
                }
            }

            reportsToSave = testReportRepository.saveAll(reportsToSave);
            runsToSave = testRunRepository.saveAll(runsToSave);

            if(ordersToSave != null && ordersToSave.size() > 0) {
                ordersToSave = orderRepository.saveAll(ordersToSave);
                for(Order o : ordersToSave){
                    sendTestReportAvaialbleEmail(o);
                }
            }

            //TODO add change events here to kick off other signout workflows

            return reportsToSave;

        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot sign out reports");
        }
    }

    public List<TestReport> requestReTestForTestReports(List<TestReport> reports, RetestActionType retestAction, boolean noResult, String notes, String authorizingUserId, UserDetailsImpl user) throws JdxServiceException {
        int reportSize = reports.size();

        try {
            List<TestReport> reportsToSave = new ArrayList<>();
            List<Order> ordersToSave = new ArrayList<>();
            List<TestRun> runsToSave = new ArrayList<>();
            List<BatchRun> batchesToSave = new ArrayList<>();

            Optional<User> signatory = userService.findOne(authorizingUserId);
            if(signatory.isEmpty())
                throw new JdxServiceException("Cannot proceed with retest ticket as the approver is not found");

            //TODO external this so that the ROLE_LAB_SUPERVISOR can perform this function as well
       //     if(!signatory.get().getAuthorities().stream().anyMatch(x->x.getName().equals(Authority.ROLE_LAB_DIRECTOR)))

            reports = testReportRepository.findAllById(reports.stream().map(x -> x.getId()).collect(Collectors.toList()));

            if(reportSize != reports.size())
                throw new JdxServiceException("Didn't find all test reports to set to retest that was originally provided");

            for(TestReport report : reports) {
                //Retest out is an atomic action as of now

                report.setMeta(updateMeta(report.getMeta(), user));

                BatchRun batch = null;

                Optional<TestRun> testRun = testRunService.getTestRunForKitId(report.getTestRun().getKit().getId());
                if(testRun.isPresent()) {
                    testRun.get().addStatus(new LaboratoryStatus(LaboratoryStatusType.REFLEX, testRun.get(), true));
                    report.setTestRun(testRun.get());

                    batch = report.getTestRun().getBatch();

                    runsToSave.add(testRun.get());
                }

                    //Get attached objects
                    /*
                    //Consider updating the estimated report availability time here, otherwise order status doesn't change as it is still lab processing
                    if (report.getReportType().equals(ReportType.STANDARD)) {

                        Optional<LaboratoryOrder> labOrder = laboratoryOrderService.findLaboratoryOrderByTestRun(testRun.get());
                        //
                        //Optional<User> patient = userService.findOne(report.getPatient().getId());

                        if (labOrder.isEmpty())
                            throw new JdxServiceException("Laboratory order " + report.getLaboratoryOrderId() + " cannot be found for report " + report.getId());


                        report.setLaboratoryOrderId(labOrder.get().getId());
                        report.setLabId(labOrder.get().getLab().getId());
                        report.setOrderId(labOrder.get().getParentOrderId());

                        Optional<Order> order = orderRepository.findById(report.getOrderId());
                        if (order.isEmpty())
                            throw new JdxServiceException("Cannot find associated order for this report");

                        order.get().addOrderStatus(new OrderStatus(OrderStatusType., true, order.get()));
                        order.get().setMeta(updateMeta(order.get().getMeta(), user));
                        ordersToSave.add(order.get());
                    }
                    */

                //TODO refactor this and the front-end to take 'rejected' or check on logic
                report.setApproved(true);

                report.setSignedOut(true);
                Signout signoutDetails = new Signout();
                signoutDetails.setSignatory(signatory.get());
                signoutDetails.setSignedOutAt(Calendar.getInstance());

                signoutDetails.setNonReportable(noResult);

                report.setSignoutDetails(signoutDetails);
                report.setAvailable(false);
                Calendar now = Calendar.getInstance();
                Calendar inTwoDays = Calendar.getInstance();
                inTwoDays.set(Calendar.DATE, +2);
                report.setEstimatedToBeAvailableAt(inTwoDays);
                report.setFirstAvailableAt(inTwoDays);

                report.setRetestRequested(true);
                report.setRetestRequestDate(now);
                report.setRetestRequester(signatory.get().getFirstName() + " " + signatory.get().getLastName());

                //TODO do some magic to call LIMS with a new Retest ticket

                reportsToSave.add(report);

                BatchRun finalBatch = batch;
                //Look in the batch list for an existing batch to check for finality
                if(batch != null && !batchesToSave.stream().anyMatch(x -> x.getId().equals(finalBatch.getId())))
                    batchesToSave.add(batch);
            }

            reportsToSave = testReportRepository.saveAll(reportsToSave);
            runsToSave = testRunRepository.saveAll(runsToSave);

            if(ordersToSave != null && ordersToSave.size() > 0)
                ordersToSave = orderRepository.saveAll(ordersToSave);

            //Check batches to see if they are now complete and can be set to reviewed fully
            List<BatchRun> batchesUpdated = new ArrayList<>();
            if(batchesToSave != null && batchesToSave.size() > 0){
                for(BatchRun batch : batchesToSave){
                    long fullyReviewed = testReportRepository.countOfUnapprovedResultsInBatch(batch.getId());
                    if(fullyReviewed == 0) {
                        BatchRun tmp = batch;
                        tmp.setReviewed(true);
                        tmp.setReviewedAt(Calendar.getInstance());
                        batchesUpdated.add(tmp);
                        logger.info("All reports in batch have been reviewed");
                    } else
                        logger.info("Batch still has reports awaiting review");

                }
            }

            if(batchesUpdated != null && batchesUpdated.size() > 0)
                batchesUpdated = batchRunService.updateAll(batchesUpdated, user);

            //TODO add change events here to kick off other signout workflows

            return reportsToSave;

        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot sign out reports");
        }
    }

    public List<TestReport> approveTestReports(List<TestReport> reports, String signingUserId, UserDetailsImpl user) throws JdxServiceException {

        int reportSize = reports.size();

        try {
            List<TestReport> reportsToSave = new ArrayList<>();
            List<Order> ordersToSave = new ArrayList<>();
            List<TestRun> runsToSave = new ArrayList<>();
            List<BatchRun> batchesToSave = new ArrayList<>();

            Optional<User> signatory = userService.findOne(signingUserId);
            if(signatory.isEmpty())
                throw new JdxServiceException("Cannot proceed with approval as the approving user is not found");

            if(!signatory.get().getAuthorities().stream().anyMatch(x->x.getName().equals(Authority.ROLE_LAB_DIRECTOR)))
                reports = testReportRepository.findAllById(reports.stream().map(x -> x.getId()).collect(Collectors.toList()));

            if(reportSize != reports.size())
                throw new JdxServiceException("Didn't find all test reports to approve that was originally provided");

            for(TestReport report : reports) {
                report.setMeta(updateMeta(report.getMeta(), user));

                Optional<TestRun> testRun = testRunService.getTestRunForKitId(report.getTestRun().getKit().getId());
                if(testRun.isPresent()) {
                    testRun.get().addStatus(new LaboratoryStatus(LaboratoryStatusType.APPROVED, testRun.get(), true));
                    report.setTestRun(testRun.get());

                    runsToSave.add(testRun.get());

                    BatchRun finalBatch = testRun.get().getBatch();
                    //Look in the batch list for an existing batch to check for finality
                    if(finalBatch != null && !batchesToSave.stream().anyMatch(x -> x.getId().equals(finalBatch.getId())))
                        batchesToSave.add(finalBatch);

                    report.setApproved(true);

                    reportsToSave.add(report);
                }
            }

            reportsToSave = testReportRepository.saveAll(reportsToSave);
            runsToSave = testRunRepository.saveAll(runsToSave);

            List<BatchRun> batchesUpdated = new ArrayList<>();
            if(batchesToSave != null && batchesToSave.size() > 0){
                for(BatchRun batch : batchesToSave){
                    long fullyReviewed = testReportRepository.countOfUnapprovedResultsInBatch(batch.getId());
                    if(fullyReviewed == 0) {
                        BatchRun tmp = batch;
                        tmp.setReviewed(true);
                        tmp.setReviewedAt(Calendar.getInstance());
                        batchesUpdated.add(tmp);
                        logger.info("All reports in batch have been reviewed");
                    } else
                        logger.info("Batch still has reports awaiting review");

                }
            }

            if(batchesUpdated != null && batchesUpdated.size() > 0)
                batchesUpdated = batchRunService.updateAll(batchesUpdated, user);

            //TODO add change events here to kick off other signout workflows

            return reportsToSave;

        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot approve reports");
        }
    }

    public TestReport createTestReport(String pipelineRunId, TestReport report, UserDetailsImpl user) throws JdxServiceException {
        /*
        1.) Validate test report before processing it
        2.) Is the batch provided legit already? If not, do we want to create it now or fail?
        3.) Determine the type of report it is, research, control or standard
        4.) If research, there won't be an order nor a test run already, so need to create test run for it and assign that to the batch. denote as research in the TestReport
        5.) If control, there won't be an order too, need to create test run and assign to batch, denote as control in the TestReport
        6.) if Standard, find the parent labOrder and parentOrder and ensure that its valid
            6a.) Is this report for a sample with at a known test run already, if not, create one
            6b.) Is this report for a sample that is a reflex or redraw against the same order

         */
        try {
            if (report == null || pipelineRunId == null)
                throw new JdxServiceException("Report to save is unreadable");
            else {
                //Validate the incoming data first
                try {
                    validateResultData(report.getReportConfiguration(), report.getResultData());
                } catch (Exception e) {
                    throw new JdxServiceException("Result contains invalid result data, " + e.getMessage());
                }

                //Determine the signout type for each report regardless of type
                if (report.getSignedOutType() == null)
                    resolveSignoutType(report);

                List<TestReport> reportsDao = new ArrayList<>();
                Optional<BatchRun> batch = batchRunService.findByPipelineRunId(pipelineRunId);
                BatchRun batchRun = null;
                if (batch.isPresent())
                    batchRun = batch.get();
                else
                    throw new JdxServiceException("Cannot create test report without first creating or providing a batch that it ran in");

                try {
                    //Check to see if the test report already exists for this sample so that we don't create another one
                    Optional<TestReport> existingReport = testReportRepository.findTestReportBySampleNumberAndPipelineRunId(report.getSampleNumber(), pipelineRunId);
                    if (existingReport.isPresent())
                        return existingReport.get();
                } catch (NonUniqueResultException e) {
                    throw new JdxServiceException("Test Report for sample " + report.getSampleNumber() + " against batch " + pipelineRunId + " already exists ");
                }

                //For the batch that this sample belongs to, set it back to not reviewed since we're adding a new Test Report to it
                batchRun.setReviewed(false);
                batchRun.setReviewedAt(null);

                //If the report has control or research information, then leverage those updates, otherwise resolve if this is a standard sample or not
                //Only processing reports that are posted explicitly as control or research, otherwise everything is standard
                if (report.isControl())
                    return saveSingleTestReportAsControl(batchRun, Optional.empty(), Optional.empty(), report, user);
                else if (report.isResearchSample())
                    return saveSingleTestReportAsResearch(batchRun, Optional.empty(), Optional.empty(), report, user);
                else {
                    //Find the kit associated with this sample number, must be unique
                    Optional<Kit> oKit = kitRepository.findKitBySampleNumber(report.getSampleNumber());

                    if (oKit.isPresent()) {
                        Kit kit = oKit.get();

                        //Change this to find the tesRun for the kit where the batchId matches the testrun batchId field, that's the right one to choose.
                        //TODO This isn't working because the Test Run isn't assigned to the batch until the sample is submitted with the report - FIX!!!!!!
                        //Optional<TestRun> run = testRunService.getTestRunForKitAndBatchId(kit.getId(), batchRun.getId());
                        Optional<TestRun> run = testRunService.getTestRunForKitId(kit.getId());

                        if (run.isPresent()) {
                            Optional<LaboratoryOrder> labOrder = laboratoryOrderService.findLaboratoryOrderByTestRun(run.get());

                            if (labOrder.isPresent())
                                return saveSingleTestReportWithOrder(batchRun, run.get(), kit, report, labOrder, user);
                            else {
                                //TODO might want to check for the labOrder before sending here?
                                //This is a research sample with no previous report attached to this found test run
                                //return saveSingleTestReportAsResearch(batchRun, run, Optional.of(kit), report, user);
                                throw new JdxServiceException("No lab order associated with the test run associated with this sample: " + report.getSampleNumber());
                            }
                        } else
                            throw new JdxServiceException("Run must be created for kit before a result can be registered for the sample, kit code: " + oKit.get().getCode());
                    } else
                        throw new JdxServiceException("Not kit can be found associated with the provided sample Id in this report : " + report.getSampleNumber());

                    //If no associated kit and test run, then default to reseach sample
                    //return saveSingleTestReportAsResearch(batchRun, Optional.empty(), Optional.empty(), report, user);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot create new test report: " + e.getMessage());
        }
    }

    public List<TestReport> createTestReports(String batchId, List<TestReport> reports, UserDetailsImpl user) throws JdxServiceException {

        //TODO remove this dependency and allow this method to handle 1 or more reports up to a configurable limit on post
        if (reports.size() == 1) {
            List<TestReport> results = new ArrayList<>();
            results.add(createTestReport(batchId, reports.get(0), user));
            return results;
        }

        List<TestReport> reportsDao = new ArrayList<>();
        Optional<BatchRun> batch = batchRunService.findByPipelineRunId(batchId);
        BatchRun batchRun = null;
        if(batch.isPresent())
            batchRun = batch.get();
        else {
            batchRun = new BatchRun();
            batchRun.setPipelineRunId(batchId);
        }

        List<TestReport> returnTestReports = new ArrayList<>();

        if (reports != null && reports.size() > 0) {
            List<TestRun> testRuns = new ArrayList<>();
            List<TestRun> testRunsToCreate = new ArrayList<>();
            for (TestReport report : reports) {

         /*
                if(report != null) {
                    if(report.isControl())
                        returnTestReports.add(saveSingleTestReportAsControl(batchRun, report, user));
                    else if(report.isResearchSample())
                        returnTestReports.add(saveSingleTestReportAsResearch(batchRun, Optional.empty(), Optional.empty(), report, user));
                    else {
                        Optional<Kit> kit = kitRepository.findKitBySampleNumber(report.getSampleNumber());
                        if (kit.isPresent()){
                            Optional<TestRun> run = testRunService.getTestRunForKitId(kit.get().getId());
                            if (run.isPresent() && !report.isNoOrder()) {
                                run.get().setBatch(batchRun);
                                returnTestReports.add(saveSingleTestReportWithOrder(run.get(), kit.get(), report, user));
                            }
                            else {
                                run.get().setBatch(batchRun);
                                returnTestReports.add(saveSingleTestReportWithOrder(run.get(), kit.get(), report, user));
                            }
                        }
                    }
                }

 */

            }
        }
        return returnTestReports;
    }

    protected TestReport saveSingleTestReportWithOrder(BatchRun batchRun, TestRun run, Kit kit, TestReport report, Optional<LaboratoryOrder> laboratoryOrder, UserDetailsImpl user) throws JdxServiceException {
        if (report != null && report.getSampleNumber() != null && run != null && kit != null) {
            Calendar NOW = Calendar.getInstance();

            if (laboratoryOrder.isEmpty())
                throw new JdxServiceException("Laboratory Order must be present for this Kit before the report can be published");
            else {
                //Grab the parent order associated with this laboratoryOrder
                Optional<Order> order = orderRepository.findById(laboratoryOrder.get().getParentOrderId());

                if(order.isEmpty())
                    throw new JdxServiceException("Cannot find parent order associated with sample " + report.getSampleNumber());



                Optional<OrderLineItem> lineItem = order.get().getLineItems().stream().filter(x->x.getLaboratoryOrderDetails().getId().equals(laboratoryOrder.get().getId())).findFirst();
                if(lineItem.isEmpty())
                    throw new JdxServiceException("Cannot find line item associated with laboratory order for sample " + report.getSampleNumber());

                report.setReportType(ReportType.STANDARD);

                return finalizeAndSaveReport(report, batchRun, run, kit, laboratoryOrder.get().getLab(), Optional.of(laboratoryOrder.get().getPatient()), laboratoryOrder, order, user, false, false);
            }
        }

        throw new JdxServiceException("Cannot create test report");
    }

    protected TestReport saveSingleTestReportWithOrder(TestReport report, UserDetailsImpl user) throws JdxServiceException {
        if (report != null && report.getSampleNumber() != null) {
            Optional<Kit> kit = kitRepository.findKitBySampleNumber(report.getSampleNumber());
            if (!kit.isPresent())
                throw new JdxServiceException("Sample Number does not associate to a known Kit");
            else {
                Optional<TestRun> run = testRunService.getTestRunForKitId(kit.get().getId());
                if (!run.isPresent()) {
                    throw new JdxServiceException("Test run must already be established for a report to be publishable");
                } else {
                    report.setTestRun(run.get());
                    run.get().addStatus(new LaboratoryStatus(LaboratoryStatusType.RESULT_AWAITING_REVIEW, run.get(), true));
                    Optional<LaboratoryOrder> laboratoryOrder = laboratoryOrderService.findLaboratoryOrderByTestRun(run.get());
                    if (!laboratoryOrder.isPresent())
                        throw new JdxServiceException("Laboratory Order must be present for this Kit before the report can be published");
                    else {
                        run.get().setLaboratoryOrder(laboratoryOrder.get());
                        report.getTestRun().setLaboratoryOrder(laboratoryOrder.get());

                        Optional<OrderLineItem> lineItem = orderLineItemRepository.findOrderLineItemByLaboratoryOrderDetails_Id(laboratoryOrder.get().getId());
                        if (!lineItem.isPresent())
                            throw new JdxServiceException("Order and at least one line item must exist for this report");
                        else {
                            laboratoryOrder.get().setOrderLineItem(lineItem.get());
                            Optional<Order> order = orderRepository.findOrderByLineItems(lineItem.get());
                            if (order.isPresent()) {
                                order.get().addOrderStatus(new OrderStatus(OrderStatusType.LABORATORY_PROCESSING, true, order.get()));
                                //          report.setTestRun(run.get());
                                report.setPatient(laboratoryOrder.get().getPatient());
                                report.setOrderId(order.get().getId());
                                report.setLaboratoryOrderId(laboratoryOrder.get().getId());
                                //          report.setLaboratoryOrderId(laboratoryOrder.get().getId());
                                report.getResultData().setReport(report);
                                logger.info("Trying to create report for reportName " + report.getResultData().getReportName());
                            } else
                                throw new JdxServiceException("Order must exist for this sample and reoprt");
                        }
                    }

                }
            }

            report.setMeta(buildMeta(user));

            //TODO update the counters via the Stats object when creating test reports

            return testReportRepository.save(report);
        }

        throw new JdxServiceException("Cannot create test report");
    }

    protected TestReport saveSingleTestReportAsControl(BatchRun batchRun, Optional<TestRun> foundRun, Optional<Kit> foundKit, TestReport report, UserDetailsImpl user) throws JdxServiceException {
        try {
            if (report != null && report.getSampleNumber() != null) {
                boolean newKit = false;
                boolean newTestRun = false;
                Kit k = null;
                TestRun r = null;

                if (foundKit.isPresent())
                    k = foundKit.get();
                else {
                    k = new Kit();
                    k.setSampleNumber(report.getSampleNumber());
                    //k.makeControlKit();
                    k.setCode(report.getSampleNumber());
                    k.setAssigned(true);
                    k.setMeta(buildMeta(user));
                    k.setActivated(true);
                    newKit = true;
                }

                if (foundRun.isPresent())
                    r = foundRun.get();
                else {
                    r = new TestRun();
                    //r.makeResearchRun(k);
                    r.setType(TestRunType.CONTROL);
                    r.setReportConfiguration(report.getReportConfiguration());
                    r.setEndTime(Calendar.getInstance());
                    r.setCompleted(false);
                    r.setBatch(batchRun);
                    newTestRun = true;
                }

                //make sure that this testrun doesn't have a parent order
                //  Optional<LaboratoryOrder> labOrder = laboratoryOrderService.findLaboratoryOrderByTestRun(r);
                //  if (labOrder.isPresent())
                //      return saveSingleTestReportWithOrder(batchRun, r, k, report, labOrder, user); //throw new JdxServiceException("Cannot add this as a research project as a Lab Order exists for the used KitId");

                Optional<Laboratory> oLaboratory = laboratoryService.getDefaultLaboratory();
                Laboratory laboratory = null;
                if(oLaboratory.isPresent())
                    laboratory = oLaboratory.get();

                report.setResearchSample(false);
                report.setControl(true);
                report.setReportType(ReportType.CONTROL);
                report.setNoOrder(true);

                logger.info("Saving control report " + report.getSampleNumber());

                return finalizeAndSaveReport(report, batchRun, r, k, laboratory, Optional.empty(), Optional.empty(), Optional.empty(), user, newKit, newTestRun);
            }
        } catch( Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot create new research sample " + e.getMessage());
        }

        throw new JdxServiceException("Cannot create test report");
    }

    protected TestReport saveSingleTestReportAsResearch(BatchRun batchRun, Optional<TestRun> foundRun, Optional<Kit> foundKit, TestReport report, UserDetailsImpl user) throws JdxServiceException {
        try {
            if (report != null && report.getSampleNumber() != null) {
                boolean newKit = false;
                boolean newTestRun = false;
                Kit k = null;
                TestRun r = null;

                if (foundKit.isPresent())
                    k = foundKit.get();
                else {
                    k = new Kit();
                    k.setSampleNumber(report.getSampleNumber());
                    //k.makeControlKit();
                    k.setCode(report.getSampleNumber());
                    k.setAssigned(true);
                    k.setMeta(buildMeta(user));
                    k.setActivated(true);
                    newKit = true;
                }

                if (foundRun.isPresent())
                    r = foundRun.get();
                else {
                    r = new TestRun();
                    //r.makeResearchRun(k);
                    r.setType(TestRunType.RESEARCH);
                    r.setReportConfiguration(report.getReportConfiguration());
                    r.setEndTime(Calendar.getInstance());
                    r.setCompleted(false);
                    r.setBatch(batchRun);
                    newTestRun = true;
                }

                //make sure that this testrun doesn't have a parent order
              //  Optional<LaboratoryOrder> labOrder = laboratoryOrderService.findLaboratoryOrderByTestRun(r);
              //  if (labOrder.isPresent())
              //      return saveSingleTestReportWithOrder(batchRun, r, k, report, labOrder, user); //throw new JdxServiceException("Cannot add this as a research project as a Lab Order exists for the used KitId");

                Optional<Laboratory> oLaboratory = laboratoryService.getDefaultLaboratory();
                Laboratory laboratory = null;
                if(oLaboratory.isPresent())
                    laboratory = oLaboratory.get();

                report.setResearchSample(true);
                report.setReportType(ReportType.RESEARCH);
                report.setNoOrder(true);

                logger.info("Saving research report " + report.getSampleNumber());

                return finalizeAndSaveReport(report, batchRun, r, k, laboratory, Optional.empty(), Optional.empty(), Optional.empty(), user, newKit, newTestRun);
            }
        } catch( Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot create new research sample " + e.getMessage());
        }

        throw new JdxServiceException("Cannot create test report for research sample");
    }

    protected TestReport finalizeAndSaveReport(TestReport report, BatchRun batchRun, TestRun run, Kit kit, Laboratory laboratory, Optional<User> patient, Optional<LaboratoryOrder> labOrder, Optional<Order> order, UserDetailsImpl updater, boolean newKit, boolean newTestRun) throws JdxServiceException {
        try {
            report.setAvailable(false);
            report.setFirstAvailableAt(Calendar.getInstance());
            report.setEstimatedToBeAvailableAt(Calendar.getInstance());
            report.getResultData().setReport(report);

            //Set up
            report.setApproved(false);
            report.setSignedOut(false);
            report.setSignoutDetails(null);
            report.setDeliveredToPatient(false);
            report.setDeliveredToProvider(false);
            report.setRetestRequested(false);

            if(batchRun != null && run != null && kit != null) {
                report.setTestRun(run);
                report.setBatchRunId(batchRun.getId());
                report.setPipelineRunId(batchRun.getPipelineRunId());

                run.setBatch(batchRun);
                run.setCompleted(true);
                run.addStatus(new LaboratoryStatus(LaboratoryStatusType.RESULT_AWAITING_REVIEW, run, true));

                kit.setTestRun(run);
                run.setKit(kit);

            } else
                throw new JdxServiceException("Cannot save test report since the batchRun or testRun is not available");

            //If order is not present, labOrder and patient should not be as well
            if(order.isPresent() && labOrder.isPresent() && patient.isPresent()) {
                //Process order stuff first
                report.setOrderId(order.get().getId());
                order.get().addOrderStatus(new OrderStatus(OrderStatusType.RESULTS_IN_REVIEW, true, order.get()));
                order.get().setResultsAvailable(true);
                order.get().setRequiresShipment(false);

                //TODO update this code when > 1 lineitems is supported
                if(order.get().getLineItems() != null && order.get().getLineItems().size() == 1 && order.get().getLineItems().get(0).getCurrentFulfillmentId() != null) {
                    Optional<Fulfillment> currentFulfillment = order.get().getLineItems().get(0).getFulfillments().stream().filter(x->x.getId().equals(order.get().getLineItems().get(0).getCurrentFulfillmentId())).findFirst();
                    if(currentFulfillment.isPresent()) {
                        currentFulfillment.get().setCompleted(true);
                        currentFulfillment.get().setMeta(updateMeta(currentFulfillment.get().getMeta(), updater));
                    }
                }
                else
                    throw new JdxServiceException("Cannot process > 1 report and line item combination at this time");

                labOrder.get().setReportableTestReportId(report.getId());
                labOrder.get().setReportableTestRunId(run.getId());

                //Process report stuff next
                report.setPatient(patient.get());
                report.setLaboratoryOrderId(labOrder.get().getId());
                //Process labOrder last
            } else {
                //throw new JdxServiceException("Issue finalizing the report for writing as the order and patient information is invalid");
                report.setOrderId(null);
                report.setPatient(null);
                report.setLaboratoryOrderId(null);
            }

            if(laboratory != null)
                report.setLabId(laboratory.getId());


            Calendar NOW = Calendar.getInstance();
            report.setAvailable(false);
            report.setFirstAvailableAt(NOW);
            NOW.add(Calendar.HOUR, 6);
            report.setEstimatedToBeAvailableAt(NOW);
            report.setMeta(buildMeta(updater));


            if(batchRun.getSequencingRunId() == null)
                batchRun = batchRunService.save(batchRun, updater);

            if (newKit) {
                kit.setMeta(buildMeta(updater));
                kit = kitRepository.save(kit);
            }
            if (newTestRun)
                run = testRunRepository.save(run);

            return testReportRepository.save(report);


        } catch( Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot create new research sample " + e.getMessage());
        }
    }

    private void resolveSignoutType(TestReport report){
        Report r = report.getResultData();
        //This is manual if any of the following conditions are met
        if(r.getQc() == null)
            throw new JdxServiceException("Cannot determine signout type since no QC data can be found");

        report.setSignedOutType(SignedOutType.AUTOMATIC);

        if(!r.getQc().isPassed())
            report.setSignedOutType(SignedOutType.MANUAL);

        if(report.getReportConfiguration().equals(ReportConfiguration.NIPS_BASIC) || report.getReportConfiguration().equals(ReportConfiguration.NIPS_PLUS) ||
                report.getReportConfiguration().equals(ReportConfiguration.NIPS_ADVANCED)) {
            if (r.getData().getT13() != null && r.getData().getT13().getCall() != null && r.getData().getT13().getCall())
                report.setSignedOutType(SignedOutType.MANUAL);
            if (r.getData().getT18() != null && r.getData().getT18().getCall() != null && r.getData().getT18().getCall())
                report.setSignedOutType(SignedOutType.MANUAL);
            if (r.getData().getT21() != null && r.getData().getT21().getCall() != null && r.getData().getT21().getCall())
                report.setSignedOutType(SignedOutType.MANUAL);
            if (r.getData().getSca() != null && !r.getData().getSca().getScaResult().equals(SCAResultType.XX) &&
                    !r.getData().getSca().getScaResult().equals(SCAResultType.XY))
                report.setSignedOutType(SignedOutType.MANUAL);
        } else if(report.getReportConfiguration().equals(ReportConfiguration.FST) && r.getData().getFst() != null) {
            //If the result is not conclusively a male or female
            if(!r.getData().getFst().getGenderResult().equals(GenderResultType.MALE) && !r.getData().getFst().getGenderResult().equals(GenderResultType.FEMALE))
                report.setSignedOutType(SignedOutType.MANUAL);
           // else if(!r.getData().getFst().getScaResult().equals(SCAResultType.XX) && !r.getData().getFst().getScaResult().equals(SCAResultType.XY))
           //     return SignedOutType.MANUAL;
            //If we have a low confidence result
            else if(r.getConfidenceIndex() != null && r.getConfidenceIndex().equals(ConfidenceIndexType.LOW))
                report.setSignedOutType(SignedOutType.MANUAL);
            //If low reads is true, then we have an issue
            else if(r.getQc().getLowReads() != null && r.getQc().getLowReads())
                report.setSignedOutType(SignedOutType.MANUAL);
            //if this failed fragment distribution
            else if(r.getQc().getFragmentDistribution() != null && !r.getQc().getFragmentDistribution())
                report.setSignedOutType(SignedOutType.MANUAL);
        }
    }

    public void delete(String reportId, UserDetailsImpl user) throws JdxServiceException{
        try {
            //Optional<TestReport> report = testReportRepository.findById(reportId);
            //if (report.isPresent())
                testReportRepository.deleteById(reportId);
            //else
              //  throw new JdxServiceException("Cannot delete test report with id " + reportId);
        } catch(Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot delete test report with id " + reportId);
        }
    }

    public void validateResultData(ReportConfiguration config, Report report) throws JdxServiceException {
        if(config == null || report == null)
            throw new JdxServiceException("Cannot validate report data as the report is null or corrupt");

        switch(config) {
            case FST: validateFSTReport(report);
            break;
            case NIPS_BASIC: validateNIPSReport(config, report);
            break;
            case NIPS_PLUS: validateNIPSReport(config, report);
            break;
            case NIPS_ADVANCED: validateNIPSReport(config, report);
            break;
            default:
                throw new JdxServiceException("Cannot resolve test report configuration in order to validate results");
        }

    }

    public void validateNIPSReport(ReportConfiguration config, Report report) throws JdxServiceException {
        if(report.getData() == null)
            throw new JdxServiceException("Test Report does not contain any valid data");
        //Cannot contain FST data
        if(report.getData().getFst() != null) {
            report.getData().setFst(null);
            throw new JdxServiceException("Cannot support FST payload for report of type NIPS");
        }

        //Must contain trisomy QC data
        if(report.getQc().getC13Snr() == null)
            throw new JdxServiceException("chr13Snr boolean is not present for NIPS result");
        if(report.getQc().getChr13Sens() == null)
            throw new JdxServiceException("chr13Sens value is not present for NIPS result");
        if(report.getQc().getC18Snr() == null)
            throw new JdxServiceException("chr18Snr boolean is not present for NIPS result");
        if(report.getQc().getChr18Sens() == null)
            throw new JdxServiceException("chr18Sens value is not present for NIPS result");
        if(report.getQc().getC21Snr() == null)
            throw new JdxServiceException("chr21Snr boolean is not present for NIPS result");
        if(report.getQc().getChr21Sens() == null)
            throw new JdxServiceException("chr21Sens value is not present for NIPS result");
        if(report.getQc().getLowReads() == null)
            throw new JdxServiceException("Low reads boolean is not present for NIPS result");
        if(report.getQc().getRawCounts() == null)
            throw new JdxServiceException("Raw count is not present for NIPS result");
        if(report.getQc().getSnpIdentity() == null)
            throw new JdxServiceException("SNP identify is not present for NIPS result");

        if(report.getData().getFetalFraction() == null)
            throw new JdxServiceException("Fetal Fraction Transformed is not present for NIPS result");

        //Must contain trisomy read data
        if(report.getData().getT13() == null) {
            throw new JdxServiceException("T13 data not present for a NIPS result");
        } else {
            if(report.getData().getT13().getCall() == null)
                throw new JdxServiceException("T13 call not present for a NIPS result");
            if(report.getData().getT13().getzScore() == null)
                throw new JdxServiceException("T13 zscore not present for a NIPS result");
            if(report.getData().getT13().getConfidenceLower() == null || report.getData().getT13().getConfidenceUpper() == null)
                throw new JdxServiceException("T13 confident interval not present for a NIPS result");
        }
        if(report.getData().getT18() == null) {
            throw new JdxServiceException("T18 data not present for a NIPS result");
        } else {
            if(report.getData().getT18().getCall() == null)
                throw new JdxServiceException("T18 call not present for a NIPS result");
            if(report.getData().getT18().getzScore() == null)
                throw new JdxServiceException("T18 zscore not present for a NIPS result");
            if(report.getData().getT18().getConfidenceLower() == null || report.getData().getT18().getConfidenceUpper() == null)
                throw new JdxServiceException("T18 confident interval not present for a NIPS result");
        }
        if(report.getData().getT21() == null) {
            throw new JdxServiceException("T21 data not present for a NIPS result");
        } else {
            if(report.getData().getT21().getCall() == null)
                throw new JdxServiceException("T21 call not present for a NIPS result");
            if(report.getData().getT21().getzScore() == null)
                throw new JdxServiceException("T21 zscore not present for a NIPS result");
            if(report.getData().getT21().getConfidenceLower() == null || report.getData().getT21().getConfidenceUpper() == null)
                throw new JdxServiceException("T21 confident interval not present for a NIPS result");
        }

        if(config.equals(ReportConfiguration.NIPS_PLUS) || config.equals(ReportConfiguration.NIPS_ADVANCED)){
            if(report.getData().getEuploid() == null)
                throw new JdxServiceException("Euploid data must be present for an NIPS result");
            if(report.getData().getSca() == null) {
                throw new JdxServiceException("SCA data should  be present in a NIPS Plus or Advanced result");
            } else {
                if(report.getData().getSca().getGenderResult() == null)
                    throw new JdxServiceException("SCA fetal sex result must  be present in a NIPS Plus or Advanced result");
                if(report.getData().getSca().getScaResult() == null)
                    throw new JdxServiceException("SCA result must  be present in a NIPS Plus or Advanced result");
                if(report.getData().getSca().getGenderConfidence() == null)
                    throw new JdxServiceException("SCA fetal sex confidence interval must  be present in a NIPS Plus or Advanced result");
                if(report.getData().getSca().getScaConfidence() == null)
                    throw new JdxServiceException("SCA confident interval must  be present in a NIPS Plus or Advanced result");
                if(report.getData().getSca().getxVec() == null)
                    throw new JdxServiceException("SCA x_vec result must  be present in a NIPS Plus or Advanced result");
                if(report.getData().getSca().getyVec() == null)
                    throw new JdxServiceException("SCA y_vec result must  be present in a NIPS Plus or Advanced result");
                if(report.getData().getSca().getyVec2() == null)
                    throw new JdxServiceException("SCA y_vec2 result must  be present in a NIPS Plus or Advanced result");
                if(report.getData().getSca().getXzScores() == null)
                    throw new JdxServiceException("SCA xz_scores result must  be present in a NIPS Plus or Advanced result");
            }
        } else {
            if(report.getData().getSca() != null) {
                throw new JdxServiceException("SCA data should not be present in a NIPS basic result");
            }
        }
    }

    public void validateFSTReport(Report report) throws JdxServiceException {
        //Looking for the presence fst test and nothing else
        if(report.getData() != null){
            if(report.getData().getFst() == null)
                throw new JdxServiceException("FST data not present for a Fetal Sex result");
            if(report.getData().getEuploid() != null) {
                report.getData().setEuploid(null);
                throw new JdxServiceException("Euploid data must not be present for a FST result");
            }
            if(report.getData().getT13() != null) {
                report.getData().setT13(null);
                throw new JdxServiceException("T13 data must not be present for a FST result");
            }
            if(report.getData().getT18() != null) {
                report.getData().setT18(null);
                throw new JdxServiceException("T18 data must not be present for a FST result");
            }
            if(report.getData().getT21() != null) {
                report.getData().setT21(null);
                throw new JdxServiceException("T21 data must not be present for a FST result");
            }

            if(report.getData().getSca() != null) {
                report.getData().setSca(null);
                throw new JdxServiceException("SCA data must not be present for a FST result");
            }


            //Check for presence of microdeletions and fail if present
        }
    }

    private int randomNum(){
        Random rand = new Random(); //instance of random class
        int lowerbound = 10000000;
        int upperbound = 15000000;
        //generate random values from 0-24
        int int_random = rand.nextInt();

        return int_random;
    }


    public List<TestReportsReviewResultsDto> getAllLabReviewStats(String afterDate) {
        Calendar date = null;
        if (StringUtils.hasText(afterDate)) {
            date = DateUtil.convertStringToCalendar(afterDate, DateUtil.DATE_FORMAT_YYYY_MM_DD);
        } else {
            date = DateUtil.nowCalendar();
            //Set the query back 7 days
            //date.set(Calendar.DATE, -7);
            date.set(Calendar.YEAR, -20);
        }

        //TODO refactor this to account for breaking out all lab results separately

        TestReportsReviewResultsDto resultsDto = new TestReportsReviewResultsDto();
        resultsDto.setRecentlySignedOut(testReportRepository.countRecentlySignedOut(null, date));
        resultsDto.setRecentlyAutomaticallySignedOut(testReportRepository.countRecentlySignedOutAutomatic(null, date));
        resultsDto.setRecentlyManuallySignedOut(testReportRepository.countRecentlySignedOutManual(null, date));
        resultsDto.setResultsAwaitingSignOutConfirmation(testReportRepository.countTestReportsRequiringConfirmation(null, date));
        resultsDto.setResultsAwaitingInvestigation(testReportRepository.countTestReportsRequiringReview(null, date));

        List<TestReportsReviewResultsDto> listOfResults = new ArrayList<>();
        listOfResults.add(resultsDto);

        return listOfResults;
    }

    public List<TestReportsReviewResultsDto> getTestReportResults(Optional<String> lab, String afterDate) {
        Calendar date = null;
        if (StringUtils.hasText(afterDate)) {
            date = DateUtil.convertStringToCalendar(afterDate, DateUtil.DATE_FORMAT_YYYY_MM_DD);
        } else {
            date = DateUtil.nowCalendar();
            //Set the query back 7 days
            date.set(Calendar.DATE, -7);
        }
        String labId = null;
        if(lab.isPresent())
            labId = lab.get();
        else
            return getAllLabReviewStats(afterDate);

        TestReportsReviewResultsDto resultsDto = new TestReportsReviewResultsDto();
        resultsDto.setRecentlySignedOut(testReportRepository.countRecentlySignedOut(labId, date));
        resultsDto.setRecentlyAutomaticallySignedOut(testReportRepository.countRecentlySignedOutAutomatic(labId, date));
        resultsDto.setRecentlyManuallySignedOut(testReportRepository.countRecentlySignedOutManual(labId, date));
        resultsDto.setResultsAwaitingSignOutConfirmation(testReportRepository.countTestReportsRequiringConfirmation(labId, date));
        resultsDto.setResultsAwaitingInvestigation(testReportRepository.countTestReportsRequiringReview(labId, date));

        List<TestReportsReviewResultsDto> listOfResults = new ArrayList<>();
        listOfResults.add(resultsDto);

        return listOfResults;
    }

    public TestReportsAwaitingReviewDto getAwaitingReports(String labId, String reportType) {
        String lab = null;
        if(labId != null)
            lab = labId;
        else {
            Optional<Laboratory> defaultLab = laboratoryService.getDefaultLaboratory();
            if(defaultLab.isPresent())
                lab = defaultLab.get().getId();
            else
                throw new JdxServiceException("Cannot find a laboratory to get review stats for");
        }

        ReportConfiguration reportConfiguration = ReportConfiguration.getType(reportType);
        if (reportConfiguration == null) {
            throw new JdxServiceException("Invalid report configuration type " + reportType);
        }
        TestReportsAwaitingReviewDto reviewDto = new TestReportsAwaitingReviewDto();
        reviewDto.setReportConfiguration(reportConfiguration);
        reviewDto.setTotalAwaitingReview(testReportRepository.countAwaitingReview(lab, reportConfiguration));
        reviewDto.setTotalRequiringAutomaticReview(testReportRepository.countRequiringAutomaticReview(lab, reportConfiguration));
        reviewDto.setTotalRequiringManualReview(testReportRepository.countRequiringManualReview(lab, reportConfiguration));
        return reviewDto;
    }

    public LaboratoryReviewStatisticsDto getAllAwaitingReports(String labId) {
        ReportConfiguration configs[] = ReportConfiguration.class.getEnumConstants();
        List<TestReportsAwaitingReviewDto> counts = new ArrayList<>();

        for(ReportConfiguration config : configs) {
            TestReportsAwaitingReviewDto reviewDto = new TestReportsAwaitingReviewDto();
            reviewDto.setReportConfiguration(config);
            reviewDto.setTotalAwaitingReview(testReportRepository.countAwaitingReview(labId, config));
            reviewDto.setTotalRequiringAutomaticReview(testReportRepository.countRequiringAutomaticReview(labId, config));
            reviewDto.setTotalRequiringManualReview(testReportRepository.countRequiringManualReview(labId, config));
            counts.add(reviewDto);
        }

        LaboratoryReviewStatisticsDto statsGroup = new LaboratoryReviewStatisticsDto();
        statsGroup.setLaboratoryId(labId);
        statsGroup.setReview(counts);

        return statsGroup;
    }

    public LaboratoryReviewStatisticsDto getAllAwaitingReports() {
        ReportConfiguration configs[] = ReportConfiguration.class.getEnumConstants();
        List<TestReportsAwaitingReviewDto> counts = new ArrayList<>();

        String lab = null;
        Optional<Laboratory> defaultLab = laboratoryService.getDefaultLaboratory();
        if (defaultLab.isPresent())
            lab = defaultLab.get().getId();
        else
            throw new JdxServiceException("Cannot find a laboratory to get review stats for");

        //TODO grab each labId and iterate through them to set the statsGroup id
        for(ReportConfiguration config : configs) {
            TestReportsAwaitingReviewDto reviewDto = new TestReportsAwaitingReviewDto();
            reviewDto.setReportConfiguration(config);
            reviewDto.setTotalAwaitingReview(testReportRepository.countAwaitingReview(lab, config));
            reviewDto.setTotalRequiringAutomaticReview(testReportRepository.countRequiringAutomaticReview(lab, config));
            reviewDto.setTotalRequiringManualReview(testReportRepository.countRequiringManualReview(lab, config));
            counts.add(reviewDto);
        }

        LaboratoryReviewStatisticsDto statsGroup = new LaboratoryReviewStatisticsDto();
        statsGroup.setLaboratoryId(lab);
        statsGroup.setReview(counts);

        return statsGroup;
    }

    public long countOfUnapprovedResultsInBatch(String batchId) throws JdxServiceException {
        return testReportRepository.countOfUnapprovedResultsInBatch(batchId);
    }

    public void sendTestReportAvaialbleEmail(Order order) {
        if(order != null && order.getLineItems().size() > 0) {
            String content = "Hi " + order.getCustomer().getFirstName() + ", your results are now available for " + order.getLineItems().get(0).getProductName() +  "\n";
            content += "Click here to return to junodx.com to log in and see your results";
            if(!mailService.sendEmail(order.getCustomer().getEmail(), "Junodx - You results are available", content, false, false))
                throw new JdxServiceException("Cannot send results avaialble email");
        }
    }

    public TestReport updateTestReportPatientViewedResult(String reportId, String patientId, UserDetailsImpl updater) throws JdxServiceException {
        if(reportId == null || patientId == null)
            throw new JdxServiceException("Cannot update viewed status for report as report and patient information not provided");

        Optional<TestReport> report = testReportRepository.findById(reportId);
        if(report.isEmpty())
            throw new JdxServiceException("Not test report exists for this reportId: " + reportId);

        if(report.get().getPatient() != null && report.get().getPatient().getId() != null){
            if(!report.get().getPatient().getId().equals(patientId))
                throw new JdxServiceException("Report must be viewed by the patient who is the owner of this report in order to update viewed status");
        }

        report.get().setViewedByPatient(true);
        report.get().setViewedByPatientAt(Calendar.getInstance());
        report.get().setMeta(updateMeta(report.get().getMeta(), updater));

        if(report.get().getOrderId() != null) {
            OrderStatus status = new OrderStatus();
            status.setStatusType(OrderStatusType.RESULTS_VIEWED);

            orderService.updateOrderWithNewStatus(report.get().getOrderId(), status, updater);
        }

        return testReportRepository.save(report.get());
    }

}

