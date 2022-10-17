package com.example.project.samples;

import com.example.project.domain.Category;
import com.example.project.domain.Game;
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

    public static Category getCategoryMock(){
        return Category.builder()
                .id(1L)
                .canAssignRoles(false)
                .basicMaxUsers(5)
                .name("Match Making")
                .game(Game.builder().name("CSGO").build()).build();
    }
}
