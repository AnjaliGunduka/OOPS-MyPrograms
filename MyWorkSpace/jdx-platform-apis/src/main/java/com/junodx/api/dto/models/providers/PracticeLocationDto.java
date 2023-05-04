package com.junodx.api.dto.models.providers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Phone;
import com.junodx.api.models.providers.Practice;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class PracticeLocationDto {
    private String id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Address address;

    @JsonInclude(JsonInclude.Include.NON_NULL)

    private Phone phone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Phone fax;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PracticeDto practice;

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Phone getFax() {
        return fax;
    }

    public void setFax(Phone fax) {
        this.fax = fax;
    }

    public PracticeDto getPractice() {
        return practice;
    }

    public void setPractice(PracticeDto practice) {
        this.practice = practice;
    }
}
