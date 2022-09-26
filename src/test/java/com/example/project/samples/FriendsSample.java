package com.example.project.samples;

import com.example.project.model.FriendDTO;
import com.example.project.model.FriendRequestDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.project.samples.UserMockSample.getCurrentUserMsgDTOMock;
import static com.example.project.samples.UserMockSample.getUserMsgDTOMock;

public class FriendsSample {

    public static FriendRequestDTO getFriendRequestDTOMock(){
        return FriendRequestDTO.builder()
                .id(1L)
                .accepted(false)
                .sendingUser(getCurrentUserMsgDTOMock())
                .invitedUser(getUserMsgDTOMock()).build();
    }

    public static FriendDTO getFriendDTOMock(){
        return FriendDTO.builder()
                .id(1L)
                .chatId(1L)
                .user(getUserMsgDTOMock())
                .build();
    }
}
