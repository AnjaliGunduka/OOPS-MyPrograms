package com.junodx.api.dto.models.providers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.Phone;
import com.junodx.api.models.providers.MedicalLicense;
import com.junodx.api.models.providers.Practice;
import com.junodx.api.models.providers.Specialty;

import javax.persistence.*;
import java.util.List;

public class ProviderOrderDto {
    @JsonIgnore
    private String id;

    private String firstName;

    private String lastName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Phone contactPhone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String npi;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String upin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Phone getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(Phone contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getNpi() {
        return npi;
    }

    public void setNpi(String npi) {
        this.npi = npi;
    }

    public String getUpin() {
        return upin;
    }

    public void setUpin(String upin) {
        this.upin = upin;
    }
}
