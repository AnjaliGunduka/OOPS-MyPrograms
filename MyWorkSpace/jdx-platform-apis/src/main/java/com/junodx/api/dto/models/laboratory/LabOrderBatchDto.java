package com.junodx.api.dto.models.laboratory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.models.commerce.types.OrderStatusType;
import com.junodx.api.models.laboratory.types.LaboratoryStatusType;

import java.util.Calendar;
import java.util.List;

public class LabOrderBatchDto {
    private String id;
    private UserOrderDto patient;
    private List<TestRunBatchDto> testRuns;
    private String parentOrderId;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar estArrivalInLab;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar receivedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(String parentOrderId) {
        this.parentOrderId = parentOrderId;
    }

    public UserOrderDto getPatient() {
        return patient;
    }

    public void setPatient(UserOrderDto patient) {
        this.patient = patient;
    }

    public List<TestRunBatchDto> getTestRuns() {
        return testRuns;
    }

    public void setTestRuns(List<TestRunBatchDto> testRun) {
        this.testRuns = testRun;
    }

    public Calendar getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Calendar receivedAt) {
        this.receivedAt = receivedAt;
    }

    public Calendar getEstArrivalInLab() {
        return estArrivalInLab;
    }

    public void setEstArrivalInLab(Calendar estArrivalInLab) {
        this.estArrivalInLab = estArrivalInLab;
    }
}
