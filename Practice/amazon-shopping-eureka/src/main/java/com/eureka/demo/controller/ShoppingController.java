package com.eureka.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ShoppingController {
	@Autowired
	RestTemplate template;
	
	@GetMapping("/amazon-payment/{price}")	
	public String invokePaymentService(@PathVariable int price) {
		String url="http://localhost:8082/payment-provider/payNow/"+price;
		return template.getForObject(url, String.class);
	}
}
