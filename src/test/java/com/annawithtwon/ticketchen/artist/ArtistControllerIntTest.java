package com.annawithtwon.ticketchen.artist;

import com.annawithtwon.ticketchen.event.Event;
import com.annawithtwon.ticketchen.event.EventRepository;
import com.annawithtwon.ticketchen.exception.ErrorMessage;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArtistControllerIntTest {

    private final String UrlPath = "/artists";

    private String jwtAdminToken = "Bearer " + JWT.create()
            .withSubject("username")
            .withClaim("role", "admin")
            .sign(Algorithm.HMAC256("poke".getBytes()));

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private EventRepository eventRepository;

    @AfterEach
    private void truncateDb() {
        eventRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    void shouldGetAllArtists() throws Exception {
        // arrange
        List<Artist> expectedArtists = List.of(
                new Artist("artist1"),
                new Artist("artist2"),
                new Artist("artist3")
        );
        artistRepository.saveAll(expectedArtists);

        // act - assert
        mockMvc.perform(
                    get(UrlPath)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.*", hasSize(3)))
                .andExpect(jsonPath("$.items.*.id").exists())
                .andExpect(jsonPath("$.items.*.name").exists())
                .andExpect(jsonPath("$.items.*.numberOfEvents").exists());
    }

    @Test
    void shouldGetOneArtist() throws Exception {
        Artist artist = artistRepository.save(new Artist("artist"));

        mockMvc.perform(
                    get(UrlPath + "/" + artist.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(artist.getId().toString()))
                .andExpect(jsonPath("$.name").value(artist.getName()))
                .andExpect(jsonPath("$.numberOfEvents").value(0));
    }

    @Test
    void shouldGetArtistWithRightNumberOfEvents() throws Exception {
        Artist artist = artistRepository.save(new Artist("artist"));
        eventRepository.save(new Event("name", "location", OffsetDateTime.now(), Set.of(artist)));

        mockMvc.perform(
                        get(UrlPath + "/" + artist.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(artist.getId().toString()))
                .andExpect(jsonPath("$.name").value(artist.getName()))
                .andExpect(jsonPath("$.numberOfEvents").value(1));
    }

    @Test
    void shouldReturnArtistNotFoundError() throws Exception {
        UUID id = UUID.randomUUID();
        String expectedErrorMessage = ErrorMessage.ARTIST_NOT_FOUND.toString();

        mockMvc.perform(
                    get(UrlPath + "/" + id)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(expectedErrorMessage));
    }

    @Test
    void shouldCreateArtist() throws Exception {
        String name = "Sabaton";
        MvcResult result = mockMvc.perform(
                    post(UrlPath)
                            .header("Authorization", jwtAdminToken)
                            .content("{\"name\": \"" + name + "\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        Optional<Artist> savedArtist = artistRepository.findById(UUID.fromString(id));
        assertThat(savedArtist).isPresent();
        assertThat(savedArtist.get().getName()).isEqualTo(name);
    }

    @Test
    void shouldReturnArtistExistsError() throws Exception {
        String name = "Sabaton";
        artistRepository.save(new Artist(name));
        String expectedErrorMessage = ErrorMessage.ARTIST_EXISTS.toString();

        mockMvc.perform(
                    post(UrlPath)
                            .header("Authorization", jwtAdminToken)
                            .content("{\"name\": \"" + name + "\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value(expectedErrorMessage));
    }
}