package com.library.service.book.service;

import com.library.service.books.entity.RequestBook$;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.library.service.book.entity.Book;
import com.library.service.book.entity.RequestBook;
import com.library.service.book.enums.RequestStatus;
import com.library.service.book.exception.Constants;
import com.library.service.book.exception.NoOfCopiesNotAvailableException;
import com.library.service.book.exception.NotFoundException;
import com.library.service.book.repository.BookRepository;
import com.library.service.book.repository.RequestRepository;
import com.library.service.book.request.ProvideBookRequest;
import com.speedment.jpastreamer.application.JPAStreamer;

@Service
public class ProvideBookService {

	@Autowired
	BookService bookService;
	@Autowired
	RequestBookService requestBookService;
	@Autowired
	RequestRepository requestRepository;
	@Autowired
	private JPAStreamer jpaStreamer;
	@Autowired
	private BookRepository bookRepository;

	/**
	 * 
	 * @param provideBookRequest
	 * @return
	 * @throws NoOfCopiesNotAvailableException
	 */
	public RequestBook createIssue(ProvideBookRequest provideBookRequest) throws NoOfCopiesNotAvailableException {
		Book book = bookService.getBookById(provideBookRequest.getBookId());
		RequestBook requestBook = requestBookService.getRequestBook(provideBookRequest.getRequestBookId());
		requestBook.setStatus(provideBookRequest.getStatus());
	//	requestBook.setRequestDate(provideBookRequest.getRequestDate());
		int availableBooks = book.getNumberOfCopies();
		if (provideBookRequest.getNoOfBooks() > availableBooks) {
			throw new NoOfCopiesNotAvailableException(Constants.STUDENTBOOK_NOOFCOPIES_NOTAVAILABLE);
		}
		book.setNumberOfCopies(availableBooks - provideBookRequest.getNoOfBooks());
		provideBookRequest.setNoOfBooks(provideBookRequest.getNoOfBooks());
		bookRepository.save(book);
		return requestRepository.save(requestBook);

	}

	public RequestBook getBookStatus(RequestStatus status) {
		return jpaStreamer.stream(RequestBook.class).filter(RequestBook$.status.equal(status)).findFirst()
				.orElseThrow(() -> new NotFoundException(Constants.BOOK_NOT_FOUND));

	}

}
