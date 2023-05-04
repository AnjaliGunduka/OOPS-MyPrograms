package com.junodx.api.services.auth;

import com.junodx.api.models.auth.User;
import com.junodx.api.repositories.UserRepository;
import com.junodx.api.security.AuthTokenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        logger.info("UserDetailsServiceImpl gave me a user for " + user);
        return UserDetailsImpl.build(user);
    }

    @Transactional
    public UserDetails loadUserByEmailAndClientId(String email, String clientId) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmailAndClientId(email, clientId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        return UserDetailsImpl.build(user);
    }
}
