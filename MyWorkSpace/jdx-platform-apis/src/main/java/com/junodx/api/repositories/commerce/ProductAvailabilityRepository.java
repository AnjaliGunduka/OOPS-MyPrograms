package com.junodx.api.repositories.commerce;

import com.junodx.api.models.commerce.ProductAvailablity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductAvailabilityRepository extends JpaRepository<ProductAvailablity, Long> {
    Optional<ProductAvailablity> findProductAvailablityByProduct_Id(String id);
}
