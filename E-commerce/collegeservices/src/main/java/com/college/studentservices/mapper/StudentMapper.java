package com.college.studentservices.mapper;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.college.studentservices.entities.College;
import com.college.studentservices.entities.Status;
import com.college.studentservices.entities.Student;
import com.college.studentservices.request.StudentRequest;

@Service
public class StudentMapper {

	public Student mapCreateStudentRequestToStudent(StudentRequest studentRequest, College college) {
		return new Student(college.getCode(), studentRequest.getType(), Status.valueOf(studentRequest.getStatus()),
				studentRequest.getStudentName(), college);
	}

}
