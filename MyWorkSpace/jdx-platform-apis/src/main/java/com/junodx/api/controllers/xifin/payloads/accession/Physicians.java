package com.junodx.api.controllers.xifin.payloads.accession;

public class Physicians {
  private Physican ordering;
  private Physican primary;
  private Physican referring;

  public Physican getOrdering() {
    return ordering;
  }

  public void setOrdering(Physican ordering) {
    this.ordering = ordering;
  }

  public Physican getPrimary() {
    return primary;
  }

  public void setPrimary(Physican primary) {
    this.primary = primary;
  }

  public Physican getReferring() {
    return referring;
  }

  public void setReferring(Physican referring) {
    this.referring = referring;
  }
}
