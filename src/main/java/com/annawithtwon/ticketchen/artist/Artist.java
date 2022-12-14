package com.annawithtwon.ticketchen.artist;

import com.annawithtwon.ticketchen.event.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name; // TODO: add unique constraint

    @JsonIgnore
    @ManyToMany(mappedBy = "participatingArtists", fetch = FetchType.LAZY)
    private Set<Event> events = new HashSet<>();

    @Formula(value = "(SELECT COUNT(*) FROM public.artist_event e WHERE e.artist_id=id)")
    private Integer numberOfEvents;

    public Artist() { }

    public Artist(String name) {
        this.name = name;
    }

    public Artist(UUID id, String name) {
        this.id = id;
        this.name = name;
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

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Integer getNumberOfEvents() {
        return numberOfEvents;
    }

    public void setNumberOfEvents(Integer numberOfEvents) {
        this.numberOfEvents = numberOfEvents;
    }
}
