package com.example.project.mappers;

import com.example.project.chat.model.ChatDTO;
import com.example.project.domain.GroupRoom;
import com.example.project.model.GroupNotifInfoDTO;
import com.example.project.model.GroupRoomDTO;
import com.example.project.model.GroupRoomUpdateDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;

import static com.example.project.samples.CategorySample.getCategoryDTOMock;
import static com.example.project.samples.GameSample.getGameDTOMock;
import static com.example.project.samples.GroupRoomSample.*;
import static com.example.project.samples.InGameRolesSample.getTakenInGameRoleDTOMock;
import static com.example.project.samples.InGameRolesSample.getTakenInGameRoleMock;
import static com.example.project.samples.UserMockSample.getUserDTOMock;
import static org.junit.jupiter.api.Assertions.*;

class GroupRoomMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final InGameRolesMapper inGameRolesMapper = Mappers.getMapper(InGameRolesMapper.class);
    private final TakenInGameRoleMapper takenInGameRoleMapper = new TakenInGameRoleMapperImpl(userMapper, inGameRolesMapper);
    private final GroupRoomMapper groupRoomMapper = new GroupRoomMapperImpl(userMapper,takenInGameRoleMapper);

    @Test
    void should_map_group_room_to_group_roomDTO() {
        //given
        GroupRoom groupRoom = getGroupRoomMock();
        groupRoom.setTakenInGameRoles(Collections.singletonList(getTakenInGameRoleMock()));

        //when
        GroupRoomDTO result = groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRoom);

        //then
        assertEquals(groupRoom.getId(), result.getId());
        assertEquals(groupRoom.getGroupLeader().getId(), result.getGroupLeader().getId());
        assertEquals(groupRoom.getUsers().get(0).getId(), result.getUsers().get(0).getId());
        assertEquals(groupRoom.getName(), result.getName());
        assertEquals(groupRoom.getGame().getId(), result.getGame().getId());
        assertEquals(groupRoom.getDescription(), result.getDescription());
        assertEquals(groupRoom.getCategory().getId(), result.getCategory().getId());
        assertEquals(groupRoom.getJoinCode(), result.getJoinCode());
        assertEquals(groupRoom.getCity(), result.getCity());
        assertEquals(groupRoom.getMaxUsers(), result.getMaxUsers());
        assertEquals(groupRoom.getTakenInGameRoles().get(0).getId(),result.getTakenInGameRoles().get(0).getId());
    }

    @Test
    void should_map_group_roomDTO_to_group_room() {
        //given
        GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        groupRoomDTO.setGroupLeader(getUserDTOMock());
        groupRoomDTO.setGame(getGameDTOMock());
        groupRoomDTO.setCategory(getCategoryDTOMock());
        groupRoomDTO.setChatDTO(ChatDTO.builder().id(1L).build());
        groupRoomDTO.setTakenInGameRoles(Collections.singletonList(getTakenInGameRoleDTOMock()));

        //when
        GroupRoom result = groupRoomMapper.mapGroupRoomDTOToGroupRoom(groupRoomDTO);

        //then
        assertEquals(groupRoomDTO.getId(), result.getId());
        assertEquals(groupRoomDTO.getGroupLeader().getId(), result.getGroupLeader().getId());
        assertEquals(groupRoomDTO.getUsers().get(0).getId(), result.getUsers().get(0).getId());
        assertEquals(groupRoomDTO.getName(), result.getName());
        assertEquals(groupRoomDTO.getGame().getId(), result.getGame().getId());
        assertEquals(groupRoomDTO.getDescription(), result.getDescription());
        assertEquals(groupRoomDTO.getCategory().getId(), result.getCategory().getId());
        assertEquals(groupRoomDTO.getJoinCode(), result.getJoinCode());
        assertEquals(groupRoomDTO.getCity(), result.getCity());
        assertEquals(groupRoomDTO.getMaxUsers(), result.getMaxUsers());
        assertEquals(groupRoomDTO.getTakenInGameRoles().get(0).getId(),result.getTakenInGameRoles().get(0).getId());
    }

    @Test
    void should_update_group_room_by_group_room_updateDTO() {
        //given
        GroupRoomUpdateDTO groupRoomUpdateDTO = getGroupRoomUpdateDTOMock();
        GroupRoom groupRoom = getGroupRoomMock();

        //when
        GroupRoom result = groupRoomMapper.updateGroupRoomFromGroupRoomUpdateDTO(groupRoomUpdateDTO, groupRoom);

        //then
        assertEquals(groupRoomUpdateDTO.getName(),result.getName());
        assertEquals(groupRoomUpdateDTO.getDescription(), result.getDescription());
        assertEquals(groupRoomUpdateDTO.getMaxUsers(), result.getMaxUsers());
        assertEquals(groupRoom.getId(), result.getId());
        assertEquals(groupRoom.getGroupLeader(), result.getGroupLeader());
        assertEquals(groupRoom.getUsers(), result.getUsers());
        assertEquals(groupRoom.getGame(), result.getGame());
        assertEquals(groupRoom.getCategory(), result.getCategory());
        assertEquals(groupRoom.getJoinCode(), result.getJoinCode());
        assertEquals(groupRoom.getCity(), result.getCity());
        assertEquals(groupRoom.getChat(), result.getChat());
        assertEquals(groupRoom.getTakenInGameRoles(),result.getTakenInGameRoles());

    }

    @Test
    void should_map_id_and_name_by_group_room() {
        //given
        GroupRoom groupRoom = getGroupRoomMock();

        //when
        GroupNotifInfoDTO result = groupRoomMapper.mapGroupRoomToGroupNotifInfoDTO(groupRoom);

        //then
        assertEquals(groupRoom.getId(), result.getId());
        assertEquals(groupRoom.getName(), result.getName());
    }
}