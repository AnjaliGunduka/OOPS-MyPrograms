package com.studentcollege.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.studentcollege.cleint.CollegeClient;
import com.studentcollege.entity.Center;
import com.studentcollege.mapper.CenterMapper;
import com.studentcollege.repository.CenterRepository;
import com.studentcollege.request.CenterRequest;
import com.studentcollege.request.ExamRequest;
import com.studentcollege.response.ExamResponse;



@Service
public class CenterService {
	@Autowired
	CollegeClient collegeClient;

	@Autowired
	CenterRepository centerRepository;

	
	@Autowired
	CenterMapper centerMapper;
	
	private static final Logger log = LoggerFactory.getLogger(CenterService.class);

	public Center makeCenter(CenterRequest centerRequest )
	{
		ModelMapper modelMapper = new ModelMapper();
		ExamRequest examRequest = modelMapper.map(centerRequest, ExamRequest.class);
		log.info("Center initiated");
		try {
			ExamResponse examResponse = collegeClient.adding(examRequest);
			log.info("Center from College student");
			Center center=centerMapper.mapExamResponseToCenter(examResponse,centerRequest.getExamCenterName());
			return centerRepository.save(center);
	}catch (Exception e) {
		e.printStackTrace();
	}
		return null;
	
	
	}
	
	
}
