package com.book.order.service.demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.book.order.service.demo.request.UserDataDTO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class MainController {


	@PostMapping("/signin")
	  @ApiOperation(value = "${MainController.signin}")
	  @ApiResponses(value = {
	      @ApiResponse(code = 400, message = "Something went wrong"), 
	      @ApiResponse(code = 422, message = "Invalid username/password supplied")})
	  public String login(
	      @ApiParam("Username") @RequestParam String username, 
	      @ApiParam("Password") @RequestParam String password) {
			return username;
	    //return userService.signin(username, password);
	  }

	  @PostMapping("/signup")
	  @ApiOperation(value = "${MainController.signup}")
	  @ApiResponses(value = {
	      @ApiResponse(code = 400, message = "Something went wrong"), 
	      @ApiResponse(code = 403, message = "Access denied"), 
	      @ApiResponse(code = 422, message = "Username is already in use")})
	  public String signup(@ApiParam("Signup User") @RequestBody UserDataDTO user) {
		return "Sign";
	   // return userService.signup();
	  }

	
}
