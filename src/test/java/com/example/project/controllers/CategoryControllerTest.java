package com.example.project.controllers;

import com.example.project.model.CategoryDTO;
import com.example.project.model.GameDTO;
import com.example.project.services.CategoryService;
import com.example.project.services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static com.example.project.samples.CategorySample.getCategoryDTOMock;
import static com.example.project.samples.GameSample.getGameDTOMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;
    @Mock
    private GameService gameService;
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
    void getGames() throws Exception {
        //given
        final GameDTO gameDTO = getGameDTOMock();
        when(gameService.getGames()).thenReturn(Collections.singletonList(gameDTO));

        //when + then
        mockMvc.perform(get("/api/v1/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(gameDTO));

        verify(gameService,times(1)).getGames();
    }

    @Test
    void getCategoriesByGame() throws Exception{
        //given
        final CategoryDTO categoryDTO = getCategoryDTOMock();
        final String gameName = getGameDTOMock().getName();
        when(categoryService.getCategoriesByGame(any(String.class))).thenReturn(Collections.singletonList(categoryDTO));

        //when + then
        mockMvc.perform(get(baseUrl+"/all/"+gameName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(categoryDTO));

        verify(categoryService,times(1)).getCategoriesByGame(gameName);
    }
}