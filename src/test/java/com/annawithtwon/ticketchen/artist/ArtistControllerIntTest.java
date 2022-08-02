package com.annawithtwon.ticketchen.artist;

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

import java.util.List;
import java.util.Optional;
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

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ArtistRepository repository;

    @AfterEach
    private void truncateDb() {
        repository.deleteAll();
    }

    @Test
    void shouldGetAllArtists() throws Exception {
        // arrange
        List<Artist> expectedArtists = List.of(
                new Artist("artist1"),
                new Artist("artist2"),
                new Artist("artist3")
        );
        repository.saveAll(expectedArtists);

        // act - assert
        mockMvc.perform(
                    get("/artists")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.*.id").exists())
                .andExpect(jsonPath("$.*.name").exists());
    }

    @Test
    void shouldGetOneArtist() throws Exception {
        Artist artist = repository.save(new Artist("artist"));

        mockMvc.perform(
                    get("/artists/" + artist.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(artist.getId().toString()))
                .andExpect(jsonPath("$.name").value(artist.getName()));
    }

    @Test
    void shouldReturnArtistNotFoundError() throws Exception {
        UUID id = UUID.randomUUID();
        String expectedErrorMessage = ErrorMessage.ARTIST_NOT_FOUND.toString();

        mockMvc.perform(
                    get("/artists/" + id)
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
                    post("/artists")
                        .content("{\"name\": \"" + name + "\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andReturn();

        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        Optional<Artist> savedArtist = repository.findById(UUID.fromString(id));
        assertThat(savedArtist).isPresent();
        assertThat(savedArtist.get().getName()).isEqualTo(name);
    }

    @Test
    void shouldReturnArtistExistsError() throws Exception {
        String name = "Sabaton";
        repository.save(new Artist(name));
        String expectedErrorMessage = ErrorMessage.ARTIST_EXISTS.toString();

        mockMvc.perform(
                    post("/artists")
                            .content("{\"name\": \"" + name + "\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value(expectedErrorMessage));
    }
}