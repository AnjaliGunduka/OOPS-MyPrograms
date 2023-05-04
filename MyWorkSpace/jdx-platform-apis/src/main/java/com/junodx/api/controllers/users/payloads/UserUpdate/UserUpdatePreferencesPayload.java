package com.junodx.api.controllers.users.payloads.UserUpdate;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserUpdatePreferencesPayload {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private UserUpdateFetalSexResultsPreferences fstPreferences;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Boolean optOut;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Boolean smsMessages;

	public UserUpdateFetalSexResultsPreferences getFstPreferences() {
		return fstPreferences;
	}

	public void setFstPreferences(UserUpdateFetalSexResultsPreferences fstPreferences) {
		this.fstPreferences = fstPreferences;
	}

	public Boolean getOptOut() {
		return optOut;
	}

	public void setOptOut(Boolean optOut) {
		this.optOut = optOut;
	}

	public Boolean getSmsMessages() {
		return smsMessages;
	}

	public void setSmsMessages(Boolean smsMessages) {
		this.smsMessages = smsMessages;
	}
}
