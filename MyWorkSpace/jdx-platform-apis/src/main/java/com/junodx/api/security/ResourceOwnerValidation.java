package com.junodx.api.security;

import com.junodx.api.models.auth.Authority;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.auth.UserNotInClientException;
import com.junodx.api.services.auth.UserNotOwnerException;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public class ResourceOwnerValidation {
    public static boolean validateOwnership(UserDetailsImpl user, String userId, String clientId) throws UserNotOwnerException, UserNotInClientException {
        if(userId != null && !userId.equals(user.getUserId()))
            throw new UserNotOwnerException("Requesting user is not owner");

        if(clientId != null && !clientId.equals(user.getClientId()))
            throw new UserNotInClientException("Requesting cannot access resource for client");

        return true;
    }

    public static boolean resourceRequiresRestrictedScope(UserDetailsImpl user) {
        if(user == null)
            throw new JdxServiceException("Cannot resolve security context to detrmine scope of access");

        if(user.getAuthorities().stream().filter(x->x.getAuthority().equals(Authority.ROLE_PATIENT)).findAny().isPresent())
            return true;

        return false;
    }
}
