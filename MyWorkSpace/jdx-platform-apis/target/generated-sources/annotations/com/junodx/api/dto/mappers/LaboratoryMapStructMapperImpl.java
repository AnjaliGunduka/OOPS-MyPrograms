package com.junodx.api.dto.mappers;

import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.dto.models.auth.UserSignoutDto;
import com.junodx.api.dto.models.laboratory.KitBatchDto;
import com.junodx.api.dto.models.laboratory.KitDto;
import com.junodx.api.dto.models.laboratory.KitLaboratoryDto;
import com.junodx.api.dto.models.laboratory.LabOrderBatchDto;
import com.junodx.api.dto.models.laboratory.LaboratoryDto;
import com.junodx.api.dto.models.laboratory.LaboratoryInOrderDto;
import com.junodx.api.dto.models.laboratory.LaboratoryOrderDetailsBatchDto;
import com.junodx.api.dto.models.laboratory.LaboratoryOrderDto;
import com.junodx.api.dto.models.laboratory.LaboratoryReviewGroupDto;
import com.junodx.api.dto.models.laboratory.LaboratoryReviewStatisticsDto;
import com.junodx.api.dto.models.laboratory.LaboratoryStatisticsDto;
import com.junodx.api.dto.models.laboratory.LaboratoryStatusDto;
import com.junodx.api.dto.models.laboratory.TestReportsAwaitingReviewDto;
import com.junodx.api.dto.models.laboratory.TestRunBatchDto;
import com.junodx.api.dto.models.laboratory.TestRunDto;
import com.junodx.api.dto.models.laboratory.reports.FSTReportDto;
import com.junodx.api.dto.models.laboratory.reports.NIPSBasicReportDto;
import com.junodx.api.dto.models.laboratory.reports.ReportDto;
import com.junodx.api.dto.models.laboratory.reports.SignoutDto;
import com.junodx.api.dto.models.laboratory.reports.TestQCDto;
import com.junodx.api.dto.models.laboratory.reports.TestReportBatchDto;
import com.junodx.api.dto.models.laboratory.reports.TestReportDto;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.models.laboratory.Laboratory;
import com.junodx.api.models.laboratory.LaboratoryOrder;
import com.junodx.api.models.laboratory.LaboratoryReviewGroup;
import com.junodx.api.models.laboratory.LaboratoryReviewStatistics;
import com.junodx.api.models.laboratory.LaboratoryStatistics;
import com.junodx.api.models.laboratory.LaboratoryStatus;
import com.junodx.api.models.laboratory.Signout;
import com.junodx.api.models.laboratory.TestReport;
import com.junodx.api.models.laboratory.TestRun;
import com.junodx.api.models.laboratory.reports.FSTRawData;
import com.junodx.api.models.laboratory.reports.NIPSBasicRawData;
import com.junodx.api.models.laboratory.reports.Report;
import com.junodx.api.models.laboratory.tests.TestQC;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-06T11:57:40+0530",
    comments = "version: 1.4.1.Final, compiler: Eclipse JDT (IDE) 1.4.50.v20210914-1429, environment: Java 15.0.2 (Oracle Corporation)"
)
@Component
public class LaboratoryMapStructMapperImpl implements LaboratoryMapStructMapper {

    @Override
    public KitDto kitToKitDto(Kit kit) {
        if ( kit == null ) {
            return null;
        }

        KitDto kitDto = new KitDto();

        kitDto.setId( kit.getId() );
        kitDto.setSampleNumber( kit.getSampleNumber() );
        kitDto.setType( kit.getType() );
        kitDto.setMeta( kit.getMeta() );
        kitDto.setCode( kit.getCode() );

        return kitDto;
    }

    @Override
    public KitLaboratoryDto kitToKitLaboratoryDto(Kit kit) {
        if ( kit == null ) {
            return null;
        }

        KitLaboratoryDto kitLaboratoryDto = new KitLaboratoryDto();

        kitLaboratoryDto.setCode( kit.getCode() );
        kitLaboratoryDto.setSampleNumber( kit.getSampleNumber() );
        kitLaboratoryDto.setType( kit.getType() );

        return kitLaboratoryDto;
    }

    @Override
    public LaboratoryDto laboratoryToLaboratoryDto(Laboratory laboratory) {
        if ( laboratory == null ) {
            return null;
        }

        LaboratoryDto laboratoryDto = new LaboratoryDto();

        laboratoryDto.setId( laboratory.getId() );
        laboratoryDto.setName( laboratory.getName() );

        return laboratoryDto;
    }

