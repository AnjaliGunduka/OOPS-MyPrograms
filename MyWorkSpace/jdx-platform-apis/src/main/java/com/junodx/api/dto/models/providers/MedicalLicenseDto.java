package com.junodx.api.dto.models.providers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.models.core.types.State;
import com.junodx.api.models.providers.Provider;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class MedicalLicenseDto {
    private State state;

    @Column(name = "license_number")
    private String licenseNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProviderDto provider;

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

    public ProviderDto getProvider() {
        return provider;
    }

    public void setProvider(ProviderDto provider) {
        this.provider = provider;
    }
}
