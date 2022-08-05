package com.annawithtwon.ticketchen.event;

import com.annawithtwon.ticketchen.artist.Artist;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@JsonDeserialize(using = EventDeserializer.class)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    @NotNull
    @NotEmpty
    private String name;

    @Column(nullable = false)
    @NotNull
    @NotEmpty
    private String location;

    @Column(nullable = false)
    @NotNull
    // TODO: ensure date is not in the past
    private OffsetDateTime date;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "artist_event",
            joinColumns = { @JoinColumn(name = "event_id") },
            inverseJoinColumns = { @JoinColumn(name = "artist_id") })
    @NotNull
    @NotEmpty
    // TODO: add cascade
    private Set<Artist> participatingArtists = new HashSet<>();

    public Event() {
    }

    public Event(String name, String location, OffsetDateTime date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public Event(String name, String location, OffsetDateTime date, Set<Artist> participatingArtists) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.participatingArtists = participatingArtists;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Set<Artist> getParticipatingArtists() {
        return participatingArtists;
    }

    public void setParticipatingArtists(Set<Artist> participatingArtists) {
        this.participatingArtists = participatingArtists;
    }

    public void addParticipatingArtist(Artist artist) {
        participatingArtists.add(artist);
    }
}
