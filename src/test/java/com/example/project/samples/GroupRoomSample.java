package com.example.project.samples;

import com.example.project.model.GroupRoomDTO;
import com.example.project.model.GroupRoomUpdateDTO;
import com.example.project.model.JoinCodeDTO;
import com.example.project.model.UserGroupsListDTO;

import java.util.Collections;
import java.util.List;

import static com.example.project.samples.UserMockSample.getUserDTOMock;
import static com.example.project.samples.UserMockSample.getUserMock;

public class GroupRoomSample {

    public static GroupRoomDTO getGroupRoomDTOMock() {
        return GroupRoomDTO.builder()
                .id(1L)
                .city("Lublin")
                .maxUsers(5)
                .name("Group Mock")
                .open(true)
                .joinCode("sersad23")
                .users(List.of(getUserDTOMock()))
                .inGameRolesActive(false)
                .build();
    }

    public static GroupRoomUpdateDTO getGroupRoomUpdateDTOMock() {
        return GroupRoomUpdateDTO.builder()
                .maxUsers(5)
                .name("Group Mock")
                .description("HEH")
                .build();
    }


    public static UserGroupsListDTO getUsersGroupListDTOMock() {
        return UserGroupsListDTO.builder()
                .groupRooms(Collections.singletonList(getGroupRoomDTOMock()))
                .id(1L)
                .build();
    }

    public static JoinCodeDTO getJoinCodeDTOMock() {
        return JoinCodeDTO.builder()
                .code("Heh")
                .build();
    }
}
