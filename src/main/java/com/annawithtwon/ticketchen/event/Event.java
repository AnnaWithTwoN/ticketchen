package com.annawithtwon.ticketchen.event;

import com.annawithtwon.ticketchen.artist.Artist;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private OffsetDateTime date;

    @ManyToMany
    @JoinTable(name = "artist_event",
            joinColumns = { @JoinColumn(name = "artist_id") },
            inverseJoinColumns = { @JoinColumn(name = "event_id") })
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
