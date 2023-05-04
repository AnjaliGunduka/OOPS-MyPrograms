package com.college.studentservices.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.college.studentservices.Repositories.StudentRepository;
import com.college.studentservices.common.Constants;
import com.college.studentservices.entities.College;
import com.college.studentservices.entities.Student;
import com.college.studentservices.exceptions.NotFoundException;
import com.college.studentservices.mapper.StudentMapper;
import com.college.studentservices.request.StudentRequest;







@Service
public class StudentService {
	@Autowired
	CollegeService collegeService;
	@Autowired
	StudentMapper studentMapper;
	@Autowired
	StudentRepository studentRepository;
	
	private static final Logger log = LoggerFactory.getLogger(StudentService.class);

	public Student createStudent(StudentRequest studentRequest, Long collegeId) {

		College college = collegeService.getCollege(collegeId);
		Student student = studentMapper.mapCreateStudentRequestToStudent(studentRequest, college);
		log.info("Creating College Account");
		Student createdStudent = studentRepository.save(student);
		log.info("Collge Student created");
		return createdStudent;
	}
	public Student getStudent(Long collegeId,Long studentId)
	{
		College college=collegeService.getCollege(collegeId);
		Optional<Student> student=studentRepository.findByIdAndCollege(studentId, college);
		if(!student.isPresent())
		{
			throw new NotFoundException(Constants.STUDENT_NOT_FOUND);
		}
		return student.get();
	}
	
	

	
}
