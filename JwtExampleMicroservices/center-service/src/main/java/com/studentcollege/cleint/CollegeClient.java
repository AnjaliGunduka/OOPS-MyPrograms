package com.studentcollege.cleint;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.studentcollege.request.ExamRequest;
import com.studentcollege.response.ExamResponse;



@FeignClient(url = "${collegeservices.domain}", name = "COLLEGE-CLIENT")
public interface CollegeClient {
	@RequestMapping(value = "/collegeService/v1/exams/adding", method = RequestMethod.POST, consumes = "application/json")
	public ExamResponse adding(@RequestBody ExamRequest examRequest);

}
