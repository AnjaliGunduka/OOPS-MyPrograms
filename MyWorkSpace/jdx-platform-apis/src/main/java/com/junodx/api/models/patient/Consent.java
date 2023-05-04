package com.junodx.api.models.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.laboratory.LaboratoryOrder;
import com.junodx.api.models.patient.types.ConsentType;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table(name = "patient_consent")
public class Consent {
	@Id
	private String id;

	@OneToOne
	@JoinColumn(name = "lab_order_id", nullable = false)
	@JsonIgnore
	private LaboratoryOrder laboratoryOrder;

	@ManyToOne
	@JoinColumn(name = "patient_id", nullable = false)
	@JsonIgnore
	private User patient;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String formName;

	@Enumerated(EnumType.STRING)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ConsentType type;

	private boolean approval;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Calendar approvalDate;

	public Consent() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LaboratoryOrder getLaboratoryOrder() {
		return laboratoryOrder;
	}

	public void setLaboratoryOrder(LaboratoryOrder laboratoryOrder) {
		this.laboratoryOrder = laboratoryOrder;
	}

	public User getPatient() {
		return patient;
	}

	public void setPatient(User patient) {
		this.patient = patient;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public ConsentType getType() {
		return type;
	}

	public void setType(ConsentType type) {
		this.type = type;
	}

	public boolean isApproval() {
		return approval;
	}

	public void setApproval(boolean approval) {
		this.approval = approval;
	}

	public Calendar getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Calendar approvalDate) {
		this.approvalDate = approvalDate;
	}

}
