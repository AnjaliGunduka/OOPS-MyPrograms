package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	
	@PostMapping(value = "/addStudent", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Student> Student(@RequestBody Student request) {
		Student response = studentService.createStudent(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(value = "/student/{email}")
	public ResponseEntity<Optional<Student>> getStudent(@PathVariable("email") String email) {
		Optional<Student> student = studentService.getStudent(email);
		return ResponseEntity.status(HttpStatus.OK).body(student);
	}

	@DeleteMapping(value = "/student/{id}")
	public ResponseEntity<Optional<Student>> deleteStudent(@PathVariable("id") int id) {
		Optional<Student> student = studentService.deleteStudent(id);
		return ResponseEntity.status(HttpStatus.OK).body(student);
	}
	
	
	
}
