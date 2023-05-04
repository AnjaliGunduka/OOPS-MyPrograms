package com.junodx.api.controllers.xifin.payloads.accession;

import java.util.List;

public class CreateAccession {
  private AccessionContactInfo accessionContactInfo;
  private String accessionId;
  private List<AccessionLevelDiagnosisCode> accessionLevelDiagnosisCodes;
  private List<AdditionalReportCopy> additionalReportCopy;
  private String admitDate;
  private boolean callBack;
  private String callBackPhone;
  private ChainOfCustody chainOfCustody;
  private boolean checkClientBillingRules;
  private String clientBillingCategory;
  private String clientId;
  private String clientProductId;
  private ClientQuestions clientQuestions;
  private ClinicalTrial clinicalTrial;
  private boolean createOrUpdatePatientDemo;
  private String dateOfService;
  private DialysisInfo dialysisInfo;
  private String dischargeDate;
  private String finalReportedDate;
  private String forceToEPHoldQueueNote;
  private boolean ignoreErrors;
  private List<InsuranceInfo> insuranceInfo;
  private boolean lowConfidence;
  private String lowConfidenceReason;
  private boolean mspForm;
  private boolean noCharge;
  private List<OccurrenceCode> occurrenceCode;
  private List<OrderedTest> orderedTests;
  private boolean paidInFull;
  private String patientDemoEffectiveDate;
  private PatientInfo patientInfo;
  private boolean patientSignatureOnFile;
  private String phlebotomistUserId;
  private boolean physicianSignatureOnFile;
  private Physicians physicians;
  private String pscLocationId;
  private String receiptDate;
  private boolean reportCopyToPatient;
  private String requisitionId;
  private boolean returnErrors;
  private SpecimenInfo specimenInfo;
  private boolean stat;
  private String timeOfService;
  private TravelFee travelFee;
  private ValueCode valueCode;

  public AccessionContactInfo getAccessionContactInfo() {
    return accessionContactInfo;
  }

  public void setAccessionContactInfo(AccessionContactInfo accessionContactInfo) {
    this.accessionContactInfo = accessionContactInfo;
  }

  public String getAccessionId() {
    return accessionId;
  }

  public void setAccessionId(String accessionId) {
    this.accessionId = accessionId;
  }

  public List<AccessionLevelDiagnosisCode> getAccessionLevelDiagnosisCodes() {
    return accessionLevelDiagnosisCodes;
  }

  public void setAccessionLevelDiagnosisCodes(List<AccessionLevelDiagnosisCode> accessionLevelDiagnosisCodes) {
    this.accessionLevelDiagnosisCodes = accessionLevelDiagnosisCodes;
  }

  public List<AdditionalReportCopy> getAdditionalReportCopy() {
    return additionalReportCopy;
  }

  public void setAdditionalReportCopy(List<AdditionalReportCopy> additionalReportCopy) {
    this.additionalReportCopy = additionalReportCopy;
  }

  public List<InsuranceInfo> getInsuranceInfo() {
    return insuranceInfo;
  }

  public void setInsuranceInfo(List<InsuranceInfo> insuranceInfo) {
    this.insuranceInfo = insuranceInfo;
  }

  public String getAdmitDate() {
    return admitDate;
  }

  public void setAdmitDate(String admitDate) {
    this.admitDate = admitDate;
  }

  public boolean isCallBack() {
    return callBack;
  }

  public void setCallBack(boolean callBack) {
    this.callBack = callBack;
  }

  public String getCallBackPhone() {
    return callBackPhone;
  }

  public void setCallBackPhone(String callBackPhone) {
    this.callBackPhone = callBackPhone;
  }

  public ChainOfCustody getChainOfCustody() {
    return chainOfCustody;
  }

  public void setChainOfCustody(ChainOfCustody chainOfCustody) {
    this.chainOfCustody = chainOfCustody;
  }

  public boolean isCheckClientBillingRules() {
    return checkClientBillingRules;
  }

  public void setCheckClientBillingRules(boolean checkClientBillingRules) {
    this.checkClientBillingRules = checkClientBillingRules;
  }

  public String getClientBillingCategory() {
    return clientBillingCategory;
  }

