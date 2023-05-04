package com.library.service.book.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.library.service.book.entity.StudentBookIssue;
import com.library.service.book.enums.RequestedStudent;
import com.library.service.book.exception.InsufficientNoOfCopiesException;
import com.library.service.book.request.StudentBookIssueRequest;
import com.library.service.book.response.StudentBookIssueResponse;
import com.library.service.book.service.BookService;
import com.library.service.book.service.RequestBookService;
import com.library.service.book.service.StudentBookIssueService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/library-service")
public class BookController {
	@Autowired
	BookService bookService;

	@ApiOperation(value = "Creates a Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PostMapping(value = "/bookscreate")
	public Book createBook(@RequestBody Book book) {
		return bookService.createBook(book);
	}

	@ApiOperation(value = "Gets a Book By bookName")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid Book bookName") })
	@GetMapping(value = "/books/{bookName}")
	public List<Book> getBookByBookName(@Valid  @PathVariable String bookName) {
		return  bookService.getBooksByBookName(bookName);
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
	public Book getBookById(@PathVariable Long id)   {
		return bookService.getBookById(id);
	}

	@ApiOperation(value = "Update a Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details updated  Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PutMapping(value = "/books/{id}")
	public String updateBook(@PathVariable Long id, @RequestBody Book book) {
		bookService.updateBook(id, book);
		return  "Book Updated Successfully";
	}

	@ApiOperation(value = "Delete a Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details updated  Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@DeleteMapping(value = "/books/{id}")
	public String deleteBook(@PathVariable Long id) {
		bookService.deleteBook(id);
		return  "Book Deleted Successfully";
	}

	@ApiOperation(value = "Fetch Books")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details Successfully Fetched"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Something went wrong") })
	 @GetMapping("/fetch/{bookName}/{numberOfCopies}")
	    public List<Book> getBooksByBookNmaeAndCategory(@PathVariable String bookName,@PathVariable int numberOfCopies) {
	        return bookService.getBooksByBookNameAndNoOfCopies(bookName,numberOfCopies);
	    }
	@Autowired
	RequestBookService requestBookService;

	@ApiOperation(value = "View Requested Books")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBook.class, message = "All the Books Fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping("/requests/viewrequests")
	public List<RequestBook> findAllRequestedBooks() {
		return requestBookService.getAllRequestedBooks();
	}
	
	@ApiOperation(value = "search requestbook based on for requestid")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBook.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping("/{id}")
	public ResponseEntity<RequestBook> findByRequestId(@PathVariable Long id) {

		return ResponseEntity.ok(requestBookService.getRequestBook(id));
	}
	@Autowired
	StudentBookIssueService studentBookIssueService;

	@ApiOperation(value = "search requestbook based on for requestid and studentCardno")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = RequestBook.class, message = "Request for book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping("/fetch/{cardNo}/{id}/viewdetails")
    public RequestBook getRequestBookbasedOnIdAndCardNo(@PathVariable String cardNo,@PathVariable Long id) {
        return requestBookService.getRequestBooks(cardNo,id);
    }
	
	
	
}
