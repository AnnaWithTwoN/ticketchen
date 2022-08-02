package com.annawithtwon.ticketchen.artist;

import com.annawithtwon.ticketchen.exception.ResourceExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {
    @Mock
    private ArtistRepository repository;
    @InjectMocks
    private ArtistService service;

    @Test
    void shouldReturnAllArtists() {
        // arrange
        List<Artist> expectedArtists = List.of(
                new Artist("artist1"),
                new Artist("artist2")
        );
        when(repository.findAll()).thenReturn(expectedArtists);

        // act
        List<Artist> artists = service.getArtists();

        // assert
        assertThat(artists).isEqualTo(expectedArtists);
    }

    @Test
    void shouldCreateArtist() {
        // arrange
        Artist expectedArtist = new Artist("artist1");
        when(repository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // act
        Artist artist = service.createArtist(expectedArtist);

        // assert
        assertThat(artist).isEqualTo(expectedArtist);
    }

    @Test
    void shouldThrowArtistExistsException() {
        // arrange
        when(repository.save(any())).thenThrow(new ResourceExistsException("Artist"));
        // TODO: add enum/macro messages
        String expectedMessage = "Artist already exists";

        // act
        // assert
        assertThatThrownBy(() -> service.createArtist(new Artist("artist1")))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage(expectedMessage);
    }
}