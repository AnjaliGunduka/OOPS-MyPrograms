package com.junodx.api.controllers.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.payloads.MessageResponse;
import com.junodx.api.models.auth.OAuthClient;
import com.junodx.api.services.auth.AuthorizationService;
import com.junodx.api.services.auth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/oauth/accounts")
public class OAuthClientController {

    @Autowired
    private OAuthService oAuthService;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> create(@RequestBody OAuthClient oauthClient) {
        try {
            OAuthClient client = oAuthService.create(oauthClient);
            if(client != null)
                return ResponseEntity.ok().body(client);
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Cannot create OAuth client"));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Cannot create OAuth client"));
    }

    @GetMapping("/{clientId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> get(@PathVariable("clientId") String clientId) {
        try {
            Optional<OAuthClient> client = oAuthService.get(clientId);
            if(client.isPresent())
                return ResponseEntity.ok().body(client.get());
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Cannot find OAuth client"));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Cannot find OAuth client"));
    }

    @DeleteMapping("/{clientId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("clientId") String clientId){
        try {
            oAuthService.delete(clientId);

            return ResponseEntity.ok().body("Successfully deleted oauth client " + clientId);
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Cannot find OAuth client"));
        }
    }
}

