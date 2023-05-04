package com.junodx.api.controllers.xifin.payloads.accession;

public class SpecimenQuestion {
  private String questionId;
  private String response;
  private String specimenType;

  public String getQuestionId() {
    return questionId;
  }

  public void setQuestionId(String questionId) {
    this.questionId = questionId;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  public String getSpecimenType() {
    return specimenType;
  }

  public void setSpecimenType(String specimenType) {
    this.specimenType = specimenType;
  }
}
