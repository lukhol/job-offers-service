package com.lukhol.dna.exercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.text.SimpleDateFormat;

@SpringBootApplication
public class ExerciseApplication implements ApplicationListener<ApplicationReadyEvent> {

	public static final String DATE_FORMAT = "dd-mm-yyyy"; //TODO: Move to constants or properties file

	public static void main(String[] args) {
		SpringApplication.run(ExerciseApplication.class, args);
	}

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		objectMapper.setDateFormat(simpleDateFormat);
	}
}
