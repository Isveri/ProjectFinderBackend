package com.example.project.mappers;

import com.example.project.domain.TakenInGameRole;
import com.example.project.model.TakenInGameRoleDTO;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true),
        uses = {UserMapper.class,InGameRolesMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class TakenInGameRoleMapper {

    public abstract TakenInGameRoleDTO mapTakenInGameRoleToTakenInGameRoleDTO(TakenInGameRole takenInGameRole);

    public abstract TakenInGameRole mapTakenInGameRoleDTOToTakenInGameRole(TakenInGameRoleDTO takenInGameRoleDTO);
}
