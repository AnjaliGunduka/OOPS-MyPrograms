package com.junodx.api.controllers.xifin.payloads;

public class StreetAddress {
  private Address address;
  private Contact conatct1;
  private Contact contact2;

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Contact getConatct1() {
    return conatct1;
  }

  public void setConatct1(Contact conatct1) {
    this.conatct1 = conatct1;
  }

  public Contact getContact2() {
    return contact2;
  }

  public void setContact2(Contact contact2) {
    this.contact2 = contact2;
  }
}
