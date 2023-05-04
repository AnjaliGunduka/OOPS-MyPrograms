package com.book.order.service.demo.cleint;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.book.order.service.demo.response.BookResponse;





@FeignClient(url = "${bookservicess.domain}", name = "ORDER-CLIENT")
public interface OrderClient {
	@RequestMapping(value = "/bookServicess/v1/books/{id}", method = RequestMethod.POST, consumes = "application/json")
	public BookResponse adding(@RequestBody BookResponse bookResponse);
	
	
	
}
