package com.student.library.services.controller;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.library.services.entity.Student;
import com.student.library.services.request.StudentRequest;
import com.student.library.services.response.StudentResponse;
import com.student.library.services.service.StudentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/studentService")
public class StudentController {
	@Autowired
	StudentService studentService;

	@ApiOperation(value = "Creates an Student")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = StudentResponse.class, message = "Student created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PostMapping(value = "/students")
	public StudentResponse createStudent(@RequestBody StudentRequest studentRequest) {
		Student createdStudent = studentService.createStudent(studentRequest);
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(createdStudent, StudentResponse.class);
	}

	@ApiOperation(value = "Gets Student")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Student.class, message = "Student Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping(value = "/students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Student> getStudent(@PathVariable("id") Long id) {
		Student college = studentService.getStudent(id);
		return ResponseEntity.status(HttpStatus.OK).body(college);

	}

}
