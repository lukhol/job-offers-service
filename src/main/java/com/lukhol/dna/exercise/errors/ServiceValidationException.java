package com.lukhol.dna.exercise.errors;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServiceValidationException extends RuntimeException {

    public ServiceValidationException(String message) {
        super(message);
    }
}