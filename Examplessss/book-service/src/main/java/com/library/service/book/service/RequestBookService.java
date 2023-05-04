package com.library.service.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.library.service.book.entity.RequestBook;
import com.library.service.book.exception.Constants;
import com.library.service.book.exception.NotFoundException;
import com.library.service.book.repository.RequestRepository;

@Service
public class RequestBookService {

	
	@Autowired
	RequestRepository requestRepository;
	@Autowired
	StudentService studentService;

	/**
	 * 
	 * @param cardNo
	 * @param requestId
	 * @return
	 */

	public RequestBook getRequestBook(String cardNo) {
		return requestRepository.findBycardNo(cardNo)
				.orElseThrow(() -> new NotFoundException(Constants.REQUESTBOOK_NOT_FOUND));
	}

	/**
	 * 
	 * @param requestId
	 * @return
	 */
	public RequestBook getRequestBook(Long requestBookId) {
		return requestRepository.findById(requestBookId)
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
