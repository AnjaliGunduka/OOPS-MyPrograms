package com.tem.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/customerService/v1")
@Api(value = "Customer Management", tags = { "Customer Management" })
@Validated
public class CustomerController {
	
}
