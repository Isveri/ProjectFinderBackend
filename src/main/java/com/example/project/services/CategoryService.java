package com.example.project.services;

import com.example.project.domain.Category;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();
    Category getCategoryByName(String name);


}
