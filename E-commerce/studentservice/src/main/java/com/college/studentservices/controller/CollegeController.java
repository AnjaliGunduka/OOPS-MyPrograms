package com.college.studentservices.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.college.studentservices.entities.College;
import com.college.studentservices.request.CollegeRequest;
import com.college.studentservices.response.CollegeResponse;
import com.college.studentservices.services.CollegeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/collegeServices/v1")
@Api(value = "College Management", tags = { "College Management" })
@Validated
public class CollegeController {
	@Autowired
	CollegeService collegeService;
	@ApiOperation(value = "Creates a College")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = CollegeResponse.class, message = "Colllege created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters")})
	@PostMapping(value = "/colleges", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollegeResponse> createCollege(@Valid @RequestBody CollegeRequest collegeRequest) {
		College createdCollege = collegeService.createCollege(collegeRequest);
		ModelMapper modelMapper = new ModelMapper();
		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(createdCollege, CollegeResponse.class));
	}
	@ApiOperation(value = "Gets a College")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = College.class, message = "College Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping(value = "/colleges/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<College> getCollege(@Valid @Positive(message = "Invalid College Id") @PathVariable("id") Long id) {
		College college = collegeService.getCollege(id);
		return ResponseEntity.status(HttpStatus.OK).body(college);
	
	}
}
