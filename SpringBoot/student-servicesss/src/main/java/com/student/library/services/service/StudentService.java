package com.student.library.services.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.student.library.services.entity.Student;
import com.student.library.services.exception.Constants;
import com.student.library.services.exception.NotFoundException;
import com.student.library.services.repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	StudentRepository studentRepository;
	
	private static final Logger log = LoggerFactory.getLogger(StudentService.class);

	/**
	 * This method is used to Update Student based on id
	 * 
	 * @param id
	 * @param student
	 * @return
	 */
	
	@Transactional
	public Student updateStudent(Long id, Student student) {
		log.info("Student updated");
		Student updateStudent = studentRepository.getStudentById(id);
		updateStudent.setId(id);
		updateStudent.setBranch(student.getBranch());
		updateStudent.setEmail(student.getEmail());
		updateStudent.setUserName(student.getUserName());
		return studentRepository.save(updateStudent);

	}

	/**
	 * This method is used to Login Student based on userNameOremail
	 * 
	 * @param userNameOremail
	 * @param password
	 * @return
	 */
	public String login(String userNameOremail, String password) {
		log.info("Login successful");
		if (userNameOremail.endsWith(".com")) {
			Student student = studentRepository.findByEmail(userNameOremail);
			if (student.getEmail().equals(userNameOremail) && student.getPassword().equals(password)) {
			} else {
				throw new NotFoundException(Constants.STUDENT_NOT_FOUND);
			}
		} else {
			Student student = studentRepository.findByUserName(userNameOremail);
			if (student.getUserName().equals(userNameOremail) && student.getPassword().equals(password)) {
			} else {
				throw new NotFoundException(Constants.STUDENT_NOT_FOUND);
			}
		}
		return "Login successful";
	}

}
