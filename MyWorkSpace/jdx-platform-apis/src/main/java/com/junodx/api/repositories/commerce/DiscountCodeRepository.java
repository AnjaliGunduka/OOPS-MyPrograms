package com.junodx.api.repositories.commerce;

import com.junodx.api.models.commerce.DiscountCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountCodeRepository extends JpaRepository<DiscountCode, Long> {
    Optional<DiscountCode> findDiscountCodeByCode(String code);
}
