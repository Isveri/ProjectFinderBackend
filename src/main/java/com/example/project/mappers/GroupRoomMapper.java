package com.example.project.mappers;

import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import com.example.project.model.GroupNotifInfoDTO;
import com.example.project.model.GroupRoomDTO;
import com.example.project.model.GroupRoomUpdateDTO;
import com.example.project.model.UserDTO;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(builder = @Builder(disableBuilder = true),
        uses = {UserMapper.class,TakenInGameRoleMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class GroupRoomMapper {

    public abstract GroupRoomDTO mapGroupRoomToGroupRoomDTO(GroupRoom groupRoom);

    public abstract GroupRoom mapGroupRoomDTOToGroupRoom(GroupRoomDTO groupRoomDTO);

    public abstract GroupRoom updateGroupRoomFromGroupRoomUpdateDTO(GroupRoomUpdateDTO groupRoomUpdateDTO, @MappingTarget GroupRoom groupRoom);

    public abstract GroupNotifInfoDTO mapGroupRoomToGroupNotifInfoDTO(GroupRoom groupRoom);

}
