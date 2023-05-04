package com.junodx.api.controllers.xifin.payloads.accession;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.controllers.xifin.payloads.MessageHeader;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateAccessionPayload {
  private MessageHeader messageHeader;
  private AccessionPayload payload;

  public MessageHeader getMessageHeader() {
    return messageHeader;
  }

  public void setMessageHeader(MessageHeader messageHeader) {
    this.messageHeader = messageHeader;
  }

  public AccessionPayload getPayload() {
    return payload;
  }

  public void setPayload(AccessionPayload payload) {
    this.payload = payload;
  }
}
