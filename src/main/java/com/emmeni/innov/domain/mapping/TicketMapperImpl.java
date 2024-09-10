package com.emmeni.innov.domain.mapping;

import org.springframework.stereotype.Component;

import com.emmeni.innov.domain.Ticket;
import com.emmeni.innov.domain.dto.TicketDto;

@Component
public class TicketMapperImpl implements TicketMapper {

	@Override
	public TicketDto toDto(Ticket source) {
		return source.entity();
	}

	@Override
	public Ticket fromDto(TicketDto destination) {
		return destination.entity();
	}

}
