package com.junodx.api.controllers.xifin.payloads.client;

import com.junodx.api.controllers.xifin.payloads.Address;

public class RefundAddress {
  private Address address;

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }
}
