package com.junodx.api.controllers.xifin.payloads;

public class Contact {
  private String email;
  private String fax;
  private String name;
  private String phone;
  private String preferredContactMethod;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPreferredContactMethod() {
    return preferredContactMethod;
  }

  public void setPreferredContactMethod(String preferredContactMethod) {
    this.preferredContactMethod = preferredContactMethod;
  }
}
