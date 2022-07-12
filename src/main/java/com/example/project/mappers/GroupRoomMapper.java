package com.example.project.mappers;

import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import com.example.project.model.GroupRoomDTO;
import com.example.project.model.UserDTO;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true),
        uses = UserMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class GroupRoomMapper {

    public abstract GroupRoomDTO mapGroupRoomToGroupRoomDTO(GroupRoom groupRoom);

    public abstract GroupRoom mapGroupRoomDTOToGroupRoom(GroupRoomDTO groupRoomDTO);

}
