package com.junodx.api.models.laboratory.tests;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LabConstants {

    @Id
    private Long id;

    @Column(name = "chr13_lower_cutoff")
    private float chr13LowerCutoff;

    @Column(name = "chr13_upper_cutoff")
    private float chr13UpperCutoff;

    @Column(name = "chr18_lower_cutoff")
    private float chr18LowerCutoff;

    @Column(name = "chr18_upper_cutoff")
    private float chr18UpperCutoff;

    @Column(name = "chr21_lower_cutoff")
    private float chr21LowerCutoff;

    @Column(name = "chr21_upper_cutoff")
    private float chr21UpperCutoff;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getChr13LowerCutoff() {
        return chr13LowerCutoff;
    }

    public void setChr13LowerCutoff(float chr13LowerCutoff) {
        this.chr13LowerCutoff = chr13LowerCutoff;
    }

    public float getChr13UpperCutoff() {
        return chr13UpperCutoff;
    }

    public void setChr13UpperCutoff(float chr13UpperCutoff) {
        this.chr13UpperCutoff = chr13UpperCutoff;
    }

    public float getChr18LowerCutoff() {
        return chr18LowerCutoff;
    }

    public void setChr18LowerCutoff(float chr18LowerCutoff) {
        this.chr18LowerCutoff = chr18LowerCutoff;
    }

    public float getChr18UpperCutoff() {
        return chr18UpperCutoff;
    }

    public void setChr18UpperCutoff(float chr18UpperCutoff) {
        this.chr18UpperCutoff = chr18UpperCutoff;
    }

    public float getChr21LowerCutoff() {
        return chr21LowerCutoff;
    }

    public void setChr21LowerCutoff(float chr21LowerCutoff) {
        this.chr21LowerCutoff = chr21LowerCutoff;
    }

    public float getChr21UpperCutoff() {
        return chr21UpperCutoff;
    }

    public void setChr21UpperCutoff(float chr21UpperCutoff) {
        this.chr21UpperCutoff = chr21UpperCutoff;
    }
}
