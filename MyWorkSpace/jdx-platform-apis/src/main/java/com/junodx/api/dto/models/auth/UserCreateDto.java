package com.junodx.api.dto.models.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.auth.types.UserType;

import java.util.Calendar;

public class UserCreateDto {
    private String firstName;
    private String lastName;
    private String email;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    private Calendar dateOfBirth;
    private UserType userType;
    private boolean activated;
    private UserStatus status;

    public UserCreateDto(){

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

    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
