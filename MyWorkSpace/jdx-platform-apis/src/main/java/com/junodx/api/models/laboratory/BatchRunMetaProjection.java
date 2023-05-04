package com.junodx.api.models.laboratory;

import java.util.Calendar;

public interface BatchRunMetaProjection {
    String getId();
    Calendar getStartTime();
    Calendar getEndTime();
    String getPipelineVersion();
    String getSequencingRunId();
    String getPipelineRunId();
    String getModelId();
    String getLimsPlateId();
    Calendar getReviewedAt();
    String getRunId();
    boolean getReviewed();
    int getTotalSamples();
    String getReportConfigurationsInRun();

}
