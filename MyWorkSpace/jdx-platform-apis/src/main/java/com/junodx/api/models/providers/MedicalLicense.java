package com.junodx.api.models.providers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.models.core.types.State;

import javax.persistence.*;

@Entity
@Table(name = "medical_license")
public class MedicalLicense {

	@Id
	@JsonIgnore
	private Long id;
	private State state;

	@Column(name = "license_number")
	private String licenseNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@JoinColumn(name = "provider_id", nullable = false)
	private Provider provider;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}
}
