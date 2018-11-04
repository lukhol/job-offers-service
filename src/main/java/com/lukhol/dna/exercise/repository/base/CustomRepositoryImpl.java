package com.lukhol.dna.exercise.repository.base;

import com.lukhol.dna.exercise.model.base.EntityState;
import com.lukhol.dna.exercise.model.base.StateAuditable;
import com.lukhol.dna.exercise.model.User;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class CustomRepositoryImpl<T extends StateAuditable<User>, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements CustomRepository<T, ID> {

    private EntityManager entityManager;

    public CustomRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void softDelete(T entity) {
        entity.setState(EntityState.DELETED);
    }

    @Override
    @Transactional
    public void softDelete(ID id) {
        T entity = (T)entityManager.find(StateAuditable.class, id);
        entity.setState(EntityState.DELETED);
    }
}