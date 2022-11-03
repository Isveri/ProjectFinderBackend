package com.example.project.mappers;

import com.example.project.domain.Category;
import com.example.project.model.CategoryDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.example.project.samples.CategorySample.getCategoryMock;
import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private final CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    @Test
    void should_map_category_to_categoryDTO() {
        //given
        Category category = getCategoryMock();

        //when
        CategoryDTO result = categoryMapper.mapCategoryToCategoryDTO(category);

        //then
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());
        assertEquals(category.getBasicMaxUsers(), result.getBasicMaxUsers());
    }
}