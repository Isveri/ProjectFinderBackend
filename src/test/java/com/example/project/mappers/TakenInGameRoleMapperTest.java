package com.example.project.mappers;

import com.example.project.domain.TakenInGameRole;
import com.example.project.model.TakenInGameRoleDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.example.project.samples.InGameRolesSample.getTakenInGameRoleDTOMock;
import static com.example.project.samples.InGameRolesSample.getTakenInGameRoleMock;
import static org.junit.jupiter.api.Assertions.*;

class TakenInGameRoleMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private final InGameRolesMapper inGameRolesMapper = Mappers.getMapper(InGameRolesMapper.class);

    private final TakenInGameRoleMapper inGameRoleMapper = new TakenInGameRoleMapperImpl(userMapper,inGameRolesMapper);

    @Test
    void should_map_taken_in_game_role_to_taken_in_game_roleDTO() {
        //given
        TakenInGameRole takenInGameRole = getTakenInGameRoleMock();

        //when
        TakenInGameRoleDTO result = inGameRoleMapper.mapTakenInGameRoleToTakenInGameRoleDTO(takenInGameRole);

        //then
        assertEquals(takenInGameRole.getId(), result.getId());
        assertEquals(takenInGameRole.getInGameRole().getId(), result.getInGameRole().getId());
        assertEquals(takenInGameRole.getUser().getId(), result.getUser().getId());
    }

    @Test
    void should_map_taken_in_game_roleDTO_to_taken_in_game_role() {
        //given
        TakenInGameRoleDTO takenInGameRoleDTO = getTakenInGameRoleDTOMock();

        //when
        TakenInGameRole result = inGameRoleMapper.mapTakenInGameRoleDTOToTakenInGameRole(takenInGameRoleDTO);

        //then
        assertEquals(takenInGameRoleDTO.getId(), result.getId());
        assertEquals(takenInGameRoleDTO.getInGameRole().getId(), result.getInGameRole().getId());
        assertEquals(takenInGameRoleDTO.getUser().getId(), result.getUser().getId());
    }
}