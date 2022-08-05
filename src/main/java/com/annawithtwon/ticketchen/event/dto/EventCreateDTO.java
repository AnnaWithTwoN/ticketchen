package com.annawithtwon.ticketchen.event.dto;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

public class EventCreateDTO {

    private String name;
    private String location;
    private OffsetDateTime date;
    private Set<UUID> participatingArtistIds;

    public EventCreateDTO() {
    }

    public EventCreateDTO(String name, String location, OffsetDateTime date, Set<UUID> participatingArtistIds) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.participatingArtistIds = participatingArtistIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public Set<UUID> getParticipatingArtistIds() {
        return participatingArtistIds;
    }

    public void setParticipatingArtistIds(Set<UUID> participatingArtistIds) {
        this.participatingArtistIds = participatingArtistIds;
    }
}
