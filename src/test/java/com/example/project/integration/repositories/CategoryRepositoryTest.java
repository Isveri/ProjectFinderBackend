package com.example.project.integration.repositories;

import com.example.project.domain.Category;
import com.example.project.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.project.samples.CategorySample.getCategoryMock;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    @Rollback
    void setUp() {
        category = getCategoryMock();
        categoryRepository.save(category);
    }

    @Test
    void should_return_category_by_name() {
        //given
        String categoryName = category.getName();

        //when
        Category result = categoryRepository.findByName(categoryName);

        //then
        assertEquals(categoryName,result.getName());

    }

    @Test
    void should_return_null(){
        //given
        String categoryName = "Not";

        //when
        Category result = categoryRepository.findByName(categoryName);

        //then
        assertNull(result);
    }

    @Test
    void should_return_all_categories_by_game_name() {
        //given
        String gameName = category.getGame().getName();

        //when
        List<Category> results = categoryRepository.findAllByGameName(gameName);

        //then
        assertFalse(results.isEmpty());
        assertEquals(results.get(0).getName(), category.getName());

    }

    @Test
    void should_return_empty_array(){
        //given
        String gameName = "Heh";

        //when
        List<Category> results = categoryRepository.findAllByGameName(gameName);

        //then
        assertTrue(results.isEmpty());
    }

}