package com.annawithtwon.ticketchen.exception;

public enum ErrorMessage {
    ARTIST_NOT_FOUND(createNotFoundErrorMessage("Artist")),
    EVENT_NOT_FOUND(createNotFoundErrorMessage("Event")),
    ARTIST_EXISTS(createExistsErrorMessage("Artist")),
    EVENT_EXISTS(createExistsErrorMessage("Event"));

    private final String message;

    ErrorMessage(final String message) {
        this.message = message;
    }

    private static String createNotFoundErrorMessage(String resource) {
        return resource + " not found";
    }

    private static String createExistsErrorMessage(String resource) {
        return resource + " already exists";
    }

    @Override
    public String toString() {
        return message;
    }
}
