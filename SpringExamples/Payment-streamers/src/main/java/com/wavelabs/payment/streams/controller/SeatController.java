package com.wavelabs.payment.streams.controller;

import javassist.NotFoundException;
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
import com.wavelabs.payment.streams.model.Seat;
import com.wavelabs.payment.streams.model.User;
import com.wavelabs.payment.streams.service.SeatService;
import com.wavelabs.payment.streams.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/seats")
@RestController
public class SeatController {
	@Autowired
    private  SeatService seatService;

	 @PostMapping
	    public Seat saveSeat(@RequestBody Seat seat){
	        return seatService.saveSeat(seat);
	    }
    @GetMapping
    public ResponseEntity<List<Seat>> findAllSeats() {

        return ResponseEntity.ok(seatService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> findSeatById(@PathVariable  Long id) throws NotFoundException {

        return ResponseEntity.ok(seatService.findById(id));
    }

}
