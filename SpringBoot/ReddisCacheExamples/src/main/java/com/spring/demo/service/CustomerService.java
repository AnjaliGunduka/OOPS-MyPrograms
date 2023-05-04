package com.spring.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.spring.demo.entity.Customer;

@Service
@CacheConfig(cacheNames= {"Customer"})
public class CustomerService {

	private final Logger log = LoggerFactory.getLogger(Customer.class);

	
	@Cacheable(key="#id")
	public Customer getCustomerById(String id) {
		log.info("Getting Informtion"+id);
		return new Customer("userId", "user@gmail.com", id);

	}

}
