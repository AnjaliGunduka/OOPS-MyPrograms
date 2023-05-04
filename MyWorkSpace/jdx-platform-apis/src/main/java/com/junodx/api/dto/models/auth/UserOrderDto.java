package com.junodx.api.dto.models.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.core.Address;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Embeddable 
public class UserOrderDto {
	private String customerId;
	private String firstName;
	private String lastName;
	private String email;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String dateOfBirth;
	private String phone;
	private String stripeCustomerId;
	private String cardConnectCustomerId;

	@Enumerated(EnumType.STRING)
	private UserType userType;
	private boolean useMobileForNotifications;
	private boolean isPatient;
	private Address shippingAddress;

	public UserOrderDto() {
		customerId = UUID.randomUUID().toString();
	}

	public UserOrderDto(String id, String first, String last, String email) {
		this.customerId = id;
		this.firstName = first;
		this.lastName = last;
		this.email = email;

	}

	public UserOrderDto(String customerId, String lastName, String email, String phone, String stripeCustomerId,
			UserType userType, Address shippingAddress) {
		super();
		this.customerId = customerId;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.stripeCustomerId = stripeCustomerId;
		this.userType = userType;
		this.shippingAddress = shippingAddress;
	}

	public UserOrderDto(String customerId, String firstName, String lastName, String email, String dateOfBirth,
			String phone, String stripeCustomerId, UserType userType,
			boolean useMobileForNotifications, boolean isPatient, Address shippingAddress) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.phone = phone;
		this.stripeCustomerId = stripeCustomerId;
		
		this.userType = userType;
		this.useMobileForNotifications = useMobileForNotifications;
		this.isPatient = isPatient;
		this.shippingAddress = shippingAddress;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String id) {
		this.customerId = id;
	}

	public void setId(String id) {
		this.customerId = id;
	}

	public String getId() {
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
