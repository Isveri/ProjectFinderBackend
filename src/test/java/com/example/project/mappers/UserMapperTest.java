package com.example.project.mappers;

import com.example.project.domain.User;
import com.example.project.model.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;

import static com.example.project.samples.InGameRolesSample.getInGameRoleDTOMock;
import static com.example.project.samples.InGameRolesSample.getInGameRoleMock;
import static com.example.project.samples.PlatformSample.getPlatformMock;
import static com.example.project.samples.UserMockSample.*;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void should_map_user_to_userDTO() {
        //given
        User user = getCurrentUserMock();
        user.setInGameRoles(Collections.singletonList(getInGameRoleMock()));

        //when
        UserDTO result = userMapper.mapUserToUserDTO(user);

        //then
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(),result.getUsername());
        assertEquals(user.getName(),result.getName());
        assertEquals(user.getCity(),result.getCity());
        assertEquals(user.getInfo(),result.getInfo());
        assertEquals(user.getEmail(),result.getEmail());
        assertEquals(user.getAge(),result.getAge());
        assertEquals(user.getInGameRoles().get(0).getId(),result.getInGameRoles().get(0).getId());
        assertEquals(user.getPhone(),result.getPhone());
        assertEquals(user.getPassword(),result.getPassword());
        assertEquals(user.getRole().getId(),result.getRole().getId());
    }

    @Test
    void should_update_user_from_userDTO() {
        //given
        User user = getCurrentUserMock();
        UserDTO userDTO = getCurrentUserDTOMock();
        userDTO.setInGameRoles(Collections.singletonList(getInGameRoleDTOMock()));
        userDTO.setRole(RoleDTO.builder().id(1L).build());

        //when
        User result = userMapper.updateUserFromUserDTO(userDTO, user);

        //then
        assertEquals(userDTO.getId(), result.getId());
        assertEquals(userDTO.getUsername(),result.getUsername());
        assertEquals(userDTO.getName(),result.getName());
        assertEquals(userDTO.getCity(),result.getCity());
        assertEquals(userDTO.getInfo(),result.getInfo());
        assertEquals(userDTO.getEmail(),result.getEmail());
        assertEquals(userDTO.getAge(),result.getAge());
        assertEquals(userDTO.getInGameRoles().get(0).getId(),result.getInGameRoles().get(0).getId());
        assertEquals(userDTO.getPhone(),result.getPhone());
        assertEquals(userDTO.getPassword(),result.getPassword());
        assertEquals(userDTO.getRole().getId(),result.getRole().getId());
        assertEquals(user.getEnabled(), result.getEnabled());
        assertEquals(user.getAccountNonExpired(),result.getAccountNonExpired());

    }

    @Test
    void should_map_userDTO_to_user() {
        //given
        UserDTO userDTO = getUserDTOMock();
        userDTO.setInGameRoles(Collections.singletonList(getInGameRoleDTOMock()));
        userDTO.setRole(RoleDTO.builder().id(1L).build());

        //when
        User result = userMapper.mapUserDTOToUser(userDTO);

        //then
        assertEquals(userDTO.getId(), result.getId());
        assertEquals(userDTO.getUsername(),result.getUsername());
        assertEquals(userDTO.getName(),result.getName());
        assertEquals(userDTO.getCity(),result.getCity());
        assertEquals(userDTO.getInfo(),result.getInfo());
        assertEquals(userDTO.getEmail(),result.getEmail());
        assertEquals(userDTO.getAge(),result.getAge());
        assertEquals(userDTO.getInGameRoles().get(0).getId(),result.getInGameRoles().get(0).getId());
        assertEquals(userDTO.getPhone(),result.getPhone());
        assertEquals(userDTO.getPassword(),result.getPassword());
        assertEquals(userDTO.getRole().getId(),result.getRole().getId());

    }

    @Test
    void should_map_user_to_user_profileDTO() {
        //given
        User user = getCurrentUserMock();
        user.setPlatforms(Collections.singletonList(getPlatformMock()));
        user.setInGameRoles(Collections.singletonList(getInGameRoleMock()));

        //when
        UserProfileDTO result = userMapper.mapUserToUserProfileDTO(user);

        //then
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getRole().getId(), result.getRole().getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getAge(), result.getAge());
        assertEquals(user.getInfo(), result.getInfo());
        assertEquals(user.getCity(), result.getCity());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getInGameRoles().get(0).getId(), result.getInGameRoles().get(0).getId());
        assertEquals(user.getPhone(), result.getPhone());
        assertEquals(user.getPlatforms().get(0).getId(), result.getPlatforms().get(0).getId());

    }

    @Test
    void should_map_user_to_user_msgDTO() {
        //given
        User user = getCurrentUserMock();

        //when
        UserMsgDTO result = userMapper.mapUserToUserMsgDTO(user);

        //then
        assertEquals(user.getId(),result.getId());
        assertEquals(user.getRole().getId(),result.getRole().getId());
        assertEquals(user.getUsername(),result.getUsername());
    }

    @Test
    void should_map_user_msgDTO_to_user() {
        //given
        UserMsgDTO userMsgDTO = getUserMsgDTOMock();
        userMsgDTO.setRole(RoleDTO.builder().id(1L).build());

        //when
        User result = userMapper.mapUserMsgDTOToUser(userMsgDTO);

        //then
        assertEquals(userMsgDTO.getId(),result.getId());
        assertEquals(userMsgDTO.getRole().getId(),result.getRole().getId());
        assertEquals(userMsgDTO.getUsername(),result.getUsername());
    }

    @Test
    void should_map_user_to_banned_userDTO() {
        //given
        User user = getCurrentUserMock();
        user.setBannedBy("Evi");
        user.setReason("Toxic");

        //when
        BannedUserDTO result = userMapper.mapUserToBannedUserDTO(user);

        //then
        assertEquals(user.getId(),result.getId());
        assertEquals(user.getBannedBy(),result.getBannedBy());
        assertEquals(user.getUsername(),result.getUsername());
        assertEquals(user.getReason(),result.getReason());
    }
}