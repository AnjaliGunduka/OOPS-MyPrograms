package com.junodx.api.models.commerce;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.core.Address;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Embeddable
public class UserCheckout {
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String dateOfBirth;
    private String phone;
    private String stripeCustomerId;
    private String cardConnectCustomerId;

    @Enumerated(EnumType.STRING)
    private UserType userType;
    private boolean useMobileForNotifications;
    private boolean isPatient;
    private Address shippingAddress;

    public UserCheckout(){
        customerId = UUID.randomUUID().toString();
    }

    public UserCheckout(String id, String first, String last, String email){
        this.customerId = id;
        this.firstName = first;
        this.lastName = last;
        this.email = email;

    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String id) {
        this.customerId = id;
    }

    public void setId(String id){
        this.customerId = id;
    }

    public String getId(){
        return this.customerId;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isUseMobileForNotifications() {
        return useMobileForNotifications;
    }

    public void setUseMobileForNotifications(boolean useMobileForNotifications) {
        this.useMobileForNotifications = useMobileForNotifications;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getCardConnectCustomerId() {
        return cardConnectCustomerId;
    }

    public void setCardConnectCustomerId(String cardConnectCustomerId) {
        this.cardConnectCustomerId = cardConnectCustomerId;
    }

    public boolean isPatient() {
        return isPatient;
    }

    public void setPatient(boolean patient) {
        isPatient = patient;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
