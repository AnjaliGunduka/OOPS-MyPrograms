package com.junodx.api.repositories.auth;

import com.junodx.api.models.auth.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findVerificationCodeByUser_Id(String userId);
    Optional<VerificationCode> findVerificationCodeByUser_EmailAndCode(String email, String code);
    Optional<VerificationCode> findVerificationCodeByUser_Email(String email);
    Optional<VerificationCode> findVerificationCodeByCode(String code);

    void deleteAllByUser_Id(String id);
}
