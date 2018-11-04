package com.lukhol.dna.exercise.configuration;


import com.lukhol.dna.exercise.repository.base.CustomRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableJpaRepositories(basePackages = "com.lukhol.dna.exercise", repositoryBaseClass = CustomRepositoryImpl.class)
public class JpaConfig {
}