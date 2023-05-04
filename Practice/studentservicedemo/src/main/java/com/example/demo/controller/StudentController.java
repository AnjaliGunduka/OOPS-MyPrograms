package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Student;
import com.example.demo.feign.StudentFeignClient;

@RestController
@RequestMapping("/stu")
public class StudentController {

	@Autowired
	private StudentFeignClient studentFeignClient;

	@GetMapping(value = "/getstudents/{id}")
	public ResponseEntity<Student> getStudent(@PathVariable("id") int id) {
		Student student = studentFeignClient.getStudent(id);
		return ResponseEntity.status(HttpStatus.OK).body(student);
	}
	@GetMapping(value = "/students")
	public ResponseEntity<List<Student>> getStudents() {
		List<Student> student = studentFeignClient.getStudents();
		return ResponseEntity.status(HttpStatus.OK).body(student);
	}
}
