package com.library.service.book.mapper;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.library.service.book.entity.Student;
import com.library.service.book.request.StudentRequest;





@Service
public class StudentMapper {

	public Student mapCreateStudent(StudentRequest studentRequest) {
		return new Student(generateCardNumber(studentRequest.getUserName()), studentRequest.getUserName(),
				studentRequest.getPassword(),studentRequest.getBranch(), studentRequest.getSemester());
	}

	private String generateCardNumber(String cardNo) {
		return (String.valueOf(Math.abs(cardNo.hashCode())) + String.valueOf(Instant.now().getEpochSecond()))
				.substring(0, 5);
	}
}
