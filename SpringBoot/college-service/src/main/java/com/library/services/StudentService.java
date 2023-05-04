package com.library.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.Repositories.StudentRepository;
import com.library.common.Constants;
import com.library.entities.Student;
import com.library.exceptions.NotFoundException;
import com.library.mapper.StudentMapper;
import com.library.request.StudentRequest;

@Service
public class StudentService {
	
	@Autowired
	StudentMapper studentMapper;
	@Autowired
	StudentRepository studentRepository;
	
	private static final Logger log = LoggerFactory.getLogger(StudentService.class);

	public Student createStudent(StudentRequest studentRequest) {
		Student student = studentMapper.mapCreateStudentRequestToStudent(studentRequest);
		log.info("Creating College Account");
		Student createdStudent = studentRepository.save(student);
		log.info("Collge Student created");
		return createdStudent;
	}
	public Student getStudent(Long studentId)
	{
		
		Optional<Student> student=studentRepository.findById(studentId);
		if(!student.isPresent())
		{
			throw new NotFoundException(Constants.STUDENT_NOT_FOUND);
		}
		return student.get();
	}
	
	

	
}
