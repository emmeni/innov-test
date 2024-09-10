package com.emmeni.innov.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emmeni.innov.domain.User;
import com.emmeni.innov.domain.dto.UserDto;
import com.emmeni.innov.domain.mapping.UserMapper;
import com.emmeni.innov.exception.InnovException;
import com.emmeni.innov.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/users")
@RestController
@Tag(name = "User Api", description = "The User API. Contains the operations to manage users.")
@Validated
public class UserController extends BaseController<User, UserDto> {
	
	@Autowired
	private UserService userService;
	
	@Autowired 
    private UserMapper customMapper;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	

	@Override
	protected UserService getService() {
		return userService;
	}
	
	@Override
	protected UserMapper getMapper() {
		return customMapper;
	}

	public ResponseEntity<?> save(@Valid @RequestBody UserDto entity) {
		log.debug("Call of save entity : {}", entity);
		try {
			User user = getService().findByUsername(entity.getUsername(), LocaleContextHolder.getLocale());
			if (user != null) {
				String message = messageSource.getMessage("name.already.exist", new Object[] { entity.getUsername() }, LocaleContextHolder.getLocale());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
			}
			user = getMapper().fromDto(entity);
			user.setPassword(passwordEncoder.encode(user.getPassword()));			
			user = getService().save(user, LocaleContextHolder.getLocale());
			user = getService().getById((Long) user.getId());
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} catch (IllegalArgumentException ex) {
			log.debug(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		} catch (InnovException e) {
			log.debug(e.getMessage());
			return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
		} catch (Exception e) {
			log.error("Unexpected error while save entity : {}", entity, e);
			String message = messageSource.getMessage("unable.to.save.entity", new Object[] { entity }, LocaleContextHolder.getLocale());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}	


	@GetMapping("/{id}/ticket")
	@Operation(summary = "Load tickets", description = "Method to load the user ticket by userId")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		log.debug("Call of get by Id : {}", id);
		try {
			List<?> items = getService().getTicketsByUserId(id);
			log.debug("Found : {}", items);
			return ResponseEntity.status(HttpStatus.OK).body(items);
		} catch (InnovException e) {
			log.debug(e.getMessage());
			return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
		} catch (Exception e) {
			log.error("Unexpected error while retrieving entity with id : {}", id, e);
			String message = messageSource.getMessage("unable.to.retieve.entity.by.id", new Object[] { id }, LocaleContextHolder.getLocale());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

}
