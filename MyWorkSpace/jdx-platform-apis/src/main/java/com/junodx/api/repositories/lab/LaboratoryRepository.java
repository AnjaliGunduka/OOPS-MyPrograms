package com.junodx.api.repositories.lab;

import com.junodx.api.models.laboratory.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LaboratoryRepository extends JpaRepository<Laboratory, String> {
    Optional<Laboratory> findLaboratoryByDefaultLaboratoryIsTrue();
    List<Laboratory> findAllByIdIn(List<String> ids);
}
