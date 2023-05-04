package com.junodx.api.dto.models.laboratory;

import com.junodx.api.models.laboratory.types.ReportConfiguration;

public class TestReportsAwaitingReviewDto {
  private ReportConfiguration name;
  private int totalAwaitingReview;
  private int totalRequiringManualReview;
  private int totalRequiringAutomaticReview;

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

  public ReportConfiguration getReportConfiguration() {
    return name;
  }

  public void setReportConfiguration(ReportConfiguration reportConfiguration) {
    this.name = reportConfiguration;
  }
}
