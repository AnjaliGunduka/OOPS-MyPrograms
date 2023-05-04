package com.junodx.api.services.auth;

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import com.junodx.api.models.auth.OAuthClient;
import com.junodx.api.repositories.OAuthClientRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
public class OAuthService extends ServiceBase {
    @Autowired
    private OAuthClientRepository oAuthClientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);

    public OAuthClient create(OAuthClient client) throws JdxServiceException  {
        //TODO need to passwordEncode the secret for this client
        //passwordEncoder.encode(user.getPassword()
        if(client == null)
            throw new JdxServiceException("Cannot create new oauth client, payload not present in request");

        if(client.getClientSecret() == null)
            throw new JdxServiceException("Cannot create new oauth client, secret is not set");

        client.setClientSecret(passwordEncoder.encode(client.getClientSecret()));

        return oAuthClientRepository.save(client);
    }

    public Optional<OAuthClient> get(String id){
        return oAuthClientRepository.findById(id);
    }

    public Optional<OAuthClient> findByClientId(String id) {return oAuthClientRepository.findOAuthClientByClientId(id);}

    public OAuthClient update(OAuthClient client){
        Optional<OAuthClient> update = oAuthClientRepository.findById(client.getId());
        if(update.isPresent()){
            if(client.getClientId() != null) update.get().setClientId(client.getClientId());

            //TODO need to passwordEncode this secret
            if(client.getClientSecret() != null) update.get().setClientSecret(client.getClientSecret());

            return oAuthClientRepository.save(update.get());
        }

        return null;
    }

    public void delete(String clientId){
        Optional<OAuthClient> delete = oAuthClientRepository.findById(clientId);
        if(delete.isPresent())
            oAuthClientRepository.delete(delete.get());
    }
}
