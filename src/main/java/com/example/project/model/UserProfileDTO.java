package com.example.project.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Setter
@Getter
public class UserProfileDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String info;
    private int age;
    private int phone;
    private String city;
    private RoleDTO role;
    private List<InGameRolesDTO> inGameRoles;
    private List<PlatformDTO> platforms;
}
