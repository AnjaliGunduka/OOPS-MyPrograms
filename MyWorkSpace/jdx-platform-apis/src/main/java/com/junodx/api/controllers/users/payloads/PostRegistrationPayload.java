package com.junodx.api.controllers.users.payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Calendar;

public class PostRegistrationPayload {

	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private boolean alreadyRegistered;

	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private boolean requiresVerification;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private String email;
	private boolean verificationCodeSent;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String idToken;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Calendar expiration;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Calendar createdAt;

	public PostRegistrationPayload() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isVerificationCodeSent() {
		return verificationCodeSent;
	}

	public void setVerificationCodeSent(boolean verificationCodeSent) {
		this.verificationCodeSent = verificationCodeSent;
	}

	public Calendar getExpiration() {
		return expiration;
	}

	public void setExpiration(Calendar expiration) {
		this.expiration = expiration;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	public boolean isAlreadyRegistered() {
		return alreadyRegistered;
	}

	public void setAlreadyRegistered(boolean alreadyRegistered) {
		this.alreadyRegistered = alreadyRegistered;
	}

	public boolean isRequiresVerification() {
		return requiresVerification;
	}

	public void setRequiresVerification(boolean requiresVerification) {
		this.requiresVerification = requiresVerification;
	}
}
