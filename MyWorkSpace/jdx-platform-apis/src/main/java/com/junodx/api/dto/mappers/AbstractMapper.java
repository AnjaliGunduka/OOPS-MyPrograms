package com.junodx.api.dto.mappers;

import com.junodx.api.dto.models.laboratory.reports.*;
import com.junodx.api.models.laboratory.reports.*;
import org.mapstruct.Mapper;

@Mapper
public abstract class AbstractMapper {

    /*
    Report reportDtoToReport(ReportDto report){
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

     */
}
