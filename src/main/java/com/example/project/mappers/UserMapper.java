package com.example.project.mappers;

import com.example.project.domain.User;
import com.example.project.model.UserDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true))
public abstract class UserMapper {

    public abstract UserDTO mapUserToUserDTO(User user);

    public abstract User mapUserDTOToUser(UserDTO userDTO);
}
