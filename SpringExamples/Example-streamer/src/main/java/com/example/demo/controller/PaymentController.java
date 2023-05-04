package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.entity.Payment;
import com.example.demo.service.PaymentService;



@RestController
@RequestMapping("/payments")
public class PaymentController {
	 @Autowired
	 PaymentService paymentService;
	 
	 @PostMapping
	    public Payment savePayment(@RequestBody Payment payment){
	        return paymentService.savePayment(payment);
	    }
	 
	 @PostMapping("/ids")
	    public  List<Payment> getPaymentsByIds(@RequestBody List<Long> ids){
	        return paymentService.getPaymentsByIds(ids);
	    }
	 
	 @GetMapping("/paymentsAll")
	    public List<Payment> getPayments() {
	        return paymentService.getPayments();
	    }
	 
	
}
