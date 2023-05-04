package com.junodx.api.controllers.xifin.payloads;

public class ShippingAddress {
  private Address address;
  private Contact contact;

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
