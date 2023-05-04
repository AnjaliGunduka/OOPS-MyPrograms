package com.online.web.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BillingAddress implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long billingAddressId;
	
	
	private String strretName;
	
	
	private String apartmentNumber;
	

	private String city;
	
	
	private String state;
	
	
	private String country;
	
	
	private String zipCode;
	
	


	public long getBillingAddressId() {
		return billingAddressId;
	}

	public void setBillingAddressId(long billingAddressId) {
		this.billingAddressId = billingAddressId;
	}

	public String getStrretName() {
		return strretName;
	}

	public void setStrretName(String strretName) {
		this.strretName = strretName;
	}

	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	
	
	
	
}
