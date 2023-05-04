package com.junodx.api.services.auth;

import com.junodx.api.models.auth.AccessToken;
import com.junodx.api.models.auth.RefreshToken;
import com.junodx.api.models.auth.User;
import com.junodx.api.repositories.RefreshTokenRepository;
import com.junodx.api.repositories.UserRepository;
import com.junodx.api.security.JwtUtils;
import com.junodx.api.security.exceptions.AccessTokenExpirationException;
import com.junodx.api.security.exceptions.TokenRefreshException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {
    @Value("${jdx.auth.refreshtoken.duration.seconds}")
    private long refreshTokenDuration;

    @Value("${jdx.auth.accesstoken.duration.milliseconds}")
    private long accessTokenDuration;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    public Optional<RefreshToken> findRefreshTokenByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenDuration));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public AccessToken createAccessToken(User user){
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(jwtUtils.generateTokenFromUser(user, accessTokenDuration));
        accessToken.setUser(user);
        accessToken.setExpiryDate(Instant.now().plusMillis(accessTokenDuration));

        return accessToken;
    }

    public RefreshToken verifyRefreshTokenExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please authorize a new ID Token.");
        }
        return token;
    }

    public RefreshToken getRefreshTokenIfNotExpired(User user) throws TokenRefreshException {
        Optional<RefreshToken> token = refreshTokenRepository.findByUser(user);
        if(token.isPresent()) {
            try {
                return verifyRefreshTokenExpiration(token.get());
            } catch(TokenRefreshException expiredTokenException) {
                return null;
            }
        }
        return null;
    }

    public AccessToken verifyAccessTokenExpiration(AccessToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new AccessTokenExpirationException(token.getToken(), "Access token was expired. Please sign in again");
        }
        return token;
    }


    @Transactional
    public int deleteRefreshTokenByUserId(String userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

    public long getAccessTokenDuration(){
        return this.accessTokenDuration;
    }

    public long getRefreshTokenDuration(){
        return this.refreshTokenDuration;
    }
}
