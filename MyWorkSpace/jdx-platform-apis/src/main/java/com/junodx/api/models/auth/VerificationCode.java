package com.junodx.api.models.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Random;

@Entity
public class VerificationCode {

	@Transient
	@JsonIgnore
	private final int numDigits = 5;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// 5 digits
	@Column(name = "code", unique = true)
	private String code;

	private Calendar createdAt;

	private Calendar expiresAt;

	public String generateCode() {
		Random rand = new Random();
		int maxNumber = 10;
		String code = "";

		for (int i = 0; i < numDigits; i++)
			code += rand.nextInt(10);

		this.code = code;

		return code;
	}

	public VerificationCode(String code) {
		super();
		this.code = code;
	}

	public VerificationCode() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	@Override
	public String toString() {
		return "VerificationCode [numDigits=" + numDigits + ", id=" + id + ", user=" + user + ", code=" + code
				+ ", createdAt=" + createdAt + ", expiresAt=" + expiresAt + "]";
	}

}
