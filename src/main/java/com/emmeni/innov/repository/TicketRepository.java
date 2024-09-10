package com.emmeni.innov.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emmeni.innov.domain.Ticket;

public interface TicketRepository extends PersistentRepository<Ticket> {
	
	@Query(value = "SELECT t FROM com.emmeni.innov.domain.Ticket t Where t.user.id = :userId")
	public List<?> getTicketsForUser(@Param("userId") Long userId);
}
