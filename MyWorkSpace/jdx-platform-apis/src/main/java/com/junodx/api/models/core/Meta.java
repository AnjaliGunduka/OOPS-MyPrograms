package com.junodx.api.models.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.junodx.api.services.auth.UserDetailsImpl;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Calendar;

@Embeddable
public class Meta {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "created_at", updatable = false)
	protected Calendar createdAt;

	@Column(name = "created_by", updatable = false)
	protected String createdBy;

	@Column(name = "created_by_id", updatable = false)
	protected String createdById;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	protected Calendar lastModifiedAt;
	protected String lastModifiedBy;
	protected String lastModifiedById;

	public Meta() {
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public Calendar getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Calendar lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedById() {
		return lastModifiedById;
	}

	public void setLastModifiedById(String lastModifiedById) {
		this.lastModifiedById = lastModifiedById;
	}

	public static Meta setMeta(Meta obj, UserDetailsImpl u) {
		if (obj == null)
			obj = new Meta();

		if (obj.getCreatedAt() == null) {
			obj.setCreatedAt(Calendar.getInstance());
			obj.setCreatedBy(u.getEmail());
			obj.setCreatedById(u.getUserId());
		}
		obj.setLastModifiedAt(Calendar.getInstance());
		obj.setLastModifiedBy(u.getEmail());
		obj.setLastModifiedById(u.getUserId());

		return obj;
	}

	@Override
	public String toString() {
		String returnVal = "";
		returnVal += "{\n";
		returnVal += "\t\"createdBy\": \"" + this.createdBy + "\",\n";
		returnVal += "\t\"createdById\": \"" + this.createdById + "\",\n";
		returnVal += "\t\"createdAt\": \"" + this.createdAt + "\",\n";
		returnVal += "\t\"lastModifiedBy\": \"" + this.lastModifiedBy + "\",\n";
		returnVal += "\t\"lastModifiedById\": \"" + this.lastModifiedById + "\",\n";
		returnVal += "\t\"lastModifiedAt\": \"" + this.lastModifiedAt + "\"\n";
		returnVal += "}";

		return returnVal;
	}
}
