package com.example.project.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class GameDTO {
    private String name;
    private Long id;
    private List<InGameRolesDTO> inGameRolesDTOList;
}
