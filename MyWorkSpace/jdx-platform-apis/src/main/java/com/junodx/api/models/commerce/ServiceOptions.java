package com.junodx.api.models.commerce;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.auth.FetalSexResultsPreferences;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ServiceOptions {
	@Column(name = "assisted_sample_collection", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean assistedSampleCollection;

	@Column(name = "self_collected", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean selfCollected;

	public boolean isAssistedSampleCollection() {
		return assistedSampleCollection;
	}

	public ServiceOptions() {
		super();
	}

	public void setAssistedSampleCollection(boolean assistedSampleCollection) {
		if (this.selfCollected)
			this.assistedSampleCollection = false;

		this.assistedSampleCollection = assistedSampleCollection;
	}

	public boolean isSelfCollected() {
		return selfCollected;
	}

	public void setSelfCollected(boolean selectCollected) {
		if (this.assistedSampleCollection)
			this.selfCollected = false;

		this.selfCollected = selectCollected;
	}
}
