package com.example.project.mappers;

import com.example.project.domain.User;
import com.example.project.model.UserGroupsListDTO;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true),
        uses = UserMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UserGroupListMapper {

    public abstract UserGroupsListDTO mapuserToUserGroupsListDTO(User user);
}
