package com.refactorizando.sample.jpastreamer.service;

import com.refactorizando.sample.jpastreamer.model.Seat;
import com.speedment.jpastreamer.application.JPAStreamer;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SeatService {

    private final JPAStreamer jpaStreamer;

    public List<Seat> findAll() {

        return jpaStreamer.stream(Seat.class).collect(Collectors.toList());

    }

    public List<Seat> findByReference(String ref) {

        return jpaStreamer.stream(Seat.class)
                .filter(seat-> seat.getRef().equalsIgnoreCase(ref))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Seat findById (Long id) {

        return jpaStreamer.stream(Seat.class).filter(seat -> seat.getId().equals(id))
                .findFirst()
                .orElseThrow(()-> new NotFoundException("seat not found"));
    }

}
