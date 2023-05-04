package com.student.library.services.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.student.library.services.entity.Book;
import com.student.library.services.entity.RequestBook;
import com.student.library.services.entity.Student;
import com.student.library.services.request.JwtRequest;
import com.student.library.services.request.ProvideBookRequest;
import com.student.library.services.response.JWTTokenResponse;

@FeignClient(url = "http://localhost:8688", name = "BOOK-SERVICE-CLIENT")
public interface BookServiceClient {

	@GetMapping(value = "/bookService/books")
	List<Book> getAllBooks(@RequestHeader(name = "Authorization") String authorization);

	@GetMapping(value = "/bookService/books/{id}")
	Book getBookById(@RequestHeader(name = "Authorization") String authorization, @PathVariable Long id);

	@GetMapping(value = "/bookService/booksname/{bookName}")
	Book getBooksByBookName(@RequestHeader(name = "Authorization") String authorization, @PathVariable String bookName);

	@GetMapping(value = "/bookService/student/viewstudents")
	List<Student> getAllStudents(@RequestHeader(name = "Authorization") String authorization);

	@PostMapping(value = "/bookService/books/{bookId}/createIssue")
	RequestBook createIssue(@RequestHeader(name = "Authorization") String authorization,
			@RequestBody ProvideBookRequest provideBookRequest);

	@GetMapping(value = "/bookService/student/students/{id}")
	Student getStudent(@RequestHeader(name = "Authorization") String authorization, @PathVariable Long id);

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
	public JWTTokenResponse getAuthToken(@RequestBody JwtRequest jwtRequest);
}
