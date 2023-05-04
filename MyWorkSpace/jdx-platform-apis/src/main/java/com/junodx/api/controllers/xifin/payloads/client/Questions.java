package com.junodx.api.controllers.xifin.payloads.client;

import java.util.List;

public class Questions {
  private List<Question> question;

  public List<Question> getQuestion() {
    return question;
  }

  public void setQuestion(List<Question> question) {
    this.question = question;
  }
}
