package com.example.project.chat.mappers;

import com.example.project.chat.model.CustomNotification;
import com.example.project.chat.model.CustomNotificationDTO;
import com.example.project.mappers.GroupRoomMapper;
import com.example.project.mappers.UserMapper;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(builder = @Builder(disableBuilder = true),
        uses = {UserMapper.class, GroupRoomMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class NotificationMapper {

    public abstract CustomNotification mapCustomNotificationDTOToCustomNotification(CustomNotificationDTO customNotificationDTO);

    public abstract CustomNotificationDTO mapCustomNotificationToCustomNotificationDTO(CustomNotification customNotification);
}
