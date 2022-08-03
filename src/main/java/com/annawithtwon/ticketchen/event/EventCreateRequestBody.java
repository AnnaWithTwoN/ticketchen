package com.annawithtwon.ticketchen.event;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class EventCreateRequestBody {
    private String name;
    private String location;
    private OffsetDateTime date;
    private List<UUID> participatingArtistIds;

    public EventCreateRequestBody() {
    }

    public EventCreateRequestBody(String name, String location, OffsetDateTime date, List<UUID> participatingArtistIds) {
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

    public List<UUID> getParticipatingArtistIds() {
        return participatingArtistIds;
    }

    public void setParticipatingArtistIds(List<UUID> participatingArtistIds) {
        this.participatingArtistIds = participatingArtistIds;
    }
}
