package com.example.project.model;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CategoryDTO {
    private Long id;
    private String name;
}
