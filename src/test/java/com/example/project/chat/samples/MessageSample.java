package com.example.project.chat.samples;

import com.example.project.chat.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.example.project.samples.GroupRoomSample.getGroupRoomMock;
import static com.example.project.samples.UserMockSample.getCurrentUserMock;
import static com.example.project.samples.UserMockSample.getUserMsgDTOMockv2;

public class MessageSample {


    public static Message getMessageMock() {
        return Message.builder()
                .id(1L)
                .text("mock")
                .statuses(new ArrayList<>(Arrays.asList(getMessageStatusMock())))
                .user(getCurrentUserMock())
                .build();
    }

    public static MessageDTO getMessageDTOMock() {
        return MessageDTO.builder()
                .connectedUsers(Collections.singletonList(getCurrentUserMock().getId()))
                .id(1L)
                .groupId(getGroupRoomMock().getId())
                .text("mock").build();
    }

    public static MessageLogsDTO getMessageLogsDTOMock(){
        return MessageLogsDTO.builder()
                .id(1L)
                .user(getUserMsgDTOMockv2())
                .build();
    }

    public static UnreadMessageCountDTO getUnreadMessageCountDTOMock(){
        return UnreadMessageCountDTO.builder()
                .count(10)
                .userId(1L)
                .build();
    }

    public static MessageStatus getMessageStatusMock(){
        return MessageStatus.builder()
                .id(1L)
                .user(getCurrentUserMock())
                .status(MessageStatus.Status.UNREAD).build();
    }
}
