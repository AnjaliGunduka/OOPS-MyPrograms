package com.junodx.api.controllers.xifin.payloads.accession;

public class DialysisInfo {
  private String dialysisType;
  private String medication;

  public String getDialysisType() {
    return dialysisType;
  }

  public void setDialysisType(String dialysisType) {
    this.dialysisType = dialysisType;
  }

  public String getMedication() {
    return medication;
  }

  public void setMedication(String medication) {
    this.medication = medication;
  }
}
