package com.college.studentservices.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.college.studentservices.Repositories.CollegeRepository;

import com.college.studentservices.common.Constants;
import com.college.studentservices.entities.College;
import com.college.studentservices.entities.Status;

import com.college.studentservices.exceptions.InactiveCollegeException;

import com.college.studentservices.request.ExamRequest;

@Service
public class ExamService {
	@Autowired	
	CollegeService collegeService;
    
	
	@Autowired
	CollegeRepository collegeRepository;
	
	private static final Logger log = LoggerFactory.getLogger(ExamService.class);

	public College adding(ExamRequest examRequest)
	{
		College  college = collegeService.getCollege(examRequest.getCollegeId());

		checkCollegeActive(college);
		college.setExamCenter(college.getExamCenter());
		log.info("Adding to College Student");
		return collegeRepository.save(college);
	}
	private void checkCollegeActive(College college) {
		if (college.getStatus() == Status.INACTIVE) {
			throw new InactiveCollegeException(Constants.COLLEGE_NOT_FOUND);
		}
	}

}
