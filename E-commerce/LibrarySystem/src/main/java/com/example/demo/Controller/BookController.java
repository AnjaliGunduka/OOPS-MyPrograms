package com.example.demo.Controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Book;
import com.example.demo.Entity.Category;
import com.example.demo.Request.BookRequest;
import com.example.demo.service.BookService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/library-services")
public class BookController {
	@Autowired
	BookService bookService;

	@ApiOperation(value = "Creates a Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = BookRequest.class, message = "Book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PostMapping(value = "/bookscreate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Book> createCollege(@Valid @RequestBody Book bookRequest) {
		Book createdBook = bookService.createBook(bookRequest);
		ModelMapper modelMapper = new ModelMapper();
		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(createdBook, Book.class));
	}

	@ApiOperation(value = "Gets a Book By bookName")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Book.class, message = "Book Details fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid Book bookName") })
	@GetMapping(value = "/books/{bookName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Book> getBookByBookName(
			@Valid @Positive(message = "Invalid Book BookName") @PathVariable("bookName") String bookName) {
		return  bookService.getBooksByBookName(bookName) ;
	} 
	@ApiOperation(value = "View Books")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = BookRequest.class, message = "All the Books Fetched Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@GetMapping("/books")
	public ResponseEntity<List<Book>> findAllBooks() {
		return ResponseEntity.ok(bookService.getAllBooks());
	}

	@ApiOperation(value = "Update a Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = BookRequest.class, message = "Book Details updated  Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PutMapping(value = "/books/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateBook(@PathVariable Long id, @RequestBody Book book) {
		bookService.updateBook(id, book);
	}

	@ApiOperation(value = "Delete a Book")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = BookRequest.class, message = "Book Details updated  Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@DeleteMapping(value = "/books/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteBook(@PathVariable Long id) {
		bookService.deleteBook(id);
	}

	@ApiOperation(value = "Fetch Books")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = BookRequest.class, message = "Book Details Successfully Fetched"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Something went wrong") })
	 @GetMapping("/fetch/{bookName}/{numberOfCopies}")
	    public List<Book> getBooksByBookNmaeAndCategory(@PathVariable String bookName,@PathVariable int numberOfCopies) {
	        return bookService.getBooksByBookNameAndNoOfCopies(bookName,numberOfCopies);
	    }
	
	
	
	
	
	
}
