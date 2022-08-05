package com.annawithtwon.ticketchen.ticket.dto;

import java.util.UUID;

public class TicketCreateDTO {

    private UUID eventId;
    private String section;
    private Integer price; // in cents

    public TicketCreateDTO() {
    }

    public TicketCreateDTO(UUID eventId, String section, Integer price) {
        this.eventId = eventId;
        this.section = section;
        this.price = price;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
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
