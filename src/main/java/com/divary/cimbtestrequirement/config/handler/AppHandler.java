package com.divary.cimbtestrequirement.config.handler;

import com.divary.cimbtestrequirement.config.handler.exception.ErrorException;
import com.divary.cimbtestrequirement.dto.response.BaseResponse;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AppHandler {

    @ExceptionHandler(value = {ErrorException.class})
    public ResponseEntity<BaseResponse<Object>> exceptionHandler(ErrorException e) {

        return ResponseEntity.status(e.getHttpStatus()).body(getResponseBody(e.getHttpStatus(), e.getMessage()));
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<BaseResponse<Object>> usernameNotFoundExceptionHandler(UsernameNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(getResponseBody(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<BaseResponse<Object>> badCredentialsExceptionHandler(BadCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(getResponseBody(HttpStatus.UNAUTHORIZED, "Sorry, your password was incorrect."));
    }

    @ExceptionHandler(value = {PropertyReferenceException.class})
    public ResponseEntity<BaseResponse<Object>> propertyReferenceExceptionHandler(PropertyReferenceException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getResponseBody(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> validationExceptionHandler(MethodArgumentNotValidException ex) {
        Map<String, String> errorList = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> errorList.put(((FieldError) error).getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getResponseBody(HttpStatus.BAD_REQUEST, errorList, "error validation"));

    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<BaseResponse<Object>> sQLIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getResponseBody(HttpStatus.BAD_REQUEST,ex.getMessage()));

    }

    private BaseResponse<Object> getResponseBody(HttpStatus httpStatus, String message) {
        return BaseResponse.builder()
                .code(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .message(message)
                .build();
    }

    private BaseResponse<Object> getResponseBody(HttpStatus httpStatus, Map<String, String> errorList, String message) {
        return BaseResponse.builder()
                .data(errorList)
                .code(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .message(message)
                .build();
    }
}
