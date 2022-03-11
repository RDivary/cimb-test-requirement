package com.divary.cimbtestrequirement.config.handler.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;

    public ErrorException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
