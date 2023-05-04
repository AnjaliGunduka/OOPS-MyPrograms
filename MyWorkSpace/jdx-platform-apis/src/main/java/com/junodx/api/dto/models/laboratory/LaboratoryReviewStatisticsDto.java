package com.junodx.api.dto.models.laboratory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.laboratory.LaboratoryReviewGroup;

import java.util.List;

public class LaboratoryReviewStatisticsDto {
    private String laboratoryId;

    private List<TestReportsAwaitingReviewDto> review;

    public String getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(String laboratoryId) {
        this.laboratoryId = laboratoryId;
    }

    public List<TestReportsAwaitingReviewDto> getReview() {
        return review;
    }

    public void setReview(List<TestReportsAwaitingReviewDto> review) {
        this.review = review;
    }
}
