package com.library.controller;

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

import com.library.entities.Student;
import com.library.request.StudentRequest;
import com.library.response.CreateStudentResponse;
import com.library.response.StudentResponse;
import com.library.services.StudentService;

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
	@PostMapping(value = "/students", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreateStudentResponse> createStudent(
			@Valid @RequestBody StudentRequest studentRequest) {
		Student createdStudent = studentService.createStudent(studentRequest);
		ModelMapper modelMapper = new ModelMapper();
		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(createdStudent, CreateStudentResponse.class));
	}
	
	@ApiOperation(value = "Gets an Student")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = StudentResponse.class, message = "Student Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping(value = "/students/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StudentResponse> getStudent(
			@Valid @Positive(message = "Invalid Student Id") @PathVariable("studentId") Long studentId) {
		Student student = studentService.getStudent( studentId);
		ModelMapper modelMapper = new ModelMapper();
		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(student, StudentResponse.class));


		
		
	}
			
			
	
}
