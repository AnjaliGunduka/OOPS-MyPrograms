package com.college.studentservices.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CollegeRequest {
	@NotBlank(message = "College Name should not be null or Empty")
	private String name;
	@NotBlank(message = "college Code should not be null or Empty")
	@Size(min = 5, max = 5, message = "Bank Code Length must be 5 characters")
	private String code;
	@NotBlank(message = "College Address should not be null or Empty")
	private String address;
	private String phoneNumber;
	@Email(message = "Invalid Email format")
	private String email;
	private String examCenter;
	public String getExamCenter() {
		return examCenter;
	}
	public void setExamCenter(String examCenter) {
		this.examCenter = examCenter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
