package com.junodx.api.models.patient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.providers.Provider;
import com.junodx.api.models.core.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="patient")
public class PatientDetails {

    @Id
    private String id;


    @OneToOne(mappedBy = "patientDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MedicalDetails medicalDetails;


    @OneToMany(mappedBy = "patientDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Medication> medications;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "patient_providers",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "provider_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Provider> providers;

    @OneToMany(targetEntity = Note.class, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Note> notes;

    @OneToOne(mappedBy = "patientDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PatientChart chart;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private User user;

    public PatientDetails(){
        id = UUID.randomUUID().toString();
        this.notes = new ArrayList<>();
 //       this.medications = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MedicalDetails getMedicalDetails() {
        return medicalDetails;
    }

    public void setMedicalDetails(MedicalDetails medicalDetails) {
        this.medicalDetails = medicalDetails;
        if(this.medicalDetails != null)
            this.medicalDetails.setPatientDetails(this);
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
        for(Medication m : medications)
            m.setPatientDetails(this);
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PatientChart getChart() {
        return chart;
    }

    public void setChart(PatientChart chart) {
        this.chart = chart;
    }
}
