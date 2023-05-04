package com.book.order.service.demo.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.order.service.demo.entity.Order;
import com.book.order.service.demo.request.OrderRequest;
import com.book.order.service.demo.service.OrderService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/orderService/v1/order")
@Api(value = "Order Management", tags = { "Order Management" })
@Validated
public class OrderController {
	@Autowired
	OrderService orderService;
	@PostMapping(value = "/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderRequest> makeOrder(@Valid @RequestBody OrderRequest orderRequest){
		
		Order order = orderService.makeOrder(orderRequest);
		ModelMapper modelMapper = new ModelMapper();
		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(order, OrderRequest.class));
	}
	}
	
	

