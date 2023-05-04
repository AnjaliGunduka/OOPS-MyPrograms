package com.junodx.api.dto.mappers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.dto.models.auth.UserSignoutDto;
import com.junodx.api.dto.models.commerce.*;
import com.junodx.api.dto.models.laboratory.*;
import com.junodx.api.dto.models.laboratory.reports.*;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.commerce.*;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.laboratory.*;
import com.junodx.api.models.laboratory.reports.*;
import com.junodx.api.models.laboratory.reports.types.SignedOutType;
import com.junodx.api.models.laboratory.tests.TestQC;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.laboratory.types.ReportType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Calendar;
import java.util.List;

@Mapper (
        componentModel = "spring"
)

public interface LaboratoryMapStructMapper {
    LaboratoryMapStructMapper INSTANCE = Mappers.getMapper(LaboratoryMapStructMapper.class);
    UserMapStructMapper USER_INSTANCE = Mappers.getMapper(UserMapStructMapper.class);

    //Dao -> Dto

    KitDto kitToKitDto(Kit kit);
    KitLaboratoryDto kitToKitLaboratoryDto(Kit kit);
    LaboratoryDto laboratoryToLaboratoryDto(Laboratory laboratory);
    LaboratoryInOrderDto laboratoryToLaboratoryInOrderDto(Laboratory laboratory);
    LaboratoryOrderDto laboratoryOrderToLaboratoryOrderDto(LaboratoryOrder laboratory);
    LaboratoryReviewGroupDto laboratoryReviewGroupToLaboratoryReviewGroupDto(LaboratoryReviewGroup laboratoryReviewGroup);
    LaboratoryReviewStatisticsDto laboratoryReviewStatisticsToLaboratoryReviewStatisticsDto(LaboratoryReviewStatistics laboratoryReviewStatistics);
    LaboratoryStatisticsDto laboratoryStatisticsToLaboratoryStatisticsDto(LaboratoryStatistics laboratoryStatistics);
    List<LaboratoryStatisticsDto> laboratoryStatisticsToLaboratoryStatisticsDtos(List<LaboratoryStatistics> stats);
    List<LaboratoryReviewStatisticsDto> laboratoryReviewStatisticsToLaboratoryReviewStatisticsDtos(List<LaboratoryReviewStatistics> stats);
    LaboratoryStatusDto laboratoryStatusToLaboratoryStatusDto(LaboratoryStatus laboratoryStatus);
    TestRunDto testRunToTestRunDto(TestRun testRun);
    List<TestReportDto> testReportToTestReportsDto(List<TestReport> reports);

    LaboratoryOrderDetailsBatchDto laboratoryOrderToLaboratoryOrderDetailsBatchDto(LaboratoryOrder labOrder);

    FSTReportDto fstReportToFSTReportDto(FSTRawData fstRawData);
    NIPSBasicReportDto NIPSBasicReportToNIPSBasicReportDto(NIPSBasicRawData nipsBasicRawData);
    ReportDto reportToReportDto(Report report);
    SignoutDto signoutToSignoutDto(Signout signout);
    TestQCDto testQCToTestQCDto(TestQC testQC);

    //Dto -> Dao
    TestReport testReportDtoToTestReport(TestReportDto report);
    Kit kitDtoToKit(KitDto kit);
    Laboratory laboratoryDtoToLaboratory(LaboratoryDto laboratory);
    LaboratoryOrder laboratoryOrderDtoToLaboratoryOrder(LaboratoryOrderDto laboratory);
    LaboratoryReviewGroup laboratoryReviewGroupDtoToLaboratoryReviewGroup(LaboratoryReviewGroupDto laboratoryReviewGroup);
    LaboratoryReviewStatistics laboratoryReviewStatisticsDtoToLaboratoryReviewStatistics(LaboratoryReviewStatisticsDto laboratoryReviewStatistics);
    LaboratoryStatistics laboratoryStatisticsDtoToLaboratoryStatistics(LaboratoryStatisticsDto laboratoryStatistics);
    LaboratoryStatus laboratoryStatusDtoToLaboratoryStatus(LaboratoryStatusDto laboratoryStatus);
    TestRun testRunDtoToTestRun(TestRunDto testRun);

    FSTRawData fstReportDtoToFSTReport(FSTReportDto fstReport);
    NIPSBasicRawData NIPSBasicReportDtoToNIPSBasicReport(NIPSBasicReportDto nipsBasicReport);
    TestQC TestQCDtoToTestQC(TestQCDto qc);
    Signout signoutDtoToSignout(SignoutDto signout);

    LabOrderBatchDto orderToOrderLabBatchDto(LaboratoryOrder dao);
    List<LabOrderBatchDto> orderToOrderLabBatchDtos(List<LaboratoryOrder> orders);
    TestRunBatchDto testRunToTestRunBatchDto(TestRun run);
    List<TestRunBatchDto> testRunToTestRunBatchDtos(List<TestRun> runs);
    KitBatchDto kitToKitBatchDto(Kit kit);
    TestReportBatchDto testReportToTestReportBatchDto(TestReport report);
    List<TestReportBatchDto> testReportToTestReportBatchDtos(List<TestReport> reports);

