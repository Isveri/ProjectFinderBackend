package com.example.project.samples;

import com.example.project.domain.Friend;
import com.example.project.domain.FriendRequest;
import com.example.project.model.FriendDTO;
import com.example.project.model.FriendRequestDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.project.samples.UserMockSample.*;

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

    public static Friend getFriendMock(){
        return Friend.builder()
                .id(1L)
                .user(getUserMock())
                .build();
    }
    public static FriendRequest getFriendRequestMock(){
        return FriendRequest.builder()
                .id(1L)
                .sendingUser(getUserMock())
                .invitedUser(getCurrentUserMock())
                .build();
    }
    public static FriendRequest getFriendRequestMockv2(){
        return FriendRequest.builder()
                .id(2L)
                .sendingUser(getCurrentUserMock())
                .invitedUser(getSendingUserMock())
                .build();
    }
}
