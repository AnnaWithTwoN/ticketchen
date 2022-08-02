package com.annawithtwon.ticketchen.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HttpErrorHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        HttpError error = new HttpError(ex.getMessage(), ex, HttpStatus.NOT_FOUND);

        return ResponseEntity
                .status(error.getStatus())
                .body(error);
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<Object> handleResourceExistsException(ResourceExistsException ex) {
        HttpError error = new HttpError(ex.getMessage(), ex, HttpStatus.CONFLICT);

        return ResponseEntity
                .status(error.getStatus())
                .body(error);
    }
}
