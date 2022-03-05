package com.example.demo.category;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //spin up h2 database for testing
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckIfCategoryPresentByName() {
        // Given
        String name = "Frontend";
        Category category = new Category(name);
        underTest.save(category);
        // When
        Optional<Category> categoryByName = underTest.findByName(name);
        //Then
        assertThat(categoryByName).isPresent();
    }

    @Test
    void itShouldCheckIfCategoryNotPresentByName() {
        // Given
        String name = "Frontend";
        // When
        Optional<Category> categoryByName = underTest.findByName(name);
        //Then
        assertThat(categoryByName).isNotPresent();
    }
}