    @Override
    public LaboratoryInOrderDto laboratoryToLaboratoryInOrderDto(Laboratory laboratory) {
        if ( laboratory == null ) {
            return null;
        }

        LaboratoryInOrderDto laboratoryInOrderDto = new LaboratoryInOrderDto();

        laboratoryInOrderDto.setId( laboratory.getId() );
        laboratoryInOrderDto.setName( laboratory.getName() );

        return laboratoryInOrderDto;
    }

    @Override
    public LaboratoryOrderDto laboratoryOrderToLaboratoryOrderDto(LaboratoryOrder laboratory) {
        if ( laboratory == null ) {
            return null;
        }

        LaboratoryOrderDto laboratoryOrderDto = new LaboratoryOrderDto();

        laboratoryOrderDto.setId( laboratory.getId() );
        laboratoryOrderDto.setParentOrderId( laboratory.getParentOrderId() );
        laboratoryOrderDto.setLab( laboratoryToLaboratoryDto( laboratory.getLab() ) );
        laboratoryOrderDto.setTestRuns( testRunListToTestRunDtoList( laboratory.getTestRuns() ) );
        laboratoryOrderDto.setLimsOrderId( laboratory.getLimsOrderId() );
        laboratoryOrderDto.setPatient( userToUserOrderDto( laboratory.getPatient() ) );
        laboratoryOrderDto.setDateReceivedInLab( laboratory.getDateReceivedInLab() );
        laboratoryOrderDto.setMeta( laboratory.getMeta() );

        return laboratoryOrderDto;
    }

    @Override
    public LaboratoryReviewGroupDto laboratoryReviewGroupToLaboratoryReviewGroupDto(LaboratoryReviewGroup laboratoryReviewGroup) {
        if ( laboratoryReviewGroup == null ) {
            return null;
        }

        LaboratoryReviewGroupDto laboratoryReviewGroupDto = new LaboratoryReviewGroupDto();

        laboratoryReviewGroupDto.setId( laboratoryReviewGroup.getId() );
        laboratoryReviewGroupDto.setLaboratoryReviewStatistics( laboratoryReviewStatisticsToLaboratoryReviewStatisticsDto( laboratoryReviewGroup.getLaboratoryReviewStatistics() ) );
        laboratoryReviewGroupDto.setName( laboratoryReviewGroup.getName() );
        laboratoryReviewGroupDto.setTotalAwaitingReview( laboratoryReviewGroup.getTotalAwaitingReview() );
        laboratoryReviewGroupDto.setTotalRequiringManualReview( laboratoryReviewGroup.getTotalRequiringManualReview() );
        laboratoryReviewGroupDto.setTotalRequiringAutomaticReview( laboratoryReviewGroup.getTotalRequiringAutomaticReview() );

        return laboratoryReviewGroupDto;
    }

    @Override
    public LaboratoryReviewStatisticsDto laboratoryReviewStatisticsToLaboratoryReviewStatisticsDto(LaboratoryReviewStatistics laboratoryReviewStatistics) {
        if ( laboratoryReviewStatistics == null ) {
            return null;
        }

        LaboratoryReviewStatisticsDto laboratoryReviewStatisticsDto = new LaboratoryReviewStatisticsDto();

        laboratoryReviewStatisticsDto.setLaboratoryId( laboratoryReviewStatistics.getLaboratoryId() );
        laboratoryReviewStatisticsDto.setReview( laboratoryReviewGroupListToTestReportsAwaitingReviewDtoList( laboratoryReviewStatistics.getReview() ) );

        return laboratoryReviewStatisticsDto;
    }

    @Override
    public LaboratoryStatisticsDto laboratoryStatisticsToLaboratoryStatisticsDto(LaboratoryStatistics laboratoryStatistics) {
        if ( laboratoryStatistics == null ) {
            return null;
        }

        LaboratoryStatisticsDto laboratoryStatisticsDto = new LaboratoryStatisticsDto();

        laboratoryStatisticsDto.setId( laboratoryStatistics.getId() );
        laboratoryStatisticsDto.setInterval( laboratoryStatistics.getInterval() );
        laboratoryStatisticsDto.setIntervalType( laboratoryStatistics.getIntervalType() );
        laboratoryStatisticsDto.setRunsToBeApproved( laboratoryStatistics.getRunsToBeApproved() );
        laboratoryStatisticsDto.setResultsAwaitingSignOutConfirmation( laboratoryStatistics.getResultsAwaitingSignOutConfirmation() );
        laboratoryStatisticsDto.setResultsAwaitingInvestigation( laboratoryStatistics.getResultsAwaitingInvestigation() );
        laboratoryStatisticsDto.setRecentlySignedOut( laboratoryStatistics.getRecentlySignedOut() );
        laboratoryStatisticsDto.setRecentlyManuallySignedOut( laboratoryStatistics.getRecentlyManuallySignedOut() );
        laboratoryStatisticsDto.setRecentlyAutomaticallySignedOut( laboratoryStatistics.getRecentlyAutomaticallySignedOut() );
        laboratoryStatisticsDto.setUpcomingResults( laboratoryStatistics.getUpcomingResults() );
        laboratoryStatisticsDto.setMeta( laboratoryStatistics.getMeta() );
        laboratoryStatisticsDto.setLaboratory( laboratoryToLaboratoryDto( laboratoryStatistics.getLaboratory() ) );

        return laboratoryStatisticsDto;
    }

