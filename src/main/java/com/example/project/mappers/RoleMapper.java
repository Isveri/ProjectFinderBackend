package com.example.project.mappers;

import com.example.project.domain.Role;
import com.example.project.model.RoleDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true))
public abstract class RoleMapper {
    public abstract RoleDTO mapRoleToRoleDTO(Role role);
}
