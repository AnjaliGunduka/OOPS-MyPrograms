package com.junodx.api.controllers.xifin.payloads.client;

import com.junodx.api.controllers.xifin.payloads.Address;
import com.junodx.api.controllers.xifin.payloads.Contact;

public class AdditionalStatementCopy {
  private String accountName;
  private Address address;
  private Contact contact;

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

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
}