    @Override
    public List<LaboratoryStatisticsDto> laboratoryStatisticsToLaboratoryStatisticsDtos(List<LaboratoryStatistics> stats) {
        if ( stats == null ) {
            return null;
        }

        List<LaboratoryStatisticsDto> list = new ArrayList<LaboratoryStatisticsDto>( stats.size() );
        for ( LaboratoryStatistics laboratoryStatistics : stats ) {
            list.add( laboratoryStatisticsToLaboratoryStatisticsDto( laboratoryStatistics ) );
        }

        return list;
    }

    @Override
    public List<LaboratoryReviewStatisticsDto> laboratoryReviewStatisticsToLaboratoryReviewStatisticsDtos(List<LaboratoryReviewStatistics> stats) {
        if ( stats == null ) {
            return null;
        }

        List<LaboratoryReviewStatisticsDto> list = new ArrayList<LaboratoryReviewStatisticsDto>( stats.size() );
        for ( LaboratoryReviewStatistics laboratoryReviewStatistics : stats ) {
            list.add( laboratoryReviewStatisticsToLaboratoryReviewStatisticsDto( laboratoryReviewStatistics ) );
        }

        return list;
    }

    @Override
    public LaboratoryStatusDto laboratoryStatusToLaboratoryStatusDto(LaboratoryStatus laboratoryStatus) {
        if ( laboratoryStatus == null ) {
            return null;
        }

        LaboratoryStatusDto laboratoryStatusDto = new LaboratoryStatusDto();

        laboratoryStatusDto.setId( laboratoryStatus.getId() );
        laboratoryStatusDto.setStatus( laboratoryStatus.getStatus() );
        laboratoryStatusDto.setCurrent( laboratoryStatus.isCurrent() );
        laboratoryStatusDto.setCreatedAt( laboratoryStatus.getCreatedAt() );
        laboratoryStatusDto.setCreatedBy( laboratoryStatus.getCreatedBy() );

        return laboratoryStatusDto;
    }

    @Override
    public TestRunDto testRunToTestRunDto(TestRun testRun) {
        if ( testRun == null ) {
            return null;
        }

        TestRunDto testRunDto = new TestRunDto();

        testRunDto.setId( testRun.getId() );
        testRunDto.setStartTime( testRun.getStartTime() );
        testRunDto.setEndTime( testRun.getEndTime() );
        testRunDto.setKit( kitToKitLaboratoryDto( testRun.getKit() ) );
        testRunDto.setCurrentStatus( testRun.getCurrentStatus() );
        testRunDto.setReport( testReportToTestReportDto( testRun.getReport() ) );

        return testRunDto;
    }

    @Override
    public List<TestReportDto> testReportToTestReportsDto(List<TestReport> reports) {
        if ( reports == null ) {
            return null;
        }

        List<TestReportDto> list = new ArrayList<TestReportDto>( reports.size() );
        for ( TestReport testReport : reports ) {
            list.add( testReportToTestReportDto( testReport ) );
        }

        return list;
    }

    @Override
    public LaboratoryOrderDetailsBatchDto laboratoryOrderToLaboratoryOrderDetailsBatchDto(LaboratoryOrder labOrder) {
        if ( labOrder == null ) {
            return null;
        }

        LaboratoryOrderDetailsBatchDto laboratoryOrderDetailsBatchDto = new LaboratoryOrderDetailsBatchDto();

        laboratoryOrderDetailsBatchDto.setId( labOrder.getId() );
        laboratoryOrderDetailsBatchDto.setReportableTestRunId( labOrder.getReportableTestRunId() );
        laboratoryOrderDetailsBatchDto.setReportableTestReportId( labOrder.getReportableTestReportId() );
        laboratoryOrderDetailsBatchDto.setReportConfiguration( labOrder.getReportConfiguration() );

        return laboratoryOrderDetailsBatchDto;
    }

