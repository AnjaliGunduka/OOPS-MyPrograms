package com.junodx.api.models.providers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.models.providers.types.SpecialtyType;

import javax.persistence.*;

@Entity
@Table(name = "specialty")
public class Specialty {
	@Id
	@JsonIgnore
	private Long id;

	private SpecialtyType type;

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

	public SpecialtyType getType() {
		return type;
	}

	public void setType(SpecialtyType type) {
		this.type = type;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}
}