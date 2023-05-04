package com.junodx.api.models.laboratory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.laboratory.types.ResultType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ResultValue {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name="result_type")
    private ResultType type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name="result_value")
    private Serializable value;

    public ResultValue(ResultType type, Serializable value){
        this.type = type;
        this.value = value;
    }

    public ResultValue() {
    }

    public ResultType getType() {
        return type;
    }

    public void setType(ResultType type) {
        this.type = type;
    }

    public Serializable getValue() {
        return value;
    }

    public void setValue(Serializable value) {
        this.value = value;
    }
}
