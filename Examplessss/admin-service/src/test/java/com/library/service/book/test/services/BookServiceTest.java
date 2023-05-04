package com.library.service.book.test.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.library.service.book.entity.Book;
import com.library.service.book.enums.Status;
import com.library.service.book.repository.BookRepository;
import com.library.service.book.service.BookService;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
	@InjectMocks
	BookService bookService;
	@Mock
	BookRepository bookRepository;

	public Book testBooks() {
		Book book = new Book(1L, "core", "martin", Status.AVAILABLE, "anjali", "1", 8, "computers");
		return book;

	}

	@Test
	public void testGetBookById() {
		when(bookRepository.findById(1L)).thenReturn(Optional.of(testBooks()));
		assertThat(bookService.getBookById(1L).getId()).isEqualTo(testBooks().getId());

	}

	@Test
	public void testgetAllBooks() {
		List<Book> book = new ArrayList<>();
		book.add(testBooks());
		when(bookRepository.findAll()).thenReturn(book);
		assertThat(bookService.getAllBooks()).isEqualTo(book);

	}

	@Test
	public void testUpdateBook() {
		List<Book> books = new ArrayList<Book>();
		books.add(testBooks());
		Book book = books.get(0);
		when(bookRepository.getBookById(book.getId())).thenReturn(book);
		when(bookRepository.save(book)).thenReturn(book);
		assertThat(bookService.updateBook(book.getId(), book)).isEqualTo(book);
	}

	@Test
	public void testCreateBooks() {
		when(bookRepository.save(Mockito.any())).thenReturn(testBooks());
		assertThat(bookService.createBook(testBooks()).getId()).isEqualTo(testBooks().getId());
	}

	@Test
	public void testdeleteBook() {
		bookService.deleteBook(testBooks().getId());
		verify(bookRepository, times(1)).deleteById(testBooks().getId());
	}
}
