package com.wavelabs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wavelabs.model.Student;
import com.wavelabs.service.StudentService;

@Controller("/SpringMVC_HibernateCRUD")
public class StudentController {
	@Autowired
	StudentService studentService;

	@RequestMapping(value = "/getAllStudents")
	public String getCountries(Model model) {

		List<Student> listOfStudents = studentService.getAllStudents();
		model.addAttribute("student", new Student());
		model.addAttribute("listOfStudents", listOfStudents);
		return "studentDetails";
	}

	@RequestMapping(value = "/editsave", method = RequestMethod.POST)
	public String editsave(@ModelAttribute("student") Student student) {
		studentService.updateStudent(student);
		return "redirect:/getAllStudents";
	}

	@RequestMapping(value = "/addStudent")
	public String addstudent(@ModelAttribute("student") Student student) {
		studentService.addStudent(student);
		return "redirect:/getAllStudents";
	}

	@RequestMapping(value = "/updatestudent/{studId}")
	public String updatestudent(@PathVariable String studId, Model model) {
		Student student = studentService.getStudent(studId);
		model.addAttribute("student", student);
		return "editStudent";
	}

	@RequestMapping(value = "/deletestudent/{studId}", method = RequestMethod.GET)
	public String deletestudent(@PathVariable String studId) {
		studentService.deleteStudent(studId);
		return "redirect:/getAllStudents";

	}

	@RequestMapping("/studentform")
	public String showform(Model m) {
		m.addAttribute("command", new Student());
		return "addStudent";
	}
}
