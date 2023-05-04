package com.spring.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarksController {
	@Autowired
	private StudentService studentService;

	@PostMapping("/marks")
	public ResponseEntity<Student> studentMarksNow(@RequestBody Student student) {
		Student students=studentService.getStudent(student);
		return new ResponseEntity<>(students, HttpStatus.OK);
	}

}
