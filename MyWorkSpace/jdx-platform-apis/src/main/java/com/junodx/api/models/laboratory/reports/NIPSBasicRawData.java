package com.junodx.api.models.laboratory.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.laboratory.tests.*;
import com.junodx.api.models.laboratory.types.ReportConfiguration;

public class NIPSBasicRawData extends RawData {
    //public String reportName;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Float fetalFraction;

    //Tests
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EuploidTest euploid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T13Test t13;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T18Test t18;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T21Test t21;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SCATest sca;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FetalSexTest fst;
    //private Microdeletions microdeletions;

    public NIPSBasicRawData(){

    }

    public Float getFetalFraction() {
        return fetalFraction;
    }

    public void setFetalFraction(Float fetalFraction) {
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

    public FetalSexTest getFst() {
        return fst;
    }

    public void setFst(FetalSexTest fst) {
        this.fst = fst;
    }


}
