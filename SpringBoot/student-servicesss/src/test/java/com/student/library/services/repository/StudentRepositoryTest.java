//package com.student.library.services.repository;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import com.student.library.services.entity.Student;
//
//
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class StudentRepositoryTest {
//	@Autowired
//	StudentRepository studentRepository;
//
//	@Test
//	void findById() {
//		Student student = new Student();
//		student.setId(1L);
//		student = studentRepository.save(student);
//		Student fetchedStudent = studentRepository.findById(student.getId()).get();
//		assertNotNull(fetchedStudent);
//	}
//
//	@Test
//	void findAll() {
//		List<Student> listOfStudents = studentRepository.findAll();
//		assertTrue(listOfStudents.size() > 0);
//	}
//}
