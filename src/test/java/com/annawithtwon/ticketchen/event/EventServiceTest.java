package com.annawithtwon.ticketchen.event;

import com.annawithtwon.ticketchen.artist.Artist;
import com.annawithtwon.ticketchen.artist.ArtistService;
import com.annawithtwon.ticketchen.exception.ErrorMessage;
import com.annawithtwon.ticketchen.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private ArtistService artistService;

    @InjectMocks
    private EventService eventService;

    @Test
    void shouldThrowExWhenEventsDoesNotExist() {
        // arrange
        UUID randomId = UUID.randomUUID();
        when(eventRepository.findById(randomId)).thenThrow(new ResourceNotFoundException(ErrorMessage.EVENT_NOT_FOUND));
        String expectedMessage = ErrorMessage.EVENT_NOT_FOUND.toString();

        // act
        // assert
        assertThatThrownBy(() -> eventService.getOneEvent(randomId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(expectedMessage);
    }

    @Test
    void shouldCreateArtist() {
        // arrange
        Event expectedEvent = new Event(
                "name",
                "location",
                OffsetDateTime.now(),
                Set.of(new Artist("artist"))
        );
        when(eventRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // act
        Event actualEvent = eventService.createEvent(expectedEvent);

        // assert
        assertThat(actualEvent).isEqualTo(expectedEvent);
    }

    /*@Test
    void shouldThrowExWhenArtistDoesNotExist() {
        // arrange
        Event event = new Event(
                "name",
                "location",
                OffsetDateTime.now(),
                Set.of(new Artist("artist"))
        );
        when(artistService.getOneArtist(any())).thenThrow(new ResourceNotFoundException(ErrorMessage.ARTIST_NOT_FOUND));
        String expectedMessage = ErrorMessage.ARTIST_NOT_FOUND.toString();

        // act
        // assert
        assertThatThrownBy(() -> eventService.createEvent(event))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(expectedMessage);
    }*/
}