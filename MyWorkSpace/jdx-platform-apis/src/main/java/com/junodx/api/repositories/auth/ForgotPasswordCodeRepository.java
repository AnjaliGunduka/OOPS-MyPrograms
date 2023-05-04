package com.junodx.api.repositories.auth;

import com.junodx.api.models.auth.ForgotPasswordCode;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgotPasswordCodeRepository extends JpaRepository<ForgotPasswordCode, Long> {
    Optional<ForgotPasswordCode> findForgotPasswordCodeByCode(String code);
}
