package com.junodx.api.repositories.auth;

import com.junodx.api.models.auth.EmailChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailChangeRepository extends JpaRepository<EmailChange, Long> {
    Optional<EmailChange> findEmailChangeByUserId(String userId);
}
