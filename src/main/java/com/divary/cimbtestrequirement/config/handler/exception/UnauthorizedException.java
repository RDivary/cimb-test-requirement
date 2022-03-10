package com.divary.cimbtestrequirement.config.handler.exception;

import com.divary.cimbtestrequirement.config.handler.ErrorException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ErrorException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
