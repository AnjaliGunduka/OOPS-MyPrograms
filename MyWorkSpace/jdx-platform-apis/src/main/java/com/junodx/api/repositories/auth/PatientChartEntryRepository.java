package com.junodx.api.repositories.auth;

import com.junodx.api.models.patient.PatientChartEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientChartEntryRepository extends JpaRepository<PatientChartEntry, String> {
}
