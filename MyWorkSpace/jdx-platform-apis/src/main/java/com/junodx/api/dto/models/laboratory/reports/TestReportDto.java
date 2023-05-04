package com.junodx.api.dto.models.laboratory.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.dto.models.laboratory.TestRunDto;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.laboratory.Signout;
import com.junodx.api.models.laboratory.TestRun;
import com.junodx.api.models.laboratory.reports.Report;
import com.junodx.api.models.laboratory.reports.types.SignedOutType;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.laboratory.types.ReportType;

import javax.persistence.*;
import java.util.Calendar;

public class TestReportDto {
    private String id;
    private boolean isAvailable;
    private ReportConfiguration reportConfiguration;
    private Calendar estimatedToBeAvailableAt;
    private Calendar firstAvailableAt;
    private Calendar completedAt;
    private boolean signedOut;
    private SignedOutType signedOutType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SignoutDto signoutDetails;
    private String resultsUrl;
    private Report resultData;
    private UserOrderDto patient;
    private String orderId;
    private String laboratoryOrderId;
    private String testRunId;
    private String batchRunId;
    private String sampleNumber;
    private int age;
    private boolean deliveredToProvider;
    private boolean deliveredToPatient;

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

    public Calendar getFirstAvailableAt() {
        return firstAvailableAt;
    }

    public void setFirstAvailableAt(Calendar firstAvailableAt) {
        this.firstAvailableAt = firstAvailableAt;
    }

    public boolean isSignedOut() {
        return signedOut;
    }

    public void setSignedOut(boolean signedOut) {
        this.signedOut = signedOut;
    }

    public SignoutDto getSignoutDetails() {
        return signoutDetails;
    }

    public void setSignoutDetails(SignoutDto signoutDetails) {
        this.signoutDetails = signoutDetails;
    }

    public String getResultsUrl() {
        return resultsUrl;
    }

    public void setResultsUrl(String resultsUrl) {
        this.resultsUrl = resultsUrl;
    }

    public Report getResultData() {
        return resultData;
    }

    public void setResultData(Report resultData) {
        this.resultData = resultData;
    }

    public UserOrderDto getPatient() {
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

    public String getLaboratoryOrderId() {
        return laboratoryOrderId;
    }

    public void setLaboratoryOrderId(String laboratoryOrderId) {
        this.laboratoryOrderId = laboratoryOrderId;
    }

    public String getTestRunId() {
        return testRunId;
    }

    public void setTestRunId(String testRun) {
        this.testRunId = testRun;
    }

    public String getBatchRunId() {
        return batchRunId;
    }

    public void setBatchRunId(String batchRunId) {
        this.batchRunId = batchRunId;
    }

    public String getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public Calendar getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Calendar completedAt) {
        this.completedAt = completedAt;
    }

    public SignedOutType getSignedOutType() {
        return signedOutType;
    }

    public void setSignedOutType(SignedOutType signedOutType) {
        this.signedOutType = signedOutType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isDeliveredToProvider() {
        return deliveredToProvider;
    }

    public void setDeliveredToProvider(boolean deliveredToProvider) {
        this.deliveredToProvider = deliveredToProvider;
    }

    public boolean isDeliveredToPatient() {
        return deliveredToPatient;
    }

    public void setDeliveredToPatient(boolean deliveredToPatient) {
        this.deliveredToPatient = deliveredToPatient;
    }
}
