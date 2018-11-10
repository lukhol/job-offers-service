package com.lukhol.dna.exercise.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class Config {

    public static final String DATE_FORMAT = "dd-mm-yyyy"; //TODO: Move to constants or properties file

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        mapper.setDateFormat(simpleDateFormat);
        return mapper;
    }
}