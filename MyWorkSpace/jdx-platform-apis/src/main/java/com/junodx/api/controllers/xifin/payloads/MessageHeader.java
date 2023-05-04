package com.junodx.api.controllers.xifin.payloads;

public class MessageHeader {
  private String orgAlias;
  private String organizationName;
  private String sequenceNumber;
  private String sourceApplication;
  private String userId;

  public String getOrgAlias() {
    return orgAlias;
  }

  public void setOrgAlias(String orgAlias) {
    this.orgAlias = orgAlias;
  }

  public String getOrganizationName() {
    return organizationName;
  }

  public void setOrganizationName(String organizationName) {
    this.organizationName = organizationName;
  }

  public String getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(String sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public String getSourceApplication() {
    return sourceApplication;
  }

  public void setSourceApplication(String sourceApplication) {
    this.sourceApplication = sourceApplication;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
