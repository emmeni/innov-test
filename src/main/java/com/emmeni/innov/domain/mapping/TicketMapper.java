package com.emmeni.innov.domain.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.emmeni.innov.domain.Ticket;
import com.emmeni.innov.domain.dto.TicketDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TicketMapper extends CustomMapper<Ticket, TicketDto> {
	
}
