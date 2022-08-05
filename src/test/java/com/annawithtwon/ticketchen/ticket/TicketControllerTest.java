package com.annawithtwon.ticketchen.ticket;

import com.annawithtwon.ticketchen.artist.Artist;
import com.annawithtwon.ticketchen.event.Event;
import com.annawithtwon.ticketchen.event.EventRepository;
import com.annawithtwon.ticketchen.exception.ErrorMessage;
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
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private EventRepository eventRepository;

    @AfterEach
    private void truncateDb() {
        eventRepository.deleteAll();
    }

    @Test
    void shouldGetAllTickets() throws Exception {
        // arrange
        Event randomEvent = eventRepository.save(new Event("Nashestvije", "Russia", OffsetDateTime.now()));
        List<Ticket> expectedEvents = List.of(
                new Ticket(randomEvent, "main", 100),
                new Ticket(randomEvent, "secondary", 200)
        );
        ticketRepository.saveAll(expectedEvents);

        // act - assert
        mockMvc.perform(
                        get("/tickets")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.*.id").exists())
                .andExpect(jsonPath("$.*.event").exists())
                .andExpect(jsonPath("$.*.section").exists())
                .andExpect(jsonPath("$.*.price").exists());
    }

    @Test
    void shouldReturnTicketNotFoundError() throws Exception {
        String expectedErrorMessage = ErrorMessage.TICKET_NOT_FOUND.toString();

        mockMvc.perform(
                        get("/tickets/" + UUID.randomUUID())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(expectedErrorMessage));
    }

    @Test
    void shouldCreateEvent() throws Exception {
        Event randomEvent = eventRepository.save(new Event("Nashestvije", "Russia", OffsetDateTime.now()));
        MvcResult result = mockMvc.perform(
                        post("/tickets")
                                .content("{\n" +
                                        "    \"eventId\": \"" + randomEvent.getId() + "\",\n" +
                                        "    \"section\": \"1\",\n" +
                                        "    \"price\": 7800\n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.section").value("1"))
                .andExpect(jsonPath("$.price").value(7800))
                .andExpect(jsonPath("$.event.id").value(randomEvent.getId().toString()))
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        Optional<Ticket> savedTicket = ticketRepository.findById(UUID.fromString(id));
        assertThat(savedTicket).isPresent();
    }

    @Test
    void shouldReturnErrorWhenNoEventProvided() throws Exception {
        String expectedErrorMessage = ErrorMessage.PARAMETER_MISSING.toString();

        mockMvc.perform(
                        post("/tickets")
                                .content("{\n" +
                                        "    \"section\": \"1\",\n" +
                                        "    \"price\": 7800\n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(expectedErrorMessage));
    }

    @Test
    void shouldReturnErrorWhenEventDoesNotExist() throws Exception {
        String expectedErrorMessage = ErrorMessage.EVENT_NOT_FOUND.toString();
        mockMvc.perform(
                        post("/tickets")
                                .content("{\n" +
                                        "    \"eventId\": \"" + UUID.randomUUID() + "\",\n" +
                                        "    \"section\": \"1\",\n" +
                                        "    \"price\": 7800\n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(expectedErrorMessage));
    }
}