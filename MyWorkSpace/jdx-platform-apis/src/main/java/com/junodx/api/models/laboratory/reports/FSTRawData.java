package com.junodx.api.models.laboratory.reports;

import com.junodx.api.models.laboratory.tests.SCATest;

public class FSTRawData extends RawData {
    public static final String NAME = "FST";

    private SCATest sca;

    public FSTRawData(){

    }

    public SCATest getSca() {
        return sca;
    }

    public void setSca(SCATest sca) {
        this.sca = sca;
    }
}
