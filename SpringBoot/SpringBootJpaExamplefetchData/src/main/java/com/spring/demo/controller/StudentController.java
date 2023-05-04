package com.spring.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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

	@RequestMapping("/getStudent")
	public ModelAndView getStudent(@RequestParam int sid) {
		ModelAndView mv = new ModelAndView("showstudent.jsp");
		Student student = repo.findById(sid).orElse(new Student());
		System.out.println(repo.findByMarks(800));
		System.out.println(repo.findBySidGreaterThan(530));
		System.out.println(repo.findBySnameSorted("anjali"));
		mv.addObject(student);
		return mv;
	}

}
