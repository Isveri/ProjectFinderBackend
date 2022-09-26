package com.example.project.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@Getter
@Builder
public class GameDTO {
    private String name;
    private Long id;
    private List<InGameRolesDTO> inGameRoles;
    private List<CategoryDTO> categories;
    private boolean assignRolesActive;
}
