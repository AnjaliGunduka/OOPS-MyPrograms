package com.junodx.api.controllers.xifin.payloads.accession;

import com.junodx.api.controllers.xifin.payloads.Address;

import java.util.List;

public class InsuranceInfo {
  private Address address;
  private String authorizationNumber;
  private String caseId;
  private String claimNotes;
  private String comments;
  private String dateOfBirth;
  private String delayReasonCode;
  private String demonstrationProjectId;
  private List<EligibilityInfo> eligibilityInfo;
  private EmployerInfo employerInfo;

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String getAuthorizationNumber() {
    return authorizationNumber;
  }

  public void setAuthorizationNumber(String authorizationNumber) {
    this.authorizationNumber = authorizationNumber;
  }

  public String getCaseId() {
    return caseId;
  }

  public void setCaseId(String caseId) {
    this.caseId = caseId;
  }

  public String getClaimNotes() {
    return claimNotes;
  }

  public void setClaimNotes(String claimNotes) {
    this.claimNotes = claimNotes;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getDelayReasonCode() {
    return delayReasonCode;
  }

  public void setDelayReasonCode(String delayReasonCode) {
    this.delayReasonCode = delayReasonCode;
  }

  public String getDemonstrationProjectId() {
    return demonstrationProjectId;
  }

  public void setDemonstrationProjectId(String demonstrationProjectId) {
    this.demonstrationProjectId = demonstrationProjectId;
  }

  public List<EligibilityInfo> getEligibilityInfo() {
    return eligibilityInfo;
  }

  public void setEligibilityInfo(List<EligibilityInfo> eligibilityInfo) {
    this.eligibilityInfo = eligibilityInfo;
  }

  public EmployerInfo getEmployerInfo() {
    return employerInfo;
  }

  public void setEmployerInfo(EmployerInfo employerInfo) {
    this.employerInfo = employerInfo;
  }
}
