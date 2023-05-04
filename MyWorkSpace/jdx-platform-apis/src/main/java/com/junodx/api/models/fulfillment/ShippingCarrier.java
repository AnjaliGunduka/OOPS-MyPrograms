package com.junodx.api.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.Phone;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "shipping_carrier")
public class ShippingCarrier {
	@Id
	private String id;

	private String name;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Address contactAddress;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Phone contactPhone;

	private Meta meta;

	public ShippingCarrier() {
		this.id = UUID.randomUUID().toString();
		contactAddress = new Address();
		contactPhone = new Phone();
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

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}
}
