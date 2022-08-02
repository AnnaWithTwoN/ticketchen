package com.annawithtwon.ticketchen.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(ErrorMessage message) {
        super(message.toString());
    }

    public ResourceNotFoundException(ErrorMessage message, Throwable cause) {
        super(message.toString(), cause);
    }
}
