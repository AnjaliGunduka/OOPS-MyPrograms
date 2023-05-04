package com.junodx.api.controllers.xifin.payloads.accession;

import com.junodx.api.controllers.xifin.payloads.Address;
import com.junodx.api.controllers.xifin.payloads.Contact;

public class ChainOfCustody {
  private String chainOfCustodyReasonType;
  private EmployerInfo employerInfo;
  private Address mroAddress;
  private Contact mroContact;
  private String mroName;
  private String specimenCollector;
  private boolean specimenCollectorSignatureOnFile;
  private int specimenTemp;
  private boolean specimenTemperatureInRange;
  private boolean specimenTemperatureNotInRange;
  private boolean splitSpecimenCollection;

  public String getChainOfCustodyReasonType() {
    return chainOfCustodyReasonType;
  }

  public void setChainOfCustodyReasonType(String chainOfCustodyReasonType) {
    this.chainOfCustodyReasonType = chainOfCustodyReasonType;
  }

  public EmployerInfo getEmployerInfo() {
    return employerInfo;
  }

  public void setEmployerInfo(EmployerInfo employerInfo) {
    this.employerInfo = employerInfo;
  }

  public Address getMroAddress() {
    return mroAddress;
  }

  public void setMroAddress(Address mroAddress) {
    this.mroAddress = mroAddress;
  }

  public Contact getMroContact() {
    return mroContact;
  }

  public void setMroContact(Contact mroContact) {
    this.mroContact = mroContact;
  }

  public String getMroName() {
    return mroName;
  }

  public void setMroName(String mroName) {
    this.mroName = mroName;
  }

  public String getSpecimenCollector() {
    return specimenCollector;
  }

  public void setSpecimenCollector(String specimenCollector) {
    this.specimenCollector = specimenCollector;
  }

  public boolean isSpecimenCollectorSignatureOnFile() {
    return specimenCollectorSignatureOnFile;
  }

  public void setSpecimenCollectorSignatureOnFile(boolean specimenCollectorSignatureOnFile) {
    this.specimenCollectorSignatureOnFile = specimenCollectorSignatureOnFile;
  }

  public int getSpecimenTemp() {
    return specimenTemp;
  }

  public void setSpecimenTemp(int specimenTemp) {
    this.specimenTemp = specimenTemp;
  }

  public boolean isSpecimenTemperatureInRange() {
    return specimenTemperatureInRange;
  }

  public void setSpecimenTemperatureInRange(boolean specimenTemperatureInRange) {
    this.specimenTemperatureInRange = specimenTemperatureInRange;
  }

  public boolean isSpecimenTemperatureNotInRange() {
    return specimenTemperatureNotInRange;
  }

  public void setSpecimenTemperatureNotInRange(boolean specimenTemperatureNotInRange) {
    this.specimenTemperatureNotInRange = specimenTemperatureNotInRange;
  }

  public boolean isSplitSpecimenCollection() {
    return splitSpecimenCollection;
  }

  public void setSplitSpecimenCollection(boolean splitSpecimenCollection) {
    this.splitSpecimenCollection = splitSpecimenCollection;
  }
}
