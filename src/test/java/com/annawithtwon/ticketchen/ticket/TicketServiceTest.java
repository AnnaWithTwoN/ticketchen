package com.annawithtwon.ticketchen.ticket;

import com.annawithtwon.ticketchen.event.Event;
import com.annawithtwon.ticketchen.event.EventService;
import com.annawithtwon.ticketchen.exception.ErrorMessage;
import com.annawithtwon.ticketchen.exception.ResourceNotFoundException;
import com.annawithtwon.ticketchen.ticket.dto.TicketCreateDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventService eventService;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void shouldThrowExWhenTicketDoesNotExist() {
        // arrange
        UUID randomId = UUID.randomUUID();
        ErrorMessage expectedMessage = ErrorMessage.TICKET_NOT_FOUND;
        when(ticketRepository.findById(randomId)).thenThrow(new ResourceNotFoundException(expectedMessage));

        // act
        // assert
        assertThatThrownBy(() -> ticketService.getOneTicket(randomId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(expectedMessage.toString());
    }

    @Test
    void shouldCreateTicket() {
        // arrange
        Event event = new Event();
        event.setId(UUID.randomUUID());
        TicketCreateDTO expectedTicket = new TicketCreateDTO(
                event.getId(),
                "main",
                100
        );
        when(eventService.getOneEvent(event.getId())).thenReturn(event);
        when(ticketRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // act
        Ticket actualTicket = ticketService.createTicket(expectedTicket);

        // assert
        assertThat(actualTicket.getSection()).isEqualTo(expectedTicket.getSection());
        assertThat(actualTicket.getPrice()).isEqualTo(expectedTicket.getPrice());
        assertThat(actualTicket.getEvent().getId()).isEqualTo(expectedTicket.getEventId());
    }

    @Test
    void shouldThrowExWhenEventDoesNotExist() {
        // arrange
        UUID eventId = UUID.randomUUID();
        TicketCreateDTO expectedTicket = new TicketCreateDTO(
                eventId,
                "main",
                100
        );
        ErrorMessage expectedMessage = ErrorMessage.EVENT_NOT_FOUND;
        when(eventService.getOneEvent(eventId)).thenThrow(new ResourceNotFoundException(expectedMessage));

        // act
        // assert
        assertThatThrownBy(() -> ticketService.createTicket(expectedTicket))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(expectedMessage.toString());
    }
}