package com.lukhol.dna.exercise.controller;

import com.lukhol.dna.exercise.errors.ErrorResponse;
import com.lukhol.dna.exercise.errors.ServiceValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvisor {

    @ExceptionHandler(ServiceValidationException.class)
    public ResponseEntity<?> handleServiceValidationException(ServiceValidationException e) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }
}