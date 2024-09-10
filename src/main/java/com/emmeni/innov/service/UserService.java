package com.emmeni.innov.service;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.emmeni.innov.domain.Ticket;
import com.emmeni.innov.domain.User;
import com.emmeni.innov.exception.InnovException;
import com.emmeni.innov.repository.TicketRepository;
import com.emmeni.innov.repository.TicketSpecification;
import com.emmeni.innov.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService extends PersistentService<User> {

	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TicketRepository ticketRepository;

	@Override
	public UserRepository getRepository() {
		return userRepository;
	}

    public User findByUsername(String username, Locale locale) {
    	log.debug("Begin of find user by name method");
    	if (!StringUtils.hasLength(username)) {
			String message = messageSource.getMessage("bad.request.parameter", new Object[] { }, locale);
			throw new InnovException(message);
		}
    	log.debug("End of find user by name method");
    	return getRepository().findByEmailOrUsername(username, username)
    			.orElse(null);
    }

	public List<?> getTicketsByUserId(Long userId) {
		log.debug("Begin of find tickets by user id method");
		Specification<Ticket> spec = Specification.where(null);

        if (userId != null) {
            spec = spec.and(TicketSpecification.hasUserId(userId));
        }
		log.debug("End of find tickets by user id method");
    	return ticketRepository.findAll(spec);
	}

}
