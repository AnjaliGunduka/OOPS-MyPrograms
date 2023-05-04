package com.junodx.api.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Phone;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "shipping_target")
public class ShippingTarget {
	@Id
	private String id;

	private String recipientName;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Address address;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Phone phone;

	public ShippingTarget() {
		this.id = UUID.randomUUID().toString();
		this.address = new Address();
		this.phone = new Phone();
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}
}
