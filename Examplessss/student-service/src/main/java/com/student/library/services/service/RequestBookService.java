package com.student.library.services.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.library.services.entity.Book;
import com.student.library.services.entity.RequestBook;
import com.student.library.services.entity.Student;
import com.student.library.services.enums.RequestStatus;
import com.student.library.services.exception.RequestStatusException;
import com.student.library.services.exception.Constants;
import com.student.library.services.exception.NotFoundException;
import com.student.library.services.mapper.RequestMapper;
import com.student.library.services.repository.RequestRepository;
import com.student.library.services.repository.StudentRepository;
import com.student.library.services.request.BookRequest;

import com.student.library.services.client.BookServiceClient;

@Service
public class RequestBookService {
	@Autowired
	RequestRepository requestRepository;
	@Autowired
	RequestMapper requestMapper;
	@Autowired
	StudentService studentService;
	@Autowired
	AuthService authService;
	@Autowired
	BookServiceClient bookServiceClient;
	@Autowired
	StudentRepository studentRepository;

	/**
	 * 
	 * @param bookRequest
	 * @param studentId
	 * @return
	 */

	public RequestBook getRequestBook(Long requestBookId) {
		return requestRepository.findById(requestBookId)
				.orElseThrow(() -> new NotFoundException(Constants.REQUESTBOOK_NOT_FOUND));
	}

	/**
	 * Create a RequestBook
	 * 
	 * @param bookRequest
	 * @param studentId
	 * @return
	 */

	public RequestBook createRequestBook(BookRequest bookRequest, Long studentId) {

		Student student = bookServiceClient.getStudent(authService.getAuthToken(), studentId);
		Book book = bookServiceClient.getBooksByBookName(authService.getAuthToken(), bookRequest.getBookName());
		RequestBook requestBook = requestMapper.mapCreateStudentRequest(bookRequest, student, book);
		if (requestBook.getStatus() == RequestStatus.REQUESTED) {
			requestRepository.save(requestBook);
		} else {
			throw new RequestStatusException(Constants.STATUS_NOT_FOUND);
		}
		return requestBook;
	}
	/**
	 * search based on cardNo
	 * @param cardNo
	 * @return
	 */

	public RequestBook getRequestBooks(String cardNo) {
		return requestRepository.findBycardNo(cardNo)
				.orElseThrow(() -> new NotFoundException(Constants.REQUESTBOOK_NOT_FOUND));
	}

	/**
	 * 
	 * @return
	 */

	public List<RequestBook> getAllRequestedBooks() {
		return requestRepository.findAll();
	}

}
