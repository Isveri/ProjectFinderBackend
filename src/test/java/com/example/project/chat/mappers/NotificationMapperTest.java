package com.example.project.chat.mappers;

import com.example.project.chat.model.CustomNotification;
import com.example.project.chat.model.CustomNotificationDTO;
import com.example.project.mappers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.example.project.chat.samples.CustomNotificationSample.getCustomNotificationDTOMock;
import static com.example.project.chat.samples.CustomNotificationSample.getCustomNotificationMoc;
import static org.junit.jupiter.api.Assertions.*;

class NotificationMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private final InGameRolesMapper inGameRolesMapper = Mappers.getMapper(InGameRolesMapper.class);
    private final TakenInGameRoleMapper takenInGameRoleMapper = new TakenInGameRoleMapperImpl(userMapper,inGameRolesMapper);
    private final GroupRoomMapper groupRoomMapper = new GroupRoomMapperImpl(userMapper,takenInGameRoleMapper);
    private final NotificationMapper notificationMapper = new NotificationMapperImpl(groupRoomMapper);


    @Test
    void should_map_custom_notification_to_custom_notificationDTO() {
        //given
        CustomNotificationDTO customNotificationDTO = getCustomNotificationDTOMock();

        //when
        CustomNotification result = notificationMapper.mapCustomNotificationDTOToCustomNotification(customNotificationDTO);

        //then
        assertEquals(customNotificationDTO.getId(), result.getId());
        assertEquals(customNotificationDTO.getType(), result.getType());
        assertEquals(customNotificationDTO.getMsg(), result.getMsg());
        assertEquals(customNotificationDTO.getGroupRoom().getId(), result.getGroupRoom().getId());
    }

    @Test
    void should_map_custom_notificationDTO_to_custom_notification() {
        //given
        CustomNotification customNotification = getCustomNotificationMoc();

        //when
        CustomNotificationDTO result = notificationMapper.mapCustomNotificationToCustomNotificationDTO(customNotification);

        //then
        assertEquals(customNotification.getId(), result.getId());
        assertEquals(customNotification.getType(), result.getType());
        assertEquals(customNotification.getMsg(), result.getMsg());
        assertEquals(customNotification.getGroupRoom().getId(), result.getGroupRoom().getId());

    }
}