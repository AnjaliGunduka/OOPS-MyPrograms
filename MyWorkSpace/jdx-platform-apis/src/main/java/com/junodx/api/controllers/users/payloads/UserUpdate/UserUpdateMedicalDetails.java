package com.junodx.api.controllers.users.payloads.UserUpdate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.patient.Vital;

import java.util.List;

public class UserUpdateMedicalDetails {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isPregnant;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Float gestationalAge;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer numberOfFetuses;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean threeOrMoreFetuses;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean noOrganTransplant;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean noBloodTransfusion;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Vital> vitals;

    public UserUpdateMedicalDetails() {

    }

    public Boolean getPregnant() {
        return isPregnant;
    }

    public void setPregnant(Boolean pregnant) {
        isPregnant = pregnant;
    }

    public Float getGestationalAge() {
        return gestationalAge;
    }

    public void setGestationalAge(Float gestationalAge) {
        this.gestationalAge = gestationalAge;
    }

    public Integer getNumberOfFetuses() {
        return numberOfFetuses;
    }

    public void setNumberOfFetuses(Integer numberOfFetuses) {
        this.numberOfFetuses = numberOfFetuses;
    }

    public Boolean getThreeOrMoreFetuses() {
        return threeOrMoreFetuses;
    }

    public void setThreeOrMoreFetuses(Boolean threeOrMoreFetuses) {
        this.threeOrMoreFetuses = threeOrMoreFetuses;
    }

    public Boolean getNoOrganTransplant() {
        return noOrganTransplant;
    }

    public void setNoOrganTransplant(Boolean noOrganTransplant) {
        this.noOrganTransplant = noOrganTransplant;
    }

    public Boolean getNoBloodTransfusion() {
        return noBloodTransfusion;
    }

    public void setNoBloodTransfusion(Boolean noBloodTransfusion) {
        this.noBloodTransfusion = noBloodTransfusion;
    }

    public List<Vital> getVitals() {
        return vitals;
    }

    public void setVitals(List<Vital> vitals) {
        this.vitals = vitals;
    }
}