    @Override
    public FSTReportDto fstReportToFSTReportDto(FSTRawData fstRawData) {
        if ( fstRawData == null ) {
            return null;
        }

        FSTReportDto fSTReportDto = new FSTReportDto();

        fSTReportDto.setSca( fstRawData.getSca() );

        return fSTReportDto;
    }

    @Override
    public NIPSBasicReportDto NIPSBasicReportToNIPSBasicReportDto(NIPSBasicRawData nipsBasicRawData) {
        if ( nipsBasicRawData == null ) {
            return null;
        }

        NIPSBasicReportDto nIPSBasicReportDto = new NIPSBasicReportDto();

        if ( nipsBasicRawData.getFetalFraction() != null ) {
            nIPSBasicReportDto.setFetalFraction( nipsBasicRawData.getFetalFraction() );
        }
        nIPSBasicReportDto.setEuploid( nipsBasicRawData.getEuploid() );
        nIPSBasicReportDto.setT13( nipsBasicRawData.getT13() );
        nIPSBasicReportDto.setT18( nipsBasicRawData.getT18() );
        nIPSBasicReportDto.setT21( nipsBasicRawData.getT21() );
        nIPSBasicReportDto.setSca( nipsBasicRawData.getSca() );

        return nIPSBasicReportDto;
    }

    @Override
    public ReportDto reportToReportDto(Report report) {
        if ( report == null ) {
            return null;
        }

        ReportDto reportDto = new ReportDto();

        reportDto.setId( report.getId() );
        reportDto.setReportName( report.getReportName() );
        reportDto.setData( report.getData() );

        return reportDto;
    }

    @Override
    public SignoutDto signoutToSignoutDto(Signout signout) {
        if ( signout == null ) {
            return null;
        }

        SignoutDto signoutDto = new SignoutDto();

        signoutDto.setSignatory( userToUserSignoutDto( signout.getSignatory() ) );
        signoutDto.setSignedOutAt( signout.getSignedOutAt() );

        return signoutDto;
    }

    @Override
    public TestQCDto testQCToTestQCDto(TestQC testQC) {
        if ( testQC == null ) {
            return null;
        }

        TestQCDto testQCDto = new TestQCDto();

        testQCDto.setPassed( testQC.isPassed() );

        return testQCDto;
    }

    @Override
    public TestReport testReportDtoToTestReport(TestReportDto report) {
        if ( report == null ) {
            return null;
        }

        TestReport testReport = new TestReport();

        testReport.setId( report.getId() );
        testReport.setAvailable( report.isAvailable() );
        testReport.setReportConfiguration( report.getReportConfiguration() );
        testReport.setEstimatedToBeAvailableAt( report.getEstimatedToBeAvailableAt() );
        testReport.setFirstAvailableAt( report.getFirstAvailableAt() );
        testReport.setSignedOut( report.isSignedOut() );
        testReport.setSignoutDetails( signoutDtoToSignout( report.getSignoutDetails() ) );
        testReport.setResultsUrl( report.getResultsUrl() );
        testReport.setOrderId( report.getOrderId() );
        testReport.setLaboratoryOrderId( report.getLaboratoryOrderId() );
        testReport.setCompletedAt( report.getCompletedAt() );
        testReport.setSignedOutType( report.getSignedOutType() );
        testReport.setResultData( report.getResultData() );
        testReport.setSampleNumber( report.getSampleNumber() );
        testReport.setPatient( userOrderDtoToUser( report.getPatient() ) );
        testReport.setDeliveredToProvider( report.isDeliveredToProvider() );
        testReport.setDeliveredToPatient( report.isDeliveredToPatient() );
        testReport.setBatchRunId( report.getBatchRunId() );

        return testReport;
    }

    @Override
    public Kit kitDtoToKit(KitDto kit) {
        if ( kit == null ) {
            return null;
        }

        Kit kit1 = new Kit();

        kit1.setId( kit.getId() );
        kit1.setSampleNumber( kit.getSampleNumber() );
        kit1.setType( kit.getType() );
        kit1.setMeta( kit.getMeta() );
        kit1.setCode( kit.getCode() );

        return kit1;
    }

    @Override
    public Laboratory laboratoryDtoToLaboratory(LaboratoryDto laboratory) {
        if ( laboratory == null ) {
            return null;
        }

        Laboratory laboratory1 = new Laboratory();

        laboratory1.setId( laboratory.getId() );
        laboratory1.setName( laboratory.getName() );

        return laboratory1;
    }

