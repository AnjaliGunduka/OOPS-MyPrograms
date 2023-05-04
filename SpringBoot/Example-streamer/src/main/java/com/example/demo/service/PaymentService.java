package com.example.demo.service;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Payment$;
import com.example.demo.entity.Payment;
import com.example.demo.repository.PaymentRepository;

import com.speedment.jpastreamer.application.JPAStreamer;

@Service
public class PaymentService {
	@Autowired
	private PaymentRepository paymentrepository;

	@Autowired
	private JPAStreamer jpaStreamer;

	public Payment savePayment(Payment payment) {
		return paymentrepository.save(payment);
	}

	public List<Payment> getPaymentsByIds(List<Long> ids) {
		return jpaStreamer.stream(Payment.class).filter(Payment$.id.in(ids)).collect(Collectors.toList());

	}

	public List<Payment> getPayments() {
		return jpaStreamer.stream(Payment.class).collect(Collectors.toList());
	}

	

}
