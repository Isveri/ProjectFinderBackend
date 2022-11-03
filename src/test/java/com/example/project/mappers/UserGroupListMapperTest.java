package com.example.project.mappers;

import com.example.project.domain.User;
import com.example.project.model.UserGroupsListDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;

import static com.example.project.samples.GroupRoomSample.getGroupRoomMock;
import static com.example.project.samples.UserMockSample.getCurrentUserMock;
import static org.junit.jupiter.api.Assertions.*;

class UserGroupListMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final UserGroupListMapper userGroupListMapper = new UserGroupListMapperImpl(userMapper);

    @Test
    void should_map_user_to_user_group_listDTO() {
        //given
        User user = getCurrentUserMock();
        user.setGroupRooms(Arrays.asList(getGroupRoomMock()));

        //when
        UserGroupsListDTO result = userGroupListMapper.mapuserToUserGroupsListDTO(user);

        //then
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getGroupRooms().get(0).getId(), result.getGroupRooms().get(0).getId());
        assertEquals(user.getRole().getId(),result.getRole().getId());
    }
}