package com.library.service.book.test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.LocalDate;
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
import com.google.gson.Gson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.service.book.controller.BookController;
import com.library.service.book.entity.Book;
import com.library.service.book.entity.RequestBook;
import com.library.service.book.entity.Student;
import com.library.service.book.enums.RequestStatus;
import com.library.service.book.enums.Status;
import com.library.service.book.request.ProvideBookRequest;
import com.library.service.book.service.ProvideBookService;

@ExtendWith(MockitoExtension.class)
public class ProvideBookControllerTest {
	@Mock
	ProvideBookService provideBookService;
	@InjectMocks
	BookController bookController;
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
	}

	public ProvideBookRequest getProvideBookRequest() {
		ProvideBookRequest provideBookRequest = new ProvideBookRequest(1L, 1L, 1,Instant.now(),
				RequestStatus.APPROVED);
		return provideBookRequest;
	}

	public RequestBook testRequestBooks() {
		RequestBook requestBook = new RequestBook(3L, "core", RequestStatus.REQUESTED, "12345", 6, Instant.now(),
				testStudents(), testBooks());
		return requestBook;
	}

	public static Student testStudents() {
		Student student = new Student(1L, "anjali", "anjali", "cse", 1, "12345", "anjali@gmail.com");
		return student;
	}

	public static Book testBooks() {
		Book book = new Book(1L, "core", "martin", Status.AVAILABLE, "anjali", "1", 8, "computers");
		return book;

	}

	@Test
	public void testcreateIssue() throws Exception {
		 String gson = new Gson().toJson(getProvideBookRequest());
		when(provideBookService.createIssue(Mockito.any())).thenReturn(testRequestBooks());
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonBody = objectMapper.writeValueAsString(testRequestBooks());
		this.mockMvc.perform(post("/bookService/providebook").content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				 .content(gson))
				.andExpect(status().isOk()).andDo(print());
	}

}
