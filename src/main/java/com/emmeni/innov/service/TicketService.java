package com.emmeni.innov.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.emmeni.innov.domain.Ticket;
import com.emmeni.innov.exception.InnovException;
import com.emmeni.innov.repository.TicketRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TicketService extends PersistentService<Ticket> {
	
	@Autowired
	private TicketRepository ticketRepository;

	@Override
	public TicketRepository getRepository() {
		return ticketRepository;
	}

	public Ticket getById(Long id) {
		log.debug("Try to get by id : {}", id);
		if (getRepository() == null && id != null) {
			return null;
		}
		Optional<Ticket> item = getRepository().findById(id);
		if (item.isPresent()) {
			Ticket ticket = item.get();
			if (ticket.getUser() != null) {
				ticket.setUserId(ticket.getUser().getId());
			}
			return ticket;
		}
		return null;
	}

	public void delete(Long id) {
		if (getRepository().existsById(id)) {
			getRepository().deleteById(id);
        } else {
			String message = messageSource.getMessage("ticket.not.found", new Object[] { }, LocaleContextHolder.getLocale());
			throw new InnovException(message);
        }
	}

}
