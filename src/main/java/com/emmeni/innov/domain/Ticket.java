package com.emmeni.innov.domain;

import com.emmeni.innov.domain.base.Persistent;
import com.emmeni.innov.domain.dto.TicketDto;
import com.emmeni.innov.enumeration.TicketStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "Ticket")
@Table(name = "INNOV_TICKET")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Ticket extends Persistent {

	private static final long serialVersionUID = -3436977487218641349L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String description;

	@Enumerated(EnumType.STRING)
	private TicketStatus status;

	@JsonIgnore
	@ToString.Exclude
	@EqualsAndHashCode.Exclude	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

    @Transient
    private Long userId;

	@PostLoad
	public void SetTransientId() {
		if (this.getUser() != null) {
			this.setUserId(this.getUser().getId());
		}
	}
	
	public TicketDto entity() {
		TicketDto ticket = new TicketDto();
		ticket.setId(id);
		ticket.setUserId(userId);
		ticket.setTitle(title);
		ticket.setDescription(description);
		ticket.setStatus(status);
		ticket.setUser(user);
		return ticket;
	}

	@Override
	public Ticket copy() {
		Ticket ticket = Ticket.builder().title(title).description(description).status(status).build();
		return ticket;
	}

}
