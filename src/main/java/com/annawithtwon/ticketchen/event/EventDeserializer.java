package com.annawithtwon.ticketchen.event;

import com.annawithtwon.ticketchen.artist.Artist;
import com.annawithtwon.ticketchen.artist.ArtistService;
import com.annawithtwon.ticketchen.exception.ErrorMessage;
import com.annawithtwon.ticketchen.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;

@JsonComponent
public class EventDeserializer extends JsonDeserializer<Event> {

    private ArtistService artistService;

    @Autowired
    public void setArtistService(ArtistService artistService) {
        this.artistService = artistService;
    }

    @Override
    public Event deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, ResourceNotFoundException {
        JsonNode node = jp.getCodec().readTree(jp);
        Iterator<JsonNode> iterator = node.get("participatingArtistIds").elements();
        Set<Artist> participatingArtists = new HashSet<>();
        while (iterator.hasNext()) {
            Artist artist = artistService.getOneArtist(UUID.fromString(iterator.next().asText()));
            participatingArtists.add(artist);
        }

        return new Event(
                node.get("name").asText(),
                node.get("location").asText(),
                OffsetDateTime.parse(node.get("date").asText()),
                participatingArtists);
    }

}
