package com.student.library.services.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.student.library.services.entity.Book;
import com.student.library.services.entity.RequestBook;
import com.student.library.services.entity.Student;
import com.student.library.services.enums.RequestStatus;
import com.student.library.services.enums.Status;
import com.student.library.services.exception.RequestStatusException;
import com.student.library.services.request.RequestBookDto;
import com.student.library.services.response.RequestBookResponse;
import com.student.library.services.service.RequestBookService;

@ExtendWith(MockitoExtension.class)
public class RequestControllerTest {

	@Mock
	RequestBookService requestBookService;
	@InjectMocks
	BookController bookController;

	Student studentResponse = new Student(1L, "anjali", "anjali", "cse", 3, "12345", "anjali@gmail.com");

	

	Book book = new Book(1L, "core", "martin", Status.AVAILABLE, "anjali", "1", 8,  "computers");

	RequestBook requestBook = new RequestBook(3L, "core", RequestStatus.APPROVED, "12345", 6, Instant.now(),
			studentResponse, book);

	RequestBookDto requestBookDto = new RequestBookDto("core", "APPROVED", 6, Instant.now(), "12345");

	@Test
	public void testaddRequestBook() throws RequestStatusException {
		when(requestBookService.addRequestBook(requestBookDto, studentResponse.getId())).thenReturn(requestBook);
		ResponseEntity<RequestBookResponse> response = bookController.createRequestBook(requestBookDto,
				studentResponse.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		ResponseEntity<RequestBookResponse> res = bookController.createRequestBook(null, null);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
		assertEquals(null, res.getBody());
	}

	@Test
	public void testgetAllRequestBooks() {
		List<RequestBook> requestBooks = new ArrayList<RequestBook>();
		requestBooks.add(requestBook);
		when(requestBookService.getAllRequestedBooks()).thenReturn(requestBooks);
		ResponseEntity<List<RequestBook>> response = bookController.getAllRequestedBooks();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(requestBooks, response.getBody());
	}

	@Test
	public void testgetBookById() {
		when(requestBookService.getRequestBook(requestBook.getId())).thenReturn(requestBook);
		ResponseEntity<RequestBook> response = bookController.findByRequestId(requestBook.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(requestBook, response.getBody());
		ResponseEntity<RequestBook> res = bookController.findByRequestId(null);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
		assertEquals(null, res.getBody());
	}

	@Test
	public void testRequestBookByCardno() {
		when(requestBookService.getRequestBooks(requestBook.getCardNo())).thenReturn(requestBook);
		ResponseEntity<RequestBook> response = bookController.getRequestBookByCardNo(requestBook.getCardNo());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(requestBook, response.getBody());
		ResponseEntity<RequestBook> res = bookController.getRequestBookByCardNo(null);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
		assertEquals(null, res.getBody());
	}

}
