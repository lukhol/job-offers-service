package com.lukhol.dna.exercise.repository;

import com.lukhol.dna.exercise.BaseTest;
import com.lukhol.dna.exercise.model.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryRepositoryTests extends BaseTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Before
    public void setUp() {
//        Category it = new Category();
//        it.setName("IT");
//
//        Category food = new Category();
//        food.setName("Food & Drink");
//
//        persistAndFlush(it);
//        persistAndFlush(food);
    }

    @Test
    @Transactional
    public void canGetCategoryByName_success() {
        Optional<Category> categoryOptional = categoryRepository.findByName("IT");
        assertThat(categoryOptional).isPresent();
    }

    @Test
    @Transactional
    public void canGetCategoryByName_notExistingOne() {
        Optional<Category> categoryOptional = categoryRepository.findByName("dsadsa");
        assertThat(categoryOptional).isNotPresent();
    }
}