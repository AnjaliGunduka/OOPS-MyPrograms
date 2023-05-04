package com.example.demo.controller;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Users;
import com.example.demo.enums.Role;
import com.example.demo.request.CreateUserRequest;
import com.example.demo.request.LoginRequest;
import com.example.demo.response.LoginResponse;
import com.example.demo.service.JwtAuthenticationService;
import com.example.demo.service.UserService;
import com.example.demo.utils.EnableTokenAuthorisation;
import com.example.demo.utils.TokenPayLoadDetails;

@RestController
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	JwtAuthenticationService jwtAuthenticationService;

	@PostMapping(value = "/addUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginResponse> addUser(@Valid @RequestBody CreateUserRequest request) {
//		request.setRole(role);j
		LoginResponse response = userService.signUpUser(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginResponse> userLogin(@Valid @RequestBody LoginRequest request) {
		LoginResponse response = userService.login(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@GetMapping(value = "/user/{id}")
	@EnableTokenAuthorisation
	public ResponseEntity<Users> getUser(@RequestHeader(required = true, value = "Authorization")
	String authorization,@PathVariable("id") long id,  HttpServletRequest httpRequest) {
		TokenPayLoadDetails details = jwtAuthenticationService.getTokenPayLoadDetails(httpRequest);
		Users user = userService.getUser(id,details);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

}
