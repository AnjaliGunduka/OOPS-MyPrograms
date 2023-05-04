package com.junodx.api.dto.models.laboratory.reports;

import com.junodx.api.models.laboratory.tests.*;

public class NIPSBasicReportDto extends ReportDto {
    public static final String NAME = "NIPS_BASIC";

    private float fetalFraction;

    //Tests
    private EuploidTest euploid;
    private T13Test t13;
    private T18Test t18;
    private T21Test t21;
    private SCATest sca;

    public float getFetalFraction() {
        return fetalFraction;
    }

    public void setFetalFraction(float fetalFraction) {
        this.fetalFraction = fetalFraction;
    }

    public EuploidTest getEuploid() {
        return euploid;
    }

    public void setEuploid(EuploidTest euploid) {
        this.euploid = euploid;
    }

    public T13Test getT13() {
        return t13;
    }

    public void setT13(T13Test t13) {
        this.t13 = t13;
    }

    public T18Test getT18() {
        return t18;
    }

    public void setT18(T18Test t18) {
        this.t18 = t18;
    }

    public T21Test getT21() {
        return t21;
    }

    public void setT21(T21Test t21) {
        this.t21 = t21;
    }

    public SCATest getSca() {
        return sca;
    }

    public void setSca(SCATest sca) {
        this.sca = sca;
    }
}
