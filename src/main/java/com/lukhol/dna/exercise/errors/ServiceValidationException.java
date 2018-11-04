package com.lukhol.dna.exercise.errors;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@NoArgsConstructor
public class ServiceValidationException extends RuntimeException {

    private Map<String, String> errors = new HashMap<>();

    public ServiceValidationException(String message) {
        super(message);
    }

    public ServiceValidationException(Map<String, String> errors) {
        this.errors = errors;
    }

    public void addError(String key, String message) {
        if(!errors.containsKey(key)) {
            errors.put(key, message);
        } else {
            errors.replace(key, message);
            log.error("Key: " + key + " replaces by value: "  + message);
        }
    }
}