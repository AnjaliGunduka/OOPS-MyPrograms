package com.junodx.api.controllers.xifin.payloads.client;

public class Po {
  private String expDate;
  private String issueDate;
  private String poAmt;
  private String poNumber;

  public String getExpDate() {
    return expDate;
  }

  public void setExpDate(String expDate) {
    this.expDate = expDate;
  }

  public String getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(String issueDate) {
    this.issueDate = issueDate;
  }

  public String getPoAmt() {
    return poAmt;
  }

  public void setPoAmt(String poAmt) {
    this.poAmt = poAmt;
  }

  public String getPoNumber() {
    return poNumber;
  }

  public void setPoNumber(String poNumber) {
    this.poNumber = poNumber;
  }
}
