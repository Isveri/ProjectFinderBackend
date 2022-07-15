package com.example.project.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private RoleDTO role;
    private List<InGameRolesDTO> inGameRoles;
}
