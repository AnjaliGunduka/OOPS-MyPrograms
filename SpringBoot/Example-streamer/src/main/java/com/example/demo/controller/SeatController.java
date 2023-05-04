package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.entity.Seat;
import com.example.demo.service.SeatService;

@RestController
@RequestMapping("/seats")
public class SeatController {
	 @Autowired
	 SeatService seatService;
	 
	 
	 
	 @PostMapping
	    public Seat savePayment(@RequestBody Seat seat){
	        return seatService.saveSeat(seat);
	    }
	 
	 @PostMapping("/ids")
	    public  List<Seat> getSeatsByIds(@RequestBody List<Long> ids){
	        return seatService.getSeatssByIds(ids);
	    }
	 
	 @GetMapping("/seatssAll")
	    public List<Seat> getPayments() {
	        return seatService.getSeats();
	    }
}
