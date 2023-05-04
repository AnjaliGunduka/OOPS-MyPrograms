package com.student.library.services.controller;


import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.library.services.entity.StudentBookIssue;
import com.student.library.services.exception.InvalidBookException;
import com.student.library.services.request.StudentBookIssueRequest;
import com.student.library.services.response.StudentBookIssueResponse;
import com.student.library.services.service.StudentBookIssueService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping(value = "/studentService/returnbooks")
public class ReturnBookController {
	@Autowired
	StudentBookIssueService studentBookIssueService;
	@ApiOperation(value = "Return the book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response =  StudentBookIssueResponse.class, message = "Book Returned Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PostMapping(value = "/issuebook/return")
	public ResponseEntity<StudentBookIssueResponse> createStudentBookIssue(@RequestBody StudentBookIssueRequest studentBookIssueRequest) throws InvalidBookException {
		StudentBookIssue  createdStudentBookIssue= studentBookIssueService.createStudentBook(studentBookIssueRequest);
		ModelMapper modelMapper = new ModelMapper();
		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(createdStudentBookIssue,  StudentBookIssueResponse.class));
	}
	
	
}
