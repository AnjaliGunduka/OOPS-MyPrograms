package com.library.service.book.test.services;

import static org.junit.Assert.assertEquals;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.library.service.book.entity.Student;
import com.library.service.book.mapper.StudentMapper;
import com.library.service.book.repository.StudentRepository;
import com.library.service.book.request.StudentRequest;
import com.library.service.book.service.StudentService;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
	@Mock
	StudentRepository studentRepository;
	@InjectMocks
	StudentService studentService;
	@Mock
	StudentMapper studentMapper;
	@Mock
	PasswordEncoder passwordEncoder;
	@Mock
	AuthenticationManager authenticationManager;

	public Student testStudents() {
		Student student = new Student(1L, "anjali", "anjali", "cse", 1, "12345", "anjali@gmail.com");
		return student;
	}

	public StudentRequest testAddStudentsRequests() {
		StudentRequest studentRequest = new StudentRequest("cse", "anjali", "anjali", 1, "anjali@gmail.com");
		return studentRequest;
	}

	@Test
	public void testCreateStudent() throws Exception {
		when(studentRepository.save(testStudents())).thenReturn(testStudents());
		when(passwordEncoder.encode(testStudents().getPassword())).thenReturn(testStudents().getPassword());
		assertEquals(false, studentService.userNameExists(testStudents().getUserName()));
		assertEquals(testStudents(), studentService.createStudent(testAddStudentsRequests()));
	}

	@Test
	public void testgetAllStudents() {
		List<Student> student = new ArrayList<>();
		student.add(testStudents());
		Mockito.when(studentRepository.findAll()).thenReturn(student);
		assertEquals(student, studentService.getAllStudents());
	}

	@Test
	public void testGetStudentsById() {
		Mockito.when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudents()));
		assertEquals(testStudents().getId(), studentService.getStudent(1L).getId());

	}
}
