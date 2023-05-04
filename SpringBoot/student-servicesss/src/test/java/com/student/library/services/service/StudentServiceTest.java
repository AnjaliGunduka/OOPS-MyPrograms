//package com.student.library.services.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.Assert.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.student.library.services.entity.Student;
//import com.student.library.services.exception.Constants;
//import com.student.library.services.exception.NotFoundException;
//import com.student.library.services.repository.StudentRepository;
//
//@ExtendWith(MockitoExtension.class)
//public class StudentServiceTest {
//	@Mock
//	StudentRepository studentRepository;
//	@InjectMocks
//	StudentService studentService;
//
//	Student student = new Student(1L, "anjali", "anjali", "cse", 1, "12345", "anjali@gmail.com");
//
//	@Test
//	public void testUpdateStudent() {
//		List<Student> students = new ArrayList<Student>();
//		students.add(student);
//		Student student = students.get(0);
//		when(studentRepository.getStudentById(student.getId())).thenReturn(student);
//		when(studentRepository.save(student)).thenReturn(student);
//		assertThat(studentService.updateStudent(student.getId(), student)).isEqualTo(student);
//		assertThat(student.getBranch()).isEqualTo("cse");
//		assertThat(student.getEmail()).isEqualTo("anjali@gmail.com");
//		assertThat(student.getUserName()).isEqualTo("anjali");
//	}
//
//	@Test
//	public void testLogin() {
//		when(studentRepository.findByEmail(student.getEmail())).thenReturn(student);
//		when(studentRepository.findByUserName(student.getUserName())).thenReturn(student);
//		assertEquals("Login successful", studentService.login(student.getUserName(), student.getPassword()));
//		assertEquals("Login successful", studentService.login(student.getEmail(), student.getPassword()));
//	}
//
//	Student students = new Student(1L, "anju", "anju", "cs", 8, "12345", "anju@gmail.com");
//
//	@Test
//	public void testGetStudentsUserNameOrEmailWithNotFoundException() throws NotFoundException {
//		when(studentRepository.findByEmail("anjali@gmail.com")).thenReturn(students);
//		Exception exception = assertThrows(NotFoundException.class,
//				() -> studentService.login(student.getEmail(), student.getPassword()));
//		assertThat(Constants.STUDENT_NOT_FOUND).isEqualTo(exception.getMessage());
//		when(studentRepository.findByUserName("anjali")).thenReturn(students);
//		Exception exceptions = assertThrows(NotFoundException.class,
//				() -> studentService.login(student.getUserName(), student.getPassword()));
//		assertThat(Constants.STUDENT_NOT_FOUND).isEqualTo(exceptions.getMessage());
//	}
//
//}
