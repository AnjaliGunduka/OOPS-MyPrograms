package com.college.studentservices.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.college.studentservices.Repositories.StudentRepository;
import com.college.studentservices.common.Constants;
import com.college.studentservices.entities.Status;
import com.college.studentservices.entities.Student;
import com.college.studentservices.exceptions.InactiveStudentException;
import com.college.studentservices.request.ExamRequest;

@Service
public class ExamService {
	@Autowired	
	CollegeService collegeService;
    
	@Autowired
	StudentService studentService;
	@Autowired
	StudentRepository studentRepository;
	
	private static final Logger log = LoggerFactory.getLogger(ExamService.class);

	public Student adding(ExamRequest examRequest)
	{
		Student student = studentService.getStudent(examRequest.getCollegeId(), examRequest.getStudentId());

		checkStudentActive(student);
		student.setStudentName(student.getStudentName());
		log.info("Adding to College Student");
		return studentRepository.save(student);
	}
	private void checkStudentActive(Student student) {
		if (student.getStatus() == Status.INACTIVE) {
			throw new InactiveStudentException(Constants.COLLEGE_NOT_FOUND);
		}
	}
	
}
