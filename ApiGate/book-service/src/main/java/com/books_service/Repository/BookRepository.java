package com.books_service.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.books_service.Entity.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, String> {
}

