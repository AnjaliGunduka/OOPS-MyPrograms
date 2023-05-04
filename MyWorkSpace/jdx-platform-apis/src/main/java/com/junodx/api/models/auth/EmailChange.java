package com.junodx.api.models.auth;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "email_change")
public class EmailChange {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "user_id", unique = true)
	private String userId;

	@Column(name = "existing_email", nullable = false)
	private String existingEmail;

	@Column(name = "new_email", nullable = false)
	private String newEmail;

	private Calendar createdAt;

	@Column(name = "expires_at", nullable = false)
	private Calendar expiresAt;

	public EmailChange() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getExistingEmail() {
		return existingEmail;
	}

	public void setExistingEmail(String existingEmail) {
		this.existingEmail = existingEmail;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
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
