package com.junodx.api.dto.models.providers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.Phone;
import com.junodx.api.models.providers.Location;
import com.junodx.api.models.providers.Provider;

import javax.persistence.*;
import java.util.List;

public class PracticeDto {
    private String id;

    private String name;

    private boolean isActive;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Address contactAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Phone primaryPhone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Phone secondaryPhone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String patientEmail;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String billingEmail;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String primaryEmail;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ProviderDto> providers;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Location> locations;

    private Meta meta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Address getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(Address contactAddress) {
        this.contactAddress = contactAddress;
    }

    public Phone getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(Phone primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public Phone getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(Phone secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getBillingEmail() {
        return billingEmail;
    }

    public void setBillingEmail(String billingEmail) {
        this.billingEmail = billingEmail;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public List<ProviderDto> getProviders() {
        return providers;
    }

    public void setProviders(List<ProviderDto> providers) {
        this.providers = providers;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
