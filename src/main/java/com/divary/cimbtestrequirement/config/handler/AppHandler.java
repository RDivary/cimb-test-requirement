package com.divary.cimbtestrequirement.config.handler;

import com.divary.cimbtestrequirement.config.handler.exception.ErrorException;
import com.divary.cimbtestrequirement.dto.response.BaseResponse;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AppHandler {

    @ExceptionHandler(value = {ErrorException.class})
    public ResponseEntity<BaseResponse<Object>> exceptionHandler(ErrorException e) {
        return getResponse(e.getHttpStatus(), e.getMessage());
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<BaseResponse<Object>> usernameNotFoundExceptionHandler(UsernameNotFoundException ex) {
        return getResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<BaseResponse<Object>> badCredentialsExceptionHandler(BadCredentialsException ex) {
        return getResponse(HttpStatus.UNAUTHORIZED, "Sorry, your password was incorrect");
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

        return getResponse(HttpStatus.BAD_REQUEST, errorList, "error validation");
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<BaseResponse<Object>> sQLIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getResponseBody(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<Object>> accessDeniedExceptionHandler(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(getResponseBody(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<Object>> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        return getResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    private BaseResponse<Object> getResponseBody(HttpStatus httpStatus, String message) {
        return BaseResponse.builder()
                .code(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .message(message)
                .build();
    }

    private ResponseEntity<BaseResponse<Object>> getResponse(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus).body(BaseResponse.builder()
                .code(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .message(message)
                .build());
    }

    private ResponseEntity<BaseResponse<Object>> getResponse(HttpStatus httpStatus, Map<String, String> errorList, String message) {
        return ResponseEntity.status(httpStatus).body(BaseResponse.builder()
                .data(errorList)
                .code(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .message(message)
                .build());
    }
}
