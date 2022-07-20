package com.example.project.mappers;

import com.example.project.domain.Category;
import com.example.project.model.CategoryDTO;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true),
        uses = GameMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CategoryMapper {

    public abstract CategoryDTO mapCategoryToCategoryDTO(Category category);
}
