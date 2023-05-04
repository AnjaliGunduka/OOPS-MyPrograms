package com.junodx.api.controllers.xifin.payloads.accession;

public class AccessionContactInfo {
  private String contactDate;
  private String contactInfo;
  private boolean followUpComplete;
  private String followUpDate;
  private String followUpUserId;
  private boolean printOnStatement;
  private String printableNotes;
  private String userId;
  private boolean voided;

  public String getContactDate() {
    return contactDate;
  }

  public void setContactDate(String contactDate) {
    this.contactDate = contactDate;
  }

  public String getContactInfo() {
    return contactInfo;
  }

  public void setContactInfo(String contactInfo) {
    this.contactInfo = contactInfo;
  }

  public boolean isFollowUpComplete() {
    return followUpComplete;
  }

  public void setFollowUpComplete(boolean followUpComplete) {
    this.followUpComplete = followUpComplete;
  }

  public String getFollowUpDate() {
    return followUpDate;
  }

  public void setFollowUpDate(String followUpDate) {
    this.followUpDate = followUpDate;
  }

  public String getFollowUpUserId() {
    return followUpUserId;
  }

  public void setFollowUpUserId(String followUpUserId) {
    this.followUpUserId = followUpUserId;
  }

  public boolean isPrintOnStatement() {
    return printOnStatement;
  }

  public void setPrintOnStatement(boolean printOnStatement) {
    this.printOnStatement = printOnStatement;
  }

  public String getPrintableNotes() {
    return printableNotes;
  }

  public void setPrintableNotes(String printableNotes) {
    this.printableNotes = printableNotes;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public boolean isVoided() {
    return voided;
  }

  public void setVoided(boolean voided) {
    this.voided = voided;
  }
}
