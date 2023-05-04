package com.college.studentservices.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COLLEGE")
public class College {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "CODE")
	private String code;
	@Column(name = "ADDRESS")
	private String address;
	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;
	@Column(name = "EMAIL")
	private String email;
	@Column(name = "EXAM_CENTER")
	private String examCenter;
	public String getExamCenter() {
		return examCenter;
	}
	public void setExamCenter(String examCenter) {
		this.examCenter = examCenter;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Status getStatus() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
}
