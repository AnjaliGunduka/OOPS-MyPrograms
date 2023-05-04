package com.book.order.service.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.book.order.service.demo.entity.Order;


public interface BookRepository extends CrudRepository<Order, Long> {

	
// List<Book> findBook(String criteria, String type);
}
