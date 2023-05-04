package com.student.library.services.service;


import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.student.library.services.entity.Student;
import com.student.library.services.exception.Constants;
import com.student.library.services.exception.NotFoundException;
import com.student.library.services.mapper.StudentMapper;
import com.student.library.services.repository.StudentRepository;
import com.student.library.services.request.StudentRequest;

@Service
public class StudentService {
	
	@Autowired
	StudentMapper studentMapper;
	@Autowired
	StudentRepository studentRepository;
	
	private static final Logger log = LoggerFactory.getLogger(StudentService.class);

	public Student createStudent(StudentRequest studentRequest) {
		Student student = studentMapper.mapCreateStudent(studentRequest);	
		Student createdStudent = studentRepository.save(student);
		log.info("Student created");
		return createdStudent;
	}
	
	
	public Student getStudent(Long id) {
		Optional<Student> student = studentRepository.findById(id);
		if (!student.isPresent()) {
			throw new NotFoundException(Constants.STUDENT_NOT_FOUND);
		}
		return student.get();
	}


	
	

	
}
