package com.junodx.api.dto.models.laboratory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.laboratory.TestRun;
import com.junodx.api.models.laboratory.types.LaboratoryStatusType;

import javax.persistence.*;
import java.util.Calendar;

public class LaboratoryStatusDto {
    @JsonIgnore
    private Long id;

    private LaboratoryStatusType status;

    private boolean isCurrent;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar createdAt;

    private String createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LaboratoryStatusType getStatus() {
        return status;
    }

    public void setStatus(LaboratoryStatusType status) {
        this.status = status;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
