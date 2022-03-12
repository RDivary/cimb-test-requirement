package com.divary.cimbtestrequirement.controller;

import com.divary.cimbtestrequirement.config.handler.exception.NotFoundException;
import com.divary.cimbtestrequirement.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class BaseController {

    protected static final String ROLE_USER = "hasRole('ROLE_USER')";
    protected static final String ROLE_ADMIN = "hasRole('ROLE_ADMIN') ";
    protected static final String ROLE_USER_ADMIN = "hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ";

    protected static final String AUTHORIZATION = "Authorization";

    protected ResponseEntity<BaseResponse<Object>> getResponseOk(Object data, String message) {
        return ResponseEntity.status(HttpStatus.OK).body(getResponse(data, message, HttpStatus.OK));
    }

    protected ResponseEntity<BaseResponse<Object>> getResponseCreated(Object data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(getResponse(data, message, HttpStatus.CREATED));
    }

    protected <T extends List<?>> ResponseEntity<BaseResponse<Object>> getResponseList(T data, String message){
        if (data.isEmpty()) throw new NotFoundException(message.toLowerCase() + " not found");
        return getResponseOk(data, message + " Found");
    }

    protected BaseResponse<Object> getResponse(Object data, String message, HttpStatus httpStatus) {
        return BaseResponse.builder()
                .data(data)
                .code(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .message(message)
                .build();
    }
}
