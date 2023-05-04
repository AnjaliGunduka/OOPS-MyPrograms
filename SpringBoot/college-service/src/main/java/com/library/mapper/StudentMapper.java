package com.library.mapper;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.library.entities.Student;
import com.library.request.StudentRequest;

@Service
public class StudentMapper {

	public Student mapCreateStudentRequestToStudent(StudentRequest studentRequest) {
		return new Student(generatecardNo(studentRequest.getSemester()), studentRequest.getFirstName(),
				studentRequest.getLastName(), studentRequest.getUserName(), studentRequest.getPassword(),
				studentRequest.getSemester(), studentRequest.getBranch());
	}

	private String generatecardNo(String semester) {
		return (String.valueOf(Math.abs(semester.hashCode())) + String.valueOf(Instant.now().getEpochSecond())
				.substring(0, 3));
	}

}
