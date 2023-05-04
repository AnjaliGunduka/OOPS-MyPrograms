package com.junodx.api.dto.models.laboratory.reports;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.laboratory.reports.Report;
import com.junodx.api.models.laboratory.reports.types.SignedOutType;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.laboratory.types.ReportType;

import java.util.Calendar;

public class TestReportBatchDto {
    private String id;
    private boolean isAvailable;
    private ReportConfiguration reportConfiguration;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Calendar estimatedToBeAvailableAt;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar completedAt;
    private boolean approved;
    private boolean signedOut;
    private SignedOutType signedOutType;
    private UserOrderDto patient;
    private String orderId;
    private String orderNumber;
    private String laboratoryOrderId;
    private String testRunId;
    private String sampleNumber;
    private ReportType reportType;
    private boolean reportable;
    private TestQCDto qc;
    private int age;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public ReportConfiguration getReportConfiguration() {
        return reportConfiguration;
    }

    public void setReportConfiguration(ReportConfiguration reportConfiguration) {
        this.reportConfiguration = reportConfiguration;
    }

    public Calendar getEstimatedToBeAvailableAt() {
        return estimatedToBeAvailableAt;
    }

    public void setEstimatedToBeAvailableAt(Calendar estimatedToBeAvailableAt) {
        this.estimatedToBeAvailableAt = estimatedToBeAvailableAt;
    }

    public Calendar getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Calendar completedAt) {
        this.completedAt = completedAt;
    }

    public boolean isSignedOut() {
        return signedOut;
    }

    public void setSignedOut(boolean signedOut) {
        this.signedOut = signedOut;
    }

    public SignedOutType getSignedOutType() {
        return signedOutType;
    }

    public void setSignedOutType(SignedOutType signedOutType) {
        this.signedOutType = signedOutType;
    }

    public UserOrderDto getPatient() {
        if(this.patient == null)
            this.patient = new UserOrderDto();
        return patient;
    }

    public void setPatient(UserOrderDto patient) {
        this.patient = patient;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getLaboratoryOrderId() {
        return laboratoryOrderId;
    }

    public void setLaboratoryOrderId(String laboratoryOrderId) {
        this.laboratoryOrderId = laboratoryOrderId;
    }

    public String getTestRunId() {
        return testRunId;
    }

    public void setTestRunId(String testRunId) {
        this.testRunId = testRunId;
    }

    public String getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
