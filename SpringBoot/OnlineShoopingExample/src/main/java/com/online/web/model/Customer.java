package com.online.web.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;



@Entity
public class Customer implements Serializable{

	
	private static final long serialVersionUID = 3L;
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long customerId;
	
	
	private String customerName;
	
	
	
	private String customerEmailAddress;
	
	
	private String custometPhoneNumber;
	
	
	private String username;
	
	
	//@Size(min=6,max=30)
	private String password;
	
	
	private boolean enabled;
	
	
	@OneToOne
	@JoinColumn(name="shippingAddressId")
	private ShippingAddress  shippingAddress; 
	

	@OneToOne
	@JoinColumn(name="billingAddressId")
	private BillingAddress billingAddress  ; 
	
	@OneToOne
	@JoinColumn(name="cartId")
	private Cart cart;

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmailAddress() {
		return customerEmailAddress;
	}

	public void setCustomerEmailAddress(String customerEmailAddress) {
		this.customerEmailAddress = customerEmailAddress;
	}

	public String getCustometPhoneNumber() {
		return custometPhoneNumber;
	}

	public void setCustometPhoneNumber(String custometPhoneNumber) {
		this.custometPhoneNumber = custometPhoneNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public ShippingAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(ShippingAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public BillingAddress getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(BillingAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	
	
	
	
}
