package com.junodx.api.models.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

@Entity
public class ForgotPasswordCode {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	private String code;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	private String clientId;
	private Calendar createdAt;
	private Calendar expiresAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ForgotPasswordCode() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String generateCode() {
		return UUID.randomUUID().toString();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public Calendar getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Calendar expiresAt) {
		this.expiresAt = expiresAt;
	}

}
