package com.junodx.api.services.auth;


import com.junodx.api.models.auth.User;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public interface UserService {

    Optional<User> findOneByEmailAndClientId(String email, String clientId);
    User save(User user, UserDetailsImpl updater);
    User update(User user, UserDetailsImpl updater);
    Optional<User> findOne(String id);
    void delete(String id);
    void deleteByUserId(String uuid);
    public User getSystemUser();
    public User createDefaultAuthorities(User user);
}
