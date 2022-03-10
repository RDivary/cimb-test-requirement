package com.divary.cimbtestrequirement.config.handler.exception;


import com.divary.cimbtestrequirement.config.handler.ErrorException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends ErrorException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
