package com.student.library.services.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.library.services.entity.RequestBook;
import com.student.library.services.entity.Student;
import com.student.library.services.exception.Constants;
import com.student.library.services.exception.NotFoundException;
import com.student.library.services.mapper.RequestMapper;
import com.student.library.services.repository.RequestRepository;
import com.student.library.services.request.BookRequest;

@Service
public class RequestBookService {
	@Autowired
	RequestRepository requestRepository;
	@Autowired
	RequestMapper requestMapper;
	@Autowired
	StudentService studentService;

	/**
	 * 
	 * @param bookRequest
	 * @param studentId
	 * @return
	 */

	public RequestBook createRequestBook(BookRequest bookRequest, Long studentId) {

		Student student = studentService.getStudent(studentId);
		RequestBook requestBook = requestMapper.mapCreateStudentRequest(bookRequest, student);
		RequestBook createdStudent = requestRepository.save(requestBook);
		return createdStudent;
	}

	/**
	 * 
	 * @param requestId
	 * @param studentId
	 * @return
	 */
	public RequestBook getRequestBook(Long requestId, Long studentId) {
		Student student = studentService.getStudent(studentId);
		Optional<RequestBook> requestbook = requestRepository.findByIdAndStudent(requestId, student);
		if (!requestbook.isPresent()) {
			throw new NotFoundException(Constants.REQUESTBOOK_NOT_FOUND);
		}
		return requestbook.get();
	}

	/**
	 * 
	 * @param requestId
	 * @return
	 */

	public RequestBook getRequestBooks(Long requestId) {
		Optional<RequestBook> requestbook = requestRepository.findById(requestId);
		if (!requestbook.isPresent()) {
			throw new NotFoundException(Constants.REQUESTBOOK_NOT_FOUND);
		}
		return requestbook.get();
	}

}
