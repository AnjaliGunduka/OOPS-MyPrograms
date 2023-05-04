package com.student.library.services.mapper;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.student.library.services.entity.Student;
import com.student.library.services.request.StudentRequest;

@Service
public class StudentMapper {

	public Student mapCreateStudent(StudentRequest studentRequest) {
		return new Student(generateCardNumber(studentRequest.getUserName()), studentRequest.getUserName(),
				studentRequest.getPassword(),studentRequest.getBranch(), studentRequest.getSemester(),studentRequest.getEmail());
	}

	private String generateCardNumber(String cardNo) {
		return (String.valueOf(Math.abs(cardNo.hashCode())) + String.valueOf(Instant.now().getEpochSecond()))
				.substring(0, 5);
	}
}
