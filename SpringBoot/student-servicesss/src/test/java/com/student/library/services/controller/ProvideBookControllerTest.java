package com.student.library.services.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;




import com.student.library.services.client.BookServiceClient;
import com.student.library.services.entity.Book;
import com.student.library.services.entity.RequestBook;
import com.student.library.services.entity.Student;
import com.student.library.services.enums.RequestStatus;
import com.student.library.services.enums.Status;
import com.student.library.services.exception.BookNotProvidedException;
import com.student.library.services.request.ProvideBookRequest;
import com.student.library.services.response.ProvideBookResponse;
import com.student.library.services.service.AuthService;
import com.student.library.services.service.ProvideBookService;

@ExtendWith(MockitoExtension.class)
public class ProvideBookControllerTest {
	@Mock
	ProvideBookService provideBookService;
	@InjectMocks
	BookController bookController;
	@Autowired
	AuthService authService;
	@Autowired
	BookServiceClient bookServiceClient;

	Student student = new Student(1L, "anjali", "Anjali", "cse", 1, "12345", "anjali@gmail.com");


	Book book = new Book(1L, "core", "martin", Status.AVAILABLE, "anjali", "1", 8,  "computers");

	ProvideBookRequest provideBookRequest = new ProvideBookRequest(1L, 1L, Instant.now(), 1,
			RequestStatus.APPROVED);
	

//	RequestBook requestBook = new RequestBook(3L, "core", RequestStatus.REQUESTED, "12345", 6, new Date(2020 - 8 - 8),
//			student, book);
	RequestBook requestBook = new RequestBook(3L, "core", RequestStatus.REQUESTED, "12345", 6, Instant.now(),
			student, book);

	@Test
	public void testaddProvideBook() throws BookNotProvidedException {
		when(provideBookService.addReturnBook(provideBookRequest)).thenReturn(requestBook);
		ResponseEntity<ProvideBookResponse> response = bookController.addReturnBook(provideBookRequest);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		ResponseEntity<ProvideBookResponse> res = bookController.addReturnBook(null);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
		assertEquals(null, res.getBody());
	}

}
