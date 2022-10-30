package com.example.project.services;

import com.example.project.domain.Category;
import com.example.project.mappers.CategoryMapper;
import com.example.project.model.CategoryDTO;
import com.example.project.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

//    @Override
//    public List<CategoryDTO> getAllCategories() {
//        return null;
//    }
//
//    @Override
//    public Category getCategoryByName(String name) {
//        return null;
//    }

    @Override
    public List<CategoryDTO> getCategoriesByGame(String game) {
        return categoryRepository.findAllByGameName(game)
                .stream()
                .map(categoryMapper::mapCategoryToCategoryDTO)
                .collect(Collectors.toList());
    }
}