    @Override
    public LaboratoryOrder laboratoryOrderDtoToLaboratoryOrder(LaboratoryOrderDto laboratory) {
        if ( laboratory == null ) {
            return null;
        }

        LaboratoryOrder laboratoryOrder = new LaboratoryOrder();

        laboratoryOrder.setId( laboratory.getId() );
        laboratoryOrder.setLab( laboratoryDtoToLaboratory( laboratory.getLab() ) );
        laboratoryOrder.setDateReceivedInLab( laboratory.getDateReceivedInLab() );
        laboratoryOrder.setTestRuns( testRunDtoListToTestRunList( laboratory.getTestRuns() ) );
        laboratoryOrder.setMeta( laboratory.getMeta() );
        laboratoryOrder.setLimsOrderId( laboratory.getLimsOrderId() );
        laboratoryOrder.setPatient( userOrderDtoToUser( laboratory.getPatient() ) );
        laboratoryOrder.setParentOrderId( laboratory.getParentOrderId() );

        return laboratoryOrder;
    }

    @Override
    public LaboratoryReviewGroup laboratoryReviewGroupDtoToLaboratoryReviewGroup(LaboratoryReviewGroupDto laboratoryReviewGroup) {
        if ( laboratoryReviewGroup == null ) {
            return null;
        }

        LaboratoryReviewGroup laboratoryReviewGroup1 = new LaboratoryReviewGroup();

        laboratoryReviewGroup1.setId( laboratoryReviewGroup.getId() );
        laboratoryReviewGroup1.setName( laboratoryReviewGroup.getName() );
        laboratoryReviewGroup1.setTotalAwaitingReview( laboratoryReviewGroup.getTotalAwaitingReview() );
        laboratoryReviewGroup1.setTotalRequiringManualReview( laboratoryReviewGroup.getTotalRequiringManualReview() );
        laboratoryReviewGroup1.setTotalRequiringAutomaticReview( laboratoryReviewGroup.getTotalRequiringAutomaticReview() );
        laboratoryReviewGroup1.setLaboratoryReviewStatistics( laboratoryReviewStatisticsDtoToLaboratoryReviewStatistics( laboratoryReviewGroup.getLaboratoryReviewStatistics() ) );

        return laboratoryReviewGroup1;
    }

    @Override
    public LaboratoryReviewStatistics laboratoryReviewStatisticsDtoToLaboratoryReviewStatistics(LaboratoryReviewStatisticsDto laboratoryReviewStatistics) {
        if ( laboratoryReviewStatistics == null ) {
            return null;
        }

        LaboratoryReviewStatistics laboratoryReviewStatistics1 = new LaboratoryReviewStatistics();

        laboratoryReviewStatistics1.setReview( testReportsAwaitingReviewDtoListToLaboratoryReviewGroupList( laboratoryReviewStatistics.getReview() ) );
        laboratoryReviewStatistics1.setLaboratoryId( laboratoryReviewStatistics.getLaboratoryId() );

        return laboratoryReviewStatistics1;
    }

    @Override
    public LaboratoryStatistics laboratoryStatisticsDtoToLaboratoryStatistics(LaboratoryStatisticsDto laboratoryStatistics) {
        if ( laboratoryStatistics == null ) {
            return null;
        }

        LaboratoryStatistics laboratoryStatistics1 = new LaboratoryStatistics();

        laboratoryStatistics1.setId( laboratoryStatistics.getId() );
        laboratoryStatistics1.setLaboratory( laboratoryDtoToLaboratory( laboratoryStatistics.getLaboratory() ) );
        laboratoryStatistics1.setInterval( laboratoryStatistics.getInterval() );
        laboratoryStatistics1.setIntervalType( laboratoryStatistics.getIntervalType() );
        laboratoryStatistics1.setRunsToBeApproved( laboratoryStatistics.getRunsToBeApproved() );
        laboratoryStatistics1.setResultsAwaitingSignOutConfirmation( laboratoryStatistics.getResultsAwaitingSignOutConfirmation() );
        laboratoryStatistics1.setResultsAwaitingInvestigation( laboratoryStatistics.getResultsAwaitingInvestigation() );
        laboratoryStatistics1.setRecentlySignedOut( laboratoryStatistics.getRecentlySignedOut() );
        laboratoryStatistics1.setRecentlyManuallySignedOut( laboratoryStatistics.getRecentlyManuallySignedOut() );
        laboratoryStatistics1.setRecentlyAutomaticallySignedOut( laboratoryStatistics.getRecentlyAutomaticallySignedOut() );
        laboratoryStatistics1.setUpcomingResults( laboratoryStatistics.getUpcomingResults() );
        laboratoryStatistics1.setMeta( laboratoryStatistics.getMeta() );

        return laboratoryStatistics1;
    }

