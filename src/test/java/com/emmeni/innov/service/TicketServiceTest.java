package com.emmeni.innov.service;

// import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.emmeni.innov.domain.Ticket;
import com.emmeni.innov.enumeration.TicketStatus;
import com.emmeni.innov.repository.TicketRepository;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
	
	@Mock
	private TicketRepository ticketRepository;
	
	@InjectMocks
	private TicketService ticketService;
	
	private static Ticket ticket;
	
	private TicketRepository getRepository() {
		return ticketRepository;
	}

	@BeforeEach
	@DisplayName("Initialise mocks objects for ticket service and create one ticket")
    void initialisation() {
        MockitoAnnotations.openMocks(this);
        ticket = Ticket.builder().id(1L).title("Alcohool").description("Drinking is bad").status(TicketStatus.FINISHED).build();
    }
    
    @Test
    @DisplayName("Method test to find a ticke by id")
    void testGetById() {
    	
		given(getRepository().findById(ticket.getId()))
        .willReturn(Optional.ofNullable(ticket));
    	
        var item = ticketService.getById(1L);
        
        assertThat(item).isNotNull();
        assertThat(item.getId()).isNotNull();
        assertThat(item).isEqualTo(ticket);
	}

    @Test
    @DisplayName("Method test to delete a ticke by id")
    void testDelete() {

    	given(getRepository().existsById(1L)).willReturn(true);
        willDoNothing().given(getRepository()).deleteById(1L);
        
        ticketService.delete(ticket.getId());
        
        verify(getRepository(), times(1)).deleteById(ticket.getId());
	}

}
