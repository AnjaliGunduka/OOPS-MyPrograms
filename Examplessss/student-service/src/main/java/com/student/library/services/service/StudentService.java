package com.student.library.services.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.library.services.entity.Student;
import com.student.library.services.exception.Constants;
import com.student.library.services.exception.NotFoundException;
import com.student.library.services.repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	StudentRepository studentRepository;

	private static final Logger log = LoggerFactory.getLogger(StudentService.class);

	public void updateStudent(Long id, Student updateStudent) {
		log.info("Student updated");
		studentRepository.save(updateStudent);
	}

	public String login(String userNameOremail, String password) {

		if (userNameOremail.endsWith(".com")) {
			Student student = studentRepository.findByEmail(userNameOremail);
			if (student.getEmail().equals(userNameOremail) && student.getPassword().equals(password)) {
			} else {
				throw new NotFoundException(Constants.STUDENT_NOT_FOUND);
			}
		} else {
			Student user = studentRepository.findByUserName(userNameOremail);
			if (user.getUserName().equals(userNameOremail) && user.getPassword().equals(password)) {
			} else {
				throw new NotFoundException(Constants.STUDENT_NOT_FOUND);
			}
		}
		return "Login Successfull";
	}

}