    @Override
    public LaboratoryStatus laboratoryStatusDtoToLaboratoryStatus(LaboratoryStatusDto laboratoryStatus) {
        if ( laboratoryStatus == null ) {
            return null;
        }

        LaboratoryStatus laboratoryStatus1 = new LaboratoryStatus();

        laboratoryStatus1.setStatus( laboratoryStatus.getStatus() );
        laboratoryStatus1.setId( laboratoryStatus.getId() );
        laboratoryStatus1.setCurrent( laboratoryStatus.isCurrent() );
        laboratoryStatus1.setCreatedAt( laboratoryStatus.getCreatedAt() );
        laboratoryStatus1.setCreatedBy( laboratoryStatus.getCreatedBy() );

        return laboratoryStatus1;
    }

    @Override
    public TestRun testRunDtoToTestRun(TestRunDto testRun) {
        if ( testRun == null ) {
            return null;
        }

        TestRun testRun1 = new TestRun();

        testRun1.setId( testRun.getId() );
        testRun1.setStartTime( testRun.getStartTime() );
        testRun1.setEndTime( testRun.getEndTime() );
        testRun1.setKit( kitLaboratoryDtoToKit( testRun.getKit() ) );
        testRun1.setReport( testReportDtoToTestReport( testRun.getReport() ) );

        return testRun1;
    }

    @Override
    public FSTRawData fstReportDtoToFSTReport(FSTReportDto fstReport) {
        if ( fstReport == null ) {
            return null;
        }

        FSTRawData fSTRawData = new FSTRawData();

        fSTRawData.setSca( fstReport.getSca() );

        return fSTRawData;
    }

    @Override
    public NIPSBasicRawData NIPSBasicReportDtoToNIPSBasicReport(NIPSBasicReportDto nipsBasicReport) {
        if ( nipsBasicReport == null ) {
            return null;
        }

        NIPSBasicRawData nIPSBasicRawData = new NIPSBasicRawData();

        nIPSBasicRawData.setFetalFraction( nipsBasicReport.getFetalFraction() );
        nIPSBasicRawData.setEuploid( nipsBasicReport.getEuploid() );
        nIPSBasicRawData.setT13( nipsBasicReport.getT13() );
        nIPSBasicRawData.setT18( nipsBasicReport.getT18() );
        nIPSBasicRawData.setT21( nipsBasicReport.getT21() );
        nIPSBasicRawData.setSca( nipsBasicReport.getSca() );

        return nIPSBasicRawData;
    }

    @Override
    public TestQC TestQCDtoToTestQC(TestQCDto qc) {
        if ( qc == null ) {
            return null;
        }

        TestQC testQC = new TestQC();

        testQC.setPassed( qc.isPassed() );

        return testQC;
    }

    @Override
    public Signout signoutDtoToSignout(SignoutDto signout) {
        if ( signout == null ) {
            return null;
        }

        Signout signout1 = new Signout();

        signout1.setSignatory( userSignoutDtoToUser( signout.getSignatory() ) );
        signout1.setSignedOutAt( signout.getSignedOutAt() );

        return signout1;
    }

    @Override
    public LabOrderBatchDto orderToOrderLabBatchDto(LaboratoryOrder dao) {
        if ( dao == null ) {
            return null;
        }

        LabOrderBatchDto labOrderBatchDto = new LabOrderBatchDto();

        labOrderBatchDto.setId( dao.getId() );
        labOrderBatchDto.setParentOrderId( dao.getParentOrderId() );
        labOrderBatchDto.setPatient( userToUserOrderDto( dao.getPatient() ) );
        labOrderBatchDto.setTestRuns( testRunToTestRunBatchDtos( dao.getTestRuns() ) );
        labOrderBatchDto.setEstArrivalInLab( dao.getEstArrivalInLab() );

        return labOrderBatchDto;
    }

    @Override
    public List<LabOrderBatchDto> orderToOrderLabBatchDtos(List<LaboratoryOrder> orders) {
        if ( orders == null ) {
            return null;
        }

        List<LabOrderBatchDto> list = new ArrayList<LabOrderBatchDto>( orders.size() );
        for ( LaboratoryOrder laboratoryOrder : orders ) {
            list.add( orderToOrderLabBatchDto( laboratoryOrder ) );
        }

        return list;
    }

