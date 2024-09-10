package com.emmeni.innov.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.emmeni.innov.domain.Ticket;
import com.emmeni.innov.domain.User;
import com.emmeni.innov.enumeration.TicketStatus;
import com.emmeni.innov.repository.TicketRepository;
import com.emmeni.innov.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private TicketRepository ticketRepository;
	
	@InjectMocks
	private UserService userService;

	private User user;
	
	private UserRepository getRepository() {
		return userRepository;
	}

	@BeforeEach
	@DisplayName("Initialise mocks objects for user service and create one ticket")
    void setup(){
        MockitoAnnotations.openMocks(this);
    	user = User.builder().id(1L).username("tintin").email("tintin@gmail.com").password("tintin").tickets(List.of()).build();
    }
    
	@Test
    @DisplayName("JUnit test to find user by username method")
    void testFindByUsername() {
    	given(getRepository().findByEmailOrUsername(user.getUsername(), user.getUsername()))
        .willReturn(Optional.ofNullable(user));
    	
        var item = userService.findByUsername("tintin", Locale.getDefault());

        assertThat(item).isNotNull();
        assertThat(item.getId()).isNotNull();
        assertThat(item.getUsername()).isEqualTo(user.getUsername());
    }

	@SuppressWarnings("unchecked")
	@Test
    @DisplayName("JUnit test to find tickets for an user by his id method")
    void testGetTicketsByUserId() {
		
		Ticket ticket1 = Ticket.builder().id(1L).title("Alcohool").description("Drinking is bad").status(TicketStatus.FINISHED).user(user).build();
		Ticket ticket2 = Ticket.builder().id(2L).title("Bread").description("Food").status(TicketStatus.IN_PROGRESS).user(user).build();
		
		given(ticketRepository.findAll(any(Specification.class))).willReturn(List.of(ticket1, ticket2));		
		
	    var  tickets = userService.getTicketsByUserId(user.getId());
	    
        assertThat(tickets).isNotNull();
        assertThat(tickets.size()).isEqualTo(2);
        verify(ticketRepository, times(1)).findAll(any(Specification.class));
	}
}
