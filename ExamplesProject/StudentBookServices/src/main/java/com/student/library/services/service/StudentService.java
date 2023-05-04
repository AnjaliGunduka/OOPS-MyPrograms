package com.student.library.services.service;


import java.util.List;

import java.util.stream.Collectors;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.student.library.services.entity.Student;
import com.student.library.services.exception.Constants;
import com.student.library.services.exception.NotFoundException;
import com.student.library.services.repository.StudentRepository;




@Service
public class StudentService {
	
	@Autowired
	StudentRepository studentRepository;
	
	private static final Logger log = LoggerFactory.getLogger(StudentService.class);

	@Autowired
	private JPAStreamer jpaStreamer;
	
	/**
	 * Get All Students
	 * 
	 * @return
	 */
	public List<Student> getAllStudents() {
		return jpaStreamer.stream(Student.class).collect(Collectors.toList());
	}

	public void updateStudent(Long id, Student updateStudent) {
		log.info("Student updated");
		studentRepository.save(updateStudent);
	}
	public Student getStudent(Long id) {
		return jpaStreamer.stream(Student.class).filter(student -> student.getId().equals(id)).findFirst()
				.orElseThrow(() -> new NotFoundException(Constants.STUDENT_NOT_FOUND));
	}

	
}
