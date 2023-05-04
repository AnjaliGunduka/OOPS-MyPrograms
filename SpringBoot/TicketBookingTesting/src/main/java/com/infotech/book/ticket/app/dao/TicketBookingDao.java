package com.infotech.book.ticket.app.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.infotech.book.ticket.app.entities.Ticket;

public interface TicketBookingDao extends JpaRepository<Ticket, Integer>{
	Ticket findByEmail(String email);
	Ticket findByIds(Integer ticketId);

	Ticket save(Optional<Ticket> ticketFromDb);
}
