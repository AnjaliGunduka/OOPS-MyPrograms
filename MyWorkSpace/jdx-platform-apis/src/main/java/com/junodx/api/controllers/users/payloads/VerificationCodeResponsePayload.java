package com.junodx.api.controllers.users.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;

public class VerificationCodeResponsePayload {
	private boolean success;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String userId;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String email;

	public VerificationCodeResponsePayload() {
		super();
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
