package com.junodx.api.repositories.lab;

import com.junodx.api.controllers.payloads.ReportConfigurationPayload;
import com.junodx.api.controllers.payloads.SampleIdListPayload;
import com.junodx.api.models.core.types.IntervalType;
import com.junodx.api.models.laboratory.BatchRun;
import com.junodx.api.models.laboratory.TestReport;
import com.junodx.api.models.laboratory.reports.types.SignedOutType;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.laboratory.types.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface TestReportRepository extends JpaRepository<TestReport, String> {
    Page<TestReport> findTestReportsByLabId(String labId, Pageable pageable);
    List<TestReport> findTestReportsByPatient_Id(String id);
    List<TestReport> findTestReportsBySampleNumber(String sampleNumber);
    List<TestReport> findTestReportsByBatchRunId(String batchRunId);

    Optional<TestReport> findTestReportBySampleNumberAndPipelineRunId(String sampleNumber, String pipelineRunId);

    Page<TestReport> findTestReportsByPatient_IdAndSignedOut(String patientId, boolean isSignedOut,  Pageable pageable);
    Page<TestReport> findTestReportsByPatient_IdAndSignedOutTypeAndReportConfigurationAndAvailable(String patientId, SignedOutType signedOutType, ReportConfiguration configuration, boolean isAvailable, Pageable pageable);
    Page<TestReport> findTestReportsByLaboratoryOrderIdAndSignedOutTypeAndReportConfigurationAndAvailable(String laboratoryOrderId, SignedOutType signedOutType, ReportConfiguration configuration, boolean isAvailable, Pageable pageable);
    Page<TestReport> findTestReportsBySampleNumberAndSignedOutTypeAndReportConfigurationAndAvailable(String sampleNumber, SignedOutType signedOutType, ReportConfiguration configuration, boolean isAvailable, Pageable pageable);

    @Query("select r from TestReport r where (:type is null or r.signedOutType = :type)" +
            " and (:signedOut is null or r.signedOut = :signedOut)" +
            " and (:patientId is null or r.patient.id = :patientId)" +
            " and (:firstName is null or r.patient.firstName = :firstName)" +
            " and (:lastName is null or r.patient.lastName = :lastName)" +
            " and (:email is null or r.patient.email = :email)" +
            " and (:sampleNumber is null or r.sampleNumber = :sampleNumber)" +
            " and (:batchRunId is null or r.batchRunId = :batchRunId)" +
            " and (:labId is null or r.labId = :labId)" +
            " and (:labOrderId is null or r.laboratoryOrderId = :labOrderId)" +
            " and (:orderNumber is null or r.orderNumber = :orderNumber)" +
            " and (:isAvailable is null or r.isAvailable = :isAvailable)" +
            " and (:isApproved is null or r.approved = :isApproved)" +
            " and (:report is null or r.reportConfiguration = :report)" +
            " and (:reportType is null or r.reportType = :reportType)" +
            " and (:reportable is null or r.reportable = :reportable)" +
            " and (cast(:after as timestamp) is null or r.completedAt >= :after)" +
            " order by r.completedAt ASC")
    Page<TestReport> searchOrderByCompletedAtASC(@Param("type") SignedOutType type,
                                                 @Param("signedOut") Boolean signedOut,
                                                 @Param("patientId") String patientId,
                                                 @Param("firstName") String firstName,
                                                 @Param("lastName") String lastName,
                                                 @Param("email") String email,
                                                 @Param("sampleNumber") String sampleNumber,
                                                 @Param("batchRunId") String batchRunId,
                                                 @Param("labId") String labId,
                                                 @Param("labOrderId") String labOrderId,
                                                 @Param("orderNumber") String orderNumber,
                                                 @Param("isAvailable") Boolean isAvailable,
                                                 @Param("isApproved") Boolean isApproved,
                                                 @Param("report") ReportConfiguration report,
                                                 @Param("reportType") ReportType reportType,
                                                 @Param("reportable") Boolean reportable,
                                                 @Param("after") Calendar after,
                                                 Pageable pageable);

    @Query("select r from TestReport r where (:type is null or r.signedOutType = :type)" +
            " and (:signedOut is null or r.signedOut = :signedOut)" +
            " and (:patientId is null or r.patient.id = :patientId)" +
            //" and (:firstName is null or r.patient.firstName = :firstName)" +
            //" and (:lastName is null or r.patient.lastName = :lastName)" +
            //" and (:email is null or r.patient.email = :email)" +
            " and (:sampleNumber is null or r.sampleNumber = :sampleNumber)" +
            " and (:batchRunId is null or r.batchRunId = :batchRunId)" +
            " and (:labId is null or r.labId = :labId)" +
            " and (:labOrderId is null or r.laboratoryOrderId = :labOrderId)" +
            //" and (:orderNumber is null or r.orderNumber = :orderNumber)" +
            " and (:isAvailable is null or r.isAvailable = :isAvailable)" +
            " and (:isApproved is null or r.approved = :isApproved)" +
            " and (:report is null or r.reportConfiguration = :report)" +
           // " and (:reportType is null or r.reportType = :reportType)" +
            " and (:reportable is null or r.reportable = :reportable)" +
            " and (cast(:after as timestamp) is null or r.completedAt >= :after)" +
            " order by r.completedAt DESC")
    Page<TestReport> searchOrderByCompletedAtDSC(@Param("type") SignedOutType type,
                                                 @Param("signedOut") Boolean signedOut,
                                                 @Param("patientId") String patientId,
                                                 //@Param("firstName") String firstName,
                                                 //@Param("lastName") String lastName,
                                                 //@Param("email") String email,
                                                 @Param("sampleNumber") String sampleNumber,
                                                 @Param("batchRunId") String batchRunId,
                                                 @Param("labId") String labId,
                                                 @Param("labOrderId") String labOrderId,
                                                 //@Param("orderNumber") String orderNumber,
                                                 @Param("isAvailable") Boolean isAvailable,
                                                 @Param("isApproved") Boolean isApproved,
                                                 @Param("report") ReportConfiguration report,
                                                 //@Param("reportType") ReportType reportType,
                                                 @Param("reportable") Boolean reportable,
                                                 @Param("after") Calendar after,
                                                 Pageable pageable);

    @Query("select r from TestReport r where (:type is null or r.signedOutType = :type)" +
            " and (:signedOut is null or r.signedOut = :signedOut)" +
            " and (:patientId is null or r.patient.id = :patientId)" +
            " and (:firstName is null or r.patient.firstName = :firstName)" +
            " and (:lastName is null or r.patient.lastName = :lastName)" +
            " and (:email is null or r.patient.email = :email)" +
            " and (:sampleNumber is null or r.sampleNumber = :sampleNumber)" +
            " and (:batchRunId is null or r.batchRunId = :batchRunId)" +
            " and (:labId is null or r.labId = :labId)" +
            " and (:labOrderId is null or r.laboratoryOrderId = :labOrderId)" +
            " and (:orderNumber is null or r.orderNumber = :orderNumber)" +
            " and (:isAvailable is null or r.isAvailable = :isAvailable)" +
            " and (:isApproved is null or r.approved = :isApproved)" +
            " and (:report is null or r.reportConfiguration = :report)" +
            " and (:reportType is null or r.reportType = :reportType)" +
            " and (:reportable is null or r.reportable = :reportable)" +
            " and (cast(:after as timestamp) is null or r.completedAt >= :after)" +
            " order by r.estimatedToBeAvailableAt ASC")
    Page<TestReport> searchOrderByEstimatedToBeAvailableAtASC(@Param("type") SignedOutType type,
                                                              @Param("signedOut") Boolean signedOut,
                                                              @Param("patientId") String patientId,
                                                              @Param("firstName") String firstName,
                                                              @Param("lastName") String lastName,
                                                              @Param("email") String email,
                                                              @Param("sampleNumber") String sampleNumber,
                                                              @Param("batchRunId") String batchRunId,
                                                              @Param("labId") String labId,
                                                              @Param("labOrderId") String labOrderId,
                                                              @Param("orderNumber") String orderNumber,
                                                              @Param("isAvailable") Boolean isAvailable,
                                                              @Param("isApproved") Boolean isApproved,
                                                              @Param("report") ReportConfiguration report,
                                                              @Param("reportType") ReportType reportType,
                                                              @Param("reportable") Boolean reportable,
                                                              @Param("after") Calendar after,
                                                              Pageable pageable);

    @Query("select r from TestReport r where (:type is null or r.signedOutType = :type)" +
            " and (:signedOut is null or r.signedOut = :signedOut)" +
            " and (:patientId is null or r.patient.id = :patientId)" +
            " and (:firstName is null or r.patient.firstName = :firstName)" +
            " and (:lastName is null or r.patient.lastName = :lastName)" +
            " and (:email is null or r.patient.email = :email)" +
            " and (:sampleNumber is null or r.sampleNumber = :sampleNumber)" +
            " and (:batchRunId is null or r.batchRunId = :batchRunId)" +
            " and (:labId is null or r.labId = :labId)" +
            " and (:labOrderId is null or r.laboratoryOrderId = :labOrderId)" +
            " and (:orderNumber is null or r.orderNumber = :orderNumber)" +
            " and (:isAvailable is null or r.isAvailable = :isAvailable)" +
            " and (:isApproved is null or r.approved = :isApproved)" +
            " and (:report is null or r.reportConfiguration = :report)" +
            " and (:reportType is null or r.reportType = :reportType)" +
            " and (:reportable is null or r.reportable = :reportable)" +
            " and (cast(:after as timestamp) is null or r.completedAt >= :after)" +
            " order by r.estimatedToBeAvailableAt DESC")
    Page<TestReport> searchOrderByEstimatedToBeAvailableAtDSC(@Param("type") SignedOutType type,
                                                              @Param("signedOut") Boolean signedOut,
                                                              @Param("patientId") String patientId,
                                                              @Param("firstName") String firstName,
                                                              @Param("lastName") String lastName,
                                                              @Param("email") String email,
                                                              @Param("sampleNumber") String sampleNumber,
                                                              @Param("batchRunId") String batchRunId,
                                                              @Param("labId") String labId,
                                                              @Param("labOrderId") String labOrderId,
                                                              @Param("orderNumber") String orderNumber,
                                                              @Param("isAvailable") Boolean isAvailable,
                                                              @Param("isApproved") Boolean isApproved,
                                                              @Param("report") ReportConfiguration report,
                                                              @Param("reportType") ReportType reportType,
                                                              @Param("reportable") Boolean reportable,
                                                              @Param("after") Calendar after,
                                                              Pageable pageable);

    @Query("select r from TestReport r where (:type is null or r.signedOutType = :type)" +
            " and (:signedOut is null or r.signedOut = :signedOut)" +
            " and (:patientId is null or r.patient.id = :patientId)" +
            " and (:firstName is null or r.patient.firstName = :firstName)" +
            " and (:lastName is null or r.patient.lastName = :lastName)" +
            " and (:email is null or r.patient.email = :email)" +
            " and (:sampleNumber is null or r.sampleNumber = :sampleNumber)" +
            " and (:batchRunId is null or r.batchRunId = :batchRunId)" +
            " and (:labId is null or r.labId = :labId)" +
            " and (:labOrderId is null or r.laboratoryOrderId = :labOrderId)" +
            " and (:orderNumber is null or r.orderNumber = :orderNumber)" +
            " and (:isAvailable is null or r.isAvailable = :isAvailable)" +
            " and (:isApproved is null or r.approved = :isApproved)" +
            " and (:report is null or r.reportConfiguration = :report)" +
            " and (:reportType is null or r.reportType = :reportType)" +
            " and (:reportable is null or r.reportable = :reportable)" +
            " and (cast(:after as timestamp) is null or r.completedAt >= :after)" +
            " order by r.firstAvailableAt ASC")
    Page<TestReport> searchOrderByFirstAvailableAtASC(@Param("type") SignedOutType type,
                                                      @Param("signedOut") Boolean signedOut,
                                                      @Param("patientId") String patientId,
                                                      @Param("firstName") String firstName,
                                                      @Param("lastName") String lastName,
                                                      @Param("email") String email,
                                                      @Param("sampleNumber") String sampleNumber,
                                                      @Param("batchRunId") String batchRunId,
                                                      @Param("labId") String labId,
                                                      @Param("labOrderId") String labOrderId,
                                                      @Param("orderNumber") String orderNumber,
                                                      @Param("isAvailable") Boolean isAvailable,
                                                      @Param("isApproved") Boolean isApproved,
                                                      @Param("report") ReportConfiguration report,
                                                      @Param("reportType") ReportType reportType,
                                                      @Param("reportable") Boolean reportable,
                                                      @Param("after") Calendar after,
                                                      Pageable pageable);

    @Query("select r from TestReport r where (:type is null or r.signedOutType = :type)" +
            " and (:signedOut is null or r.signedOut = :signedOut)" +
            " and (:patientId is null or r.patient.id = :patientId)" +
            " and (:firstName is null or r.patient.firstName = :firstName)" +
            " and (:lastName is null or r.patient.lastName = :lastName)" +
            " and (:email is null or r.patient.email = :email)" +
            " and (:sampleNumber is null or r.sampleNumber = :sampleNumber)" +
            " and (:batchRunId is null or r.batchRunId = :batchRunId)" +
            " and (:labId is null or r.labId = :labId)" +
            " and (:labOrderId is null or r.laboratoryOrderId = :labOrderId)" +
            " and (:orderNumber is null or r.orderNumber = :orderNumber)" +
            " and (:isAvailable is null or r.isAvailable = :isAvailable)" +
            " and (:isApproved is null or r.approved = :isApproved)" +
            " and (:report is null or r.reportConfiguration = :report)" +
            " and (:reportType is null or r.reportType = :reportType)" +
            " and (:reportable is null or r.reportable = :reportable)" +
            " and (cast(:after as timestamp) is null or r.completedAt >= :after)" +
            " order by r.firstAvailableAt DESC")
    Page<TestReport> searchOrderByFirstAvailableAtDSC(@Param("type") SignedOutType type,
                                                      @Param("signedOut") Boolean signedOut,
                                                      @Param("patientId") String patientId,
                                                      @Param("firstName") String firstName,
                                                      @Param("lastName") String lastName,
                                                      @Param("email") String email,
                                                      @Param("sampleNumber") String sampleNumber,
                                                      @Param("batchRunId") String batchRunId,
                                                      @Param("labId") String labId,
                                                      @Param("labOrderId") String labOrderId,
                                                      @Param("orderNumber") String orderNumber,
                                                      @Param("isAvailable") Boolean isAvailable,
                                                      @Param("isApproved") Boolean isApproved,
                                                      @Param("report") ReportConfiguration report,
                                                      @Param("reportType") ReportType reportType,
                                                      @Param("reportable") Boolean reportable,
                                                      @Param("after") Calendar after,
                                                      Pageable pageable);

    @Query("select t from TestReport t where t.labId = :labId and t.signedOutType = :signoutType and t.completedAt >= :afterDate and t.signedOut = :isSignedOut")
    Page<TestReport> findTestReportsBySignedOutType(@Param("labId") String labId, @Param("signoutType") SignedOutType signedOutType, @Param("afterDate") Calendar afterDate, @Param("isSignedOut") boolean isSignedOut, Pageable pageable);

    @Query("select count(t) from TestReport t where t.labId = :labId and t.signedOut = false and t.completedAt >= :afterDate")
    int countTestReportsSignedOut(@Param("labId") String labId, @Param("afterDate") Calendar afterDate);

    @Query("select count(t) from TestReport t where (:labId is null or t.labId = :labId) and t.signedOut = false and t.approved = true  and t.signedOutType = 'MANUAL' and t.completedAt >= :afterDate")
    int countTestReportsRequiringReview(@Param("labId") String labId, @Param("afterDate") Calendar afterDate);

    @Query("select count(t) from TestReport t where (:labId is null or t.labId = :labId) and t.signedOut = false and t.approved = true  and t.signedOutType = 'AUTOMATIC' and t.completedAt >= :afterDate")
    int countTestReportsRequiringConfirmation(@Param("labId") String labId, @Param("afterDate") Calendar afterDate);


    @Query("select count(t) from TestReport t where (:labId is null or t.labId = :labId) and t.signedOut = true and t.completedAt >= :afterDate")
    int countRecentlySignedOut(@Param("labId") String labId, @Param("afterDate") Calendar afterDate);

    @Query("select count(t) from TestReport t where (:labId is null or t.labId = :labId) and t.signedOut = true and t.signedOutType = 'MANUAL' and t.completedAt >= :afterDate")
    int countRecentlySignedOutManual(@Param("labId") String labId, @Param("afterDate") Calendar afterDate);

    @Query("select count(t) from TestReport t where (:labId is null or t.labId = :labId) and t.signedOut = true and t.signedOutType = 'AUTOMATIC' and t.completedAt >= :afterDate")
    int countRecentlySignedOutAutomatic(@Param("labId") String labId, @Param("afterDate") Calendar afterDate);

    @Query("select count(t) from TestReport t where (:labId is null or t.labId = :labId) and t.signedOut = false and t.approved = true  and t.reportConfiguration = :reportConfiguration")
    int countAwaitingReview(@Param("labId") String labId, @Param("reportConfiguration") ReportConfiguration reportConfiguration);

    @Query("select count(t) from TestReport t where (:labId is null or t.labId = :labId) and t.signedOut = false and t.approved = true and t.signedOutType = 'MANUAL' and t.reportConfiguration = :reportConfiguration")
    int countRequiringManualReview(@Param("labId") String labId, @Param("reportConfiguration") ReportConfiguration reportConfiguration);

    @Query("select count(t) from TestReport t where (:labId is null or t.labId = :labId) and t.signedOut = false and t.approved = true  and t.signedOutType = 'AUTOMATIC' and t.reportConfiguration = :reportConfiguration")
    int countRequiringAutomaticReview(@Param("labId") String labId, @Param("reportConfiguration") ReportConfiguration reportConfiguration);

    @Query("select count(t) from TestReport t where t.batchRunId = :batchId and t.approved = false")
    long countOfUnapprovedResultsInBatch(@Param("batchId") String batchId);

    //The following queries are for reporting data update

}
/*
    private int interval;
    private IntervalType intervalType;
    private int runsToBeApproved;
    private int resultsAwaitingSignOutConfirmation;
    private int resultsAwaitingInvestigation;
    private int recentlySignedOut;
    private int recentlyManuallySignedOut;
    private int recentlyAutomaticallySignedOut;
    private int upcomingResults;
    
 */