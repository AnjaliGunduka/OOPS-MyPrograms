package com.junodx.api.dto.models.laboratory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.dto.models.laboratory.reports.TestReportDto;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.models.laboratory.LaboratoryOrder;
import com.junodx.api.models.laboratory.LaboratoryStatus;
import com.junodx.api.models.laboratory.types.LaboratoryStatusType;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

public class TestRunDto {
    private String id;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar startTime;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar endTime;
    private KitLaboratoryDto kit;
    //private List<LaboratoryStatusDto> status;
    private LaboratoryStatusType currentStatus;
    private TestReportDto report;


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

    public KitLaboratoryDto getKit() {
        return kit;
    }

    public void setKit(KitLaboratoryDto kit) {
        this.kit = kit;
    }

    public LaboratoryStatusType getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(LaboratoryStatusType currentStatus) {
        this.currentStatus = currentStatus;
    }

    public TestReportDto getReport() {
        return report;
    }

    public void setReport(TestReportDto report) {
        this.report = report;
    }
}
