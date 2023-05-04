package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Book;
import com.example.demo.Exception.NotFoundException;
import com.example.demo.Request.BookRequest;
import com.example.demo.repository.BookRepository;
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
	 * Creates Book and saves into the Map
	 * 
	 * BookRequest
	 * @return createdBook
	 */

	public Book createBook(BookRequest bookRequest) {
		ModelMapper modelMapper = new ModelMapper();
		Book book = modelMapper.map(bookRequest, Book.class);
		log.info("Creating Book");
		Book createdBook = bookRepository.save(book);
		log.info("Book created");
		return createdBook;
	}

	/**
	 * 
	 * Gets Book based on the Book Id using jpastreamer to Avoid complex and
	 * repetitive code
	 * 
	 * @return
	 * @stream which contains classes for processing sequences of elements
	 * @filter contains only the elements that match the Predicate
	 * 
	 */
	public Book getBookById(Long id) {
		return jpaStreamer.stream(Book.class).filter(book -> book.getId().equals(id)).findFirst()
				.orElseThrow(() -> new NotFoundException("Book not found"));
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
	public Book updateBook(Long id, BookRequest updatebookRequest) {
		ModelMapper modelMapper = new ModelMapper();
		Book book = modelMapper.map(updatebookRequest, Book.class);
		log.info("updating Book");
		Book updatedBook = bookRepository.save(book);
		log.info("Book updated");
		return updatedBook;
	}

	/**
	 * 
	 * Delete the Books
	 */
	public void deleteBook(Long  id) {
		 bookRepository.deleteById(id);
		 
	}

	

}
