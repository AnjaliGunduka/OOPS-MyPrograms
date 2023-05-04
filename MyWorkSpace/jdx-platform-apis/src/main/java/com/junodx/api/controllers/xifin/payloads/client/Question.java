package com.junodx.api.controllers.xifin.payloads.client;

public class Question {
  private boolean printable;
  private String question;
  private String required;

  public boolean isPrintable() {
    return printable;
  }

  public void setPrintable(boolean printable) {
    this.printable = printable;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getRequired() {
    return required;
  }

  public void setRequired(String required) {
    this.required = required;
  }
}
