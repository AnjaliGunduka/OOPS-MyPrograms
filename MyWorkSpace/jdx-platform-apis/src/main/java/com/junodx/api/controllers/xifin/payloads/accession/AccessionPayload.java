package com.junodx.api.controllers.xifin.payloads.accession;

import com.junodx.api.controllers.xifin.payloads.accession.CreateAccession;

public class AccessionPayload {
  private CreateAccession createAccession;

  public CreateAccession getCreateAccession() {
    return createAccession;
  }

  public void setCreateAccession(CreateAccession createAccession) {
    this.createAccession = createAccession;
  }
}
