package com.junodx.api.models.patient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class PatientChart {
    @Id
    private String id;

    @OneToMany(mappedBy = "patientChart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    protected List<PatientChartEntry> chartEntries;

    @OneToOne
    @JoinColumn(name = "patient_details_id")
    @JsonIgnore
    private PatientDetails patientDetails;

    public PatientChart() {
        this.id = UUID.randomUUID().toString();
        chartEntries = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PatientChartEntry> getChartEntries() {
        return chartEntries;
    }

    public void setChartEntries(List<PatientChartEntry> chartEntries) {
        this.chartEntries = chartEntries;
    }

    public void addChartEntry(PatientChartEntry entry){
        if(this.chartEntries == null)
            this.chartEntries = new ArrayList<>();
        this.chartEntries.add(entry);
    }

    public PatientDetails getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(PatientDetails patientDetails) {
        this.patientDetails = patientDetails;
    }
}
