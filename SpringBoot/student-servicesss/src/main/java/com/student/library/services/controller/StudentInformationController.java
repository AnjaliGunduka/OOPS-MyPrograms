package com.student.library.services.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<Student>> getAllStudents() {
		List<Student> student = bookServiceClient.getAllStudents(authService.getAuthToken());
		return new ResponseEntity<List<Student>>(student, HttpStatus.OK);
	}

	@ApiOperation(value = "Gets Student")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Student.class, message = "Student Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@GetMapping(value = "/students/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Student> getStudent(@PathVariable("studentId") Long studentId) {
		Student getstudent = bookServiceClient.getStudent(authService.getAuthToken(), studentId);
		if (getstudent != null) {
			return new ResponseEntity<Student>(getstudent, HttpStatus.OK);
		}
		return new ResponseEntity<Student>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Update a Student")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Student.class, message = "Book Details updated  Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@PutMapping(value = "/updatestudents/{studentId}")
	public ResponseEntity<Student> updateStudent(@PathVariable Long studentId, @RequestBody Student student) {
		Student students = studentService.updateStudent(studentId, student);
		if (students != null) {
			return new ResponseEntity<Student>(students, HttpStatus.OK);
		}
		return new ResponseEntity<Student>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Student Login")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = String.class, message = "Logged In Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, response = String.class, message = "Invalid parameters") })
	@PostMapping("/login/{userNameOremail}/{password}")
	public ResponseEntity<String> login(@PathVariable("userNameOremail") String userNameOremail,
			@PathVariable("password") String password) {
		String student = studentService.login(userNameOremail, password);
		return new ResponseEntity<String>(student, HttpStatus.OK);
	}

}
