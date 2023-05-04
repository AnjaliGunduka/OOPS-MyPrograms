//package com.student.library.services.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.Assert.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.student.library.services.client.BookServiceClient;
//import com.student.library.services.entity.Book;
//import com.student.library.services.entity.RequestBook;
//import com.student.library.services.entity.Student;
//import com.student.library.services.enums.RequestStatus;
//import com.student.library.services.enums.Status;
//import com.student.library.services.exception.Constants;
//import com.student.library.services.exception.NotFoundException;
//import com.student.library.services.exception.RequestStatusException;
//import com.student.library.services.mapper.RequestMapper;
//import com.student.library.services.repository.RequestRepository;
//import com.student.library.services.request.RequestBookDto;
//
//@ExtendWith(MockitoExtension.class)
//public class RequestServiceTest {
//	@Mock
//	RequestRepository requestRepository;
//	@InjectMocks
//	RequestBookService requestBookService;
//	@Mock
//	RequestMapper requestMapper;
//	@Mock
//	BookServiceClient bookServiceClient;
//	@Mock
//	AuthService authService;
//	@Mock
//	StudentServiceTest studentServiceTest;
//
//	Student student = new Student(1L, "anjali", "anjali", "cse", 3, "12345", "anjali@gmail.com");
//
//	Book book = new Book(1L, "core", "martin", Status.AVAILABLE, "anjali", "1", 8, "computers");
//	RequestBook requestBook = new RequestBook(3L, "core", RequestStatus.REQUESTED, "12345", 6, Instant.now(),
//			student, book);
//
//	RequestBookDto bookRequest = new RequestBookDto("core", "REQUESTED", 1, Instant.now(), "12345");
//
//	@Test
//	public void testCreateRequestBook() throws RequestStatusException {
//		when(bookServiceClient.getStudent("any Token", student.getId())).thenReturn(student);
//		when(bookServiceClient.getBooksByBookName("any Token", book.getBookName())).thenReturn(book);
//		when(authService.getAuthToken()).thenReturn("any Token");
//		when(requestRepository.save(Mockito.any())).thenReturn(requestBook);
//		when(requestRepository.findByStudentId(student.getId())).thenReturn(requestBook);
//		assertEquals(requestBook, requestBookService.addRequestBook(bookRequest, 1L));
//	}
//
//	@Test
//	public void testgetAllRequestBooks() {
//		List<RequestBook> requestBooks = new ArrayList<>();
//		requestBooks.add(requestBook);
//		Mockito.when(requestRepository.findAll()).thenReturn(requestBooks);
//		assertEquals(requestBooks, requestBookService.getAllRequestedBooks());
//	}
//
//	@Test
//	public void testGetRequestBooksById() {
//		Mockito.when(requestRepository.findById(1L)).thenReturn(Optional.of(requestBook));
//		assertEquals(requestBook.getId(), requestBookService.getRequestBook(1L).getId());
//
//	}
//
//	@Test
//	public void testGetRequestBookByCardno() {
//		Mockito.when(requestRepository.findBycardNo("12345")).thenReturn(Optional.of(requestBook));
//		assertEquals(requestBook.getCardNo(), requestBookService.getRequestBooks("12345").getCardNo());
//
//	}
//
//	@Test
//	public void testGetRequestBookByCardnoWithNotFoundException() throws NotFoundException {
//		when(requestRepository.findBycardNo("12345")).thenReturn(Optional.empty());
//		Exception exception = assertThrows(NotFoundException.class, () -> requestBookService.getRequestBooks("12345"));
//		assertThat(Constants.REQUESTBOOK_NOT_FOUND).isEqualTo(exception.getMessage());
//	}
//
//	@Test
//	public void testGetRequestBooksByIdWithNotFoundException() throws NotFoundException {
//		when(requestRepository.findById(1L)).thenReturn(Optional.empty());
//		Exception exception = assertThrows(NotFoundException.class, () -> requestBookService.getRequestBook(1L));
//		assertThat(Constants.REQUESTBOOK_NOT_FOUND).isEqualTo(exception.getMessage());
//	}
//
//}
