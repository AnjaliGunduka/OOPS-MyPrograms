package com.wavelabs.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wavelabs.library.model.Book;
import com.wavelabs.library.model.IssuedBook;

@Repository
public interface IssuedBookRepository extends JpaRepository<IssuedBook, Long> {
	public Long countByBookAndReturned(Book book, Integer returned);
}
