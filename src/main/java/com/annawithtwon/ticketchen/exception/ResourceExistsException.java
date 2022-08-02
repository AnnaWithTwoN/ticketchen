package com.annawithtwon.ticketchen.exception;

public class ResourceExistsException extends RuntimeException {

    public ResourceExistsException(ErrorMessage message) {
        super(message.toString());
    }

    public ResourceExistsException(ErrorMessage message, Throwable cause) {
        super(message.toString(), cause);
    }
}
