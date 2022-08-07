package com.annawithtwon.ticketchen.event;

import com.annawithtwon.ticketchen.artist.Artist;
import com.annawithtwon.ticketchen.common.PaginatedResponseDTO;
import com.annawithtwon.ticketchen.event.dto.EventCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public PaginatedResponseDTO<Event> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) boolean desc
    ) {
        return eventService.getAllEvents(page, size, sort, desc);
    }

    @GetMapping(path = "{eventId}")
    public Event getOneEvent(@PathVariable("eventId") UUID id) {
        return eventService.getOneEvent(id);
    }

    @PostMapping
    public Event createEvent(@Valid @RequestBody EventCreateDTO event) {
        return eventService.createEvent(event);
    }
}
