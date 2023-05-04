package com.bookstore.service.prices.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.service.prices.dto.PriceDto;
import com.bookstore.service.prices.entities.Price;
import com.bookstore.service.prices.services.PriceService;



@RestController

public class PricesController {
	private static final Logger log = LoggerFactory.getLogger(PricesController.class);

	@Autowired
    private PriceService priceService;

	 @PostMapping
	    public Price savePayment(@RequestBody Price payment){
	        return priceService.savePayment(payment);
	    }
}
