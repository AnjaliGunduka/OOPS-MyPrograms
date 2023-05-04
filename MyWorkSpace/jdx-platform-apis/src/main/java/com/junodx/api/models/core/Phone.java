package com.junodx.api.models.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.core.types.PhoneType;

import javax.persistence.*;

@Embeddable
public class Phone {
    @JsonIgnore
    private String countryCode;

    @JsonIgnore
    private String areaCode;

    private String phoneNumber;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int digits;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String country;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PhoneType phoneType;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean primaryPhone;

    public Phone(String number){
        this.phoneNumber = number;
    }

    public Phone() {
        this.phoneNumber = "";
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        int digits = phoneNumber.length();
        if(phoneNumber != null){
            switch(digits) {
                case 10: {
                    this.countryCode = phoneNumber.substring(0,1);
                    this.areaCode = phoneNumber.substring(1,4);
                    this.phoneNumber = phoneNumber.substring(4, phoneNumber.length());
                }
                case 9: {
                    this.countryCode = "1";
                    this.areaCode = phoneNumber.substring(0,3);
                    this.phoneNumber = phoneNumber.substring(3, phoneNumber.length());
                }
                default: this.phoneNumber = phoneNumber;
            }
        }

    }

    public int getDigits() {
        return digits;
    }

    public void setDigits(int digits) {
        this.digits = digits;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    public boolean isPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(boolean primaryPhone) {
        this.primaryPhone = primaryPhone;
    }
}
