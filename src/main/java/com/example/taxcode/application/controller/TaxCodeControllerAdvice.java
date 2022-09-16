package com.example.taxcode.application.controller;

import com.example.taxcode.application.exception.CityCodeNotFoundException;
import com.example.taxcode.application.exception.TaxCodeNotValidException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.DateTimeException;
import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class TaxCodeControllerAdvice {

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {CityCodeNotFoundException.class, IllegalArgumentException.class,
            TaxCodeNotValidException.class, DateTimeException.class})
    public ApiError handle(RuntimeException exception, WebRequest request) {
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .error(exception.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();
    }


    @Value
    @Builder
    @Jacksonized
    public static class ApiError {

        Instant timestamp;
        HttpStatus status;
        @Singular
        List<String> errors;
        String path;
    }
}
