package com.student.library.services.service;

import java.util.List;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.library.services.client.BookServiceClient;
import com.student.library.services.entity.Book;
import com.student.library.services.entity.RequestBook;
import com.student.library.services.entity.Student;
import com.student.library.services.enums.RequestStatus;
import com.student.library.services.exception.Constants;
import com.student.library.services.exception.NotFoundException;
import com.student.library.services.exception.RequestStatusException;
import com.student.library.services.mapper.RequestMapper;
import com.student.library.services.repository.RequestRepository;
import com.student.library.services.repository.StudentRepository;
import com.student.library.services.request.RequestBookDto;

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
	@Autowired
	private KieContainer kieContainer;

	/**
	 * This method is used to Search Students Books based on requestBookId
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
	 * This method is used to create a RequestBook and mapped with RequestBookDto
	 * 
	 * @param requestBookDto
	 * @param studentId
	 * @return
	 * @throws RequestStatusException
	 */

	public RequestBook addRequestBook(RequestBookDto requestBookDto, Long studentId) throws RequestStatusException {
		Student student = bookServiceClient.getStudent(authService.getAuthToken(), studentId);
		Book book = bookServiceClient.getBooksByBookName(authService.getAuthToken(), requestBookDto.getBookName());
		RequestBook requestBook = requestMapper.mapCreateStudentRequest(requestBookDto, student, book);
		KieSession kieSession = kieContainer.newKieSession();
		kieSession.insert(requestBookDto);
		kieSession.fireAllRules();
		kieSession.dispose();
		//requestBook.setStatus(RequestStatus.REQUESTED);
//		if (requestBook.getStatus() == RequestStatus.REQUESTED) {
//			requestRepository.save(requestBook);
//		} else {
//			throw new RequestStatusException(Constants.STATUS_NOT_FOUND);
//		}
		requestBook = requestRepository.save(requestBook);
		return requestBook;
	}

	/**
	 * This method is used to Search Students Books based on Student CardNo
	 * 
	 * @param cardNo
	 * @return
	 */
	public RequestBook getRequestBooks(String cardNo) {
		return requestRepository.findBycardNo(cardNo)
				.orElseThrow(() -> new NotFoundException(Constants.REQUESTBOOK_NOT_FOUND));
	}

	/**
	 * This method is used to Get all the Requested Books
	 * 
	 * @return List
	 */

	public List<RequestBook> getAllRequestedBooks() {
		return requestRepository.findAll();
	}

}
