package com.student.library.services.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

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

import com.student.library.services.client.BookServiceClient;
import com.student.library.services.entity.Book;
import com.student.library.services.entity.RequestBook;
import com.student.library.services.request.BookRequest;
import com.student.library.services.request.ProvideBookRequest;
import com.student.library.services.response.ProvideBookResponse;
import com.student.library.services.response.RequestBookResponse;
import com.student.library.services.service.AuthService;

import com.student.library.services.service.ProvideBookService;
import com.student.library.services.service.RequestBookService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/studentService")
public class BookController {

	@Autowired
	AuthService authService;
	@Autowired
	BookServiceClient bookServiceClient;

	@ApiOperation(value = "Gets a Book By bookName")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid Book bookName"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@GetMapping(value = "/getBooksByBookName/{bookName}")
	public Book getBooksByBookName(@PathVariable String bookName) {
		return bookServiceClient.getBooksByBookName(authService.getAuthToken(), bookName);
	}

	@ApiOperation(value = "View Availabale Books")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "All the Books Fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@GetMapping("/findAllBooks")
	public List<Book> findAllBooks() {
		return bookServiceClient.getAllBooks(authService.getAuthToken());
	}

	@ApiOperation(value = "Gets a Book By Id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid Book Id"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@GetMapping(value = "/getBookById/{id}")
	public Book getBookById(@Valid @Positive(message = "Invalid Book Id") @PathVariable Long id) {
		return bookServiceClient.getBookById(authService.getAuthToken(), id);
	}

	@Autowired
	RequestBookService requestBookService;

	@ApiOperation(value = "Creates an Request for Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBookResponse.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@PostMapping(value = "/students/{studentId}/requests")
	public ResponseEntity<RequestBookResponse> createRequestBook(
			@Valid @Positive(message = "Invalid Student Id") @PathVariable Long studentId,
			@Valid @RequestBody BookRequest bookRequest) {
		RequestBook createRequestBook = requestBookService.createRequestBook(bookRequest, studentId);
		ModelMapper modelMapper = new ModelMapper();
		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(createRequestBook, RequestBookResponse.class));
	}

	@ApiOperation(value = "search requestbook based on for requestid")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBookResponse.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@GetMapping("/{requestBookId}")
	public ResponseEntity<RequestBook> findByRequestId(@Valid @PathVariable Long requestBookId) {

		return ResponseEntity.ok(requestBookService.getRequestBook(requestBookId));
	}

	@ApiOperation(value = "search requestbook based  studentCardno")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBookResponse.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@GetMapping("/getRequestBooks/{cardNo}")
	public RequestBook getRequestBooks(@Valid @PathVariable String cardNo) {
		return requestBookService.getRequestBooks(cardNo);
	}

	@ApiOperation(value = "View Requested Books")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "All the Books Fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@GetMapping("/getAllRequestedBooks")
	public List<RequestBook> getAllRequestedBooks() {
		return requestBookService.getAllRequestedBooks();
	}

	@Autowired
	ProvideBookService studentBookIssueService;

	@ApiOperation(value = "Creates an return for Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = ProvideBookResponse.class, message = "Return for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@PostMapping(value = "/books/{bookId}/createReturn")
	public ResponseEntity<ProvideBookResponse> createReturn(@Valid @RequestBody ProvideBookRequest provideBookRequest) {
		RequestBook requestBook = studentBookIssueService.createReturn(provideBookRequest);
		ModelMapper modelMapper = new ModelMapper();
		ProvideBookResponse studentBookIssueResponse = modelMapper.map(requestBook, ProvideBookResponse.class);
		return ResponseEntity.status(HttpStatus.OK).body(studentBookIssueResponse);
	}

}
