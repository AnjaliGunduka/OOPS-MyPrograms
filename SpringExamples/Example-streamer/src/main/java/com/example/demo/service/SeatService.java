package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Seat$;

import com.example.demo.entity.Seat;
import com.example.demo.repository.SeatRepository;
import com.speedment.jpastreamer.application.JPAStreamer;

@Service
public class SeatService {
	@Autowired
	private SeatRepository seatrepository;

	@Autowired
	private JPAStreamer jpaStreamer;

	public Seat saveSeat(Seat seat) {
		return seatrepository.save(seat);
	}
	
	public List<Seat> getSeats() {
		return jpaStreamer.stream(Seat.class).collect(Collectors.toList());
	}
	
	public List<Seat> getSeatssByIds(List<Long> ids) {
		return jpaStreamer.stream(Seat.class).filter(Seat$.id.in(ids)).collect(Collectors.toList());

	}

}
