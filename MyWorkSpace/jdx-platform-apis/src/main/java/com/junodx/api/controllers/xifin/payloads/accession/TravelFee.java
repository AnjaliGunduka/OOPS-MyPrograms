package com.junodx.api.controllers.xifin.payloads.accession;

public class TravelFee {
  private boolean roundTripFromLab;
  private int tripMiles;
  private int tripPatientCount;
  private int tripStops;

  public boolean isRoundTripFromLab() {
    return roundTripFromLab;
  }

  public void setRoundTripFromLab(boolean roundTripFromLab) {
    this.roundTripFromLab = roundTripFromLab;
  }

  public int getTripMiles() {
    return tripMiles;
  }

  public void setTripMiles(int tripMiles) {
    this.tripMiles = tripMiles;
  }

  public int getTripPatientCount() {
    return tripPatientCount;
  }

  public void setTripPatientCount(int tripPatientCount) {
    this.tripPatientCount = tripPatientCount;
  }

  public int getTripStops() {
    return tripStops;
  }

  public void setTripStops(int tripStops) {
    this.tripStops = tripStops;
  }
}
