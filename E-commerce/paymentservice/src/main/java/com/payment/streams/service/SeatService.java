package com.payment.streams.service;

import com.payment.streams.model.Seat;
import com.payment.streams.repo.SeatRepository;
import com.payment.streams.response.SeatResponse;
import com.speedment.jpastreamer.application.JPAStreamer;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SeatService {
	@Autowired
	JPAStreamer jpaStreamer;
	@Autowired
	SeatRepository seatRepository;
	public Seat saveSeat(Seat seat) {
		return seatRepository.save(seat);
	}

	public List<Seat> findAll() {

		return jpaStreamer.stream(Seat.class).collect(Collectors.toList());

	}

	public List<SeatResponse> findByReference(String ref) {

		return jpaStreamer.stream(SeatResponse.class).filter(seat -> seat.getRef().equalsIgnoreCase(ref))
				.collect(Collectors.toList());
	}

	@SneakyThrows
	public Seat findById(Long id) throws NotFoundException {

		return jpaStreamer.stream(Seat.class).filter(seat -> seat.getId().equals(id)).findFirst()
				.orElseThrow(() -> new NotFoundException("seat not found"));
	}

}
