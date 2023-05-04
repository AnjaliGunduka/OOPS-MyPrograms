package com.junodx.api.models.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Embeddable
public class Address {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "address_person_name")
    private String name;

    @Column(name = "address_street")
    private String street;

    @Column(name = "address_city")
    private String city;

    @Column(name = "address_state")
    private String state;

    @Column(name = "address_postalCode")
    private String postalCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "address_country")
    private String country;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean primaryMailingAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean primaryAddress;

    @Column(name="is_resdential", nullable = true)
    private boolean isResidential;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isPrimaryMailingAddress() {
        return primaryMailingAddress;
    }

    public void setPrimaryMailingAddress(boolean primaryMailingAddress) {
        this.primaryMailingAddress = primaryMailingAddress;
    }

    public boolean isPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(boolean primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

}
