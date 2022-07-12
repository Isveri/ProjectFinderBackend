package com.example.project.mappers;

import com.example.project.domain.InGameRole;
import com.example.project.model.InGameRolesDTO;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true))
public abstract class InGameRolesMapper {

    public abstract InGameRolesDTO mapInGameRolesToInGameRolesDTO(InGameRole inGameRole);
}
