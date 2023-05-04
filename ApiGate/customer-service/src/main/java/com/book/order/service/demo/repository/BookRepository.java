package com.book.order.service.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.book.order.service.demo.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
// List<Book> findBook(String criteria, String type);
}
