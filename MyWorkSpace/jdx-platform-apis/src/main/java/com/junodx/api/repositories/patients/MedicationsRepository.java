package com.junodx.api.repositories.patients;

import com.junodx.api.models.patient.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationsRepository extends JpaRepository<Medication, Long> {
    public List<Medication> findMedicationsByPatientDetails_Id(String id);
}
