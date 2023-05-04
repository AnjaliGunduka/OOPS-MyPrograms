package com.junodx.api.models.laboratory.tests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.junodx.api.models.laboratory.reports.PCRQC;
import com.junodx.api.models.laboratory.reports.SequencingQC;
import com.junodx.api.models.laboratory.tests.types.QCType;
import com.junodx.api.models.laboratory.tests.types.SnpIdentityType;

import javax.persistence.*;

@Embeddable
public class TestQC {

    @Column(name = "qc_type")
    private QCType type;

    @Column(name = "c13_snr_flag", columnDefinition = "BOOLEAN DEFAULT FALSE")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Boolean c13Snr;

    @Column(name = "chr13_sens", columnDefinition = "REAL DEFAULT 0.0")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Float chr13Sens;

    @Column(name = "c18_snr_flag", columnDefinition = "BOOLEAN DEFAULT FALSE")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Boolean c18Snr;

    @Column(name = "chr18_sens", columnDefinition = "REAL DEFAULT 0.0")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Float chr18Sens;

    @Column(name = "c21_snr_flag", columnDefinition = "BOOLEAN DEFAULT FALSE")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Boolean c21Snr;

    @Column(name = "chr21_sens", columnDefinition = "REAL DEFAULT 0.0")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Float chr21Sens;

    @Column(name="low_reads_flag", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean lowReads;

    @Column(name="fragment_distribution_flag", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean fragmentDistribution;

    @Column(name = "fragment_dist_plot_path")
    private String fragmentDistributionPlotUrl;

    @Column(name = "is_passed", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isPassed;

    @Column(name = "raw_counts")
    private Integer rawCounts;

    @Enumerated(EnumType.STRING)
    @Column(name = "snp_identity_flag", columnDefinition = "varchar(255) DEFAULT 'FAIL'")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private SnpIdentityType snpIdentity;

    public TestQC(){

    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }

    public QCType getType() {
        return type;
    }

    public void setType(QCType type) {
        this.type = type;
    }

    public Boolean getC13Snr() {
        return c13Snr;
    }

    public void setC13Snr(Boolean c13Snr) {
        this.c13Snr = c13Snr;
    }

    public Float getChr13Sens() {
        return chr13Sens;
    }

    public void setChr13Sens(Float chr13Sens) {
        this.chr13Sens = chr13Sens;
    }

    public Boolean getC18Snr() {
        return c18Snr;
    }

    public void setC18Snr(Boolean c18Snr) {
        this.c18Snr = c18Snr;
    }

    public Float getChr18Sens() {
        return chr18Sens;
    }

    public void setChr18Sens(Float chr18Sens) {
        this.chr18Sens = chr18Sens;
    }

    public Boolean getC21Snr() {
        return c21Snr;
    }

    public void setC21Snr(Boolean c21Snr) {
        this.c21Snr = c21Snr;
    }

    public Float getChr21Sens() {
        return chr21Sens;
    }

    public void setChr21Sens(Float chr21Sens) {
        this.chr21Sens = chr21Sens;
    }

    public Boolean getLowReads() {
        return lowReads;
    }

    public void setLowReads(Boolean lowReads) {
        this.lowReads = lowReads;
    }

    public Boolean getFragmentDistribution() {
        return fragmentDistribution;
    }

    public void setFragmentDistribution(Boolean fragmentDistribution) {
        this.fragmentDistribution = fragmentDistribution;
    }

    public String getFragmentDistributionPlotUrl() {
        return fragmentDistributionPlotUrl;
    }

    public void setFragmentDistributionPlotUrl(String fragmentDistributionPlotUrl) {
        this.fragmentDistributionPlotUrl = fragmentDistributionPlotUrl;
    }

    public Integer getRawCounts() {
        return rawCounts;
    }

    public void setRawCounts(Integer rawCounts) {
        this.rawCounts = rawCounts;
    }

    public SnpIdentityType getSnpIdentity() {
        return snpIdentity;
    }

    public void setSnpIdentity(SnpIdentityType snpIdentity) {
        this.snpIdentity = snpIdentity;
    }
}
