package com.junodx.api.controllers.xifin.payloads.client;

public class XRef {
  private String effectiveDate;
  private String expirationDate;
  private String xRefDescription;
  private String xRefID;
  private String xRefType;

  public String getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(String effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  public String getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(String expirationDate) {
    this.expirationDate = expirationDate;
  }

  public String getxRefDescription() {
    return xRefDescription;
  }

  public void setxRefDescription(String xRefDescription) {
    this.xRefDescription = xRefDescription;
  }

  public String getxRefID() {
    return xRefID;
  }

  public void setxRefID(String xRefID) {
    this.xRefID = xRefID;
  }

  public String getxRefType() {
    return xRefType;
  }

  public void setxRefType(String xRefType) {
    this.xRefType = xRefType;
  }
}
