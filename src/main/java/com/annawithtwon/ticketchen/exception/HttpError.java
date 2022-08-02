package com.annawithtwon.ticketchen.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

public class HttpError {
    private String message;
    private String error;
    @JsonIgnore
    private Throwable throwable;
    private int status;

    public HttpError(String message, Throwable throwable, HttpStatus status) {
        this.message = message;
        this.throwable = throwable;
        this.status = status.value();
        this.error = status.getReasonPhrase();
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getError() {
        return error;
    }

    public int getStatus() {
        return status;
    }
}
