//package com.student.library.services.repository;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import com.student.library.services.entity.Book;
//import com.student.library.services.entity.RequestBook;
//import com.student.library.services.entity.Student;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class RequestBookRepositoryTest {
//
//	@Autowired
//	RequestRepository requestRepository;
//
//	@Test
//	void findById() {
//		Student student = new Student();
//		student.setId(1L);
//		Book book=new Book();
//		RequestBook requestBook = new RequestBook();
//		requestBook.setStudent(student);
//		requestBook.setBook(book);
//		book.setId(1L);
//		requestBook.setId(1L);
//		requestBook = requestRepository.save(requestBook);
//		RequestBook listOfRequestBook = requestRepository.findById(requestBook.getId()).get();
//		assertNotNull(listOfRequestBook);
//	}
//	
//	@Test
//	void findBycardNo() {
//		Student student = new Student();
//		student.setId(1L);
//		Book book=new Book();
//		RequestBook requestBook = new RequestBook();
//		requestBook.setStudent(student);
//		requestBook.setBook(book);
//		book.setId(1L);
//		requestBook.setId(3L);
//		requestBook = requestRepository.save(requestBook);
//		RequestBook fetchedBook = requestRepository.findBycardNo(requestBook.getCardNo()).get();
//		assertNotNull(fetchedBook);
//	}
//	
//	
//}
