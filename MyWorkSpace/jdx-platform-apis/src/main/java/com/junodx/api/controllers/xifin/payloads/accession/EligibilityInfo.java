package com.junodx.api.controllers.xifin.payloads.accession;

public class EligibilityInfo {
  private String eligibilityCheckedDate;
  private String eligibilityService;
  private String eligibilityStatus;
  private int eligibilityTransactionId;

  public String getEligibilityCheckedDate() {
    return eligibilityCheckedDate;
  }

  public void setEligibilityCheckedDate(String eligibilityCheckedDate) {
    this.eligibilityCheckedDate = eligibilityCheckedDate;
  }

  public String getEligibilityService() {
    return eligibilityService;
  }

  public void setEligibilityService(String eligibilityService) {
    this.eligibilityService = eligibilityService;
  }

  public String getEligibilityStatus() {
    return eligibilityStatus;
  }

  public void setEligibilityStatus(String eligibilityStatus) {
    this.eligibilityStatus = eligibilityStatus;
  }

  public int getEligibilityTransactionId() {
    return eligibilityTransactionId;
  }

  public void setEligibilityTransactionId(int eligibilityTransactionId) {
    this.eligibilityTransactionId = eligibilityTransactionId;
  }
}
