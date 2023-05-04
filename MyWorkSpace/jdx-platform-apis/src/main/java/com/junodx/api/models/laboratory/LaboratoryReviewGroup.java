package com.junodx.api.models.laboratory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.laboratory.types.ReportType;

import javax.persistence.*;

//@Entity
//@Table(name = "laboratory_review_group_statistics")
public class LaboratoryReviewGroup {
 //   @Id
 //   @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

 //   @ManyToOne(fetch = FetchType.EAGER)
 //   @JoinColumn(name = "laboratory_statistics_id", nullable = false)
    @JsonIgnore
    private LaboratoryReviewStatistics laboratoryReviewStatistics;

//    @Enumerated(EnumType.STRING)
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

    public LaboratoryReviewStatistics getLaboratoryReviewStatistics() {
        return laboratoryReviewStatistics;
    }

    public void setLaboratoryReviewStatistics(LaboratoryReviewStatistics laboratoryReviewStatistics) {
        this.laboratoryReviewStatistics = laboratoryReviewStatistics;
    }
}
