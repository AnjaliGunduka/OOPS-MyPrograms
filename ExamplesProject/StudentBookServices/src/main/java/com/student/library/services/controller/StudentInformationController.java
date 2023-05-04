package com.student.library.services.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.library.services.entity.Student;

import com.student.library.services.service.StudentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/student-service")
public class StudentInformationController {
	@Autowired
	StudentService studentService;
	@ApiOperation(value = "View All the Registered Students")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Student.class, message = " Student Details Fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping("/viewstudents")
	public List<Student> findAllStudents() {
		return studentService.getAllStudents();
	}

	
	@ApiOperation(value = "Gets Student")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Student.class, message = "Student Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping(value = "/students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Student> getStudent(@PathVariable("id") Long id) {
		Student student = studentService.getStudent(id);
		return ResponseEntity.status(HttpStatus.OK).body(student);

	}
	@ApiOperation(value = "Update a Student")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Student.class, message = "Book Details updated  Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PutMapping(value = "/books/{id}")
	public String updateStudent(@PathVariable Long id, @RequestBody Student student) {
		studentService.updateStudent(id, student);
		return  "Book Updated Successfully";
	}
}
