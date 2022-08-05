package com.annawithtwon.ticketchen.artist;

import com.annawithtwon.ticketchen.event.Event;
import com.annawithtwon.ticketchen.event.EventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// not DataJpaTest because @Feature doesn't seem to work with h2 database
@SpringBootTest
class ArtistRepositoryTest {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ArtistRepository artistRepository;

    @AfterEach
    private void truncateDb() {
        eventRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    void shouldReturnArtistWithCorrectNumberOfEvents() {
        // arrange
        Artist artist = new Artist("name");
        artistRepository.save(artist);
        eventRepository.save(new Event("name", "location", OffsetDateTime.now(), Set.of(artist)));
        eventRepository.save(new Event("name", "location", OffsetDateTime.now(), Set.of(artist)));

        // act
        Optional<Artist> actualArtist = artistRepository.findById(artist.getId());

        // assert
        assertThat(actualArtist).isPresent();
        assertThat(actualArtist.get().getNumberOfEvents()).isNotNull();
        assertThat(actualArtist.get().getNumberOfEvents()).isEqualTo(2);
    }

}