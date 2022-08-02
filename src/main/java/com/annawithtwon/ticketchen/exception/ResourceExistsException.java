package com.annawithtwon.ticketchen.exception;

public class ResourceExistsException extends RuntimeException {

    public ResourceExistsException(String resource) {
        super(resource + " already exists");
    }

    public ResourceExistsException(String resource, Throwable cause) {
        super(resource + " already exists", cause);
    }
}
