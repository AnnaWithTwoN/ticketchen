package com.annawithtwon.ticketchen.event;

import com.annawithtwon.ticketchen.artist.Artist;
import com.annawithtwon.ticketchen.artist.ArtistRepository;
import com.annawithtwon.ticketchen.exception.ErrorMessage;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.OffsetDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventControllerIntTest {

    private final String UrlPath = "/events";

    private String jwtAdminToken = "Bearer " + JWT.create()
            .withSubject("username")
            .withClaim("role", "admin")
            .sign(Algorithm.HMAC256("poke".getBytes()));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ArtistRepository artistRepository;

    @AfterEach
    private void truncateDb() {
        eventRepository.deleteAll();
    }

    @Test
    void shouldGetAllEvents() throws Exception {
        // arrange
        List<Event> expectedEvents = List.of(
                new Event("Hellfest", "France", OffsetDateTime.now()),
                new Event("Wacken", "Germany", OffsetDateTime.now()),
                new Event("Graspop", "Belgium", OffsetDateTime.now())
        );
        eventRepository.saveAll(expectedEvents);

        // act - assert
        mockMvc.perform(
                        get(UrlPath)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.*", hasSize(3)))
                .andExpect(jsonPath("$.items.*.id").exists())
                .andExpect(jsonPath("$.items.*.name").exists())
                .andExpect(jsonPath("$.items.*.location").exists())
                .andExpect(jsonPath("$.items.*.date").exists())
                .andExpect(jsonPath("$.items.*.participatingArtists").exists());
    }

    @Test
    void shouldGetOneEvent() throws Exception {
        Artist artist = artistRepository.save(new Artist("artist"));
        Event expectedEvent = new Event(
                "name",
                "country",
                OffsetDateTime.now(),
                Set.of(artist)
        );
        eventRepository.save(expectedEvent);

        mockMvc.perform(
                        get(UrlPath + "/" + expectedEvent.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedEvent.getId().toString()))
                .andExpect(jsonPath("$.name").value(expectedEvent.getName()))
                .andExpect(jsonPath("$.location").value(expectedEvent.getLocation()))
                .andExpect(jsonPath("$.participatingArtists", hasSize(1)))
                .andExpect(jsonPath("$.participatingArtists[0].id")
                    .value(artist.getId().toString()));
    }

    @Test
    void shouldReturnEventNotFoundError() throws Exception {
        UUID id = UUID.randomUUID();
        String expectedErrorMessage = ErrorMessage.EVENT_NOT_FOUND.toString();

        mockMvc.perform(
                        get(UrlPath + "/" + id)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(expectedErrorMessage));
    }

    @Test
    void shouldCreateArtist() throws Exception {
        Artist artist = artistRepository.save(new Artist("artist"));
        MvcResult result = mockMvc.perform(
                        post(UrlPath)
                                .header("Authorization", jwtAdminToken)
                                .content("{\n" +
                                        "    \"name\": \"Hellfest\",\n" +
                                        "    \"location\": \"Clisson, France\",\n" +
                                        "    \"date\": \"2022-05-14T14:20:32.0+07:00\",\n" +
                                        "    \"participatingArtistIds\": [\n" +
                                        "        \"" + artist.getId() + "\"\n" +
                                        "    ]\n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.location").exists())
                .andExpect(jsonPath("$.date").exists())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        Optional<Event> savedEvent = eventRepository.findById(UUID.fromString(id));
        assertThat(savedEvent).isPresent();
    }

    @Test
    void shouldReturnErrorWhenNoArtistProvided() throws Exception {
        String expectedErrorMessage = ErrorMessage.PARAMETER_MISSING.toString();
        mockMvc.perform(
                post(UrlPath)
                        .header("Authorization", jwtAdminToken)
                        .content("{\n" +
                                "    \"name\": \"Hellfest\",\n" +
                                "    \"location\": \"Clisson, France\",\n" +
                                "    \"date\": \"2022-05-14T14:20:32.0+07:00\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(expectedErrorMessage));
    }

    @Test
    void shouldReturnErrorWhenArtistDoesNotExist() throws Exception {
        String expectedErrorMessage = ErrorMessage.ARTIST_NOT_FOUND.toString();
        mockMvc.perform(
                        post(UrlPath)
                                .header("Authorization", jwtAdminToken)
                                .content("{\n" +
                                        "    \"name\": \"Hellfest\",\n" +
                                        "    \"location\": \"Clisson, France\",\n" +
                                        "    \"date\": \"2022-05-14T14:20:32.0+07:00\",\n" +
                                        "    \"participatingArtistIds\": [\n" +
                                        "        \"" + UUID.randomUUID() + "\"\n" +
                                        "    ]\n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(expectedErrorMessage));
    }
}