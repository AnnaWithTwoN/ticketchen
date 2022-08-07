package com.annawithtwon.ticketchen.event;

import com.annawithtwon.ticketchen.artist.Artist;
import com.annawithtwon.ticketchen.artist.ArtistService;
import com.annawithtwon.ticketchen.common.PaginatedResponseDTO;
import com.annawithtwon.ticketchen.event.dto.EventCreateDTO;
import com.annawithtwon.ticketchen.exception.ErrorMessage;
import com.annawithtwon.ticketchen.exception.ParameterMissingException;
import com.annawithtwon.ticketchen.exception.ResourceNotFoundException;
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
public class EventService {

    private final EventRepository eventRepository;
    private final ArtistService artistService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public EventService(EventRepository eventRepository, ArtistService artistService) {
        this.eventRepository = eventRepository;
        this.artistService = artistService;
    }

    public PaginatedResponseDTO<Event> getAllEvents(int page, int size, String sort, boolean desc) {
        PageRequest request = PageRequest.of(page, size);
        if (sort != null) {
            request = request.withSort(
                    desc ? Sort.Direction.DESC : Sort.Direction.ASC,
                    sort
            );
        }
        Page<Event> response = eventRepository.findAll(request);
        return new PaginatedResponseDTO(
                response.toList(),
                response.getNumber(),
                response.getSize(),
                response.getTotalPages()
        );
    }

    public Event getOneEvent(UUID id) {
        Optional<Event> event = eventRepository.findById(id) ;
        if (!event.isPresent()) {
            throw new ResourceNotFoundException(ErrorMessage.EVENT_NOT_FOUND);
        }
        return event.get();
    }

    public Event createEvent(EventCreateDTO event) {
        Event newEvent = modelMapper.map(event, Event.class);
        if (event.getParticipatingArtistIds() == null) {
            throw new ParameterMissingException(ErrorMessage.PARAMETER_MISSING);
        }
        for (UUID artistId : event.getParticipatingArtistIds()) {
            Artist artist = artistService.getOneArtist(artistId);
            // TODO: check if artist already has event on the date
            newEvent.addParticipatingArtist(artist);
        }
        return eventRepository.save(newEvent);
    }
}
