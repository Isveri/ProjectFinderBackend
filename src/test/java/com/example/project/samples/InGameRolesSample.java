package com.example.project.samples;

import com.example.project.domain.InGameRole;
import com.example.project.domain.TakenInGameRole;
import com.example.project.model.InGameRolesDTO;
import com.example.project.model.TakenInGameRoleDTO;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.project.samples.GameSample.getGameMock;
import static com.example.project.samples.UserMockSample.*;

public class InGameRolesSample {

    public static InGameRolesDTO getInGameRoleDTOMock(){
        return InGameRolesDTO.builder()
                .id(1L)
                .name("Mid")
                .build();
    }

    public static InGameRole getInGameRoleMock(){
        return InGameRole.builder()
                .id(1L)
                .name("Mid")
                .game(getGameMock())
                .users(new ArrayList<>(Arrays.asList(getCurrentUserMock())))
                .build();
    }

    public static TakenInGameRole getTakenInGameRoleMock(){
        return TakenInGameRole.builder()
                .id(1L)
                .user(getCurrentUserMock())
                .inGameRole(getInGameRoleMock())
                .build();
    }

    public static TakenInGameRoleDTO getTakenInGameRoleDTOMock(){
        return TakenInGameRoleDTO.builder()
                .id(1L)
                .user(getUserMsgDTOMockv2())
                .inGameRole(getInGameRoleDTOMock())
                .build();
    }

}
