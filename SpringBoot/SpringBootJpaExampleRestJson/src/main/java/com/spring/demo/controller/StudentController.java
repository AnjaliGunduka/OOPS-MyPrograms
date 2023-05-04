package com.spring.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.demo.dao.StudentRepo;
import com.spring.demo.model.Student;

@Controller
public class StudentController {
	@Autowired
	StudentRepo repo;

	@RequestMapping("/")
	public String home() {
		return "home.jsp";
	}

	@RequestMapping("/addStudent")
	public String addStudent(Student student) {
		repo.save(student);
		return "home.jsp";
	}
	@RequestMapping("/students")
	@ResponseBody
	public List<Student> getStudents() {

		return repo.findAll();
	}
//	@RequestMapping("/student/534")
//	@ResponseBody
//	public String getStudent() {
//
//		return repo.findById(534).toString();
//	}
	
	@RequestMapping("/student/{sid}")
	@ResponseBody
	public Optional<Student> getStudent(@PathVariable("sid")int sid) {

		return repo.findById(sid);
	}
}
