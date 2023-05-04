package com.book.order.service.demo.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.order.service.demo.cleint.OrderClient;
import com.book.order.service.demo.entity.Order;
import com.book.order.service.demo.mapper.OrderMapper;
import com.book.order.service.demo.repository.OrderRepository;
import com.book.order.service.demo.request.OrderRequest;
import com.book.order.service.demo.response.BookResponse;





@Service
public class OrderService {
	@Autowired
	OrderClient orderClient;

	@Autowired
	OrderRepository orderRepository;

	
	@Autowired
	OrderMapper orderMapper;
	private static final Logger log = LoggerFactory.getLogger(OrderService.class);
	
	public Order makeOrder(OrderRequest orderRequest)
	{
		ModelMapper modelMapper = new ModelMapper();
		BookResponse bookRequest= modelMapper.map(orderRequest, BookResponse .class);
		log.info("Order initiated");
		try {
			BookResponse bookResponse = orderClient.adding(bookRequest);
			log.info("Order from book");
			Order  order=orderMapper.mapBookResponseToOrder(bookResponse,orderRequest.getBookId());
			return orderRepository.save(order);
	}catch (Exception e) {
		e.printStackTrace();
	}
		return null;
	}
	
	
	

	
	
	
	
	
	
	
	
}
