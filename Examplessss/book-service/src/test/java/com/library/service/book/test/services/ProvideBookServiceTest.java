package com.library.service.book.test.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.library.service.book.entity.Book;
import com.library.service.book.entity.RequestBook;
import com.library.service.book.entity.Student;
import com.library.service.book.enums.RequestStatus;
import com.library.service.book.enums.Status;
import com.library.service.book.exception.NoOfCopiesNotAvailableException;
import com.library.service.book.repository.BookRepository;
import com.library.service.book.repository.RequestRepository;
import com.library.service.book.request.ProvideBookRequest;
import com.library.service.book.service.BookService;
import com.library.service.book.service.ProvideBookService;
import com.library.service.book.service.RequestBookService;

@ExtendWith(MockitoExtension.class)
public class ProvideBookServiceTest {
	@InjectMocks
	ProvideBookService provideBookService;
	@Mock
	BookRepository bookRepository;
	@Mock
	RequestRepository requestRepository;
	@Mock
	RequestBookService requestBookService;
	@Mock
	BookService bookService;
	@Mock
	BookServiceTest bookServiceTest;

	public ProvideBookRequest getProvideBookRequest() {
		ProvideBookRequest provideBookRequest = new ProvideBookRequest(1L, 1L, 1,Instant.now(), RequestStatus.APPROVED);
		return provideBookRequest;
	}

	public RequestBook testRequestBooks() {
		RequestBook requestBook = new RequestBook(3L, "core", RequestStatus.REQUESTED, "12345", 1, Instant.now(),
				testStudentResponse(), testBookResponse());
		return requestBook;
	}

	public Student testStudentResponse() {
		Student studentResponse = new Student(1L, "anjali", "anjali", "cse", 3, "12345", "anjali@gmail.com");
		return studentResponse;
	}

	public Book testBooks() {
		Book book = new Book(1L, "core", "martin", Status.AVAILABLE, "anjali", "1", 8, "computers");
		return book;

	}

	public Book testBookResponse() {
		Book bookResponse = new Book(1L, "core", "anjali", Status.AVAILABLE, "anjali", "1st", 8, "computers");
		return bookResponse;

	}

	@Test
	public void testProvideBook() throws NoOfCopiesNotAvailableException {
		List<RequestBook> books = new ArrayList<RequestBook>();
		books.add(testRequestBooks());
		testRequestBooks().setNoOfBooks(testBooks().getNumberOfCopies());
		when(requestBookService.getRequestBook(Mockito.anyLong())).thenReturn(testRequestBooks());
		when(bookService.getBookById(Mockito.anyLong())).thenReturn(testBooks());
		when(requestRepository.save(Mockito.any())).thenReturn(testRequestBooks());
		assertThat(provideBookService.createIssue(getProvideBookRequest()).getId())
				.isEqualTo(testRequestBooks().getId());
	}

}
