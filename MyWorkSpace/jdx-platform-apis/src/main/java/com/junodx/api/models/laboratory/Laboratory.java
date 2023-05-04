package com.junodx.api.models.laboratory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.Phone;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "laboratory")
public class Laboratory {

	@Id
	@Column(name = "id", nullable = false, updatable = false, unique = true)
	private String id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Address location;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Phone contact;

	@Column(name = "default_laboratory", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean defaultLaboratory;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Meta meta;

	public Laboratory() {
		this.id = UUID.randomUUID().toString();
		this.name = "";
	}

	public Laboratory(String name, Address location, Phone contact) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.location = location;
		this.contact = contact;
	}

	public static Laboratory build(String id) {
		Laboratory lab = new Laboratory();
		lab.setId(id);

		return lab;
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

	public void setName(String labName) {
		this.name = labName;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public Phone getContact() {
		return contact;
	}

	public void setContact(Phone contactNumber) {
		this.contact = contactNumber;
	}

	public boolean isDefaultLaboratory() {
		return defaultLaboratory;
	}

	public void setDefaultLaboratory(boolean defaultLaboratory) {
		this.defaultLaboratory = defaultLaboratory;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		if (this.meta == null)
			this.meta = new Meta();

		this.meta = meta;
	}
}
