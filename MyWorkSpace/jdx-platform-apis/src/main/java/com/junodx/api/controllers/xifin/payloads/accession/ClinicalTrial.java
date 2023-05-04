package com.junodx.api.controllers.xifin.payloads.accession;

public class ClinicalTrial {
  private String encounterDate;
  private String encounterName;
  private String trialName;

  public String getEncounterDate() {
    return encounterDate;
  }

  public void setEncounterDate(String encounterDate) {
    this.encounterDate = encounterDate;
  }

  public String getEncounterName() {
    return encounterName;
  }

  public void setEncounterName(String encounterName) {
    this.encounterName = encounterName;
  }

  public String getTrialName() {
    return trialName;
  }

  public void setTrialName(String trialName) {
    this.trialName = trialName;
  }
}
