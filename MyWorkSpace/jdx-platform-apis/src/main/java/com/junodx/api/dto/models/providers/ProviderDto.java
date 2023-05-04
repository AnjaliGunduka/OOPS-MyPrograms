package com.junodx.api.dto.models.providers;

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

public class ProviderDto {
    private String id;

    private String firstName;

    private String lastName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Phone contactPhone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Address contactAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String npi;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String upin;

    private boolean isPracticing;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Phone faxNumber;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SpecialtyDto> specialties;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MedicalLicenseDto> licenses;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PracticeDto practice;

    private Meta meta;

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

    public Address getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(Address contactAddress) {
        this.contactAddress = contactAddress;
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

    public boolean isPracticing() {
        return isPracticing;
    }

    public void setPracticing(boolean practicing) {
        isPracticing = practicing;
    }

    public Phone getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(Phone faxNumber) {
        this.faxNumber = faxNumber;
    }

    public List<SpecialtyDto> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<SpecialtyDto> specialties) {
        this.specialties = specialties;
    }

    public List<MedicalLicenseDto> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<MedicalLicenseDto> licenses) {
        this.licenses = licenses;
    }

    public PracticeDto getPractice() {
        return practice;
    }

    public void setPractice(PracticeDto practice) {
        this.practice = practice;
    }

    @JsonProperty("practiceId")
    public String getPracticeId(){
        if(this.practice != null && this.practice.getId() != null)
            return this.practice.getId();

        return null;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
