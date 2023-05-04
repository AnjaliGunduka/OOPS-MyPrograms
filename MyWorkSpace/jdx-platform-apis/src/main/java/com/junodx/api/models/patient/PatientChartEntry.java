package com.junodx.api.models.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
public class PatientChartEntry {
    @Id
    private String id;

    private String salesforceId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_chart_id")
    @JsonIgnore
    private PatientChart patientChart;

    private String authorEmail;
    private String authorName;

    private String authorSalesforceId;

    private String relatedTestReportId;
    private String relatedTestReportSalesforceId;

    protected String title;
    protected String note;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected Calendar timestamp;

    public PatientChartEntry() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public PatientChart getPatientChart() {
        return patientChart;
    }

    public void setPatientChart(PatientChart patientChart) {
        this.patientChart = patientChart;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getSalesforceId() {
        return salesforceId;
    }

    public void setSalesforceId(String salesforceId) {
        this.salesforceId = salesforceId;
    }

    public String getAuthorSalesforceId() {
        return authorSalesforceId;
    }

    public void setAuthorSalesforceId(String authorSalesforceId) {
        this.authorSalesforceId = authorSalesforceId;
    }

    public String getRelatedTestReportId() {
        return relatedTestReportId;
    }

    public void setRelatedTestReportId(String relatedTestReportId) {
        this.relatedTestReportId = relatedTestReportId;
    }

    public String getRelatedTestReportSalesforceId() {
        return relatedTestReportSalesforceId;
    }

    public void setRelatedTestReportSalesforceId(String relatedTestReportSalesforceId) {
        this.relatedTestReportSalesforceId = relatedTestReportSalesforceId;
    }
}
