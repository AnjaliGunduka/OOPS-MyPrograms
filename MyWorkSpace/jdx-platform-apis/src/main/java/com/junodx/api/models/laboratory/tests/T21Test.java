package com.junodx.api.models.laboratory.tests;


public class T21Test extends Test {
    public static final String Name = "Z21";

    private Float zScore;
    private Float confidenceLower;
    private Float confidenceUpper;
    private Float confidence;
    private Boolean call;

    public T21Test(){
        this.name = Name;
    }

    public T21Test(Float zScore, Float ci_lower, Float ci_upper, Boolean call){
        this.zScore = zScore;
        this.confidenceLower = ci_lower;
        this.confidenceUpper = ci_upper;
        this.call = call;
        this.name = Name;
    }

    public Float getConfidenceLower() {
        if(confidenceLower == null)
            if(confidence != null)
                this.confidenceLower = confidence;

        return confidenceLower;
    }

    public void setConfidenceLower(Float confidenceLower) {
        this.confidenceLower = confidenceLower;
    }

    public Float getConfidenceUpper() {
        if(confidenceUpper == null)
            if(confidence != null)
                this.confidenceUpper = confidence;
        return confidenceUpper;
    }

    public void setConfidenceUpper(Float confidenceUpper) {
        this.confidenceUpper = confidenceUpper;
    }

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

    public Float getzScore() {
        return zScore;
    }

    public void setzScore(Float zScore) {
        this.zScore = zScore;
    }

    public Boolean getCall() {
        return call;
    }

    public void setCall(Boolean call) {
        this.call = call;
    }
}
