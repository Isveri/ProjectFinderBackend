package com.example.project.controllers;

import com.example.project.services.CategoryService;
import com.example.project.services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

class CategoryControllerTest {

    @Mock
    private GameService gameService;
    private CategoryService categoryService;
    private MockMvc mockMvc;
    private CategoryController categoryController;

    private static final String baseUrl = "/api/v1/category";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryController = new CategoryController(gameService,categoryService);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void getGames() {
    }

    @Test
    void getCategoriesByGame() {
    }
}