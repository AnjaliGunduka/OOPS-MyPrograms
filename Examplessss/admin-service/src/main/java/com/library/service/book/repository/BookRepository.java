package com.library.service.book.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.service.book.entity.Book;
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	Optional<Book> findById(Long bookId);

	Optional<Book> findByBookName(String bookName);

	Book getBookById(Long id);

	
	
}
