package com.junodx.api.dto.models.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.core.Address;

import java.util.Calendar;

public class UserBatchDto {
 
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String username;
	private boolean activated;
	private Address primaryAddress;
	private UserType userType;
	private Integer age;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Calendar lastOrderedAt;
	private UserStatus status;

	public UserBatchDto(String id, String firstName, String lastName, String email, String username, boolean activated,
			Address primaryAddress, UserType userType, Integer age, Calendar lastOrderedAt, UserStatus status) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.activated = activated;
		this.primaryAddress = primaryAddress;
		this.userType = userType;
		this.age = age;
		this.lastOrderedAt = lastOrderedAt;
		this.status = status;
	}

	public UserBatchDto() {
		super();
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public Address getPrimaryAddress() {
		return primaryAddress;
	}

	public void setPrimaryAddress(Address primaryAddress) {
		this.primaryAddress = primaryAddress;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Calendar getLastOrderedAt() {
		return lastOrderedAt;
	}

	public void setLastOrderedAt(Calendar lastOrderedAt) {
		this.lastOrderedAt = lastOrderedAt;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
}
