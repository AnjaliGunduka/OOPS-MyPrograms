package com.spring.demo;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * 
 * @author muralikrishnak
 *
 */
@RestController
@RequestMapping(value = "/userService/v1")
@Api(value = "User Management", tags = { "User Management" })
@Validated
public class UserController {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@ApiOperation(value = "Creates a User")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = UserResponse.class, message = "User created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
		ApplicationUser applicationUser = userDetailsService.createUser(createUserRequest);
		return ResponseEntity.status(HttpStatus.OK).body(new UserResponse(applicationUser.getUsername(),
				WordUtils.capitalizeFully(applicationUser.getRole().name().replaceAll("_", " "))));
	}
}
