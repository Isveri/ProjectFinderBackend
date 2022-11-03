package com.example.project.mappers;

import com.example.project.domain.InGameRole;
import com.example.project.model.InGameRolesDTO;
import com.example.project.samples.InGameRolesSample;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.example.project.samples.InGameRolesSample.getInGameRoleDTOMock;
import static com.example.project.samples.InGameRolesSample.getInGameRoleMock;
import static org.junit.jupiter.api.Assertions.*;

class InGameRolesMapperTest {

    private final InGameRolesMapper inGameRolesMapper = Mappers.getMapper(InGameRolesMapper.class);

    @Test
    void should_map_ingame_role_to_ingame_roleDTO() {
        //given
        InGameRole inGameRole = getInGameRoleMock();

        //when
        InGameRolesDTO result = inGameRolesMapper.mapInGameRolesToInGameRolesDTO(inGameRole);

        //then
        assertEquals(inGameRole.getId(),result.getId());
        assertEquals(inGameRole.getName(),result.getName());
    }

    @Test
    void should_map_ingame_roleDTO_to_ingame_role() {
        //given
        InGameRolesDTO inGameRolesDTO = getInGameRoleDTOMock();

        //when
        InGameRole result = inGameRolesMapper.mapInGameRolesDTOToInGameRole(inGameRolesDTO);

        //then
        assertEquals(inGameRolesDTO.getId(),result.getId());
        assertEquals(inGameRolesDTO.getName(),result.getName());

    }
}