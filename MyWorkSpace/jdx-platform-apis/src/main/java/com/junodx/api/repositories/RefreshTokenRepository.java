package com.junodx.api.repositories;

import com.junodx.api.models.auth.RefreshToken;
import com.junodx.api.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findById(Long id);
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);

    int deleteByUser(User user);
}