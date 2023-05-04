package com.junodx.api.controllers.users.payloads.UserUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.auth.types.GenderTerms;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Phone;
import com.junodx.api.models.patient.Medication;
import com.junodx.api.models.patient.Vital;

import javax.persistence.*;
import java.util.List;

public class UserUpdatePayload {
    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Phone primaryPhone;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dateOfBirth;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Address primaryAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Address billingAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserUpdatePatientDetailsPayload patientDetails;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String limsContactId;

    @Column(name="stripe_customer_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stripeCustomerId;

    //@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    //private List<UserDevice> devices;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserType userType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserUpdatePreferencesPayload preferences;

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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public UserUpdatePatientDetailsPayload getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(UserUpdatePatientDetailsPayload patientDetails) {
        this.patientDetails = patientDetails;
    }

    public String getLimsContactId() {
        return limsContactId;
    }

    public void setLimsContactId(String limsContactId) {
        this.limsContactId = limsContactId;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserUpdatePreferencesPayload getPreferences() {
        return preferences;
    }

    public void setPreferences(UserUpdatePreferencesPayload preferences) {
        this.preferences = preferences;
    }
}
