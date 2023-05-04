package com.junodx.api.dto.models.laboratory.reports;

import com.junodx.api.models.laboratory.tests.types.QCType;

public class PCRQCDto extends TestQCDto {
    private static final QCType type = QCType.SEQUENCING;
    private boolean snrFlag;
    private boolean passed;

    public boolean isSnrFlag() {
        return snrFlag;
    }

    public void setSnrFlag(boolean snrFlag) {
        this.snrFlag = snrFlag;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
