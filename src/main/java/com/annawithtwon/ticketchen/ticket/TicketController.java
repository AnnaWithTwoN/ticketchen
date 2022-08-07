package com.annawithtwon.ticketchen.ticket;

import com.annawithtwon.ticketchen.common.PaginatedResponseDTO;
import com.annawithtwon.ticketchen.ticket.dto.TicketCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public PaginatedResponseDTO<Ticket> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) boolean desc
    ) {
        return ticketService.getAllTickets(page, size, sort, desc);
    }

    @GetMapping(path = "{ticketId}")
    public Ticket getOneTicket(@PathVariable("ticketId") UUID id) {
        return ticketService.getOneTicket(id);
    }

    @PostMapping
    public Ticket createTicket(@Valid @RequestBody TicketCreateDTO ticket) {
        return ticketService.createTicket(ticket);
    }

}
