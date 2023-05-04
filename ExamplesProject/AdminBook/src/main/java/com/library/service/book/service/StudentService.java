package com.library.service.book.service;


import java.util.List;

import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.service.book.entity.Student;
import com.speedment.jpastreamer.application.JPAStreamer;


@Service
public class StudentService {
	
	@Autowired
	private JPAStreamer jpaStreamer;
	/**
	 * Get All Students
	 * @return
	 */
	public List<Student> getAllStudents() {
	return jpaStreamer.stream(Student.class).collect(Collectors.toList());
	}


	
	

	
}
