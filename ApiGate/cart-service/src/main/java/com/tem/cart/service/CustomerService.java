package com.tem.cart.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.tem.cart.client.CustomerClient;
import com.tem.cart.repo.CustomerRepository;



public class CustomerService {
	@Autowired
	CustomerClient  customerClient;

	@Autowired
	CustomerRepository customerRepository;

	
}
