package com.student.library.services.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.student.library.services.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	Optional<Book> findById(Long bookId);

	
}
