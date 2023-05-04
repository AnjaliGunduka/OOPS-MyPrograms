package com.studentcollege.mapper;

import java.time.Instant;

import org.springframework.stereotype.Service;


import com.studentcollege.entity.Center;
import com.studentcollege.response.ExamResponse;





@Service
public class CenterMapper {

	public Center mapExamResponseToCenter(ExamResponse examResponse, String examCenterName) {
	return new Center(examResponse.getStudentNumber(),examResponse.getStudentName() , 
			examCenterName,examResponse.getStudentName(),examResponse.getExamCenter(),Instant.now());	
}
	
}
