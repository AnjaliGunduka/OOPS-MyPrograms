package com.example.demo.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class SecureController {

	@GetMapping("/login")
	public String main(OAuth2AuthenticationToken token) {
		System.out.println(token.getPrincipal());
		return "Welcome to Secured Resource";
	}

}
