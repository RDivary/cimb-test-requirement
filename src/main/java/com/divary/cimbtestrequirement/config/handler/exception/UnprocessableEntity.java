package com.divary.cimbtestrequirement.config.handler.exception;

import org.springframework.http.HttpStatus;

public class UnprocessableEntity extends ErrorException {

    private static final long serialVersionUID = 2053616070012836958L;

    public UnprocessableEntity(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
