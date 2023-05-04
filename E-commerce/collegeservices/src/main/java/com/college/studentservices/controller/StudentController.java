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

import com.college.studentservices.entities.Student;
import com.college.studentservices.request.StudentRequest;
import com.college.studentservices.response.CollegeResponse;
import com.college.studentservices.response.CreateStudentResponse;
import com.college.studentservices.services.StudentService;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/collegeService/v1")
@Api(value = "Student Management", tags = { "Student Management" })
@Validated
public class StudentController {
	@Autowired
	
	StudentService studentService;
	
	@ApiOperation(value = "Creates an Student")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = CreateStudentResponse.class, message = "Student created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters")})
	@PostMapping(value = "/colleges/{collegeId}/students", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreateStudentResponse> createStudent(
			@Valid @Positive(message = "Invalid college Id") @PathVariable("collegeId") Long collegeId,
			@Valid @RequestBody StudentRequest studentRequest) {
		Student createdStudent = studentService.createStudent(studentRequest, collegeId);
		ModelMapper modelMapper = new ModelMapper();
		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(createdStudent, CreateStudentResponse.class));
	}
	
	@ApiOperation(value = "Gets an Student")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = CollegeResponse.class, message = "Student Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping(value = "/colleges/{collegeId}/students/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollegeResponse> getStudent(
			@Valid @Positive(message = "Invalid College Id") @PathVariable("collegeId") Long collegeId,
			@Valid @Positive(message = "Invalid Student Id") @PathVariable("studentId") Long studentId) {
		Student student = studentService.getStudent(collegeId, studentId);
		ModelMapper modelMapper = new ModelMapper();
		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(student, CollegeResponse.class));


		
		
	}
			
			
	
}
