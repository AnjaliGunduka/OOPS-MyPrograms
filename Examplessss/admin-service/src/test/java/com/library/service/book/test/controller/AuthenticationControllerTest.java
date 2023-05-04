package com.library.service.book.test.controller;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.library.service.book.config.JwtAuthenticationEntryPoint;
import com.library.service.book.config.JwtTokenUtil;
import com.library.service.book.controller.AuthenticationController;
import com.library.service.book.model.JwtRequest;
import com.library.service.book.model.JwtResponse;
import com.library.service.book.request.CreateUserRequest;
import com.library.service.book.service.CustomUserDetailsService;
import com.library.service.book.service.UserCommandService;

@WebMvcTest(value = AuthenticationController.class)
public class AuthenticationControllerTest {

	Logger logger = LoggerFactory.getLogger(AuthenticationControllerTest.class);
	@MockBean
	private UserCommandService userCommandService;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private JwtTokenUtil jwtUtil;
	@MockBean
	CustomUserDetailsService customUserDetailsService;

	@MockBean
	JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@MockBean
	AuthenticationManager authenticationManager;

	private static UserDetails dummy;
	private static String jwtToken;

	@BeforeEach
	public void setUp() {
		dummy = new User("admin", "admin", new ArrayList<>());
		jwtToken = jwtUtil.generateToken(dummy);
	}

	@Test
	public void testLoginReturnsJwt() throws Exception {
		JwtRequest authenticationRequest = new JwtRequest("admin", "admin");
		JwtResponse authenticationResponse = new JwtResponse("anyToken");
		String jsonCreate = new Gson().toJson(authenticationRequest);
		String jsonResponse = new Gson().toJson(authenticationResponse);
		when(jwtUtil.generateToken(dummy)).thenReturn("anyToken");
		when(customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername())).thenReturn(dummy);
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonBody = objectMapper.writeValueAsString(authenticationRequest);
		this.mockMvc
				.perform(post("/admin/login").content(jsonBody).content(jsonCreate).content(jsonResponse)
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testCreateUser() throws Exception {
		CreateUserRequest userCreateDto = new CreateUserRequest("admin", "admin", "ADMIN");
		CreateUserRequest userCreateResponseDto = new CreateUserRequest("admin", "admin", "ADMIN");
		String jsonCreate = new Gson().toJson(userCreateDto);
		String jsonResponse = new Gson().toJson(userCreateResponseDto);
		when(customUserDetailsService.loadUserByUsername(userCreateDto.getUsername())).thenReturn(dummy);
		when(userCommandService.createUser(userCreateDto)).thenReturn(userCreateResponseDto);
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonBody = objectMapper.writeValueAsString(userCreateDto);
		this.mockMvc.perform(post("/admin/users").header("Authorization", "Bearer " + jwtToken).content(jsonBody)
				.content(jsonCreate).content(jsonResponse).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());

	}

}
