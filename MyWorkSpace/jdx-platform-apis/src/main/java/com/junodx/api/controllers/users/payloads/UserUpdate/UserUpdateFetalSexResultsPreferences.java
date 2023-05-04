package com.junodx.api.controllers.users.payloads.UserUpdate;

import com.junodx.api.models.auth.types.GenderTerms;

public class UserUpdateFetalSexResultsPreferences {
	protected GenderTerms genderTerms;
	protected Boolean genderFanfare;
	protected Boolean genderDelegated;
	protected String fstResultsDelegatedEmail;

	public GenderTerms getGenderTerms() {
		return genderTerms;
	}

	public void setGenderTerms(GenderTerms genderTerms) {
		this.genderTerms = genderTerms;
	}

	public Boolean getGenderFanfare() {
		return genderFanfare;
	}

	public void setGenderFanfare(Boolean genderFanfare) {
		this.genderFanfare = genderFanfare;
	}

	public Boolean getGenderDelegated() {
		return genderDelegated;
	}

	public void setGenderDelegated(Boolean genderDelegated) {
		this.genderDelegated = genderDelegated;
	}

	public String getFstResultsDelegatedEmail() {
		return fstResultsDelegatedEmail;
	}

	public void setFstResultsDelegatedEmail(String fstResultsDelegatedEmail) {
		this.fstResultsDelegatedEmail = fstResultsDelegatedEmail;
	}
}
