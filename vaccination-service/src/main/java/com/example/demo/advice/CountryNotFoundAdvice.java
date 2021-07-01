package com.example.demo.advice;

import com.example.demo.exceptions.CountryNotFoundException;
import com.example.demo.exceptions.ExceptionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@ControllerAdvice
public class CountryNotFoundAdvice {

    @ExceptionHandler(CountryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<?> countryNotFoundHandler(CountryNotFoundException e)
    {
        ExceptionModel exceptionModel = new ExceptionModel(e.getMessage(), e, ZonedDateTime.now());
        return ResponseEntity.of(Optional.of(exceptionModel));
    }
}
