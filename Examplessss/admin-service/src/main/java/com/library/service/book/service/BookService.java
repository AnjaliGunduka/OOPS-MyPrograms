package com.library.service.book.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.service.book.entity.Book;
import com.library.service.book.exception.Constants;
import com.library.service.book.exception.NotFoundException;
import com.library.service.book.repository.BookRepository;
import com.library.service.books.entity.Book$;
import com.speedment.jpastreamer.application.JPAStreamer;

@Service
public class BookService {
	private static final Logger log = LoggerFactory.getLogger(BookService.class);

	@Autowired
	private BookRepository bookRepository;

	/**
	 * 
	 * Creates Book and saves
	 * 
	 * @param category
	 * 
	 * @return createdBook
	 */
	public Book createBook(Book book) {
		log.info("Book Created");
		return bookRepository.save(book);
	}

	/**
	 * 
	 * @param bookName
	 * @return
	 */
	@Transactional
	public Book getBooksByBookName(String bookName) {
		return bookRepository.findByBookName(bookName)
				.orElseThrow(() -> new NotFoundException(Constants.BOOK_NOT_FOUND));

	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public Book getBookById(Long id) {
		return bookRepository.findById(id).orElseThrow(() -> new NotFoundException(Constants.BOOK_NOT_FOUND));
	}

	/**
	 *
	 * 
	 * @return
	 * @throws NotFoundException
	 * @stream which contains classes for processing sequences of elements
	 * @filter contains only the elements that match the Predicate
	 * 
	 */
	@Autowired
	private JPAStreamer jpaStreamer;

	@Transactional(readOnly = true)
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
		return bookRepository.findAll();

	}

	/**
	 * To update a Book details and save in DB
	 * @param id
	 * @param book
	 * @return
	 */
	@Transactional
	public Book updateBook(Long id, Book book) {
		Book updatebook = bookRepository.getBookById(id);
		log.info("Book updated");
		updatebook.setId(id);
		updatebook.setBookName(book.getBookName());
		updatebook.setNumberOfCopies(book.getNumberOfCopies());
		return bookRepository.save(updatebook);
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
