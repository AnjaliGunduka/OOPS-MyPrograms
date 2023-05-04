package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests((auth) -> {
			try {
				auth.anyRequest().authenticated().and().oauth2Login();
			} catch (Exception e) {
				throw new RuntimeException(e);
			};
		});
		return http.build();
	}
		private ClientRegistration clientRegistration() {
		return CommonOAuth2Provider.GITHUB.getBuilder("github").clientId("a42fd48212e6e15c04c5")
	           .clientSecret("5887f8974aab570241c5501d3d5103cc28d47bea").build();
}
		@Bean 
		public ClientRegistrationRepository clientRepository() {
			  	ClientRegistration clientReg = clientRegistration(); return new
			  		InMemoryClientRegistrationRepository(clientReg); }
}