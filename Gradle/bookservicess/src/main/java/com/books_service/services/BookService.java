package com.books_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Service;

import com.books_service.Entity.Book;
import com.books_service.Entity.BookResponse;
import com.books_service.Repository.BookRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class BookService {
	private static final Logger log = LoggerFactory.getLogger(BookService.class);

	@Autowired
	private BookRepository bookRepository;

	public List<Book> getAllBooks() {
		List<Book> books = new ArrayList<>();
//		Book b1 = new Book();
//		b1.add(books);
		bookRepository.findAll().forEach(books::add);	
		return books;
	}

	public List<Book> getAllBookss() {
		List<Book> books = new ArrayList<>();
		bookRepository.findAll().forEach(books::add);
		return books.stream().toList();
	}

	public Book getBook(String id) {
		return bookRepository.findById(id).orElseGet(Book::new);
	}

	public void addBook(Book book) {
		bookRepository.save(book);
	}

	public void updateBook(String id, Book whiskey) {
		bookRepository.save(whiskey);
	}

	
	public void deleteBook(String id) {
		bookRepository.deleteById(id);
	}

	


}
//
//public long getAllBookss() {
//List<Book> books = new ArrayList<>();
//bookRepository.findAll().forEach(books::add);
//return books.stream().count();
//}

//public Book getBooks(String id) {
//List<Book> list = new ArrayList<>();
//list.stream().filter(Book -> Book.getId() == Book.getId()).forEach(Book -> System.out.println("name of student:-" + list));
//return bookRepository.findById(id).orElseGet(Book::new);
//}
