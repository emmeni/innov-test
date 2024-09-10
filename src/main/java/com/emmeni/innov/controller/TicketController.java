package com.emmeni.innov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emmeni.innov.domain.Ticket;
import com.emmeni.innov.domain.User;
import com.emmeni.innov.domain.dto.TicketDto;
import com.emmeni.innov.domain.mapping.TicketMapper;
import com.emmeni.innov.exception.InnovException;
import com.emmeni.innov.service.TicketService;
import com.emmeni.innov.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/tickets")
@RestController
@Slf4j
@Tag(name = "Ticket Api", description = "The Ticket API. Contains the operations to manage tickets.")
public class TicketController extends BaseController<Ticket, TicketDto> {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private TicketMapper customMapper;

	@Override
	protected TicketService getService() {
		return ticketService;
	}

	@Override
	protected TicketMapper getMapper() {
		return customMapper;
	}

	public ResponseEntity<?> save(@RequestBody TicketDto ticket) {
		log.debug("Call of save entity : {}", ticket);
		try {
			Ticket item = getMapper().fromDto(ticket);
			item = getService().save(item, LocaleContextHolder.getLocale());
			return ResponseEntity.status(HttpStatus.OK).body(item);
		} catch (InnovException e) {
			log.debug(e.getMessage());
			return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
		} catch (Exception e) {
			log.error("Unexpected error while save entity : {}", ticket, e);
			String message = messageSource.getMessage("unable.to.save.entity", new Object[] { ticket }, LocaleContextHolder.getLocale());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	@PutMapping("/{id}/assign/{useId}")
	@Operation(summary = "Assign ticket", description = "Method to assign a ticket to user")
    public ResponseEntity<?> Update(@PathVariable("id") Long id, @PathVariable("useId") Long useId) {
		log.debug("Call of assign ticket Id : {}", id);
		try {
			User user = userService.getById(useId);
			if (user == null) {
				String message = messageSource.getMessage("user.not.found", new Object[] { }, LocaleContextHolder.getLocale());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
			}
			Ticket item = getService().getById(id);
			if (item == null) {
				String message = messageSource.getMessage("ticket.not.found", new Object[] { }, LocaleContextHolder.getLocale());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
			}
			item.setUser(user);
			item.setUserId(user.getId());
			item = getService().save(item, LocaleContextHolder.getLocale());
			log.debug("Found : {}", item);
			return ResponseEntity.status(HttpStatus.OK).body(item);
		} catch (InnovException e) {
			log.debug(e.getMessage());
			return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
		} catch (Exception e) {
			log.error("Unexpected error while updating entity with id : {}", id, e);
			String message = messageSource.getMessage("unable.to.save.entity.by.id", new Object[] { id }, LocaleContextHolder.getLocale());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ticket", description = "Method to delete a ticket")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        try {
        	getService().delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
