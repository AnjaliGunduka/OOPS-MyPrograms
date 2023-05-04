package com.library.service.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import com.library.service.book.config.JwtTokenUtil;
import com.library.service.book.exception.Constants;
import com.library.service.book.model.JwtRequest;
import com.library.service.book.response.JWTTokenResponse;
import com.library.service.book.service.CustomUserDetailsService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/")
@Api(value = "Admin Login ", tags = { "Admin Login" })
@Validated
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> adminLogin(@RequestBody JwtRequest authenticationRequest) throws AccessDeniedException {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JWTTokenResponse(token, "Bearer", Constants.JWT_TOKEN_VALIDITY));
	}

	private void authenticate(String username, String password) throws AccessDeniedException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AccessDeniedException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new AccessDeniedException("INVALID_CREDENTIALS", e);
		}
	}
}
