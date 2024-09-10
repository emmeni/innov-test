package com.emmeni.innov.domain.dto;


import com.emmeni.innov.domain.Ticket;
import com.emmeni.innov.domain.User;
import com.emmeni.innov.enumeration.TicketStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

	@JsonIgnore
	private Long id;

    private Long userId;

	private String title;

	private String description;

	private TicketStatus status;

	@JsonIgnore
	@ToString.Exclude
	private User user;
	
	public Ticket entity() {
		Ticket ticket = Ticket.builder().id(id).userId(userId).title(title).description(description).status(status).user(user).build();
		return ticket;
	}
}
