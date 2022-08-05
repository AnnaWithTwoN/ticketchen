package com.annawithtwon.ticketchen.event;

import com.annawithtwon.ticketchen.artist.Artist;
import com.annawithtwon.ticketchen.artist.ArtistService;
import com.annawithtwon.ticketchen.event.dto.EventCreateDTO;
import com.annawithtwon.ticketchen.exception.ErrorMessage;
import com.annawithtwon.ticketchen.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Array;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

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
    void shouldCreateEvent() {
        // arrange
        UUID artistId = UUID.randomUUID();
        EventCreateDTO expectedEvent = new EventCreateDTO(
                "name",
                "location",
                OffsetDateTime.now(),
                Set.of(artistId)
        );
        when(artistService.getOneArtist(artistId)).thenReturn(new Artist(artistId, "artist"));
        when(eventRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // act
        Event actualEvent = eventService.createEvent(expectedEvent);

        // assert
        assertThat(actualEvent.getName()).isEqualTo(expectedEvent.getName());
        assertThat(actualEvent.getLocation()).isEqualTo(expectedEvent.getLocation());
        assertThat(actualEvent.getDate()).isEqualTo(expectedEvent.getDate());
        UUID[] actualParticipatingArtistIds = actualEvent.getParticipatingArtists().stream().map(a -> a.getId()).toArray(UUID[]::new);
        assertThat(actualParticipatingArtistIds).isEqualTo(expectedEvent.getParticipatingArtistIds().toArray());
    }

    @Test
    void shouldThrowExWhenArtistDoesNotExist() {
        // arrange
        UUID artistId = UUID.randomUUID();
        EventCreateDTO expectedEvent = new EventCreateDTO(
                "name",
                "location",
                OffsetDateTime.now(),
                Set.of(artistId)
        );
        when(artistService.getOneArtist(artistId)).thenThrow(new ResourceNotFoundException(ErrorMessage.ARTIST_NOT_FOUND));
        String expectedMessage = ErrorMessage.ARTIST_NOT_FOUND.toString();

        // act
        // assert
        assertThatThrownBy(() -> eventService.createEvent(expectedEvent))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(expectedMessage);
    }
}