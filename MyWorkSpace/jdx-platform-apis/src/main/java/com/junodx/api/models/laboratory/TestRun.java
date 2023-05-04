package com.junodx.api.models.laboratory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.models.laboratory.tests.Test;
import com.junodx.api.models.laboratory.types.LaboratoryStatusType;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.laboratory.types.SampleRunType;
import com.junodx.api.models.laboratory.types.TestRunType;
import org.hibernate.annotations.Cascade;
import org.hibernate.engine.jdbc.batch.spi.Batch;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "test_run")
public class TestRun {
    @Id
    private String id;


    @Column(name = "name", nullable = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected ReportConfiguration reportConfiguration;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Calendar startTime;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Calendar endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_order_id", referencedColumnName = "id", nullable = true)
    @JsonIgnore
    private LaboratoryOrder laboratoryOrder;

    @Column(name = "lims_report_id")
    private String limsReportId;

    @Column(name = "completed", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean completed;

  //  @Enumerated(EnumType.STRING)
  //  @Column(name = "sample_iteration_type", nullable = false)
  //  private SampleRunType sampleIterationType;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "kit_id", unique = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Kit kit;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testRun")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<LaboratoryStatus> status;

    @OneToOne(mappedBy = "testRun", fetch = FetchType.LAZY)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private TestReport report;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JoinColumn(name = "batch_run_id", nullable = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private BatchRun batch;

    @Column(name = "type")
    private TestRunType type;

    @Column(name = "retest", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean retest;

    @Column(name = "reflex", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean reflex;

    @Column(name = "redraw", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean redraw;

    public TestRun(){
        this.id = UUID.randomUUID().toString();
        this.limsReportId = UUID.randomUUID().toString();
        this.status = new ArrayList<>();
        this.type = TestRunType.RESEARCH;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void makeResearchRun(Kit kit){
        if(this.id == null)
            this.id = UUID.randomUUID().toString();

        this.kit = kit;
        this.type = TestRunType.RESEARCH;

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

    public LaboratoryOrder getLaboratoryOrder() {
        return laboratoryOrder;
    }

    public void setLaboratoryOrder(LaboratoryOrder laboratoryOrder) {
        this.laboratoryOrder = laboratoryOrder;
        this.type = TestRunType.STANDARD;
    }

    /*
    public SampleRunType getSampleIterationType() {
        return sampleIterationType;
    }

    public void setSampleIterationType(SampleRunType sampleIterationType) {
        this.sampleIterationType = sampleIterationType;
    }

     */

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public void addStatus(LaboratoryStatus status){
        if(this.status == null)
            this.status = new ArrayList<>();

        //Don't add dupes
        if(!this.status.stream().anyMatch(x->x.getStatus().equals(status.getStatus()) && x.isCurrent())) {
            setAllStatusesToNotCurrent();
            if (status.getCreatedAt() == null)
                status.setCreatedAt(Calendar.getInstance());
            this.status.add(status);
        }
    }

    public List<LaboratoryStatus> getStatus() {
        return status;
    }

    public void setStatus(List<LaboratoryStatus> status) {
        this.status = status;
        for(LaboratoryStatus s : this.status) {
            if(s.getCreatedAt() == null)
                s.setCreatedAt(Calendar.getInstance());
            s.setTestRun(this);
        }
    }

    public TestReport getReport() {
        return report;
    }

    public void setReport(TestReport report) {
        this.report = report;
    }

    public BatchRun getBatch() {
        return batch;
    }

    public void setBatch(BatchRun batch) {
        this.batch = batch;
    }

    @JsonProperty("currentStatus")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public LaboratoryStatusType getCurrentStatus(){
        if(this.status != null) {
            List<LaboratoryStatus> statusType = this.status.stream().filter(x -> x.isCurrent()).collect(Collectors.toList());
            if (this.status.size() == 0)
                return null;

            if (statusType != null && statusType.size() == 0) {
                setLatestStatusToCurrent();
                statusType = this.status.stream().filter(x -> x.isCurrent()).collect(Collectors.toList());
            } else if (statusType != null && statusType.size() > 1)
                resetStatusAndFindCurrent();

            return statusType.get(0).getStatus();
        }
        return null;
    }

    private void resetStatusAndFindCurrent(){
        List<LaboratoryStatus> statusType = this.status.stream().filter(x -> x.isCurrent()).collect(Collectors.toList());
        if(statusType != null && statusType.size() > 1) {
            Collections.sort(statusType);
            int index = 0;
            for(LaboratoryStatus s : statusType){
                if(index > 0)
                    s.setCurrent(false);
            }
        }
    }

    private void setLatestStatusToCurrent(){
        Collections.sort(this.status);
        if(this.status.size() > 0)
            this.status.get(0).setCurrent(true);
    }

    private void setAllStatusesToNotCurrent(){
        if(this.status != null){
            for(LaboratoryStatus l : this.status)
                l.setCurrent(false);
        }
    }

    public boolean isRetest() {
        return retest;
    }

    public void setRetest(boolean retest) {
        this.retest = retest;
    }

    public boolean isRedraw() {
        return redraw;
    }

    public void setRedraw(boolean redraw) {
        this.redraw = redraw;
    }

    public boolean isReflex() {
        return reflex;
    }

    public void setReflex(boolean reflex) {
        this.reflex = reflex;
    }

    public TestRunType getType() {
        return type;
    }

    public void setType(TestRunType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("batchId")
    public String getBatchId(){
        if(this.batch != null)
            return this.batch.getId();

        return null;
    }

    public ReportConfiguration getReportConfiguration() {
        return reportConfiguration;
    }

    public void setReportConfiguration(ReportConfiguration reportConfiguration) {
        this.reportConfiguration = reportConfiguration;
    }

    public String getLimsReportId() {
        return limsReportId;
    }

    public void setLimsReportId(String limsReportId) {
        this.limsReportId = limsReportId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isBeyondKitAssignment(){
        if(!this.getCurrentStatus().equals(LaboratoryStatusType.NO_KIT_ASSIGNED)
        && !this.getCurrentStatus().equals(LaboratoryStatusType.KIT_ASSIGNED)
        && !this.getCurrentStatus().equals(LaboratoryStatusType.KIT_CODES_UPDATED))
            return true;

        return false;
    }
}
