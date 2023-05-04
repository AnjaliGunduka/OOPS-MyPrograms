package com.junodx.api.controllers.xifin.payloads.accession;

public class SpecimenInfo {
  private int count;
  private String specimenContainerCode;
  private SpecimenQuestion specimenQuestion;

  private String specimenSourceDescription;
  private String specimenTempType;

  public String getSpecimenSourceDescription() {
    return specimenSourceDescription;
  }

  public void setSpecimenSourceDescription(String specimenSourceDescription) {
    this.specimenSourceDescription = specimenSourceDescription;
  }

  public String getSpecimenTempType() {
    return specimenTempType;
  }

  public void setSpecimenTempType(String specimenTempType) {
    this.specimenTempType = specimenTempType;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getSpecimenContainerCode() {
    return specimenContainerCode;
  }

  public void setSpecimenContainerCode(String specimenContainerCode) {
    this.specimenContainerCode = specimenContainerCode;
  }

  public SpecimenQuestion getSpecimenQuestion() {
    return specimenQuestion;
  }

  public void setSpecimenQuestion(SpecimenQuestion specimenQuestion) {
    this.specimenQuestion = specimenQuestion;
  }
}
