package com.lukhol.dna.exercise.repository;

import com.lukhol.dna.exercise.model.Category;
import com.lukhol.dna.exercise.repository.base.CustomRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends CustomRepository<Category, Long> {

    @Query("select c from Category c where c.name = :name")
    Optional<Category> findByName(@Param("name") String name);
}