package com.junodx.api.dto.models.providers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.models.providers.Provider;
import com.junodx.api.models.providers.types.SpecialtyType;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class SpecialtyDto {
    private SpecialtyType type;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProviderDto provider;

    public SpecialtyType getType() {
        return type;
    }

    public void setType(SpecialtyType type) {
        this.type = type;
    }

    public ProviderDto getProvider() {
        return provider;
    }

    public void setProvider(ProviderDto provider) {
        this.provider = provider;
    }
}
