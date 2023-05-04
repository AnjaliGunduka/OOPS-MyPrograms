package com.bookstore.service.prices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bookstore.service.prices.dto.PriceDto;
import com.bookstore.service.prices.entities.Price;
import com.bookstore.service.prices.exceptions.AppException;
import com.bookstore.service.prices.respositories.PriceRepository;


@Service
public class PriceService {
	@Autowired
    private  PriceRepository priceRepository;
	public Price savePayment(Price payment) {
		return priceRepository.save(payment);
	}
	

   
}
