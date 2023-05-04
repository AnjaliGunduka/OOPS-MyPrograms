package com.book.order.service.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.book.order.service.demo.model.Book;
import com.book.order.service.demo.model.Order;
import com.book.order.service.demo.repository.BookRepository;
import com.book.order.service.demo.repository.OrderRepository;

@RestController
public class CustomerController {

	@Autowired
	BookRepository bookRepository;
	@Autowired
	OrderRepository orderRepository;

	/*
	 * This End point is responsible for fetching all books available in our
	 * database.
	 */
	@RequestMapping(value = "/getBooks", method = RequestMethod.GET, produces = "application/json")
	public List<Book> getBooks() {
		List<Book> li = new ArrayList<Book>();
		bookRepository.findAll().forEach(li::add);
		return li;
	}

	/*
	 * This End point is responsible for fetching books that have been borrowed
	 */
	@RequestMapping(value = "/getBookingDetails", method = RequestMethod.GET, produces = "application/json")
	public List<Order> getBookingDetails() {
		List<Order> li = new ArrayList<Order>();
		orderRepository.findAll().forEach(li::add);
		return li;
	}

	/*
	 * This End point is responsible for fetching total number of books.
	 */

	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = "application/json")
	public long countNoofBooks() {
		return bookRepository.count();
	}

	/*
	 * This End point is responsible for deleting existing book.
	 */

	@RequestMapping(value = "/delBook", method = RequestMethod.POST, produces = "application/json")
	public void delBooks(@RequestBody List<Book> books) {
		System.out.println(books);
		bookRepository.deleteAll(books);

	}

	@RequestMapping(value = "/makeBooking", method = RequestMethod.POST, produces = "application/json")
	public void makeBooking(@RequestBody Order orderDetails) {
		orderRepository.save(orderDetails);

	}

	@RequestMapping(value = "/cancelBooking", method = RequestMethod.POST, produces = "application/json")
	public void cancelBooking(@RequestBody String orderDetails) {
		System.out.println(orderDetails.split(":")[0]);
		orderRepository.deleteByOrderId(orderDetails);

	}

}
