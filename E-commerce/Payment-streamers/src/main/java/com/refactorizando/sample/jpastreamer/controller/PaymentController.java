package com.refactorizando.sample.jpastreamer.controller;

import com.refactorizando.sample.jpastreamer.model.Payment;
import com.refactorizando.sample.jpastreamer.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {


    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> findAllPayments() {

        return ResponseEntity.ok(paymentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> findById(@PathVariable Long id) {
        
        return ResponseEntity.ok(paymentService.findById(id));
        
    }


}
