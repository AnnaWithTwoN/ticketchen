package com.annawithtwon.ticketchen.ticket;

import com.annawithtwon.ticketchen.event.Event;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private String section;

    @Column(nullable = false)
    private Integer price; // in cents

    public Ticket() {
    }

    public Ticket(Event event, String section, Integer price) {
        this.event = event;
        this.section = section;
        this.price = price;
    }

    public Ticket(UUID id, Event event, String section, Integer price) {
        this.id = id;
        this.event = event;
        this.section = section;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
