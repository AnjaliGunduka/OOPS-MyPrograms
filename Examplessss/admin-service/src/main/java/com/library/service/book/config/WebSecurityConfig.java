package com.library.service.book.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	/*
	 * Adding Swagger UI v2, Swagger UI v3 (OpenAPI) to Authentication Whitelist
	 * 
	 */
//	private static final String[] AUTH_WHITELIST = { "/v2/api-docs", "/swagger-resources", "/swagger-resources/**",
//			"/configuration/ui", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/v3/api-docs/**",
//			"/swagger-ui/**" };
	private static final String[] AUTH_SWAGGERLIST = { "/v2/api-docs", "/swagger-resources", "/swagger-resources/**",
			"/configuration/ui", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/v3/api-docs/**",
			"/swagger-ui/**" };

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// Disable CSRF (cross site request forgery)beacuse no need of cookies
		httpSecurity.csrf().disable().authorizeRequests()
				// Entry points
				.antMatchers(AUTH_SWAGGERLIST).permitAll().antMatchers("/login").permitAll()
				// Disallow everything else..
				.antMatchers("/**").authenticated().and()
				// If a user try to access a resource without having enough permissions
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
				// No session will be created or used by spring security
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// Apply JWT
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}