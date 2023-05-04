package com.book.order.service.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.book.order.service.demo.entity.Order;
import com.book.order.service.demo.repository.OrderRepository;
import com.book.order.service.demo.service.OrderService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/orderService/v1/order")
@Api(value = "Order Management", tags = { "Order Management" })
@Validated
public class OrderController {
	@Autowired
	OrderService orderService;
	@Autowired
	OrderRepository orderRepository;
	
//	@PostMapping(value = "/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<OrderRequest> makeOrder(@Valid @RequestBody OrderRequest orderRequest){
//		
//		Order order = orderService.makeOrder(orderRequest);
//		ModelMapper modelMapper = new ModelMapper();
//		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(order, OrderRequest.class));
//	}
	@RequestMapping(value = "/makeBooking", method = RequestMethod.POST, produces = "application/json")
	public void makeBooking(@RequestBody Order orderDetails) {
		orderRepository.save(orderDetails);

	}
	}
	
	

