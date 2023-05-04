package com.junodx.api.controllers.xifin.payloads;

public class BillingAddress {
  private Address address;
  private Contact contact1;
  private Contact contact2;

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Contact getContact1() {
    return contact1;
  }

  public void setContact1(Contact contact1) {
    this.contact1 = contact1;
  }

  public Contact getContact2() {
    return contact2;
  }

  public void setContact2(Contact contact2) {
    this.contact2 = contact2;
  }
}
