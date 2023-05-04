package com.wavelabs.payment.streams.service;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.wavelabs.payment.streams.model.Payment;
import com.wavelabs.payment.streams.model.Payment$;
import com.wavelabs.payment.streams.repo.PaymentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;

@Service
@RequiredArgsConstructor
public class PaymentService {
	@Autowired
	private JPAStreamer jpaStreamer;
	@Autowired
	PaymentRepository paymentRepository;

	public Payment savePayment(Payment payment) {
		return paymentRepository.save(payment);
	}

	public Payment findById(Long id) {
		return jpaStreamer.stream(of(Payment.class).joining(Payment$.user))
				.filter(payment -> payment.getId().equals(id)).findFirst().orElseThrow();
	}

	
	public List<Payment> findAll() {

		return jpaStreamer.stream(Payment.class).collect(Collectors.toList());

	}

}
