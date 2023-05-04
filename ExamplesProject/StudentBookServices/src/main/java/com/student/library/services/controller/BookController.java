package com.student.library.services.controller;

import java.util.List;


import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.library.services.entity.Book;
import com.student.library.services.entity.RequestBook;
import com.student.library.services.entity.StudentBookIssue;
import com.student.library.services.exception.InvalidBookException;
import com.student.library.services.request.BookRequest;
import com.student.library.services.request.StudentBookIssueRequest;
import com.student.library.services.response.RequestBookResponse;
import com.student.library.services.response.StudentBookIssueResponse;
import com.student.library.services.service.BookService;
import com.student.library.services.service.RequestBookService;
import com.student.library.services.service.StudentBookIssueService;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/student-service")
public class BookController {
	@Autowired
	BookService bookService;

	@ApiOperation(value = "Gets a Book By bookName")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid Book bookName") })
	@GetMapping(value = "/books/{bookName}")
	public List<Book> getBookByBookName(@PathVariable String bookName) {
		return bookService.getBooksByBookName(bookName);
	}

	@ApiOperation(value = "View Availabale Books")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "All the Books Fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping("/books")
	public List<Book> findAllBooks() {
		return bookService.getAllBooks();
	}

	@ApiOperation(value = "Gets a Book By Id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid Book Id") })
	@GetMapping(value = "/books/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Book getBookById(@PathVariable Long id) {
		return bookService.getBookById(id);
	}

	@Autowired
	RequestBookService requestBookService;

	@ApiOperation(value = "Creates an Request for Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBookResponse.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PostMapping(value = "/students/{studentId}/requests")
	public ResponseEntity<RequestBookResponse> createRequestBook(@PathVariable Long studentId,
			@RequestBody BookRequest bookRequest) {
		RequestBook createRequestBook = requestBookService.createRequestBook(bookRequest, studentId);
		ModelMapper modelMapper = new ModelMapper();
		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(createRequestBook, RequestBookResponse.class));
	}

	@Autowired
	StudentBookIssueService studentBookIssueService;

//	@ApiOperation(value = "Return the book")
//	@ApiResponses(value = {
//			@ApiResponse(code = HttpServletResponse.SC_OK, response = StudentBookIssueResponse.class, message = "Book Returned Successfully"),
//			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
//	@PostMapping(value = "/issuebook/return")
//	public ResponseEntity<StudentBookIssueResponse> createStudentBookIssue(
//			@RequestBody StudentBookIssueRequest studentBookIssueRequest) throws InvalidBookException {
//		StudentBookIssue createdStudentBookIssue = studentBookIssueService.createStudentBook(studentBookIssueRequest);
//		ModelMapper modelMapper = new ModelMapper();
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(modelMapper.map(createdStudentBookIssue, StudentBookIssueResponse.class));
//	}
	
	@ApiOperation(value = "Issue Book to students who are requested")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = StudentBookIssueResponse.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	
	@PostMapping(value = "/books/{bookId}/viewsreturns")
	public ResponseEntity<StudentBookIssueResponse> createRequestBook(@PathVariable Long bookId,
			@RequestBody StudentBookIssueRequest bookRequest) throws InvalidBookException {
		StudentBookIssue createRequestBook = studentBookIssueService.createStudentBook(bookRequest, bookId);
		ModelMapper modelMapper = new ModelMapper();
		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(createRequestBook, StudentBookIssueResponse.class));
	}
	@ApiOperation(value = "search requestbook based on for requestid")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBookResponse.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping("/{id}")
	public ResponseEntity<RequestBook> findByRequestId(@PathVariable Long id) {

		return ResponseEntity.ok(requestBookService.getBookById(id));
	}
	
	@ApiOperation(value = "search requestbook based on for requestid and studentCardno")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBookResponse.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping("/fetch/{cardNo}/{id}")
    public RequestBook getRequestBookbasedOnIdAndCardNo(@PathVariable String cardNo,@PathVariable Long id) {
        return requestBookService.getRequestBooks(cardNo,id);
    }
	
	@ApiOperation(value = "View Issued Books")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBook.class, message = "All the Books Fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping("/viewallissued")
	public List<StudentBookIssue> findAllStudentBookIssueBooks() {
		return studentBookIssueService.getAllIssuedBooks();
	}
}
