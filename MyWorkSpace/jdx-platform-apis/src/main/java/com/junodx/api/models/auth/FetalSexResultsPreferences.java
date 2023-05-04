package com.junodx.api.models.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.models.auth.types.GenderTerms;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FetalSexResultsPreferences {
    @Column(name = "gender_terms")
    protected GenderTerms genderTerms;

    @Column(name = "gender_fanfare", columnDefinition = "BOOLEAN DEFAULT FALSE")
    protected boolean genderFanfare;

    @Column(name = "gender_delegated", columnDefinition = "BOOLEAN DEFAULT FALSE")
    protected boolean genderDelegated;

    @Column(name = "fst_results_delegated_email")
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
