package com.junodx.api.controllers.xifin.payloads.accession;

public class AccessionLevelDiagnosisCode {
  private String clientContact;
  private String clientContactDate;
  private String comment;
  private boolean deleted;
  private String diagnosisCode;
  private int diagnosisCodeOrder;
  private String documentId;
  private String narrativeDiag;
  private String userId;
  private String voidType;

  public String getClientContact() {
    return clientContact;
  }

  public void setClientContact(String clientContact) {
    this.clientContact = clientContact;
  }

  public String getClientContactDate() {
    return clientContactDate;
  }

  public void setClientContactDate(String clientContactDate) {
    this.clientContactDate = clientContactDate;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public String getDiagnosisCode() {
    return diagnosisCode;
  }

  public void setDiagnosisCode(String diagnosisCode) {
    this.diagnosisCode = diagnosisCode;
  }

  public int getDiagnosisCodeOrder() {
    return diagnosisCodeOrder;
  }

  public void setDiagnosisCodeOrder(int diagnosisCodeOrder) {
    this.diagnosisCodeOrder = diagnosisCodeOrder;
  }

  public String getDocumentId() {
    return documentId;
  }

  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

  public String getNarrativeDiag() {
    return narrativeDiag;
  }

  public void setNarrativeDiag(String narrativeDiag) {
    this.narrativeDiag = narrativeDiag;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getVoidType() {
    return voidType;
  }

  public void setVoidType(String voidType) {
    this.voidType = voidType;
  }
}