    @Override
    public TestRunBatchDto testRunToTestRunBatchDto(TestRun run) {
        if ( run == null ) {
            return null;
        }

        TestRunBatchDto testRunBatchDto = new TestRunBatchDto();

        testRunBatchDto.setId( run.getId() );
        testRunBatchDto.setStartTime( run.getStartTime() );
        testRunBatchDto.setEndTime( run.getEndTime() );
        testRunBatchDto.setKit( kitToKitBatchDto( run.getKit() ) );
        testRunBatchDto.setCurrentStatus( run.getCurrentStatus() );

        return testRunBatchDto;
    }

    @Override
    public List<TestRunBatchDto> testRunToTestRunBatchDtos(List<TestRun> runs) {
        if ( runs == null ) {
            return null;
        }

        List<TestRunBatchDto> list = new ArrayList<TestRunBatchDto>( runs.size() );
        for ( TestRun testRun : runs ) {
            list.add( testRunToTestRunBatchDto( testRun ) );
        }

        return list;
    }

    @Override
    public KitBatchDto kitToKitBatchDto(Kit kit) {
        if ( kit == null ) {
            return null;
        }

        KitBatchDto kitBatchDto = new KitBatchDto();

        kitBatchDto.setId( kit.getId() );
        kitBatchDto.setCode( kit.getCode() );
        kitBatchDto.setSampleNumber( kit.getSampleNumber() );
        kitBatchDto.setType( kit.getType() );

        return kitBatchDto;
    }

    @Override
    public TestReportBatchDto testReportToTestReportBatchDto(TestReport report) {
        if ( report == null ) {
            return null;
        }

        TestReportBatchDto testReportBatchDto = new TestReportBatchDto();

        testReportBatchDto.setId( report.getId() );
        testReportBatchDto.setAvailable( report.isAvailable() );
        testReportBatchDto.setReportConfiguration( report.getReportConfiguration() );
        testReportBatchDto.setEstimatedToBeAvailableAt( report.getEstimatedToBeAvailableAt() );
        testReportBatchDto.setCompletedAt( report.getCompletedAt() );
        testReportBatchDto.setSignedOut( report.isSignedOut() );
        testReportBatchDto.setSignedOutType( report.getSignedOutType() );
        testReportBatchDto.setPatient( userToUserOrderDto( report.getPatient() ) );
        testReportBatchDto.setOrderId( report.getOrderId() );
        testReportBatchDto.setOrderNumber( report.getOrderNumber() );
        testReportBatchDto.setLaboratoryOrderId( report.getLaboratoryOrderId() );
        testReportBatchDto.setTestRunId( report.getTestRunId() );
        testReportBatchDto.setSampleNumber( report.getSampleNumber() );
        testReportBatchDto.setReportType( report.getReportType() );
        testReportBatchDto.setAge( report.getAge() );

        return testReportBatchDto;
    }

    @Override
    public List<TestReportBatchDto> testReportToTestReportBatchDtos(List<TestReport> reports) {
        if ( reports == null ) {
            return null;
        }

        List<TestReportBatchDto> list = new ArrayList<TestReportBatchDto>( reports.size() );
        for ( TestReport testReport : reports ) {
            list.add( testReportToTestReportBatchDto( testReport ) );
        }

        return list;
    }

    protected List<TestRunDto> testRunListToTestRunDtoList(List<TestRun> list) {
        if ( list == null ) {
            return null;
        }

        List<TestRunDto> list1 = new ArrayList<TestRunDto>( list.size() );
        for ( TestRun testRun : list ) {
            list1.add( testRunToTestRunDto( testRun ) );
        }

        return list1;
    }

    protected UserOrderDto userToUserOrderDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserOrderDto userOrderDto = new UserOrderDto();

        userOrderDto.setId( user.getId() );
        userOrderDto.setFirstName( user.getFirstName() );
        userOrderDto.setLastName( user.getLastName() );
        userOrderDto.setEmail( user.getEmail() );
        userOrderDto.setDateOfBirth( user.getDateOfBirth() );
        userOrderDto.setUserType( user.getUserType() );
        userOrderDto.setStripeCustomerId( user.getStripeCustomerId() );

