package com.junodx.api.controllers.users.payloads;

import java.util.Calendar;

public class EmailChangePayload {
    private String userId;
    private String existingEmail;
    private String changeToEmail;
    private boolean verificationCodeSent;
    private Calendar verificationCodeExpiresAt;

    public EmailChangePayload() {
		super();
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

    public String getChangeToEmail() {
        return changeToEmail;
    }

    public void setChangeToEmail(String changeToEmail) {
        this.changeToEmail = changeToEmail;
    }

    public boolean isVerificationCodeSent() {
        return verificationCodeSent;
    }

    public void setVerificationCodeSent(boolean verificationCodeSent) {
        this.verificationCodeSent = verificationCodeSent;
    }

    public Calendar getVerificationCodeExpiresAt() {
        return verificationCodeExpiresAt;
    }

    public void setVerificationCodeExpiresAt(Calendar verificationCodeExpiresAt) {
        this.verificationCodeExpiresAt = verificationCodeExpiresAt;
    }
}
