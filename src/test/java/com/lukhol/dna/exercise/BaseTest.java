package com.lukhol.dna.exercise;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public abstract class BaseTest {

    @Autowired
    protected EntityManager entityManager;

    @SafeVarargs
    protected final <T> void persistAndFlush(T entity, T... entities) {
        entityManager.persist(entity);

        for(T t : entities) {
            entityManager.persist(t);
        }

        entityManager.flush();
        entityManager.clear();
    }

    protected  <T> void persistAndFlush(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
        entityManager.clear();
    }
}