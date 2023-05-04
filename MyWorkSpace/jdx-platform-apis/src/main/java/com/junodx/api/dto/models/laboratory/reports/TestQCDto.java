package com.junodx.api.dto.models.laboratory.reports;

import com.junodx.api.models.laboratory.tests.types.QCType;

public class TestQCDto {
    private QCType qcType;
    private boolean passed;

    public QCType getQcType() {
        return qcType;
    }

    public void setQcType(QCType qcType) {
        this.qcType = qcType;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
