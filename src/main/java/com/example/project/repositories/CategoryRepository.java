package com.example.project.repositories;

import com.example.project.domain.Category;
import com.example.project.model.CategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category findByName(String name);
    List<Category> findAllByGameName(String name);

}
