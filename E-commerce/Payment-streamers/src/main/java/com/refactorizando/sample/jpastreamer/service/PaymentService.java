package com.refactorizando.sample.jpastreamer.service;

import com.refactorizando.sample.jpastreamer.model.Payment;
import com.refactorizando.sample.jpastreamer.model.Payment$;
import com.speedment.jpastreamer.application.JPAStreamer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final JPAStreamer jpaStreamer;

    @SneakyThrows
    public Payment findById(Long id) {
        return jpaStreamer.stream(of(Payment.class)
                .joining(Payment$.seat)
                .joining(Payment$.user))
                .filter(payment -> payment.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public List<Payment> findAll() {

        return jpaStreamer.stream(Payment.class)
                .collect(Collectors.toList());

    }

}
