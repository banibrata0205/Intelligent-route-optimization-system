package com.banibrata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
            ResponseStatusException exception) {

        HttpStatus status =
                HttpStatus.resolve(
                        exception.getStatusCode().value()
                );

        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put("timestamp",
                LocalDateTime.now());

        response.put("status",
                status.value());

        response.put("error",
                status.getReasonPhrase());

        response.put("message",
                exception.getReason());

        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException exception) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put("timestamp",
                LocalDateTime.now());

        response.put("status",
                HttpStatus.BAD_REQUEST.value());

        response.put("error",
                HttpStatus.BAD_REQUEST.getReasonPhrase());

        response.put("message",
                exception.getMessage());

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(
            Exception exception) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put("timestamp",
                LocalDateTime.now());

        response.put("status",
                HttpStatus.INTERNAL_SERVER_ERROR.value());

        response.put("error",
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

        response.put("message",
                "An unexpected server error occurred.");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
