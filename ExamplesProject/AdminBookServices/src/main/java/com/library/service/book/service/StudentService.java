package com.library.service.book.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.service.book.entity.Student;
import com.library.service.book.exception.Constants;
import com.library.service.book.exception.NotFoundException;
import com.library.service.book.mapper.StudentMapper;
import com.library.service.book.repository.StudentRepository;
import com.library.service.book.request.StudentRequest;
import com.speedment.jpastreamer.application.JPAStreamer;

@Service
public class StudentService {
	@Autowired
	StudentMapper studentMapper;
	@Autowired
	private JPAStreamer jpaStreamer;
	@Autowired
	StudentRepository studentRepository;
	private static final Logger log = LoggerFactory.getLogger(StudentService.class);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Student getStudent(Long id) {
		return jpaStreamer.stream(Student.class).filter(student -> student.getId().equals(id)).findFirst()
				.orElseThrow(() -> new NotFoundException(Constants.STUDENT_NOT_FOUND));
	}

	public Student createStudent(StudentRequest studentRequest) {
		Student student = studentMapper.mapCreateStudent(studentRequest);
		Student createdStudent = studentRepository.save(student);
		log.info("Student created");
		return createdStudent;
	}

	/**
	 * Get All Students
	 * 
	 * @return
	 */
	public List<Student> getAllStudents() {
		return jpaStreamer.stream(Student.class).collect(Collectors.toList());
	}

}
