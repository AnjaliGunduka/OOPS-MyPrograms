package com.library.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.example.demo.Entity.Book;
import com.library.example.demo.Request.BookRequest;

public interface BookRepository extends JpaRepository<Book, Long> {

	BookRequest deleteById(String id);

	
}
