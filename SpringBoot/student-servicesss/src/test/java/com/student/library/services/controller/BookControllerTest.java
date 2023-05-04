package com.student.library.services.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import com.student.library.services.client.BookServiceClient;
import com.student.library.services.entity.Book;
import com.student.library.services.enums.Status;
import com.student.library.services.service.AuthService;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

	@InjectMocks
	BookController bookController;
	@Mock
	BookServiceClient bookServiceClient;
	@Mock
	AuthService authService;


	Book book = new Book(1L, "core", "martin", Status.AVAILABLE, "anjali", "1", 8,  "computers");

	@Test
	public void testgetAllBookss() {
		List<Book> books = new ArrayList<Book>();
		books.add(book);
		when(bookServiceClient.getAllBooks(null)).thenReturn(books);
		ResponseEntity<List<Book>> response = bookController.findAllBooks();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(books, response.getBody());
	}

	@Test
	public void testgetBookById() {
		when(bookServiceClient.getBookById(null, book.getId())).thenReturn(book);
		ResponseEntity<Book> response = bookController.getBookById(book.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(book, response.getBody());
		ResponseEntity<Book> res = bookController.getBookById(null);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
		assertEquals(null, res.getBody());
	}

	@Test
	public void testgetBookByBookName() {
		when(bookServiceClient.getBooksByBookName(null, book.getBookName())).thenReturn(book);
		ResponseEntity<Book> response = bookController.getBooksByBookName(book.getBookName());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(book, response.getBody());
		ResponseEntity<Book> res = bookController.getBooksByBookName(null);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
		assertEquals(null, res.getBody());
	}

}
