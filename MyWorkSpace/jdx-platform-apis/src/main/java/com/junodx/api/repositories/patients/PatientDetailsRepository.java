package com.junodx.api.repositories.patients;

import com.junodx.api.models.patient.PatientDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientDetailsRepository extends JpaRepository<PatientDetails, String> {
    Optional<PatientDetails> findPatientDetailsByUser_Id(String id);
}
