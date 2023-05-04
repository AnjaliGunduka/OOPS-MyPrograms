package com.junodx.api.dto.models.laboratory.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.laboratory.TestReport;
import com.junodx.api.models.laboratory.reports.NIPSBasicRawData;
import com.junodx.api.models.laboratory.reports.types.SignedOutType;
import com.junodx.api.models.laboratory.tests.TestQC;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

public class ReportDto {
    private String id;
    private String sampleNumber;
    private String reportName;
    private int rawCounts;
   // private TestQCDto qc;
    private SignedOutType signedOutType;
    private String resultsUrl;
    private NIPSBasicRawData data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public int getRawCounts() {
        return rawCounts;
    }

    public void setRawCounts(int rawCounts) {
        this.rawCounts = rawCounts;
    }

    /*
    public TestQCDto getQc() {
        return qc;
    }

    public void setQc(TestQCDto qc) {
        this.qc = qc;
    }

     */

    public SignedOutType getSignedOutType() {
        return signedOutType;
    }

    public void setSignedOutType(SignedOutType signedOutType) {
        this.signedOutType = signedOutType;
    }

    public String getResultsUrl() {
        return resultsUrl;
    }

    public void setResultsUrl(String resultsUrl) {
        this.resultsUrl = resultsUrl;
    }

    public NIPSBasicRawData getData() {
        return data;
    }

    public void setData(NIPSBasicRawData data) {
        this.data = data;
    }
}
