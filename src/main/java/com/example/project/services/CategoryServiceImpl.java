package com.example.project.services;

import com.example.project.domain.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {



    @Override
    public List<Category> getAllCategories() {
        return null;
    }

    @Override
    public Category getCategoryByName(String name) {
        return null;
    }
}
