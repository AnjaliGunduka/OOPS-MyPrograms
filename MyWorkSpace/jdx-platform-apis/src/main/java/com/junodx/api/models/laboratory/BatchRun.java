package com.junodx.api.models.laboratory;

import com.fasterxml.jackson.annotation.*;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.laboratory.types.ReportConfigurationCounts;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "batch_run")
public class BatchRun {
    @Id
    private String id;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar startTime;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar endTime;

    private int totalSamples;

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratory_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Laboratory laboratory;

    @OneToMany(mappedBy = "batch", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TestRun> testRuns;

    @Column(name = "run_id")
    private Long runId;

    @Column(name = "sequencing_run_id", unique = true)
    private String sequencingRunId;

    @Column(name = "lims_plate_id", unique = true)
    private String limsPlateId;

    @Column(name = "pipeline_run_id", unique = true)
    private String pipelineRunId;

    private String pipelineVersion;
    private String modelId;

    private boolean reviewed;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar reviewedAt;

    @JsonProperty("reportConfigurations")
    @Transient
    private List<ReportConfigurationCounts> reportConfigurations;

    @JsonIgnore
    @Column(name="report_configuration_type")
    private String reportConfigurationsInRun;

    private Meta meta;

    public BatchRun(){
        this.id = UUID.randomUUID().toString();
        this.reportConfigurations = new ArrayList<>();
    }

    public static BatchRun build(Optional<String> sequencingRunId, Optional<String> pipelineRunId, Optional<String> pipelineVersion, Optional<String> modelId, Optional<Boolean> reviewed, Optional<String> limsPlateId, Optional<Laboratory> laboratory) {
        BatchRun run = new BatchRun();
        run.setSequencingRunId(sequencingRunId.orElse(null));
        run.setPipelineRunId(pipelineRunId.orElse(null));
        run.setPipelineVersion(pipelineVersion.orElse(null));
        run.setModelId(modelId.orElse(null));
        if(reviewed.isPresent())
            run.setReviewed(reviewed.get());
        run.setLimsPlateId(limsPlateId.orElse(null));
        run.setLaboratory(laboratory.orElse(null));

        return run;
    }

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

    public List<TestRun> getTestRuns() {
        return testRuns;
    }

    public void setTestRuns(List<TestRun> testRuns) {
        this.testRuns = testRuns;
    }

    public int getTotalSamples() {
        return totalSamples;
    }

    public void setTotalSamples(int totalSamples) {
        this.totalSamples = totalSamples;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }

    public String getSequencingRunId() {
        return sequencingRunId;
    }

    public void setSequencingRunId(String sequencingRunId) {
        this.sequencingRunId = sequencingRunId;
    }

    public String getLimsPlateId() {
        return limsPlateId;
    }

    public void setLimsPlateId(String limsPlateId) {
        this.limsPlateId = limsPlateId;
    }

    public String getPipelineRunId() {
        return pipelineRunId;
    }

    public void setPipelineRunId(String pipelineRunId) {
        this.pipelineRunId = pipelineRunId;
    }

    public String getPipelineVersion() {
        return pipelineVersion;
    }

    public void setPipelineVersion(String pipelineVersion) {
        this.pipelineVersion = pipelineVersion;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public Calendar getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Calendar reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }


    public List<ReportConfigurationCounts> getReportConfigurations() {
        if(this.reportConfigurationsInRun != null) {
            this.reportConfigurations.clear();
            String[] configs = this.reportConfigurationsInRun.split(",");
            for(String config : configs) {
                String[] configCount = config.split(":");
                if(configCount.length == 2) {
                    ReportConfigurationCounts count = new ReportConfigurationCounts();
                    count.setConfig(ReportConfiguration.valueOf(configCount[0]));
                    count.setCount(Integer.parseInt(configCount[1]));
                    this.reportConfigurations.add(count);
                }
            }
        }

        return reportConfigurations;
    }

    public void setReportConfigurations(List<ReportConfigurationCounts> reportConfigurations) {
        this.reportConfigurations = reportConfigurations;
        this.reportConfigurationsInRun = "";
        int count = reportConfigurations.size() - 1;
        for(ReportConfigurationCounts config : reportConfigurations){
            this.reportConfigurationsInRun += config.getConfig().name();
            this.reportConfigurationsInRun += ":" + config.getCount();
            if(count > 0) {
                this.reportConfigurationsInRun += ",";
                count--;
            }
        }
    }

    public String getReportConfigurationsInRun() {
        return reportConfigurationsInRun;
    }

    public void setReportConfigurationsInRun(String reportConfigurationsInRun) {
        this.reportConfigurationsInRun = reportConfigurationsInRun;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
