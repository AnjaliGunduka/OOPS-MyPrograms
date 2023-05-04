package com.junodx.api.security;

import java.io.IOException;
import java.util.*;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.models.auth.Authority;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.auth.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


public class AuthTokenFilter extends OncePerRequestFilter {

    @Value("${debug}")
    private boolean debug;

    @Value("${jdx.auth.disablefortesting}")
    private boolean authDisabled;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    private ObjectMapper mapper;

    public AuthTokenFilter(){
        mapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if(authDisabled) { //TODO Do this only while debugging and remove before go-live
                logger.info("Auth filter is disabled");
                List<GrantedAuthority> authList = new ArrayList<>();
                authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                UserDetailsImpl userDetails = new UserDetailsImpl(1L, "override_user", "admin@junodx.com", "1", "", "", (Collection<? extends GrantedAuthority>) authList);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                String jwt = parseJwt(request);

                if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                    String username = jwtUtils.getUserNameFromJwtToken(jwt);
                    String email = jwtUtils.getEmailFromJwtToken(jwt);
                    String userId = jwtUtils.getUserIdFromJwtToken(jwt);
                    String clientId = jwtUtils.getClientIdFromJwtToken(jwt);

                    ArrayList<Authority> authorities = jwtUtils.getAuthoritiesFromJwtToken(jwt);
                    List<GrantedAuthority> authList = new ArrayList<>();
                    Iterator<Authority> i = authorities.iterator();
                    for (Authority a : authorities)
                        authList.add(new SimpleGrantedAuthority(String.valueOf(a.getName())));


                    UserDetailsImpl userDetails = new UserDetailsImpl(1L, username, email, userId, "", clientId, (Collection<? extends GrantedAuthority>) authList);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else
                    throw new Exception("Cannot parse inbound JWT");
            }
        } catch (Exception e) {
            if(debug)
                logger.debug("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
}