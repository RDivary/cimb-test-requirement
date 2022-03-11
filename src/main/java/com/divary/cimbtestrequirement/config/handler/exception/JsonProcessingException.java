package com.divary.cimbtestrequirement.config.handler.exception;

import org.springframework.http.HttpStatus;

public class JsonProcessingException extends ErrorException {
    public JsonProcessingException() {
        super("Cannot construct instance", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
