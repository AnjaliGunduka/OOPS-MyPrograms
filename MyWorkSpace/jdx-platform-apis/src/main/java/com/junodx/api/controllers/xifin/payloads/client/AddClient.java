package com.junodx.api.controllers.xifin.payloads.client;

import com.junodx.api.controllers.xifin.payloads.*;

public class AddClient {
  private String accountName;
  private AdditionalStatementCopy additionalStatementCopy;
  private String annualDisclosureLetter;
  private boolean billReferralsToClient;
  private BillingAddress billingAddress;
  private String clientAccountType;
  private String clientID;
  private String clientStatementDeliveryMethod;
  private ClientStatementDisplayOption clientStatementDisplayOption;
  private String clientSubmissionServiceId;
  private boolean copyBillingAddressToShippingAddress;
  private String copyClientId;
  private CorrespondenceAddress correspondenceAddress;
  private boolean dailyCharges;
  private String dailyChargesFrequency;
  private String dailyChargesNotificationMethod;
  private int npi;
  private POs pOs;
  private boolean performBillingAssignment;
  private boolean performEligibilityCensusChecking;
  private boolean performEligibilityCensusCheckingCrossClients;
  private Questions questions;
  private RefundAddress refundAddress;
  private ShippingAddress shippingAddress;
  private String startDate;
  private String statementFrequency;
  private StreetAddress streetAddress;
  private String taxId;
  private XRefs xRefs;

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public AdditionalStatementCopy getAdditionalStatementCopy() {
    return additionalStatementCopy;
  }

  public void setAdditionalStatementCopy(AdditionalStatementCopy additionalStatementCopy) {
    this.additionalStatementCopy = additionalStatementCopy;
  }

  public String getAnnualDisclosureLetter() {
    return annualDisclosureLetter;
  }

  public void setAnnualDisclosureLetter(String annualDisclosureLetter) {
    this.annualDisclosureLetter = annualDisclosureLetter;
  }

  public boolean isBillReferralsToClient() {
    return billReferralsToClient;
  }

  public void setBillReferralsToClient(boolean billReferralsToClient) {
    this.billReferralsToClient = billReferralsToClient;
  }

  public BillingAddress getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(BillingAddress billingAddress) {
    this.billingAddress = billingAddress;
  }

  public String getClientAccountType() {
    return clientAccountType;
  }

  public void setClientAccountType(String clientAccountType) {
    this.clientAccountType = clientAccountType;
  }

  public String getClientID() {
    return clientID;
  }

  public void setClientID(String clientID) {
    this.clientID = clientID;
  }

  public String getClientStatementDeliveryMethod() {
    return clientStatementDeliveryMethod;
  }

  public void setClientStatementDeliveryMethod(String clientStatementDeliveryMethod) {
    this.clientStatementDeliveryMethod = clientStatementDeliveryMethod;
  }

  public ClientStatementDisplayOption getClientStatementDisplayOption() {
    return clientStatementDisplayOption;
  }

  public void setClientStatementDisplayOption(ClientStatementDisplayOption clientStatementDisplayOption) {
    this.clientStatementDisplayOption = clientStatementDisplayOption;
  }

  public String getClientSubmissionServiceId() {
    return clientSubmissionServiceId;
  }

  public void setClientSubmissionServiceId(String clientSubmissionServiceId) {
    this.clientSubmissionServiceId = clientSubmissionServiceId;
  }

  public boolean isCopyBillingAddressToShippingAddress() {
    return copyBillingAddressToShippingAddress;
  }

  public void setCopyBillingAddressToShippingAddress(boolean copyBillingAddressToShippingAddress) {
    this.copyBillingAddressToShippingAddress = copyBillingAddressToShippingAddress;
  }

  public String getCopyClientId() {
    return copyClientId;
  }

  public void setCopyClientId(String copyClientId) {
    this.copyClientId = copyClientId;
  }

  public CorrespondenceAddress getCorrespondenceAddress() {
    return correspondenceAddress;
  }

  public void setCorrespondenceAddress(CorrespondenceAddress correspondenceAddress) {
    this.correspondenceAddress = correspondenceAddress;
  }

  public boolean isDailyCharges() {
    return dailyCharges;
  }

  public void setDailyCharges(boolean dailyCharges) {
    this.dailyCharges = dailyCharges;
  }

  public String getDailyChargesFrequency() {
    return dailyChargesFrequency;
  }

  public void setDailyChargesFrequency(String dailyChargesFrequency) {
    this.dailyChargesFrequency = dailyChargesFrequency;
  }

  public String getDailyChargesNotificationMethod() {
    return dailyChargesNotificationMethod;
  }

  public void setDailyChargesNotificationMethod(String dailyChargesNotificationMethod) {
    this.dailyChargesNotificationMethod = dailyChargesNotificationMethod;
  }

  public int getNpi() {
    return npi;
  }

  public void setNpi(int npi) {
    this.npi = npi;
  }

  public POs getpOs() {
    return pOs;
  }

  public void setpOs(POs pOs) {
    this.pOs = pOs;
  }

  public boolean isPerformBillingAssignment() {
    return performBillingAssignment;
  }

  public void setPerformBillingAssignment(boolean performBillingAssignment) {
    this.performBillingAssignment = performBillingAssignment;
  }

  public boolean isPerformEligibilityCensusChecking() {
    return performEligibilityCensusChecking;
  }

  public void setPerformEligibilityCensusChecking(boolean performEligibilityCensusChecking) {
    this.performEligibilityCensusChecking = performEligibilityCensusChecking;
  }

  public boolean isPerformEligibilityCensusCheckingCrossClients() {
    return performEligibilityCensusCheckingCrossClients;
  }

  public void setPerformEligibilityCensusCheckingCrossClients(boolean performEligibilityCensusCheckingCrossClients) {
    this.performEligibilityCensusCheckingCrossClients = performEligibilityCensusCheckingCrossClients;
  }

  public Questions getQuestions() {
    return questions;
  }

  public void setQuestions(Questions questions) {
    this.questions = questions;
  }

  public RefundAddress getRefundAddress() {
    return refundAddress;
  }

  public void setRefundAddress(RefundAddress refundAddress) {
    this.refundAddress = refundAddress;
  }

  public ShippingAddress getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(ShippingAddress shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getStatementFrequency() {
    return statementFrequency;
  }

  public void setStatementFrequency(String statementFrequency) {
    this.statementFrequency = statementFrequency;
  }

  public StreetAddress getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(StreetAddress streetAddress) {
    this.streetAddress = streetAddress;
  }

  public String getTaxId() {
    return taxId;
  }

  public void setTaxId(String taxId) {
    this.taxId = taxId;
  }

  public XRefs getxRefs() {
    return xRefs;
  }

  public void setxRefs(XRefs xRefs) {
    this.xRefs = xRefs;
  }
}
