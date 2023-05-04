package com.junodx.api.dto.models.laboratory;

import com.junodx.api.models.core.types.IntervalType;
import com.junodx.api.models.laboratory.Laboratory;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

public class TestReportsReviewResultsDto {


  private int resultsAwaitingInvestigation;
  private int resultsAwaitingSignOutConfirmation;
  private int recentlySignedOut;
  private int recentlyManuallySignedOut;
  private int recentlyAutomaticallySignedOut;
  private int runsToBeApproved;
  private int upcomingResults;
  private Long id;


  public int getResultsAwaitingInvestigation() {
    return resultsAwaitingInvestigation;
  }

  public void setResultsAwaitingInvestigation(int resultsAwaitingInvestigation) {
    this.resultsAwaitingInvestigation = resultsAwaitingInvestigation;
  }

  public int getResultsAwaitingSignOutConfirmation() {
    return resultsAwaitingSignOutConfirmation;
  }

  public void setResultsAwaitingSignOutConfirmation(int resultsAwaitingSignOutConfirmation) {
    this.resultsAwaitingSignOutConfirmation = resultsAwaitingSignOutConfirmation;
  }

  public int getRecentlySignedOut() {
    return recentlySignedOut;
  }

  public void setRecentlySignedOut(int recentlySignedOut) {
    this.recentlySignedOut = recentlySignedOut;
  }

  public int getRecentlyManuallySignedOut() {
    return recentlyManuallySignedOut;
  }

  public void setRecentlyManuallySignedOut(int recentlyManuallySignedOut) {
    this.recentlyManuallySignedOut = recentlyManuallySignedOut;
  }

  public int getRecentlyAutomaticallySignedOut() {
    return recentlyAutomaticallySignedOut;
  }

  public void setRecentlyAutomaticallySignedOut(int recentlyAutomaticallySignedOut) {
    this.recentlyAutomaticallySignedOut = recentlyAutomaticallySignedOut;
  }

  public int getRunsToBeApproved() {
    return runsToBeApproved;
  }

  public void setRunsToBeApproved(int runsToBeApproved) {
    this.runsToBeApproved = runsToBeApproved;
  }

  public int getUpcomingResults() {
    return upcomingResults;
  }

  public void setUpcomingResults(int upcomingResults) {
    this.upcomingResults = upcomingResults;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
