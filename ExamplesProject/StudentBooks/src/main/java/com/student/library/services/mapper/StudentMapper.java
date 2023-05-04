package com.student.library.services.mapper;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.student.library.services.entity.Student;
import com.student.library.services.request.StudentRequest;

@Service
public class StudentMapper {

	public Student mapCreateStudent(StudentRequest studentRequest) {
		return new Student(generateStudentNumber(studentRequest.getBranch()), studentRequest.getUserName(),
				studentRequest.getPassword(),studentRequest.getBranch(), studentRequest.getSemester());
	}

	private String generateStudentNumber(String branch) {
		return (String.valueOf(Math.abs(branch.hashCode())) + String.valueOf(Instant.now().getEpochSecond()))
				.substring(0, 5);
	}
}
