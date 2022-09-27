package com.example.project.samples;

import com.example.project.model.GameDTO;

import java.util.ArrayList;

public class GameSample {

    public static GameDTO getGameDTOMock(){
        return GameDTO.builder()
                .id(1L)
                .name("CSGO")
                .categories(new ArrayList<>())
                .inGameRoles(new ArrayList<>())
                .assignRolesActive(false)
                .build();
    }
}
