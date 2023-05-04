package com.junodx.api.models.configuration.lims;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.laboratory.types.ReportConfiguration;

import javax.persistence.*;

@Entity
@Table(name = "lims_report_type")
public class ReportTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String limsId;
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_configuration")
    private ReportConfiguration reportConfiguration;

    @ManyToOne
    @JoinColumn(name = "lims_configuration_id")
    @JsonIgnore
    private LIMSConfigurationEntity limsConfiguration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLimsId() {
        return limsId;
    }

    public void setLimsId(String limsId) {
        this.limsId = limsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LIMSConfigurationEntity getLimsConfiguration() {
        return limsConfiguration;
    }

    public void setLimsConfiguration(LIMSConfigurationEntity limsConfiguration) {
        this.limsConfiguration = limsConfiguration;
    }

    public ReportConfiguration getReportConfiguration() {
        return reportConfiguration;
    }

    public void setReportConfiguration(ReportConfiguration reportConfiguration) {
        this.reportConfiguration = reportConfiguration;
    }
}
