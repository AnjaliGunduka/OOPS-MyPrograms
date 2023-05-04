package com.junodx.api.controllers.xifin.payloads.accession;

public class TestQuestion {
  private String contactMethod;
  private String question;
  private String response;

  public String getContactMethod() {
    return contactMethod;
  }

  public void setContactMethod(String contactMethod) {
    this.contactMethod = contactMethod;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }
}
