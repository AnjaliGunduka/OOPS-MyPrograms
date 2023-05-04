package com.junodx.api.models.laboratory.tests;

import com.junodx.api.models.laboratory.tests.types.GenderResultType;
import com.junodx.api.models.laboratory.tests.types.SCAResultType;

public class FetalSexTest extends Test {
    public static final String Name = "Fetal Sex Test";

    private GenderResultType genderResult;
    private SCAResultType scaResultType;
    private String scaResult;
    private Float genderConfidence;
    private Float scaConfidence;
    private Float xVec;
    private Float yVec;
    private Float yVec2;
    private Float xzScores;

    public FetalSexTest() {
        this.name = Name;
    }

    public FetalSexTest(GenderResultType gType,
                        SCAResultType scaType,
                        Float gCi,
                        Float scaCi,
                        Float xV,
                        Float yV,
                        Float yV2) {
        this.genderResult = gType;
        this.scaResultType = scaType;
        this.genderConfidence = gCi;
        this.scaConfidence = scaCi;
        this.xVec = xV;
        this.yVec = yV;
        this.yVec2 = yV2;
        this.name = Name;
    }

    public FetalSexTest(GenderResultType gType,
                        SCAResultType scaType,
                        Float gCi,
                        Float scaCi,
                        Float xV,
                        Float yV,
                        Float yV2,
                        Float xzScores) {
        this.genderResult = gType;
        this.scaResultType = scaType;
        this.genderConfidence = gCi;
        this.scaConfidence = scaCi;
        this.xVec = xV;
        this.yVec = yV;
        this.yVec2 = yV2;
        this.name = Name;
        this.xzScores = xzScores;
    }

    public GenderResultType getGenderResult() {
        return genderResult;
    }

    public void setGenderResult(GenderResultType genderResult) {
        this.genderResult = genderResult;
    }

    public SCAResultType getScaResultType() {
        return scaResultType;
    }

    public void setScaResultType(SCAResultType scaResultType) {
        this.scaResultType = scaResultType;
    }

    public String getScaResult() {
        return scaResult;
    }

    public void setScaResult(String scaResult) {
        this.scaResult = scaResult;
    }

    public Float getGenderConfidence() {
        return genderConfidence;
    }

    public void setGenderConfidence(Float genderConfidence) {
        this.genderConfidence = genderConfidence;
    }

    public Float getScaConfidence() {
        return scaConfidence;
    }

    public void setScaConfidence(Float scaConfidence) {
        this.scaConfidence = scaConfidence;
    }

    public Float getxVec() {
        return xVec;
    }

    public void setxVec(Float xVec) {
        this.xVec = xVec;
    }

    public Float getyVec() {
        return yVec;
    }

    public void setyVec(Float yVec) {
        this.yVec = yVec;
    }

    public Float getyVec2() {
        return yVec2;
    }

    public void setyVec2(Float yVec2) {
        this.yVec2 = yVec2;
    }

    public Float getXzScores() {
        return xzScores;
    }

    public void setXzScores(Float xzScores) {
        this.xzScores = xzScores;
    }
}
