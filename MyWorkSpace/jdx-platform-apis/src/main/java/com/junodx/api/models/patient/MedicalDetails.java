package com.junodx.api.models.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name="patient_medical_details")
public class MedicalDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private boolean isPregnant;
    private float gestationalAge;
    private int numberOfFetuses;
    private boolean threeOrMoreFetuses;

    @Column(name = "no_organ_transplant", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean noOrganTransplant;

    @Column(name = "no_blood_transfusion", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean noBloodTransfusion;

    private Calendar lastUpdatedAt;

    @OneToMany(mappedBy = "medicalDetails")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Vital> vitals;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Calendar conceptionDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    @JsonIgnore
    private PatientDetails patientDetails;

    public MedicalDetails(){
        this.vitals = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPregnant() {
        return isPregnant;
    }

    public void setPregnant(boolean pregnant) {
        isPregnant = pregnant;
    }

    public float getGestationalAge() {
        return gestationalAge;
    }

    public void setGestationalAge(float gestationalAge) {
        this.gestationalAge = gestationalAge;
    }

    public int getNumberOfFetuses() {
        return numberOfFetuses;
    }

    public void setNumberOfFetuses(int numberOfFetuses) {
        this.numberOfFetuses = numberOfFetuses;
    }

    public boolean isThreeOrMoreFetuses() {
        return threeOrMoreFetuses;
    }

    public void setThreeOrMoreFetuses(boolean threeOrMoreFetuses) {
        this.threeOrMoreFetuses = threeOrMoreFetuses;
    }

    public List<Vital> getVitals() {
        return vitals;
    }

    public void setVitals(List<Vital> vitals) {
        this.vitals = vitals;
        for(Vital v : this.vitals)
            v.setMedicalDetails(this);
    }

    public void addVital(Vital vital){
        if(this.vitals == null)
            this.vitals = new ArrayList<>();
        this.vitals.add(vital);
    }

    public void removeVital(Vital vital){
        if(this.vitals == null) {
            this.vitals = new ArrayList<>();
        } else
            this.vitals.remove(vital);
    }

    public PatientDetails getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(PatientDetails patientDetails) {
        this.patientDetails = patientDetails;
    }

    public Calendar getConceptionDate() {
        return conceptionDate;
    }

    public void setConceptionDate(Calendar conceptionDate) {
        this.conceptionDate = conceptionDate;
    }

    public Calendar getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Calendar lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public boolean isNoOrganTransplant() {
        return noOrganTransplant;
    }

    public void setNoOrganTransplant(boolean noOrganTransplant) {
        this.noOrganTransplant = noOrganTransplant;
    }

    public boolean isNoBloodTransfusion() {
        return noBloodTransfusion;
    }

    public void setNoBloodTransfusion(boolean noBloodTransfusion) {
        this.noBloodTransfusion = noBloodTransfusion;
    }
}
