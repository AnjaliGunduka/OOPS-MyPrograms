package com.book.order.service.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.book.order.service.demo.entity.Order;
import com.book.order.service.demo.repository.BookRepository;
import com.book.order.service.demo.repository.OrderRepository;

@RestController
public class CustomerController {

	@Autowired
	BookRepository bookRepository;
	@Autowired
	OrderRepository orderRepository;

	

	@RequestMapping(value = "/getBooks", method = RequestMethod.GET, produces = "application/json")
	public List<Order> getBooks() {
		List<Order> li = new ArrayList<Order>();
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
	public void delBooks(@RequestBody List<Order> books) {
		System.out.println(books);
		bookRepository.deleteAll(books);

	}

	@RequestMapping(value = "/makeBooking", method = RequestMethod.POST, produces = "application/json")
	public void makeBooking(@RequestBody Order orderDetails) {
		orderRepository.save(orderDetails);

	}

//	@RequestMapping(value = "/cancelBooking", method = RequestMethod.POST, produces = "application/json")
//	public void cancelBooking(@RequestBody Long orderDetails) {
//	System.out.println(orderDetails.longValue());
//		orderRepository.deleteById(orderDetails);
//
//	}
	
	
	


	
}
