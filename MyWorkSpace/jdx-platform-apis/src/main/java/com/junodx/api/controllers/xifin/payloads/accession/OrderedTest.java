package com.junodx.api.controllers.xifin.payloads.accession;

import java.util.List;

public class OrderedTest {

  private String abnComment;
  private String abnReason;
  private boolean abnReceived;
  private String alternativeTestName;
  private String authorizationNumber;
  private boolean clientBilled;
  private List<DiagnosisCode> diagnosisCodes;
  private String finalReportDate;
  private List<LabMessage> labMessages;
  private int manualPrice;
  private Modifiers modifiers;
  private String note;
  private String placeOfService;
  private String profileId;
  private String renal;
  private Physican renderingPhysician;
  private String result;
  private String splitPayorId;
  private String standingOrderId;
  private String testId;
  private List<TestQuestion> testQuestions;
  private int testSequenceNumber;
  private String transactionType;
  private int units;

  public String getAbnComment() {
    return abnComment;
  }

  public void setAbnComment(String abnComment) {
    this.abnComment = abnComment;
  }

  public String getAbnReason() {
    return abnReason;
  }

  public void setAbnReason(String abnReason) {
    this.abnReason = abnReason;
  }

  public boolean isAbnReceived() {
    return abnReceived;
  }

  public void setAbnReceived(boolean abnReceived) {
    this.abnReceived = abnReceived;
  }

  public String getAlternativeTestName() {
    return alternativeTestName;
  }

  public void setAlternativeTestName(String alternativeTestName) {
    this.alternativeTestName = alternativeTestName;
  }

  public String getAuthorizationNumber() {
    return authorizationNumber;
  }

  public void setAuthorizationNumber(String authorizationNumber) {
    this.authorizationNumber = authorizationNumber;
  }

  public boolean isClientBilled() {
    return clientBilled;
  }

  public void setClientBilled(boolean clientBilled) {
    this.clientBilled = clientBilled;
  }

  public List<DiagnosisCode> getDiagnosisCodes() {
    return diagnosisCodes;
  }

  public void setDiagnosisCodes(List<DiagnosisCode> diagnosisCodes) {
    this.diagnosisCodes = diagnosisCodes;
  }

  public String getFinalReportDate() {
    return finalReportDate;
  }

  public void setFinalReportDate(String finalReportDate) {
    this.finalReportDate = finalReportDate;
  }

  public List<LabMessage> getLabMessages() {
    return labMessages;
  }

  public void setLabMessages(List<LabMessage> labMessages) {
    this.labMessages = labMessages;
  }

  public int getManualPrice() {
    return manualPrice;
  }

  public void setManualPrice(int manualPrice) {
    this.manualPrice = manualPrice;
  }

  public Modifiers getModifiers() {
    return modifiers;
  }

  public void setModifiers(Modifiers modifiers) {
    this.modifiers = modifiers;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getPlaceOfService() {
    return placeOfService;
  }

  public void setPlaceOfService(String placeOfService) {
    this.placeOfService = placeOfService;
  }

  public String getProfileId() {
    return profileId;
  }

  public void setProfileId(String profileId) {
    this.profileId = profileId;
  }

  public String getRenal() {
    return renal;
  }

  public void setRenal(String renal) {
    this.renal = renal;
  }

  public Physican getRenderingPhysician() {
    return renderingPhysician;
  }

  public void setRenderingPhysician(Physican renderingPhysician) {
    this.renderingPhysician = renderingPhysician;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getSplitPayorId() {
    return splitPayorId;
  }

  public void setSplitPayorId(String splitPayorId) {
    this.splitPayorId = splitPayorId;
  }

  public String getStandingOrderId() {
    return standingOrderId;
  }

  public void setStandingOrderId(String standingOrderId) {
    this.standingOrderId = standingOrderId;
  }

  public String getTestId() {
    return testId;
  }

  public void setTestId(String testId) {
    this.testId = testId;
  }

  public List<TestQuestion> getTestQuestions() {
    return testQuestions;
  }

  public void setTestQuestions(List<TestQuestion> testQuestions) {
    this.testQuestions = testQuestions;
  }

  public int getTestSequenceNumber() {
    return testSequenceNumber;
  }

  public void setTestSequenceNumber(int testSequenceNumber) {
    this.testSequenceNumber = testSequenceNumber;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public int getUnits() {
    return units;
  }

  public void setUnits(int units) {
    this.units = units;
  }
}
