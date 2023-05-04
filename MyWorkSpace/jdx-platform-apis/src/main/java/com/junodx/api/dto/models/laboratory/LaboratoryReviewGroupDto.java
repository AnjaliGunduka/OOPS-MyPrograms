package com.junodx.api.dto.models.laboratory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.laboratory.types.ReportType;

public class LaboratoryReviewGroupDto {
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private LaboratoryReviewStatisticsDto laboratoryReviewStatistics;

    private ReportConfiguration name;

    private int totalAwaitingReview;
    private int totalRequiringManualReview;
    private int totalRequiringAutomaticReview;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LaboratoryReviewStatisticsDto getLaboratoryReviewStatistics() {
        return laboratoryReviewStatistics;
    }

    public void setLaboratoryReviewStatistics(LaboratoryReviewStatisticsDto laboratoryReviewStatistics) {
        this.laboratoryReviewStatistics = laboratoryReviewStatistics;
    }

    public ReportConfiguration getName() {
        return name;
    }

    public void setName(ReportConfiguration name) {
        this.name = name;
    }

    public int getTotalAwaitingReview() {
        return totalAwaitingReview;
    }

    public void setTotalAwaitingReview(int totalAwaitingReview) {
        this.totalAwaitingReview = totalAwaitingReview;
    }

    public int getTotalRequiringManualReview() {
        return totalRequiringManualReview;
    }

    public void setTotalRequiringManualReview(int totalRequiringManualReview) {
        this.totalRequiringManualReview = totalRequiringManualReview;
    }

    public int getTotalRequiringAutomaticReview() {
        return totalRequiringAutomaticReview;
    }

    public void setTotalRequiringAutomaticReview(int totalRequiringAutomaticReview) {
        this.totalRequiringAutomaticReview = totalRequiringAutomaticReview;
    }
}
