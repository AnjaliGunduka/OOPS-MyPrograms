package com.junodx.api.controllers.xifin.payloads.accession;

import com.junodx.api.controllers.xifin.payloads.Address;

public class Person {
  private Address address;
  private String dateOfBirth;
  private String email;
  private String gender;
  private String homePhone;
  private PersonName name;
  private int ssn;
  private String workPhone;

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getHomePhone() {
    return homePhone;
  }

  public void setHomePhone(String homePhone) {
    this.homePhone = homePhone;
  }

  public PersonName getName() {
    return name;
  }

  public void setName(PersonName name) {
    this.name = name;
  }

  public int getSsn() {
    return ssn;
  }

  public void setSsn(int ssn) {
    this.ssn = ssn;
  }

  public String getWorkPhone() {
    return workPhone;
  }

  public void setWorkPhone(String workPhone) {
    this.workPhone = workPhone;
  }
}
