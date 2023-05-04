package com.refactorizando.sample.jpastreamer.controller;

import com.refactorizando.sample.jpastreamer.model.Seat;
import com.refactorizando.sample.jpastreamer.model.User;
import com.refactorizando.sample.jpastreamer.service.SeatService;
import com.refactorizando.sample.jpastreamer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/seats")
@RestController
public class SeatController {

    private final SeatService seatService;


    @GetMapping
    public ResponseEntity<List<Seat>> findAllSeats() {

        return ResponseEntity.ok(seatService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> findSeatById(@PathVariable  Long id) {

        return ResponseEntity.ok(seatService.findById(id));
    }

}
