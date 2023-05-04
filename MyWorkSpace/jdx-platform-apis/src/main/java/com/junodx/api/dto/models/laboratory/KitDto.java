package com.junodx.api.dto.models.laboratory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.fulfillment.Fulfillment;
import com.junodx.api.models.laboratory.types.KitType;

import javax.persistence.*;
import java.util.UUID;


public class KitDto {
    @JsonIgnore
    private String id;

    @JsonIgnore
    private Fulfillment fulfillment;

    private String code;

    private String sampleNumber;

    private KitType type;

    @JsonIgnore
    private Meta meta;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Fulfillment getFulfillment() {
        return fulfillment;
    }

    public void setFulfillment(Fulfillment fulfillment) {
        this.fulfillment = fulfillment;
    }
}
