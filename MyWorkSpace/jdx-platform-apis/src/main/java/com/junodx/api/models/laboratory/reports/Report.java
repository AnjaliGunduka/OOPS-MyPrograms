package com.junodx.api.models.laboratory.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.junodx.api.models.laboratory.TestReport;
import com.junodx.api.models.laboratory.reports.types.SignedOutType;
import com.junodx.api.models.laboratory.tests.TestQC;
import com.junodx.api.models.laboratory.types.ConfidenceIndexType;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "report_data")
/*
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "reportName")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NIPSBasicRawData.class, name = "NIPS_BASIC"),
        @JsonSubTypes.Type(value = FSTRawData.class, name = "FST")
})

 */
public class Report {
    @Id
    protected String id;

    //@NotNull
    //@Column(name = "sample_number", nullable = false)
    //private String sampleNumber;

    @Column(name = "report_name", nullable = false)
    private String reportName;

    @OneToOne
    @JoinColumn(name = "test_report_id", nullable = false)
    @JsonIgnore
    private TestReport report;

    private TestQC qc;

    @Enumerated(EnumType.STRING)
    private ConfidenceIndexType confidenceIndex;

    @Type(type = "json")
    @Column(name = "raw_data_json", columnDefinition = "json")
    private NIPSBasicRawData data;

    public Report(){
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*
    public String getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

     */

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public TestReport getReport() {
        return report;
    }

    public void setReport(TestReport report) {
        this.report = report;
    }

    public TestQC getQc() {
        return qc;
    }

    public void setQc(TestQC qc) {
        this.qc = qc;
    }


    public NIPSBasicRawData getData() {
        return data;
    }

    public void setData(NIPSBasicRawData data) {
        this.data = data;
    }

    public ConfidenceIndexType getConfidenceIndex() {
        return confidenceIndex;
    }

    public void setConfidenceIndex(ConfidenceIndexType confidenceIndex) {
        this.confidenceIndex = confidenceIndex;
    }

}
