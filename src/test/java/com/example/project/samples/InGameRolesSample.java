package com.example.project.samples;

import com.example.project.model.InGameRolesDTO;

public class InGameRolesSample {

    public static InGameRolesDTO getInGameRoleDTOMock(){
        return InGameRolesDTO.builder()
                .id(1L)
                .name("Mid")
                .build();
    }
}
