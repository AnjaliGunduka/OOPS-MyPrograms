package com.junodx.api.controllers.users.payloads;

import com.junodx.api.models.auth.User;

public class UserVerificationPayload {
	private String email;
	private String code;
	private String clientId;

	public String getEmail() {
		return email;
	}

	public UserVerificationPayload(String email, String code, String clientId) {
		super();
		this.email = email;
		this.code = code;
		this.clientId = clientId;
	}
	

	public UserVerificationPayload() {
		super();
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
