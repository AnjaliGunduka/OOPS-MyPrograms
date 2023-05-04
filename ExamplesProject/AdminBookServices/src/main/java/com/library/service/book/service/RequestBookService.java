package com.library.service.book.service;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.service.books.entity.RequestBook$;
import com.library.service.book.entity.RequestBook;
import com.library.service.book.exception.Constants;
import com.library.service.book.exception.NotFoundException;
import com.library.service.book.repository.RequestRepository;
import com.speedment.jpastreamer.application.JPAStreamer;

@Service
public class RequestBookService {

	@Autowired
	private JPAStreamer jpaStreamer;
	@Autowired
	RequestRepository requestRepository;
	@Autowired
	StudentService studentService;

	/**
	 * 
	 * @return
	 */

	public List<RequestBook> getAllRequestedBooks() {
		return jpaStreamer.stream(RequestBook.class).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param requestId
	 * @return
	 */
	public RequestBook getRequestBook(Long requestId) {
		return jpaStreamer.stream(of(RequestBook.class).joining(RequestBook$.student))
				.filter(requestBook -> requestBook.getId().equals(requestId)).findFirst()
				.orElseThrow(() -> new NotFoundException(Constants.REQUESTBOOK_NOT_FOUND));
	}

	/**
	 * 
	 * @param cardNo
	 * @param requestId
	 * @return
	 */

	public RequestBook getRequestBooks(String cardNo, Long requestId) {
		return jpaStreamer.stream(RequestBook.class)
				.filter(RequestBook$.cardNo.equal(cardNo).and(RequestBook$.id.in(requestId))).findFirst()
				.orElseThrow(() -> new NotFoundException(Constants.REQUESTBOOK_NOT_FOUND));
	}
}
