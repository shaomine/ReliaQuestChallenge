package com.example.rqchallenge.employees.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(value = {BadRequestArguments.class})
    public ResponseEntity<ErrorResponse> handleBadArgsException(BadRequestArguments ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleClientException(HttpClientErrorException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getRawStatusCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.valueOf(ex.getRawStatusCode())).body(errorResponse);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleServerException(HttpServerErrorException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getRawStatusCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.valueOf(ex.getRawStatusCode())).body(errorResponse);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
