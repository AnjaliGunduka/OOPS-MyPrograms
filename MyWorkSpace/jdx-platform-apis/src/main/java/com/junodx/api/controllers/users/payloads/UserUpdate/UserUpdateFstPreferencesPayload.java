package com.junodx.api.controllers.users.payloads.UserUpdate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.auth.types.GenderTerms;

import javax.persistence.Column;

public class UserUpdateFstPreferencesPayload {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected GenderTerms genderTerms;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Boolean genderFanfare;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Boolean genderDelegated;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected String fstResultsDelegatedEmail;

    public GenderTerms getGenderTerms() {
        return genderTerms;
    }

    public void setGenderTerms(GenderTerms genderTerms) {
        this.genderTerms = genderTerms;
    }

    public boolean isGenderFanfare() {
        return genderFanfare;
    }

    public void setGenderFanfare(boolean genderFanfare) {
        this.genderFanfare = genderFanfare;
    }

    public boolean isGenderDelegated() {
        return genderDelegated;
    }

    public void setGenderDelegated(boolean genderDelegated) {
        this.genderDelegated = genderDelegated;
    }

    public String getFstResultsDelegatedEmail() {
        return fstResultsDelegatedEmail;
    }

    public void setFstResultsDelegatedEmail(String fstResultsDelegatedEmail) {
        this.fstResultsDelegatedEmail = fstResultsDelegatedEmail;
    }
}