        return userOrderDto;
    }

    protected TestReportsAwaitingReviewDto laboratoryReviewGroupToTestReportsAwaitingReviewDto(LaboratoryReviewGroup laboratoryReviewGroup) {
        if ( laboratoryReviewGroup == null ) {
            return null;
        }

        TestReportsAwaitingReviewDto testReportsAwaitingReviewDto = new TestReportsAwaitingReviewDto();

        testReportsAwaitingReviewDto.setTotalAwaitingReview( laboratoryReviewGroup.getTotalAwaitingReview() );
        testReportsAwaitingReviewDto.setTotalRequiringManualReview( laboratoryReviewGroup.getTotalRequiringManualReview() );
        testReportsAwaitingReviewDto.setTotalRequiringAutomaticReview( laboratoryReviewGroup.getTotalRequiringAutomaticReview() );

        return testReportsAwaitingReviewDto;
    }

    protected List<TestReportsAwaitingReviewDto> laboratoryReviewGroupListToTestReportsAwaitingReviewDtoList(List<LaboratoryReviewGroup> list) {
        if ( list == null ) {
            return null;
        }

        List<TestReportsAwaitingReviewDto> list1 = new ArrayList<TestReportsAwaitingReviewDto>( list.size() );
        for ( LaboratoryReviewGroup laboratoryReviewGroup : list ) {
            list1.add( laboratoryReviewGroupToTestReportsAwaitingReviewDto( laboratoryReviewGroup ) );
        }

        return list1;
    }

    protected UserSignoutDto userToUserSignoutDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserSignoutDto userSignoutDto = new UserSignoutDto();

        userSignoutDto.setId( user.getId() );
        userSignoutDto.setFirstName( user.getFirstName() );
        userSignoutDto.setLastName( user.getLastName() );
        userSignoutDto.setEmail( user.getEmail() );
        userSignoutDto.setPrimaryPhone( user.getPrimaryPhone() );

        return userSignoutDto;
    }

    protected User userOrderDtoToUser(UserOrderDto userOrderDto) {
        if ( userOrderDto == null ) {
            return null;
        }

        User user = new User();

        user.setId( userOrderDto.getId() );
        user.setFirstName( userOrderDto.getFirstName() );
        user.setLastName( userOrderDto.getLastName() );
        user.setEmail( userOrderDto.getEmail() );
        user.setDateOfBirth( userOrderDto.getDateOfBirth() );
        user.setUserType( userOrderDto.getUserType() );
        user.setStripeCustomerId( userOrderDto.getStripeCustomerId() );

        return user;
    }

    protected List<TestRun> testRunDtoListToTestRunList(List<TestRunDto> list) {
        if ( list == null ) {
            return null;
        }

        List<TestRun> list1 = new ArrayList<TestRun>( list.size() );
        for ( TestRunDto testRunDto : list ) {
            list1.add( testRunDtoToTestRun( testRunDto ) );
        }

        return list1;
    }

    protected LaboratoryReviewGroup testReportsAwaitingReviewDtoToLaboratoryReviewGroup(TestReportsAwaitingReviewDto testReportsAwaitingReviewDto) {
        if ( testReportsAwaitingReviewDto == null ) {
            return null;
        }

        LaboratoryReviewGroup laboratoryReviewGroup = new LaboratoryReviewGroup();

        laboratoryReviewGroup.setTotalAwaitingReview( testReportsAwaitingReviewDto.getTotalAwaitingReview() );
        laboratoryReviewGroup.setTotalRequiringManualReview( testReportsAwaitingReviewDto.getTotalRequiringManualReview() );
        laboratoryReviewGroup.setTotalRequiringAutomaticReview( testReportsAwaitingReviewDto.getTotalRequiringAutomaticReview() );

        return laboratoryReviewGroup;
    }

    protected List<LaboratoryReviewGroup> testReportsAwaitingReviewDtoListToLaboratoryReviewGroupList(List<TestReportsAwaitingReviewDto> list) {
        if ( list == null ) {
            return null;
        }

        List<LaboratoryReviewGroup> list1 = new ArrayList<LaboratoryReviewGroup>( list.size() );
        for ( TestReportsAwaitingReviewDto testReportsAwaitingReviewDto : list ) {
            list1.add( testReportsAwaitingReviewDtoToLaboratoryReviewGroup( testReportsAwaitingReviewDto ) );
        }

        return list1;
    }

    protected Kit kitLaboratoryDtoToKit(KitLaboratoryDto kitLaboratoryDto) {
        if ( kitLaboratoryDto == null ) {
            return null;
        }

        Kit kit = new Kit();

        kit.setSampleNumber( kitLaboratoryDto.getSampleNumber() );
        kit.setType( kitLaboratoryDto.getType() );
        kit.setCode( kitLaboratoryDto.getCode() );

        return kit;
    }

    protected User userSignoutDtoToUser(UserSignoutDto userSignoutDto) {
        if ( userSignoutDto == null ) {
            return null;
        }

        User user = new User();

        user.setId( userSignoutDto.getId() );
        user.setFirstName( userSignoutDto.getFirstName() );
        user.setLastName( userSignoutDto.getLastName() );
        user.setEmail( userSignoutDto.getEmail() );
        user.setPrimaryPhone( userSignoutDto.getPrimaryPhone() );

        return user;
    }
}
