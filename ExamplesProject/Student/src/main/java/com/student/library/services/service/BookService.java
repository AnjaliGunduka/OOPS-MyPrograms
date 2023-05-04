package com.student.library.services.service;



import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.student.library.services.book.entity.Book$;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.student.library.services.entity.Book;
import com.student.library.services.exception.Constants;
import com.student.library.services.exception.NotFoundException;





@Service
public class BookService {
	private static final Logger log = LoggerFactory.getLogger(BookService.class);

	@Autowired
	private JPAStreamer jpaStreamer;

	/**
	 * 
	 * Gets Book based on the Book Name using jpastreamer to Avoid complex and
	 * repetitive code
	 * 
	 * @return
	 * @stream which contains classes for processing sequences of elements
	 * @filter contains only the elements that match the Predicate
	 * 
	 */
	public List<Book> getBooksByBookName(String bookName) {
		return jpaStreamer.stream(Book.class).filter(Book$.bookName.equal(bookName)).collect(Collectors.toList());
	}
	public Book getBookById(Long id) {
		return jpaStreamer.stream(Book.class).filter(book -> book.getId().equals(id)).findFirst()
				.orElseThrow(() -> new NotFoundException(Constants.BOOK_NOT_FOUND));
	}
	/**
	 * 
	 * Search Book based on bookName and noOfCopies You Want
	 * 
	 */
	public List<Book> getBooksByBookNameAndNoOfCopies(String bookName, int numberOfCopies) {
		return jpaStreamer.stream(Book.class)
				.filter(Book$.bookName.equal(bookName).and(Book$.numberOfCopies.greaterOrEqual(numberOfCopies)))
				.collect(Collectors.toList());
	}
	
	/**
	 * 
	 * List All the Books
	 * 
	 * @return
	 */

	public List<Book> getAllBooks() {
		log.info("Get All the Books");
		return jpaStreamer.stream(Book.class).collect(Collectors.toList());

	}
	
}
