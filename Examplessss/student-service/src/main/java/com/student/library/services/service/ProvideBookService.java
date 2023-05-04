package com.student.library.services.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.library.services.client.BookServiceClient;
import com.student.library.services.entity.Book;
import com.student.library.services.entity.RequestBook;
import com.student.library.services.enums.RequestStatus;
import com.student.library.services.exception.Constants;
import com.student.library.services.exception.BookNotProvidedException;
import com.student.library.services.repository.BookRepository;
import com.student.library.services.repository.RequestRepository;
import com.student.library.services.request.ProvideBookRequest;

@Service
public class ProvideBookService {

	@Autowired
	RequestBookService requestBookService;
	@Autowired
	RequestRepository requestRepository;
	@Autowired
	AuthService authService;
	@Autowired
	BookServiceClient bookServiceClient;
	@Autowired
	private BookRepository bookRepository;

	/**
	 * 
	 * @param provideBookRequest
	 * @return
	 */

	public RequestBook createReturn(ProvideBookRequest provideBookRequest) {
		Book book = bookServiceClient.getBookById(authService.getAuthToken(), provideBookRequest.getBookId());
		RequestBook requestBook = requestBookService.getRequestBook(provideBookRequest.getRequestBookId());
		checkProvideBookAvailable(requestBook);
		requestBook.setStatus(provideBookRequest.getStatus());
		requestBook.setRequestDate(provideBookRequest.getRequestDate());
		int availableBooks = book.getNumberOfCopies();
		book.setNumberOfCopies(availableBooks + provideBookRequest.getNoOfBooks());
		provideBookRequest.setNoOfBooks(provideBookRequest.getNoOfBooks());
		bookRepository.save(book);
		return requestRepository.save(requestBook);

	}

	/**
	 * If Book Rejected throw an exception 
	 * 
	 * @param requestBook
	 */
	private void checkProvideBookAvailable(RequestBook requestBook) {
		if (requestBook.getStatus() == RequestStatus.REJECTED) {
			throw new BookNotProvidedException(Constants.NOTAVAILABLE_BOOK);
		}
	}

}
