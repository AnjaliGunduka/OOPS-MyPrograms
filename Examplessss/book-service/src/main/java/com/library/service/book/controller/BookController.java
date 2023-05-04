package com.library.service.book.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.service.book.entity.Book;
import com.library.service.book.entity.RequestBook;
import com.library.service.book.enums.RequestStatus;
import com.library.service.book.exception.NoOfCopiesNotAvailableException;
import com.library.service.book.request.ProvideBookRequest;
import com.library.service.book.response.ProvideBookResponse;
import com.library.service.book.service.BookService;
import com.library.service.book.service.ProvideBookService;
import com.library.service.book.service.RequestBookService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Book Management", tags = { "Book Management" })
@RequestMapping(value = "/bookService")
public class BookController {
	@Autowired
	BookService bookService;

	@ApiOperation(value = "Creates a Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PostMapping(value = "/bookscreate")
	public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
		Book createBook = bookService.createBook(book);
		return ResponseEntity.status(HttpStatus.OK).body(createBook);
	}

	@ApiOperation(value = "Gets a Book By bookName")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid Book bookName") })
	@GetMapping(value = "/booksname/{bookName}")
	public ResponseEntity<Book> getBooksByBookName(@Valid @PathVariable String bookName) {
		Book book = bookService.getBooksByBookName(bookName);
		return ResponseEntity.status(HttpStatus.OK).body(book);
	}

	@ApiOperation(value = "View Available Books")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid Book bookName") })
	@GetMapping("/books")
	public ResponseEntity<List<Book>> getAllBooks() {
		List<Book> book = bookService.getAllBooks();
		return ResponseEntity.status(HttpStatus.OK).body(book);
	}

	@ApiOperation(value = "Gets a Book By Id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid Book Id") })
	@GetMapping(value = "/books/{id}")
	public ResponseEntity<Book> getBookById(@Valid @PathVariable Long id) {
		Book book = bookService.getBookById(id);
		return ResponseEntity.status(HttpStatus.OK).body(book);
	}

	@ApiOperation(value = "Update a Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details updated  Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PutMapping(value = "/updatebooks/{id}")
	public ResponseEntity<Book> updateBook(@Valid @PathVariable Long id, @RequestBody Book book) {
		Book updateBook = bookService.updateBook(book.getId(), book);
		return new ResponseEntity<Book>(updateBook, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete a Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details updated  Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@DeleteMapping(value = "/deletebooks/{id}")
	public String deleteBook(@Valid @PathVariable Long id) {
		bookService.getBookById(id);
		bookService.deleteBook(id);
		return "Book Deleted Successfully";
	}

	@ApiOperation(value = "Fetch Books")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details Successfully Fetched"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Something went wrong") })
	@GetMapping("/{bookName}/{numberOfCopies}")
	public ResponseEntity<List<Book>> getBooksByBookNameAndNoOfCopies(@Valid @PathVariable String bookName,
			@PathVariable int numberOfCopies) {
		List<Book> book = bookService.getBooksByBookNameAndNoOfCopies(bookName, numberOfCopies);
		return ResponseEntity.status(HttpStatus.OK).body(book);
	}

	@Autowired
	RequestBookService requestBookService;

	@ApiOperation(value = "View Requested Books")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBook.class, message = "All the Books Fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping("/requests/viewrequests")
	public ResponseEntity<List<RequestBook>> getAllRequestedBooks() {
		List<RequestBook> requestBook = requestBookService.getAllRequestedBooks();
		return ResponseEntity.status(HttpStatus.OK).body(requestBook);
	}

	@ApiOperation(value = "search books based on studentCardno")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBook.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping("/fetch/{cardNo}/viewdetails")
	public ResponseEntity<RequestBook> getRequestBooks(@Valid @PathVariable String cardNo) {
		RequestBook requestBook = requestBookService.getRequestBook(cardNo);
		return ResponseEntity.status(HttpStatus.OK).body(requestBook);
	}

	@ApiOperation(value = "search requestbook based on for requestid")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBook.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping("/{id}")
	public ResponseEntity<RequestBook> findByRequestId(@Valid @PathVariable Long id) {
		RequestBook requestBook = requestBookService.getRequestBook(id);
		return ResponseEntity.status(HttpStatus.OK).body(requestBook);
	}

	@Autowired
	ProvideBookService provideBookService;

	@ApiOperation(value = "Provide Book to student")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = ProvideBookResponse.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PostMapping(value = "/providebook")
	public ResponseEntity<ProvideBookResponse> createIssue(@Valid @RequestBody ProvideBookRequest provideBookRequest) throws NoOfCopiesNotAvailableException
			 {
		RequestBook requestBook = provideBookService.createIssue(provideBookRequest);
		ModelMapper modelMapper = new ModelMapper();
		ProvideBookResponse provideBookResponse = modelMapper.map(requestBook, ProvideBookResponse.class);
		return ResponseEntity.status(HttpStatus.OK).body(provideBookResponse);
	}

	@ApiOperation(value = "Gets a Book By Status")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid Book bookName") })
	@GetMapping(value = "/getBookStatus/{status}")
	public ResponseEntity<RequestBook> getBookStatus(@PathVariable RequestStatus status) {
		RequestBook requestBook = provideBookService.getBookStatus(status);
		return ResponseEntity.status(HttpStatus.OK).body(requestBook);
	}

}
