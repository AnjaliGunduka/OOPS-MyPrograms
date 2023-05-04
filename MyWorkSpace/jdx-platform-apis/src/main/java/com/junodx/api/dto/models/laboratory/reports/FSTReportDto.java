package com.junodx.api.dto.models.laboratory.reports;

import com.junodx.api.models.laboratory.tests.SCATest;

public class FSTReportDto extends ReportDto {
    public static final String NAME = "FST";

    private SCATest sca;

    public SCATest getSca() {
        return sca;
    }

    public void setSca(SCATest sca) {
        this.sca = sca;
    }
}
