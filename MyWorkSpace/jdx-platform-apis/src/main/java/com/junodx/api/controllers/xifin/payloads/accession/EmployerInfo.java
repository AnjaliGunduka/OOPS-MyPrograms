package com.junodx.api.controllers.xifin.payloads.accession;

import com.junodx.api.controllers.xifin.payloads.Address;
import com.junodx.api.controllers.xifin.payloads.Contact;

public class EmployerInfo {
  private Address address;
  private Contact contact;
  private String employerId;
  private String employerName;
  private String status;

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Contact getContact() {
    return contact;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  public String getEmployerId() {
    return employerId;
  }

  public void setEmployerId(String employerId) {
    this.employerId = employerId;
  }

  public String getEmployerName() {
    return employerName;
  }

  public void setEmployerName(String employerName) {
    this.employerName = employerName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
