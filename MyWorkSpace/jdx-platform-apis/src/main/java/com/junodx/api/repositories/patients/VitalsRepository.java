package com.junodx.api.repositories.patients;

import com.junodx.api.models.patient.MedicalDetails;
import com.junodx.api.models.patient.Medication;
import com.junodx.api.models.patient.Vital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VitalsRepository  extends JpaRepository<Vital, Long> {
    public List<Vital> findVitalsByMedicalDetails_Id(Long id);
    public List<Vital> findVitalsByMedicalDetails(MedicalDetails medicalDetails);
}
