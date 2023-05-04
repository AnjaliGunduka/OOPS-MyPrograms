package com.junodx.api.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import com.junodx.api.models.auth.Authority;
import com.junodx.api.models.auth.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.*;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${com.junodx.api.security.jwt.jwtSecret}")
    private String jwtSecret;

    /*
    public String generateJwtToken(Authentication authentication) {
        UserServiceImpl userPrincipal = (UserServiceImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    */

    public JwtUtils(){

    }

    public JwtUtils(String secret){
        this.jwtSecret = secret;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public String generateTokenFromUser(User user, long expirationDuration) {
        logger.info("generating token for auths authorities size" + user.getAuthorities().size());
        logger.info("generating token for auths " + user.getAuthoritiesAsStringList());

        return Jwts.builder()
                .claim("email", user.getEmail())
                .claim("userId", user.getId())
                .claim("clientId", user.getClientId())
                .claim("name", user.getFirstName() + " " + user.getLastName())
                .claim( "roles", user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList()))
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationDuration)).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String getNameFromJwtToken(String token) {
        return (String)Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("name");
    }

    public String getEmailFromJwtToken(String token) {
        return (String)Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("email");
    }

    public String getUserIdFromJwtToken(String token) {
        return (String)Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("userId");
    }

    public String getClientIdFromJwtToken(String token) {
        return (String)Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("clientId");
    }

    public ArrayList<Authority> getAuthoritiesFromJwtToken(String token) {
        ArrayList<String> roles = (ArrayList<String>)Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("roles");
        ArrayList<Authority> authorities = new ArrayList<>();
        for(String role : roles) {
            switch (role) {
                case Authority.ROLE_LAB_DIRECTOR:
                    authorities.add(new Authority(Authority.ROLE_LAB_DIRECTOR));
                    break;
                case Authority.ROLE_LAB_TECHNICIAN:
                    authorities.add(new Authority(Authority.ROLE_LAB_TECHNICIAN));
                    break;
                case Authority.ROLE_MEDICAL_PROVIDER:
                    authorities.add(new Authority(Authority.ROLE_MEDICAL_PROVIDER));
                    break;
                case Authority.ROLE_LAB_REPORTS_VIEWER:
                    authorities.add(new Authority(Authority.ROLE_LAB_REPORTS_VIEWER));
                    break;
                case Authority.ROLE_ADMIN:
                    authorities.add(new Authority(Authority.ROLE_ADMIN));
                    break;
                default:
                    authorities.add(new Authority(Authority.ROLE_PATIENT));
                    break;
            }
        }

        return authorities;
    }


    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
