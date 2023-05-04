package com.payment.streams.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payment.streams.model.Seat;




@Repository
public interface SeatRepository extends JpaRepository<Seat, Long>{

}