  public void setClientBillingCategory(String clientBillingCategory) {
    this.clientBillingCategory = clientBillingCategory;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientProductId() {
    return clientProductId;
  }

  public void setClientProductId(String clientProductId) {
    this.clientProductId = clientProductId;
  }

  public ClientQuestions getClientQuestions() {
    return clientQuestions;
  }

  public void setClientQuestions(ClientQuestions clientQuestions) {
    this.clientQuestions = clientQuestions;
  }

  public ClinicalTrial getClinicalTrial() {
    return clinicalTrial;
  }

  public void setClinicalTrial(ClinicalTrial clinicalTrial) {
    this.clinicalTrial = clinicalTrial;
  }

  public boolean isCreateOrUpdatePatientDemo() {
    return createOrUpdatePatientDemo;
  }

  public void setCreateOrUpdatePatientDemo(boolean createOrUpdatePatientDemo) {
    this.createOrUpdatePatientDemo = createOrUpdatePatientDemo;
  }

  public String getDateOfService() {
    return dateOfService;
  }

  public void setDateOfService(String dateOfService) {
    this.dateOfService = dateOfService;
  }

  public DialysisInfo getDialysisInfo() {
    return dialysisInfo;
  }

  public void setDialysisInfo(DialysisInfo dialysisInfo) {
    this.dialysisInfo = dialysisInfo;
  }

  public String getDischargeDate() {
    return dischargeDate;
  }

  public void setDischargeDate(String dischargeDate) {
    this.dischargeDate = dischargeDate;
  }

  public String getFinalReportedDate() {
    return finalReportedDate;
  }

  public void setFinalReportedDate(String finalReportedDate) {
    this.finalReportedDate = finalReportedDate;
  }

  public String getForceToEPHoldQueueNote() {
    return forceToEPHoldQueueNote;
  }

  public void setForceToEPHoldQueueNote(String forceToEPHoldQueueNote) {
    this.forceToEPHoldQueueNote = forceToEPHoldQueueNote;
  }

  public boolean isIgnoreErrors() {
    return ignoreErrors;
  }

  public void setIgnoreErrors(boolean ignoreErrors) {
    this.ignoreErrors = ignoreErrors;
  }

  public boolean isLowConfidence() {
    return lowConfidence;
  }

  public void setLowConfidence(boolean lowConfidence) {
    this.lowConfidence = lowConfidence;
  }

  public String getLowConfidenceReason() {
    return lowConfidenceReason;
  }

  public void setLowConfidenceReason(String lowConfidenceReason) {
    this.lowConfidenceReason = lowConfidenceReason;
  }

  public boolean isMspForm() {
    return mspForm;
  }

  public void setMspForm(boolean mspForm) {
    this.mspForm = mspForm;
  }

  public boolean isNoCharge() {
    return noCharge;
  }

  public void setNoCharge(boolean noCharge) {
    this.noCharge = noCharge;
  }

  public List<OccurrenceCode> getOccurrenceCode() {
    return occurrenceCode;
  }

  public void setOccurrenceCode(List<OccurrenceCode> occurrenceCode) {
    this.occurrenceCode = occurrenceCode;
  }

  public List<OrderedTest> getOrderedTests() {
    return orderedTests;
  }

  public void setOrderedTests(List<OrderedTest> orderedTests) {
    this.orderedTests = orderedTests;
  }

  public boolean isPaidInFull() {
    return paidInFull;
  }

  public void setPaidInFull(boolean paidInFull) {
    this.paidInFull = paidInFull;
  }

  public String getPatientDemoEffectiveDate() {
    return patientDemoEffectiveDate;
  }

  public void setPatientDemoEffectiveDate(String patientDemoEffectiveDate) {
    this.patientDemoEffectiveDate = patientDemoEffectiveDate;
  }

  public PatientInfo getPatientInfo() {
    return patientInfo;
  }

  public void setPatientInfo(PatientInfo patientInfo) {
    this.patientInfo = patientInfo;
  }

  public boolean isPatientSignatureOnFile() {
    return patientSignatureOnFile;
  }

  public void setPatientSignatureOnFile(boolean patientSignatureOnFile) {
    this.patientSignatureOnFile = patientSignatureOnFile;
  }

  public String getPhlebotomistUserId() {
    return phlebotomistUserId;
  }

  public void setPhlebotomistUserId(String phlebotomistUserId) {
    this.phlebotomistUserId = phlebotomistUserId;
  }

  public boolean isPhysicianSignatureOnFile() {
    return physicianSignatureOnFile;
  }

  public void setPhysicianSignatureOnFile(boolean physicianSignatureOnFile) {
    this.physicianSignatureOnFile = physicianSignatureOnFile;
  }

  public Physicians getPhysicians() {
    return physicians;
  }

  public void setPhysicians(Physicians physicians) {
    this.physicians = physicians;
  }

  public String getPscLocationId() {
    return pscLocationId;
  }

  public void setPscLocationId(String pscLocationId) {
    this.pscLocationId = pscLocationId;
  }

  public String getReceiptDate() {
    return receiptDate;
  }

  public void setReceiptDate(String receiptDate) {
    this.receiptDate = receiptDate;
  }

  public boolean isReportCopyToPatient() {
    return reportCopyToPatient;
  }

  public void setReportCopyToPatient(boolean reportCopyToPatient) {
    this.reportCopyToPatient = reportCopyToPatient;
  }

  public String getRequisitionId() {
    return requisitionId;
  }

  public void setRequisitionId(String requisitionId) {
    this.requisitionId = requisitionId;
  }

  public boolean isReturnErrors() {
    return returnErrors;
  }

  public void setReturnErrors(boolean returnErrors) {
    this.returnErrors = returnErrors;
  }

  public SpecimenInfo getSpecimenInfo() {
    return specimenInfo;
  }

  public void setSpecimenInfo(SpecimenInfo specimenInfo) {
    this.specimenInfo = specimenInfo;
  }

  public boolean isStat() {
    return stat;
  }

  public void setStat(boolean stat) {
    this.stat = stat;
  }

  public String getTimeOfService() {
    return timeOfService;
  }

  public void setTimeOfService(String timeOfService) {
    this.timeOfService = timeOfService;
  }

  public TravelFee getTravelFee() {
    return travelFee;
  }

  public void setTravelFee(TravelFee travelFee) {
    this.travelFee = travelFee;
  }

  public ValueCode getValueCode() {
    return valueCode;
  }

  public void setValueCode(ValueCode valueCode) {
    this.valueCode = valueCode;
  }
}
