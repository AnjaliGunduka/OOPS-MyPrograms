package com.student.library.services.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.library.services.client.BookServiceClient;

import com.student.library.services.entity.Student;

import com.student.library.services.service.AuthService;

import com.student.library.services.service.StudentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/studentService")
public class StudentInformationController {
	@Autowired
	StudentService studentService;
	@Autowired
	AuthService authService;
	@Autowired
	BookServiceClient bookServiceClient;

	@ApiOperation(value = "View All the Registered Students")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Student.class, message = " Student Details Fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@GetMapping("/getAllStudents")
	public List<Student> getAllStudents() {
		return bookServiceClient.getAllStudents(authService.getAuthToken());
	}

	@ApiOperation(value = "Gets Student")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Student.class, message = "Student Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@GetMapping(value = "/students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Student getStudent(@PathVariable("id") Long id) {
		return bookServiceClient.getStudent(authService.getAuthToken(), id);

	}

	@ApiOperation(value = "Update a Student")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Student.class, message = "Book Details updated  Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@PutMapping(value = "/updatestudents/{id}")
	public String updateStudent(@PathVariable Long id, @RequestBody Student student) {
		studentService.updateStudent(id, student);
		return "Student Updated Successfully";
	}

	@ApiOperation(value = "Student Login")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = String.class, message = "Logged In Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@PostMapping("/login/{userNameOremail}/{password}")
	public String login(@PathVariable("userNameOremail") String userNameOremail,
			@PathVariable("password") String password) {
		studentService.login(userNameOremail, password);
		return "Login Successfull";

	}

}
