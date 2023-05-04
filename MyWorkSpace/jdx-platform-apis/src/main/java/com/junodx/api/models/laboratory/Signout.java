package com.junodx.api.models.laboratory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.auth.User;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table(name = "signout")
public class Signout {

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	private String id;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "lab_director_id", updatable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User signatory;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "signed_out_at", updatable = false)
	private Calendar signedOutAt;

	@Column(name = "non_reportable", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean nonReportable;

	public Signout() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getSignatory() {
		return signatory;
	}

	public void setSignatory(User signatory) {
		this.signatory = signatory;
	}

	public Calendar getSignedOutAt() {
		return signedOutAt;
	}

	public void setSignedOutAt(Calendar signedOutAt) {
		this.signedOutAt = signedOutAt;
	}

	public boolean isNonReportable() {
		return nonReportable;
	}

	public void setNonReportable(boolean nonReportable) {
		this.nonReportable = nonReportable;
	}
}
