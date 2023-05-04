package com.student.library.services.service;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.student.library.services.entity.RequestBook;
import com.student.library.services.entity.Student;
import com.student.library.services.exception.Constants;
import com.student.library.services.exception.NotFoundException;
import com.student.library.services.mapper.RequestMapper;
import com.student.library.services.repository.RequestRepository;
import com.student.library.services.request.BookRequest;

import com.student.library.services.book.entity.RequestBook$;

@Service
public class RequestBookService {
	@Autowired
	RequestRepository requestRepository;
	@Autowired
	RequestMapper requestMapper;
	@Autowired
	StudentService studentService;
	@Autowired
	private JPAStreamer jpaStreamer;

	/**
	 * 
	 * @param bookRequest
	 * @param studentId
	 * @return
	 */

	public RequestBook getBookById(Long requestId) {
		return jpaStreamer.stream(of(RequestBook.class).joining(RequestBook$.student))
				.filter(requestBook -> requestBook.getId().equals(requestId)).findFirst()
				.orElseThrow(() -> new NotFoundException(Constants.REQUESTBOOK_NOT_FOUND));
	}

	public RequestBook getRequestBooks(String cardNo, Long id) {
		return jpaStreamer.stream(RequestBook.class)
				.filter(RequestBook$.cardNo.equal(cardNo).and(RequestBook$.id.in(id))).findFirst()
				.orElseThrow(() -> new NotFoundException(Constants.REQUESTBOOK_NOT_FOUND));
	}

	
	
	public RequestBook createRequestBook(BookRequest bookRequest, Long studentId) {

		Student student = studentService.getStudent(studentId);
		RequestBook requestBook = requestMapper.mapCreateStudentRequest(bookRequest, student);
		RequestBook createdStudent = requestRepository.save(requestBook);
		return createdStudent;
	}

}
