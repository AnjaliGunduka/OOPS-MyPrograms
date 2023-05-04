package com.junodx.api.controllers.users.payloads.UserUpdate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.patient.Medication;

import java.util.List;

public class UserUpdatePatientDetailsPayload {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private UserUpdateMedicalDetails medicalDetails;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<Medication> medications;

	public UserUpdatePatientDetailsPayload() {

	}

	public UserUpdateMedicalDetails getMedicalDetails() {
		return medicalDetails;
	}

	public void setMedicalDetails(UserUpdateMedicalDetails medicalDetails) {
		this.medicalDetails = medicalDetails;
	}

	public List<Medication> getMedications() {
		return medications;
	}

	public void setMedications(List<Medication> medications) {
		this.medications = medications;
	}
}
