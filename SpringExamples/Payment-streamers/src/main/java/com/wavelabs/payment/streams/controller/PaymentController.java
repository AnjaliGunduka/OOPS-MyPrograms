package com.wavelabs.payment.streams.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wavelabs.payment.streams.model.Payment;
import com.wavelabs.payment.streams.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

	@Autowired
     PaymentService paymentService;
	
	 @PostMapping
	    public Payment savePayment(@RequestBody Payment payment){
	        return paymentService.savePayment(payment);
	    }

    @GetMapping
    public ResponseEntity<List<Payment>> findAllPayments() {

        return ResponseEntity.ok(paymentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> findById(@PathVariable Long id) {
        
        return ResponseEntity.ok(paymentService.findById(id));
        
    }


}
