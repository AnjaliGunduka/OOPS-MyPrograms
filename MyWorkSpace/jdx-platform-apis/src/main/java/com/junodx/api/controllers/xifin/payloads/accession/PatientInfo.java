package com.junodx.api.controllers.xifin.payloads.accession;

public class PatientInfo {
  private String accidentCause;
  private String accidentState;
  private String admissionSource;
  private String admissionType;
  private String clientSpecificPatientId;
  private String comments;
  private boolean emergencyIndicator;
  private String facilitySpecificPatientId;
  private String fasting;
  private int gravidaNumber;
  private int indigentPercent;
  private String maritalStatus;
  private String onsetDate;
  private String onsetType;
  private String patientId;
  private String patientLocation;
  private String patientStatusType;
  private String patientType;
  private Person person;
  private boolean pregnancy;

  public String getAccidentCause() {
    return accidentCause;
  }

  public void setAccidentCause(String accidentCause) {
    this.accidentCause = accidentCause;
  }

  public String getAccidentState() {
    return accidentState;
  }

  public void setAccidentState(String accidentState) {
    this.accidentState = accidentState;
  }

  public String getAdmissionSource() {
    return admissionSource;
  }

  public void setAdmissionSource(String admissionSource) {
    this.admissionSource = admissionSource;
  }

  public String getAdmissionType() {
    return admissionType;
  }

  public void setAdmissionType(String admissionType) {
    this.admissionType = admissionType;
  }

  public String getClientSpecificPatientId() {
    return clientSpecificPatientId;
  }

  public void setClientSpecificPatientId(String clientSpecificPatientId) {
    this.clientSpecificPatientId = clientSpecificPatientId;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public boolean isEmergencyIndicator() {
    return emergencyIndicator;
  }

  public void setEmergencyIndicator(boolean emergencyIndicator) {
    this.emergencyIndicator = emergencyIndicator;
  }

  public String getFacilitySpecificPatientId() {
    return facilitySpecificPatientId;
  }

  public void setFacilitySpecificPatientId(String facilitySpecificPatientId) {
    this.facilitySpecificPatientId = facilitySpecificPatientId;
  }

  public String getFasting() {
    return fasting;
  }

  public void setFasting(String fasting) {
    this.fasting = fasting;
  }

  public int getGravidaNumber() {
    return gravidaNumber;
  }

  public void setGravidaNumber(int gravidaNumber) {
    this.gravidaNumber = gravidaNumber;
  }

  public int getIndigentPercent() {
    return indigentPercent;
  }

  public void setIndigentPercent(int indigentPercent) {
    this.indigentPercent = indigentPercent;
  }

  public String getMaritalStatus() {
    return maritalStatus;
  }

  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  public String getOnsetDate() {
    return onsetDate;
  }

  public void setOnsetDate(String onsetDate) {
    this.onsetDate = onsetDate;
  }

  public String getOnsetType() {
    return onsetType;
  }

  public void setOnsetType(String onsetType) {
    this.onsetType = onsetType;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public String getPatientLocation() {
    return patientLocation;
  }

  public void setPatientLocation(String patientLocation) {
    this.patientLocation = patientLocation;
  }

  public String getPatientStatusType() {
    return patientStatusType;
  }

  public void setPatientStatusType(String patientStatusType) {
    this.patientStatusType = patientStatusType;
  }

  public String getPatientType() {
    return patientType;
  }

  public void setPatientType(String patientType) {
    this.patientType = patientType;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public boolean isPregnancy() {
    return pregnancy;
  }

  public void setPregnancy(boolean pregnancy) {
    this.pregnancy = pregnancy;
  }
}
