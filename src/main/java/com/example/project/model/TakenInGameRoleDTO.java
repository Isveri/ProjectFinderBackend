package com.example.project.model;


import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TakenInGameRoleDTO {

    private Long id;
    private UserMsgDTO user;
    private InGameRolesDTO inGameRole;
}
