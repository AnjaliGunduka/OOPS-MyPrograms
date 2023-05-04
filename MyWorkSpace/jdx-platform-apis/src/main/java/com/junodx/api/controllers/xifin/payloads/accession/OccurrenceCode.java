package com.junodx.api.controllers.xifin.payloads.accession;

public class OccurrenceCode {
  private String occurrenceCode;
  private String occurrenceDate;
  private String specificPyrId;

  public String getOccurrenceCode() {
    return occurrenceCode;
  }

  public void setOccurrenceCode(String occurrenceCode) {
    this.occurrenceCode = occurrenceCode;
  }

  public String getOccurrenceDate() {
    return occurrenceDate;
  }

  public void setOccurrenceDate(String occurrenceDate) {
    this.occurrenceDate = occurrenceDate;
  }

  public String getSpecificPyrId() {
    return specificPyrId;
  }

  public void setSpecificPyrId(String specificPyrId) {
    this.specificPyrId = specificPyrId;
  }
}
