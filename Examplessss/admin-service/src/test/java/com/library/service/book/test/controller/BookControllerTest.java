package com.library.service.book.test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.service.book.controller.BookController;
import com.library.service.book.entity.Book;
import com.library.service.book.enums.Status;
import com.library.service.book.service.BookService;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
	@Mock
	BookService bookService;
	@InjectMocks
	BookController bookController;
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
	}

	public Book testBooks() {
		Book book = new Book(1L, "core", "martin", Status.AVAILABLE, "anjali", "1", 8, "computers");
		return book;

	}

	@Test
	public void testgetAllBooks() throws Exception {
		List<Book> book = new ArrayList<>();
		book.add(testBooks());
		when(bookService.getAllBooks()).thenReturn(book);
		this.mockMvc.perform(get("/bookService/books")).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetBookById() throws Exception {
		Long bookId = 1L;
		when(bookService.getBookById(bookId)).thenReturn(testBooks());
		this.mockMvc.perform(get("/bookService/books/{id}", bookId)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andDo(print());
		;

	}

	@Test
	public void testgetBookByBookName() throws Exception {
		String bookName = "cores";
		when(bookService.getBooksByBookName(bookName)).thenReturn(testBooks());
		this.mockMvc.perform(get("/bookService/booksname/{bookName}", bookName)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andDo(print());
	}

	@Test
	public void testCreateBook() throws Exception {
		when(bookService.createBook(Mockito.any())).thenReturn(testBooks());
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonBody = objectMapper.writeValueAsString(testBooks());// return the data in string format
		this.mockMvc.perform(post("/bookService/bookscreate").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	@Test
	public void testDeleteBook() throws Exception {
		Long bookId = 1L;
		when(bookService.getBookById(bookId)).thenReturn(testBooks());
		this.mockMvc.perform(delete("/bookService/deletebooks/{id}", bookId)).andExpect(status().isOk());

	}

	

}
