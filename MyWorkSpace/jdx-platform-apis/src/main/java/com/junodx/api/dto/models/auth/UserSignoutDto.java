package com.junodx.api.dto.models.auth;

import com.junodx.api.models.core.Phone;

import java.util.Calendar;

public class UserSignoutDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Phone primaryPhone;

    public UserSignoutDto(){

    }

    public UserSignoutDto(String id, String first, String last, String email, Phone phone){
        this.id = id;
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.primaryPhone = phone;

    }

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

    public Phone getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(Phone primaryPhone) {
        this.primaryPhone = primaryPhone;
    }
}
