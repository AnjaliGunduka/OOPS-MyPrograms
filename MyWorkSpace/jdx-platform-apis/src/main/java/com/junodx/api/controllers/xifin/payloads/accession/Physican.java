package com.junodx.api.controllers.xifin.payloads.accession;

public class Physican {
  private String name;
  private int npi;
  private String upin;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getNpi() {
    return npi;
  }

  public void setNpi(int npi) {
    this.npi = npi;
  }

  public String getUpin() {
    return upin;
  }

  public void setUpin(String upin) {
    this.upin = upin;
  }
}
