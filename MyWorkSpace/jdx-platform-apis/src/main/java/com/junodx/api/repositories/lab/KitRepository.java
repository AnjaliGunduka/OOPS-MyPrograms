package com.junodx.api.repositories.lab;

import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.laboratory.Kit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KitRepository extends JpaRepository<Kit, String> {
    Page<Kit> findAll(Pageable pageable);
    List<Kit> findKitsByIdIn(List<String> ids);
    Optional<Kit> findKitBySampleNumber(String sampleNumber);
    List<Kit> findKitsBySampleNumber(String sampleNumber);
    List<Kit> findKitsBySampleNumberIn(List<String> ids);

    @Query("select k from Kit k where (:kitCode is null or k.code = :kitCode)" +
            "or (:sampleNumber is null or k.sampleNumber = :sampleNumber)" +
            "or (:sleeveNumber is null or k.psdSleeveNumber = :sleeveNumber)")
    List<Kit> findKitsByCodeOrSampleNumberOrPsdSleeveNumber(@Param("kitCode") String kitCode, @Param("sampleNumber") String sampleNumber, @Param("sleeveNumber") String sleeveNumber);

    List<Kit> findKitsByCodeOrSampleNumber(String code, String sample);
}