    /*
    default Report reportDtoToReport(ReportDto report){
        if(report != null){
            if(report instanceof NIPSBasicReportDto){
                report = (NIPSBasicReportDto)report;
                NIPSBasicRawData r = new NIPSBasicRawData();
                r.setId(report.getId());
                r.setSca(((NIPSBasicReportDto) report).getSca());

                TestQCDto tqcDto = report.getQc();
                if(tqcDto instanceof SequencingQCDto){
                    SequencingQC qc = new SequencingQC();
                    qc.setPassed(tqcDto.isPassed());
                    qc.setSnrFlag(((SequencingQCDto) tqcDto).isSnrFlag());
                    qc.setQCType(tqcDto.getQcType());
                    r.setQc(qc);
                } else if (tqcDto instanceof PCRQCDto){
                    PCRQC qc = new PCRQC();
                    qc.setPassed(tqcDto.isPassed());
                    qc.setSnrFlag(((PCRQCDto) tqcDto).isSnrFlag());
                    qc.setQCType(tqcDto.getQcType());
                    r.setQc(qc);
                }

                r.setRawCounts(report.getRawCounts());
                r.setEuploid(((NIPSBasicReportDto) report).getEuploid());
                r.setT13(((NIPSBasicReportDto) report).getT13());
                r.setT18(((NIPSBasicReportDto) report).getT18());
                r.setT21(((NIPSBasicReportDto) report).getT21());
                r.setFetalFraction(((NIPSBasicReportDto) report).getFetalFraction());
                r.setReportName(report.getReportName());
                r.setResultsUrl(report.getResultsUrl());
                r.setSignedOutType(report.getSignedOutType());
                r.setSampleNumber(report.getSampleNumber());

                return r;
            } else if(report instanceof FSTReportDto){
                report = (FSTReportDto)report;
                FSTRawData r = new FSTRawData();
                r.setId(report.getId());
                r.setSca(((FSTReportDto) report).getSca());

                TestQCDto tqcDto = report.getQc();
                if(tqcDto instanceof SequencingQCDto){
                    SequencingQC qc = new SequencingQC();
                    qc.setPassed(tqcDto.isPassed());
                    qc.setSnrFlag(((SequencingQCDto) tqcDto).isSnrFlag());
                    qc.setQCType(tqcDto.getQcType());
                    r.setQc(qc);
                } else if (tqcDto instanceof PCRQCDto){
                    PCRQC qc = new PCRQC();
                    qc.setPassed(tqcDto.isPassed());
                    qc.setSnrFlag(((PCRQCDto) tqcDto).isSnrFlag());
                    qc.setQCType(tqcDto.getQcType());
                    r.setQc(qc);
                }

                r.setRawCounts(report.getRawCounts());
                r.setReportName(report.getReportName());
                r.setResultsUrl(report.getResultsUrl());
                r.setSignedOutType(report.getSignedOutType());
                r.setSampleNumber(report.getSampleNumber());

                return r;
            }
        }
        return null;
    }

    default TestQC testQCDtoToTestQC(TestQCDto tqcDto){
        if(tqcDto instanceof SequencingQCDto){
            SequencingQC qc = new SequencingQC();
            qc.setPassed(tqcDto.isPassed());
            qc.setSnrFlag(((SequencingQCDto) tqcDto).isSnrFlag());
            qc.setQCType(tqcDto.getQcType());

            return qc;
        } else if (tqcDto instanceof PCRQCDto){
            PCRQC qc = new PCRQC();
            qc.setPassed(tqcDto.isPassed());
            qc.setSnrFlag(((PCRQCDto) tqcDto).isSnrFlag());
            qc.setQCType(tqcDto.getQcType());

            return qc;
        }

        return null;
    }

     */

    default TestReportDto testReportToTestReportDto(TestReport dao){
        TestReportDto dto = new TestReportDto();
        dto.setId(dao.getId());
        dto.setAvailable(dao.isAvailable());
        dto.setReportConfiguration(dao.getReportConfiguration());
        dto.setEstimatedToBeAvailableAt(dao.getEstimatedToBeAvailableAt());
        dto.setFirstAvailableAt(dao.getFirstAvailableAt());
        dto.setCompletedAt(dao.getCompletedAt());
        dto.setSignedOut(dao.isSignedOut());
        dto.setSignedOutType(dao.getSignedOutType());
        dto.setSignoutDetails(INSTANCE.signoutToSignoutDto(dao.getSignoutDetails()));
        dto.setResultsUrl(dao.getResultsUrl());
        dto.setResultData(dao.getResultData());
        dto.setPatient(USER_INSTANCE.userToUserOrderDto(dao.getPatient()));
        dto.setOrderId(dao.getOrderId());
        dto.setLaboratoryOrderId(dao.getLaboratoryOrderId());
        dto.setTestRunId(dao.getTestRun().getId());
        dto.setSampleNumber(dao.getSampleNumber());

        return dto;
    }
}
