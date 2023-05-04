package com.junodx.api.models.payment.insurance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.laboratory.tests.Test;
import com.junodx.api.models.patient.PatientDetails;

import java.util.Calendar;

public class Claim {
    @JsonIgnore
    private Long id;

    private String claimId;
    private Calendar createdAt;
    private Calendar lastUpdatedAt;
    private Test serviceRun;
    private float chargeAmount;
    private float coveredAmount;
    private float agreedAmount;
    private float billedAmount;
    private String processorClaimReferenceNumber;
    private PatientDetails patientDetails;
    private ClaimStatus status;
    private String notes;
    private InsuranceProcessorProvider claimProcessor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }

    public Calendar getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Calendar lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Test getServiceRun() {
        return serviceRun;
    }

    public void setServiceRun(Test serviceRun) {
        this.serviceRun = serviceRun;
    }

    public float getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(float chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public PatientDetails getPatient() {
        return patientDetails;
    }

    public void setPatient(PatientDetails patientDetails) {
        this.patientDetails = patientDetails;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public InsuranceProcessorProvider getClaimProcessor() {
        return claimProcessor;
    }

    public void setClaimProcessor(InsuranceProcessorProvider claimProcessor) {
        this.claimProcessor = claimProcessor;
    }

    public float getCoveredAmount() {
        return coveredAmount;
    }

    public void setCoveredAmount(float coveredAmount) {
        this.coveredAmount = coveredAmount;
    }

    public float getAgreedAmount() {
        return agreedAmount;
    }

    public void setAgreedAmount(float agreedAmount) {
        this.agreedAmount = agreedAmount;
    }

    public float getBilledAmount() {
        return billedAmount;
    }

    public void setBilledAmount(float billedAmount) {
        this.billedAmount = billedAmount;
    }

    public String getProcessorClaimReferenceNumber() {
        return processorClaimReferenceNumber;
    }

    public void setProcessorClaimReferenceNumber(String processorClaimReferenceNumber) {
        this.processorClaimReferenceNumber = processorClaimReferenceNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
