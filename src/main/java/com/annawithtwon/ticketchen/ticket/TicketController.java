package com.annawithtwon.ticketchen.ticket;

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
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
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
