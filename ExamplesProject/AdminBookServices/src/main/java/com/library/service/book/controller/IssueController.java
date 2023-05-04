package com.library.service.book.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.service.book.entity.StudentBookIssue;
import com.library.service.book.request.StudentBookIssueRequest;
import com.library.service.book.response.StudentBookIssueResponse;
import com.library.service.book.service.StudentBookIssueService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@RestController
@RequestMapping(value = "/student-service")
public class IssueController {
	@Autowired
	StudentBookIssueService studentBookIssueService;
	
	@ApiOperation(value = "Creates an Request for Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = StudentBookIssueResponse.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PostMapping(value = "/books/{bookId}/viewsrequests")
	public ResponseEntity<StudentBookIssueResponse> createRequestBook(@PathVariable Long bookId,
			@RequestBody StudentBookIssueRequest bookRequest) {
		StudentBookIssue createRequestBook = studentBookIssueService.createRequestBook(bookRequest, bookId);
		ModelMapper modelMapper = new ModelMapper();
		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(createRequestBook, StudentBookIssueResponse.class));
	}
	@ApiOperation(value = "View Availabale Books")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = StudentBookIssue.class, message = "All the Books Fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping("/getAllrequested")
	public List<StudentBookIssue> findAllBooks() {
		return studentBookIssueService.getAllRequestedBooks();
	}
}
