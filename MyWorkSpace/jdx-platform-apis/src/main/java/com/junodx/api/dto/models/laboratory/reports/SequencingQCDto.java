package com.junodx.api.dto.models.laboratory.reports;

import com.junodx.api.models.laboratory.tests.types.QCType;

public class SequencingQCDto extends TestQCDto {
    private static final QCType type = QCType.SEQUENCING;
    private boolean snrFlag;

    public boolean isSnrFlag() {
        return snrFlag;
    }

    public void setSnrFlag(boolean snrFlag) {
        this.snrFlag = snrFlag;
    }
}
