package com.library.service.book.mapper;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.library.service.book.entity.Student;
import com.library.service.book.request.StudentRequest;

@Service
public class StudentMapper {
	/**
	 * Maps Create studentRequest to student
	 * 
	 * @param studentRequest
	 * @return
	 */

	public Student mapCreateStudent(StudentRequest studentRequest) {
		return new Student(generateCardNumber(studentRequest.getUserName()), studentRequest.getUserName(),
				studentRequest.getPassword(), studentRequest.getBranch(), studentRequest.getSemester(),studentRequest.getEmail());
	}

	/**
	 * Generates Card Number based userName and Current time
	 * 
	 * @param userName
	 * @return
	 */

	private String generateCardNumber(String userName) {
		return (String.valueOf(Math.abs(userName.hashCode())) + String.valueOf(Instant.now().getEpochSecond()))
				.substring(0, 5);
	}
}
