package com.lukhol.dna.exercise.repository.base;

import com.lukhol.dna.exercise.model.base.StateAuditable;
import com.lukhol.dna.exercise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface CustomRepository<T extends StateAuditable<User>, ID extends Serializable> extends JpaRepository<T, ID> {
    void softDelete(T entity);
    void softDelete(ID id);
}