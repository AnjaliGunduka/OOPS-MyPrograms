package com.junodx.api.dto.models.laboratory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.laboratory.types.LaboratoryStatusType;

import java.util.Calendar;
import java.util.List;

public class TestRunBatchDto {
    private String id;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar startTime;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar endTime;
    private KitBatchDto kit;
    private LaboratoryStatusType currentStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public KitBatchDto getKit() {
        return kit;
    }

    public void setKit(KitBatchDto kit) {
        this.kit = kit;
    }

    public LaboratoryStatusType getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(LaboratoryStatusType currentStatus) {
        this.currentStatus = currentStatus;
    }
}
