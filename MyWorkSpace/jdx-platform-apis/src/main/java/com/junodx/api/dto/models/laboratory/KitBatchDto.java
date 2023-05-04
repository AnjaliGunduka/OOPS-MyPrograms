package com.junodx.api.dto.models.laboratory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.fulfillment.Fulfillment;
import com.junodx.api.models.laboratory.types.KitType;

public class KitBatchDto {
    private String id;
    private String code;
    private String sampleNumber;
    private KitType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public KitType getType() {
        return type;
    }

    public void setType(KitType type) {
        this.type = type;
    }
}
