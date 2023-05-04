package com.library.service.book.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.library.service.book.request.CreateUserRequest;
import com.library.service.book.response.UserResponse;
import com.library.service.book.service.UserCommandService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/admin")
@Api(value = "User Management ", tags = { "User Management" })
@Validated
public class CustomUserController {

	@Autowired
	private UserCommandService userCommandService;

	@PostMapping(value = "/createUser", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest userCreateDto) {
		userCommandService.createUser(userCreateDto);
		return ResponseEntity.ok(new UserResponse(userCreateDto.getUsername(), userCreateDto.getPassword()));
	}
}
