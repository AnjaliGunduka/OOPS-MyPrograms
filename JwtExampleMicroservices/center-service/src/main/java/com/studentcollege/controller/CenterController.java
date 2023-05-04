package com.studentcollege.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentcollege.entity.Center;
import com.studentcollege.request.CenterRequest;
import com.studentcollege.response.CenterResponse;
import com.studentcollege.service.CenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/centerService/v1")
@Api(value = "Center Management", tags = { "Center Management" })
@Validated
public class CenterController {
	@Autowired
	CenterService centerService;
	
	@ApiOperation(value = "Makes Center")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = CenterResponse.class, message = "Center Successful"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters")})
	
	@PostMapping(value = "/centers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CenterResponse> makeCenter(@Valid @RequestBody CenterRequest centerRequest){
	Center center = centerService.makeCenter(centerRequest);
	ModelMapper modelMapper = new ModelMapper();
	return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(center, CenterResponse.class));
	}


	
	
}
