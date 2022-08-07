package com.annawithtwon.ticketchen.ticket;

import com.annawithtwon.ticketchen.artist.Artist;
import com.annawithtwon.ticketchen.common.PaginatedResponseDTO;
import com.annawithtwon.ticketchen.event.Event;
import com.annawithtwon.ticketchen.event.EventService;
import com.annawithtwon.ticketchen.exception.ErrorMessage;
import com.annawithtwon.ticketchen.exception.ParameterMissingException;
import com.annawithtwon.ticketchen.exception.ResourceNotFoundException;
import com.annawithtwon.ticketchen.ticket.dto.TicketCreateDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventService eventService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public TicketService(TicketRepository ticketRepository, EventService eventService) {
        this.ticketRepository = ticketRepository;
        this.eventService = eventService;
    }

    public PaginatedResponseDTO<Ticket> getAllTickets(int page, int size, String sort, boolean desc) {
        PageRequest request = PageRequest.of(page, size);
        if (sort != null) {
            request = request.withSort(
                    desc ? Sort.Direction.DESC : Sort.Direction.ASC,
                    sort
            );
        }
        Page<Ticket> response = ticketRepository.findAll(request);
        return new PaginatedResponseDTO(
                response.toList(),
                response.getNumber(),
                response.getSize(),
                response.getTotalPages()
        );
    }

    public Ticket getOneTicket(UUID id) {
        Optional<Ticket> ticket = ticketRepository.findById(id) ;
        if (!ticket.isPresent()) {
            throw new ResourceNotFoundException(ErrorMessage.TICKET_NOT_FOUND);
        }
        return ticket.get();
    }

    public Ticket createTicket(TicketCreateDTO ticket) {
        Ticket newTicket = modelMapper.map(ticket, Ticket.class);
        if (ticket.getEventId() == null) {
            throw new ParameterMissingException(ErrorMessage.PARAMETER_MISSING);
        }
        Event event = eventService.getOneEvent(ticket.getEventId());
        newTicket.setEvent(event);
        return ticketRepository.save(newTicket);
    }
}
