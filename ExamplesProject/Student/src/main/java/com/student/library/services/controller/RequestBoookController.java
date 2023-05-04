package com.student.library.services.controller;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.library.services.entity.RequestBook;
import com.student.library.services.request.BookRequest;
import com.student.library.services.response.RequestBookResponse;
import com.student.library.services.service.RequestBookService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/student-service")
public class RequestBoookController {
	@Autowired
	RequestBookService requestBookService;

	@ApiOperation(value = "Creates an Request for Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBookResponse.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PostMapping(value = "/students/{studentId}/requests")
	public ResponseEntity<RequestBookResponse> createRequestBook(@PathVariable Long studentId, @RequestBody BookRequest bookRequest) {
		RequestBook createRequestBook = requestBookService.createRequestBook(bookRequest, studentId);
		ModelMapper modelMapper = new ModelMapper();
		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(createRequestBook, RequestBookResponse.class));
	}
}
