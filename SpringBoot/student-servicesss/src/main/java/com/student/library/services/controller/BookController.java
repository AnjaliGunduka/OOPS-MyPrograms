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
import com.student.library.services.exception.BookNotProvidedException;
import com.student.library.services.exception.RequestStatusException;
import com.student.library.services.request.ProvideBookRequest;
import com.student.library.services.request.RequestBookDto;
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
	public ResponseEntity<Book> getBooksByBookName(@PathVariable String bookName) {
		Book books = bookServiceClient.getBooksByBookName(authService.getAuthToken(), bookName);
		if (books != null) {
			return new ResponseEntity<Book>(books, HttpStatus.OK);
		}
		return new ResponseEntity<Book>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "View Availabale Books")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "All the Books Fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@GetMapping("/findAllBooks")
	public ResponseEntity<List<Book>> findAllBooks() {
		List<Book> books = bookServiceClient.getAllBooks(authService.getAuthToken());
		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);

	}

	@ApiOperation(value = "Gets a Book By Id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid Book Id"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@GetMapping(value = "/getBookById/{id}")
	public ResponseEntity<Book> getBookById(@Valid @Positive(message = "Invalid Book Id") @PathVariable Long id) {
		Book books = bookServiceClient.getBookById(authService.getAuthToken(), id);
		if (books != null) {
			return new ResponseEntity<Book>(books, HttpStatus.OK);
		}
		return new ResponseEntity<Book>(HttpStatus.BAD_REQUEST);
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
	public ResponseEntity<RequestBookResponse> createRequestBook(@Valid @RequestBody RequestBookDto bookRequest,
			@Valid @Positive(message = "Invalid Student Id") @PathVariable Long studentId)
			throws RequestStatusException {
		RequestBook createRequestBook = requestBookService.addRequestBook(bookRequest, studentId);
		ModelMapper modelMapper = new ModelMapper();
		if (createRequestBook != null) {
			return new ResponseEntity<RequestBookResponse>(
					modelMapper.map(createRequestBook, RequestBookResponse.class), HttpStatus.OK);
		}
		return new ResponseEntity<RequestBookResponse>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "search requestbook based on for requestid")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBookResponse.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@GetMapping("/{requestBookId}")

	public ResponseEntity<RequestBook> findByRequestId(@Valid @PathVariable Long requestBookId) {
		RequestBook requestBook = requestBookService.getRequestBook(requestBookId);
		if (requestBook != null) {
			return new ResponseEntity<RequestBook>(requestBook, HttpStatus.OK);
		}
		return new ResponseEntity<RequestBook>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "search requestbook based  studentCardno")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBookResponse.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@GetMapping("/getRequestBooks/{cardNo}")
	public ResponseEntity<RequestBook> getRequestBookByCardNo(@Valid @PathVariable String cardNo) {
		RequestBook requestBook = requestBookService.getRequestBooks(cardNo);
		if (requestBook != null) {
			return new ResponseEntity<RequestBook>(requestBook, HttpStatus.OK);
		}
		return new ResponseEntity<RequestBook>(HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "View Requested Books")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "All the Books Fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@GetMapping("/getAllRequestedBooks")
	public ResponseEntity<List<RequestBook>> getAllRequestedBooks() {
		List<RequestBook> requestBook = requestBookService.getAllRequestedBooks();

		return new ResponseEntity<List<RequestBook>>(requestBook, HttpStatus.OK);
	}

	@Autowired
	ProvideBookService studentBookIssueService;

	@ApiOperation(value = "Creates an return for Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = ProvideBookResponse.class, message = "Return for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@PostMapping(value = "/addReturnBook")
	public ResponseEntity<ProvideBookResponse> addReturnBook(@Valid @RequestBody ProvideBookRequest provideBookRequest)
			throws BookNotProvidedException {
		RequestBook requestBook = studentBookIssueService.addReturnBook(provideBookRequest);
		ModelMapper modelMapper = new ModelMapper();
		if (provideBookRequest != null) {
			return new ResponseEntity<ProvideBookResponse>(modelMapper.map(requestBook, ProvideBookResponse.class),
					HttpStatus.OK);

		}
		return new ResponseEntity<ProvideBookResponse>(HttpStatus.BAD_REQUEST);
	}

}
