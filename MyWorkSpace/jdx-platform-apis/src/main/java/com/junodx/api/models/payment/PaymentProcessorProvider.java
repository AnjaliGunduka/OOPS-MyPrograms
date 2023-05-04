package com.junodx.api.models.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Phone;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "payment_processor_provider")
public class PaymentProcessorProvider {
	@Id
	private String id;

	private String name;
	private Address contactAddress;
	private Phone contactPhone;
	private String contactEmail;

	public PaymentProcessorProvider() {
		this.id = UUID.randomUUID().toString();
		this.contactAddress = new Address();
		this.contactPhone = new Phone();
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(Address contactAddress) {
		this.contactAddress = contactAddress;
	}

	public Phone getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(Phone contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
}
