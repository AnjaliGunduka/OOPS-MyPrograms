package com.junodx.api.models.patient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.patient.types.MedicationType;

import javax.persistence.*;

@Entity
@Table(name = "patient_medication")
public class Medication {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	private MedicationType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_details_id", unique = false)
	@JsonIgnore
	private PatientDetails patientDetails;

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

	public MedicationType getType() {
		return type;
	}

	public void setType(MedicationType type) {
		this.type = type;
	}

	public PatientDetails getPatientDetails() {
		return patientDetails;
	}

	public void setPatientDetails(PatientDetails patientDetails) {
		this.patientDetails = patientDetails;
	}
}
