package com.emmeni.innov.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.emmeni.innov.domain.base.Persistent;
import com.emmeni.innov.domain.mapping.CustomMapper;
import com.emmeni.innov.exception.InnovException;
import com.emmeni.innov.service.PersistentService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseController<P extends Persistent, T> {

	@Autowired
	protected MessageSource messageSource;

	protected abstract PersistentService<P> getService();
	
	protected abstract CustomMapper<P, T> getMapper();

	@GetMapping("/{id}")
	@Operation(summary = "Load item", description = "Method to load item by id")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		log.debug("Call of get by Id : {}", id);
		try {
			P item = getService().getById(id);
			log.debug("Found : {}", item);
			return ResponseEntity.status(HttpStatus.OK).body(item);
		} catch (InnovException e) {
			log.debug(e.getMessage());
			return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
		} catch (Exception e) {
			log.error("Unexpected error while retrieving entity with id : {}", id, e);
			String message = messageSource.getMessage("unable.to.retieve.entity.by.id", new Object[] { id }, LocaleContextHolder.getLocale());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}
	
	@GetMapping
	@Operation(summary = "Load items", description = "Method to load all items")
    public ResponseEntity<?> getAll() {
		log.debug("Call of list entity");
		try {
			List<P> items = getService().getRepository().findAll();
			return ResponseEntity.status(HttpStatus.OK).body(items);
		} catch (InnovException e) {
			log.debug(e.getMessage());
			return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
		} catch (Exception e) {
			log.error("Unexpected error while get all entities", e);
			String message = messageSource.getMessage("unable.to.find.items", new Object[] {}, LocaleContextHolder.getLocale());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	@PostMapping
	@Operation(summary = "Save item", description = "Method to save item")
	public ResponseEntity<?> save(@Valid @RequestBody T entity) {
		log.debug("Call of save entity : {}", entity);
		try {
			P item = getService().save(getMapper().fromDto(entity), LocaleContextHolder.getLocale());
			item = getService().getById((Long) item.getId());
			return ResponseEntity.status(HttpStatus.OK).body(getMapper().toDto(item));
		} catch (InnovException e) {
			log.debug(e.getMessage());
			return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
		} catch (Exception e) {
			log.error("Unexpected error while save entity : {}", entity, e);
			String message = messageSource.getMessage("unable.to.save.entity", new Object[] { entity }, LocaleContextHolder.getLocale());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update item", description = "Method to update item")
    public ResponseEntity<?> Update(@PathVariable("id") Long id, @RequestBody T body) {
		log.debug("Call of put by Id : {}", id);
		try {
			P item = getService().getById(id);
			if (item == null) {
				String message = messageSource.getMessage("item.not.found", new Object[] { body }, LocaleContextHolder.getLocale());
				throw new InnovException(message);
			}
			item = getMapper().fromDto(body);
			item.setId(id);
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

}
