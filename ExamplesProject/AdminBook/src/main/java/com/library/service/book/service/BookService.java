package com.library.service.book.service;



import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.service.book.Exception.Constants;
import com.library.service.book.Exception.NotFoundException;
import com.library.service.books.entity.Book$;
import com.library.service.book.entity.Book;
import com.library.service.book.repository.BookRepository;
import com.speedment.jpastreamer.application.JPAStreamer;


@Service
public class BookService {
	private static final Logger log = LoggerFactory.getLogger(BookService.class);

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private JPAStreamer jpaStreamer;

	/**
	 * 
	 * Creates Book and saves
	 * 
	 * @return createdBook
	 */
	public Book createBook(Book book) {
		log.info("Book Created");
		return bookRepository.save(book);
	}
	
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
	
	/**
	 * 
	 * Gets Book based on the Book Id using jpastreamer to Avoid complex and
	 * repetitive code
	 * 
	 * @return
	 * @throws NotFoundException 
	 * @stream which contains classes for processing sequences of elements
	 * @filter contains only the elements that match the Predicate
	 * 
	 */
	public Book getBookById(Long id) {
		return jpaStreamer.stream(Book.class).filter(book -> book.getId().equals(id)).findFirst()
				.orElseThrow(() -> new NotFoundException(Constants.BOOK_NOT_FOUND));
	}
	
	
	
	/**WS
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

	/**
	 * 
	 * Update the Books
	 * 
	 * @return
	 */

	public void updateBook(Long id, Book updatebook) {
		log.info("Book updated");
		bookRepository.save(updatebook);
	}

	/**
	 * 
	 * Delete the Books
	 */
	public void deleteBook(Long id) {
		log.info("Book deleted");
		bookRepository.deleteById(id);

	}

}
