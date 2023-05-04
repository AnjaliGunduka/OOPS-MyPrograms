package com.library.service.book.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.service.book.entity.Student;
import com.library.service.book.exception.Constants;
import com.library.service.book.exception.NotFoundException;
import com.library.service.book.mapper.StudentMapper;
import com.library.service.book.repository.StudentRepository;
import com.library.service.book.request.StudentRequest;

@Service
@Transactional
public class StudentService {
	@Autowired
	StudentMapper studentMapper;
	@Autowired
	StudentRepository studentRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	private static final Logger log = LoggerFactory.getLogger(StudentService.class);

	/**
	 * search based on id
	 * 
	 * @param id
	 * @return
	 */
	public Student getStudent(Long id) {
		log.info("Get Student By Id");
		return studentRepository.findById(id).orElseThrow(() -> new NotFoundException(Constants.STUDENT_NOT_FOUND));
	}

	/**
	 * Creates Student and saves into the Map
	 * 
	 * @param studentRequest
	 * @return
	 * @throws Exception
	 */
	public Student createStudent(StudentRequest studentRequest) throws Exception {
		Student student = studentMapper.mapCreateStudent(studentRequest);
		if (userNameExists(student.getUserName())) {
			throw new Exception("There is an account with that same Username:" + student.getUserName());
		}
		student.setPassword(passwordEncoder.encode(student.getPassword()));
		log.info("Student created");
		Student createdStudent = studentRepository.save(student);
		return createdStudent;
	}

	public Boolean userNameExists(final String username) {
		return studentRepository.findByuserName(username) != null;
	}

	/**
	 * Get All Students
	 * 
	 * @return
	 */

	public List<Student> getAllStudents() {
		log.info("Get all Students");
		return studentRepository.findAll();
	}

}
