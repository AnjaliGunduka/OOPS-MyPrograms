package com.junodx.api.controllers.xifin.payloads.accession;

import com.junodx.api.controllers.xifin.payloads.Address;

public class AdditionalReportCopy {
  private Address address;
  private String clientId;
  private String contactMethodType;
  private String email;
  private String fax;
  private String name;
  private String phone;

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getContactMethodType() {
    return contactMethodType;
  }

  public void setContactMethodType(String contactMethodType) {
    this.contactMethodType = contactMethodType;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}
