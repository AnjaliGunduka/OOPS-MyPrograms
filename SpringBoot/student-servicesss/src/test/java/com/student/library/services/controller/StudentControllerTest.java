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
import com.student.library.services.entity.Student;
import com.student.library.services.repository.StudentRepository;
import com.student.library.services.service.AuthService;
import com.student.library.services.service.StudentService;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

	@Mock
	StudentRepository studentRepository;
	@Mock
	StudentService studentService;
	@InjectMocks
	StudentInformationController studentInformationController;
	@Mock
	BookServiceClient bookServiceClient;
	@Mock
	AuthService authService;

	Student student = new Student(1L, "anjali", "anjali", "cse", 1, "12345", "anjali@gmail.com");

	@Test
	public void testUpdateStudent() {
		Long studentId = 1L;
		when(studentService.updateStudent(studentId, student)).thenReturn(student);
		ResponseEntity<Student> response = studentInformationController.updateStudent(studentId, student);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(student, response.getBody());
		ResponseEntity<Student> res = studentInformationController.updateStudent(null, null);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
		assertEquals(null, res.getBody());
	}

	@Test
	public void testgetAllStudents() {
		List<Student> students = new ArrayList<>();
		students.add(student);
		when(bookServiceClient.getAllStudents(null)).thenReturn(students);
		ResponseEntity<List<Student>> response = studentInformationController.getAllStudents();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(students, response.getBody());
	}

	@Test
	public void testStudentById() {
		when(bookServiceClient.getStudent(null, student.getId())).thenReturn(student);
		ResponseEntity<Student> response = studentInformationController.getStudent(student.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(student, response.getBody());
		ResponseEntity<Student> res = studentInformationController.getStudent(null);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
		assertEquals(null, res.getBody());
	}

	@Test
	public void testLogin() {
		when(studentService.login(student.getEmail(), student.getPassword())).thenReturn("Login");
		when(studentService.login(student.getUserName(), student.getPassword())).thenReturn("Login");
		ResponseEntity<String> response = studentInformationController.login(student.getEmail(), student.getPassword());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Login", response.getBody());
		ResponseEntity<String> res = studentInformationController.login(student.getUserName(), student.getPassword());
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals("Login", res.getBody());
		
	}

}
