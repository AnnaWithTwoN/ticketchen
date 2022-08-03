package com.annawithtwon.ticketchen.event;

import com.annawithtwon.ticketchen.artist.Artist;
import com.annawithtwon.ticketchen.artist.ArtistService;
import com.annawithtwon.ticketchen.exception.ErrorMessage;
import com.annawithtwon.ticketchen.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ArtistService artistService;

    @Autowired
    public EventService(EventRepository eventRepository, ArtistService artistService) {
        this.eventRepository = eventRepository;
        this.artistService = artistService;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getOneEvent(UUID id) {
        Optional<Event> event = eventRepository.findById(id) ;
        if (!event.isPresent()) {
            throw new ResourceNotFoundException(ErrorMessage.EVENT_NOT_FOUND);
        }
        return event.get();
    }

    public Event createEvent(EventCreateRequestBody event) {
        if (event.getParticipatingArtistIds().isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessage.ARTIST_EXISTS);
        }
        Event newEvent = new Event(
                event.getName(),
                event.getLocation(),
                event.getDate());
        for (UUID artistId : event.getParticipatingArtistIds()) {
            Artist artist = artistService.getOneArtist(artistId);
            // TODO: check if artist already has event on the date
            newEvent.addParticipatingArtist(artist);
        }
        return eventRepository.save(newEvent);
    }
}
