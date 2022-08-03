package com.annawithtwon.ticketchen.event;

import com.annawithtwon.ticketchen.artist.Artist;
import com.annawithtwon.ticketchen.artist.ArtistRepository;
import com.annawithtwon.ticketchen.exception.ErrorMessage;
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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ArtistRepository artistRepository;

    private Artist artist;

    @BeforeEach
    private void setup() {
        artist = artistRepository.save(new Artist("artist"));
    }

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
                        get("/events")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.*.id").exists())
                .andExpect(jsonPath("$.*.name").exists())
                .andExpect(jsonPath("$.*.location").exists())
                .andExpect(jsonPath("$.*.date").exists())
                .andExpect(jsonPath("$.*.participatingArtists").exists());
    }

    @Test
    void shouldGetOneEvent() throws Exception {
        Event expectedEvent = new Event(
                "name",
                "country",
                OffsetDateTime.now(),
                Set.of(artist)
        );
        eventRepository.save(expectedEvent);

        mockMvc.perform(
                        get("/events/" + expectedEvent.getId())
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
                        get("/events/" + id)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(expectedErrorMessage));
    }

    @Test
    void shouldCreateArtist() throws Exception {
        MvcResult result = mockMvc.perform(
                        post("/events")
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
    void shouldReturnErrorWhenArtistDoesNotExist() throws Exception {
        String expectedErrorMessage = ErrorMessage.ARTIST_NOT_FOUND.toString();
        mockMvc.perform(
                        post("/events")
                                .content("{\n" +
                                        "    \"name\": \"Hellfest\",\n" +
                                        "    \"location\": \"Clisson, France\",\n" +
                                        "    \"date\": \"2022-05-14T14:20:32.0+07:00\",\n" +
                                        "    \"participatingArtistIds\": [\n" +
                                        "        \"" + UUID.randomUUID() + "\"\n" +
                                        "    ]\n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(expectedErrorMessage));
    }
}