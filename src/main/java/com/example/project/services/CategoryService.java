package com.example.project.services;

import com.example.project.domain.Category;
import com.example.project.model.CategoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();
    Category getCategoryByName(String name);

    List<CategoryDTO> getCategoriesByGame(String game);


}
