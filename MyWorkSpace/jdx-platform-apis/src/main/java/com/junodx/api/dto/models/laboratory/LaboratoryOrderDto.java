package com.junodx.api.dto.models.laboratory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.dto.models.commerce.ProviderApprovalDto;
import com.junodx.api.dto.models.patient.ConsentDto;
import com.junodx.api.models.core.Meta;

import java.util.Calendar;
import java.util.List;

public class LaboratoryOrderDto {
    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String requisitionFormUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String notes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProviderApprovalDto providerApproval;

    private ConsentDto patientConsent;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LaboratoryDto lab;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TestRunDto> testRuns;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Calendar dateReceivedInLab;

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    //private KitLaboratoryDto kit;

    private String limsOrderId;
    private String parentOrderId;

    private UserOrderDto patient;

    private Meta meta;

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

    public LaboratoryDto getLab() {
        return lab;
    }

    public void setLab(LaboratoryDto lab) {
        this.lab = lab;
    }

    public List<TestRunDto> getTestRuns() {
        return testRuns;
    }

    public void setTestRuns(List<TestRunDto> testRuns) {
        this.testRuns = testRuns;
    }

    public String getLimsOrderId() {
        return limsOrderId;
    }

    public void setLimsOrderId(String limsOrderId) {
        this.limsOrderId = limsOrderId;
    }

    public UserOrderDto getPatient() {
        return patient;
    }

    public void setPatient(UserOrderDto patient) {
        this.patient = patient;
    }

    public Calendar getDateReceivedInLab() {
        return dateReceivedInLab;
    }

    public void setDateReceivedInLab(Calendar dateReceivedInLab) {
        this.dateReceivedInLab = dateReceivedInLab;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
