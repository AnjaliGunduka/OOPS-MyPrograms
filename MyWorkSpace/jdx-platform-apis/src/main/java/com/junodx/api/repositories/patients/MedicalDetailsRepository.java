package com.junodx.api.repositories.patients;

import com.junodx.api.models.patient.MedicalDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalDetailsRepository extends JpaRepository<MedicalDetails, Long> {
    public Optional<MedicalDetails> findMedicalDetailsByPatientDetails_User_Id(String id);
    public Optional<MedicalDetails> findMedicalDetailsByPatientDetails_Id(String id);
}
