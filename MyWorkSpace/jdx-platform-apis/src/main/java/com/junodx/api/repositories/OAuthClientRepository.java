package com.junodx.api.repositories;

import com.junodx.api.models.auth.OAuthClient;
import com.junodx.api.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthClientRepository extends JpaRepository<OAuthClient, String> {
    //Optional<OAuthClient> findByCognitoUserPoolId();
    Optional<OAuthClient> findOAuthClientByClientId(String clientId);
    Optional<OAuthClient> findOAuthClientByClientIdAndClientSecret(String clientId, String clientSecret);
    Optional<OAuthClient> findOAuthClientByAccountUser_Id(String id);
}
