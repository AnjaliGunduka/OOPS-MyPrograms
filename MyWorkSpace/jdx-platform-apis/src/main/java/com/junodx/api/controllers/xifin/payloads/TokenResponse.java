package com.junodx.api.controllers.xifin.payloads;

public class TokenResponse {
  private String orgalias;
  private String username;
  private String token;

  public String getOrgalias() {
    return orgalias;
  }

  public void setOrgalias(String orgalias) {
    this.orgalias = orgalias;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
