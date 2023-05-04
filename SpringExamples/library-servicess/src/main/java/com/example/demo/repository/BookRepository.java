package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Book;
import com.example.demo.Request.BookRequest;

public interface BookRepository extends JpaRepository<Book, Long> {

	BookRequest deleteById(String id);

	
}
