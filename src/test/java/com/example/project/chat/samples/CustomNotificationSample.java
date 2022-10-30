package com.example.project.chat.samples;

import com.example.project.chat.model.CustomNotification;
import com.example.project.chat.model.CustomNotificationDTO;

import static com.example.project.chat.samples.GroupRoomNotifSample.getGroupNotifInfoDTOMock;
import static com.example.project.samples.GroupRoomSample.getGroupRoomMock;
import static com.example.project.samples.UserMockSample.getCurrentUserMock;

public class CustomNotificationSample {

    public static CustomNotificationDTO getCustomNotificationDTOMock(){
        return CustomNotificationDTO.builder()
                .msg("mock")
                .id(1L)
                .groupRoom(getGroupNotifInfoDTOMock())
                .type(CustomNotification.NotifType.REMOVED)
                .build();
    }

    public static CustomNotification getCustomNotificationMoc() {
        return CustomNotification.builder()
                .id(1L)
                .groupRoom(getGroupRoomMock())
                .msg("mock")
                .type(CustomNotification.NotifType.REMOVED)
                .user(getCurrentUserMock())
                .build();
    }

}
