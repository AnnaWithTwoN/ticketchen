package com.annawithtwon.ticketchen.exception;

public class ParameterMissingException extends RuntimeException {

    public ParameterMissingException(ErrorMessage message) {
        super(message.toString());
    }

    public ParameterMissingException(ErrorMessage message, Throwable cause) {
        super(message.toString(), cause);
    }
}
