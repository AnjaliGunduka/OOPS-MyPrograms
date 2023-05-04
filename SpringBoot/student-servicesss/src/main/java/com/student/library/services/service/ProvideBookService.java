package com.student.library.services.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.library.services.client.BookServiceClient;
import com.student.library.services.entity.Book;
import com.student.library.services.entity.RequestBook;
import com.student.library.services.enums.RequestStatus;
import com.student.library.services.exception.BookNotProvidedException;
import com.student.library.services.exception.Constants;
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
	 * This method is use to return a book to student Based on BookId and StudentId
	 * And change requested status and date If Book is Rejected Student try to
	 * return the book will get an exception
	 * 
	 * @param provideBookRequest
	 * @return
	 * @throws BookNotProvidedException
	 */
	public RequestBook addReturnBook(ProvideBookRequest provideBookRequest) throws BookNotProvidedException {
		Book book = bookServiceClient.getBookById(authService.getAuthToken(), provideBookRequest.getBookId());
		RequestBook requestBook = requestBookService.getRequestBook(provideBookRequest.getRequestBookId());
		if (requestBook.getStatus() == RequestStatus.REJECTED) {
			throw new BookNotProvidedException(Constants.INVALID_BOOK_REJECTED);
		}
		requestBook.setStatus(provideBookRequest.getStatus());
		requestBook.setRequestDate(provideBookRequest.getRequestDate());
		int availableBooks = book.getNumberOfCopies();
		book.setNumberOfCopies(availableBooks + provideBookRequest.getNoOfBooks());
		provideBookRequest.setNoOfBooks(provideBookRequest.getNoOfBooks());
		bookRepository.save(book);
		return requestRepository.save(requestBook);

	}

}
