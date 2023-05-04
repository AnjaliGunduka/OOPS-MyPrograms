package com.library.service.book.test.controller;

import static org.mockito.Mockito.when;
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
import com.library.service.book.controller.StudentRegistrationController;
import com.library.service.book.entity.Student;
import com.library.service.book.request.StudentRequest;
import com.library.service.book.service.StudentService;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {
	@Mock
	StudentService studentService;
	@InjectMocks
	StudentRegistrationController studentRegistrationController;
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(studentRegistrationController).build();
	}

	public Student testStudents() {
		Student student = new Student(1L, "anjali", "anjali", "cse", 1, "12345", "anjali@gmail.com");
		return student;
	}

	public StudentRequest testAddStudentsRequests() {
		StudentRequest studentRequest = new StudentRequest("cse", "anjali", "anjali", 1, "anjali@gmail.com");
		return studentRequest;
	}

	@Test
	public void testStudentById() throws Exception {
		Long studentId = 1L;
		when(studentService.getStudent(studentId)).thenReturn(testStudents());
		this.mockMvc.perform(get("/bookService/student/students/{id}", studentId)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andDo(print());
		;

	}

//	@Test
//	public void testCreateStudent() throws Exception {
//		String request = new Gson().toJson(testAddStudentsRequests());
//		when(studentService.createStudent(Mockito.any())).thenReturn(testStudents());
//		ObjectMapper objectMapper = new ObjectMapper();
//		String jsonBody = objectMapper.writeValueAsString(testStudents());// return the data in string format
//		this.mockMvc.perform(post("/bookService/student/students").content(jsonBody)
//				.contentType(MediaType.APPLICATION_JSON).content(request)).andExpect(status().isOk());
//	}
	@Test
	public void testCreateStudent() throws Exception {
		when(studentService.createStudent(Mockito.any())).thenReturn(testStudents());
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonBody = objectMapper.writeValueAsString(testStudents());// return the data in string format
		this.mockMvc
				.perform(
						post("/bookService/student/students").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testgetAllStudents() throws Exception {
		List<Student> student = new ArrayList<>();
		student.add(testStudents());
		when(studentService.getAllStudents()).thenReturn(student);
		this.mockMvc.perform(get("/bookService/student/viewstudents")).andExpect(status().isOk()).andDo(print());
	}

}
