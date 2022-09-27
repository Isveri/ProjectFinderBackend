package com.example.project.samples;

import com.example.project.model.CategoryDTO;

public class CategorySample {

    public static CategoryDTO getCategoryDTOMock(){
        return CategoryDTO.builder()
                .id(1L)
                .name("Match Making")
                .basicMaxUsers(5)
                .canAssignRoles(false)
                .build();
    }
}
