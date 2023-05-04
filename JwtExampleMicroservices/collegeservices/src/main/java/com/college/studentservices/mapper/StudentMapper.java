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
		return new Student(generateStudentNumber(college.getCode(),studentRequest.getExamcenterId()) , 
				studentRequest.getType(),Status.valueOf(studentRequest.getStatus()),
				studentRequest.getExamCenter(),		studentRequest.getStudentName(),studentRequest.getExamcenterId(),studentRequest.getCollegeCode(),college);
	}
	private String generateStudentNumber(String collegeCode, String examcenterId)
	{
		return (String.valueOf(Math.abs(collegeCode.hashCode())) + String.valueOf(Math.abs(examcenterId.hashCode()))
		+ String.valueOf(Instant.now().getEpochSecond())).substring(0, 15);
	}
	
	
	
	
}
