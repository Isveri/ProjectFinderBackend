package com.example.project.services;

import com.example.project.domain.Category;
import com.example.project.mappers.CategoryMapper;
import com.example.project.model.CategoryDTO;
import com.example.project.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.List;

import static com.example.project.samples.CategorySample.getCategoryDTOMock;
import static com.example.project.samples.CategorySample.getCategoryMock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class CategoryServiceImplTest {



    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryServiceImpl(categoryRepository, categoryMapper);
    }

    @Test
    void getCategoriesByGame() {
        //given
        CategoryDTO categoryDTO = getCategoryDTOMock();
        Category category = getCategoryMock();
        String name = "CSGO";
        when(categoryRepository.findAllByGameName(name)).thenReturn(Collections.singletonList(category));
        when(categoryMapper.mapCategoryToCategoryDTO(category)).thenReturn(categoryDTO);

        //when
        List<CategoryDTO> categories = categoryService.getCategoriesByGame(name);

        //then
        assertThat(categories.get(0).getName())
                .isEqualTo(categoryDTO.getName());

        verify(categoryRepository,times(1)).findAllByGameName(name);
        verify(categoryMapper,times(1)).mapCategoryToCategoryDTO(category);

    }
}