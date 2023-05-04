package com.junodx.api.repositories.lab;

import com.junodx.api.models.laboratory.Laboratory;
import com.junodx.api.models.laboratory.LaboratoryStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LaboratoryStatisticsRepository extends JpaRepository<LaboratoryStatistics, Long> {
    Optional<LaboratoryStatistics> findLaboratoryStatisticsByLaboratory_Id(String id);
    Optional<LaboratoryStatistics> findLaboratoryStatisticsByLaboratory(Laboratory laboratory);
}
