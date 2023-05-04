package com.junodx.api.dto.models.laboratory;

import com.junodx.api.models.laboratory.types.ReportConfiguration;

public class LaboratoryOrderDetailsBatchDto {
    private String id;
    private String reportableTestRunId;
    private String reportableTestReportId;
    private ReportConfiguration reportConfiguration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportableTestRunId() {
        return reportableTestRunId;
    }

    public void setReportableTestRunId(String reportableTestRunId) {
        this.reportableTestRunId = reportableTestRunId;
    }

    public String getReportableTestReportId() {
        return reportableTestReportId;
    }

    public void setReportableTestReportId(String reportableTestReportId) {
        this.reportableTestReportId = reportableTestReportId;
    }

    public ReportConfiguration getReportConfiguration() {
        return reportConfiguration;
    }

    public void setReportConfiguration(ReportConfiguration reportConfiguration) {
        this.reportConfiguration = reportConfiguration;
    }
}
