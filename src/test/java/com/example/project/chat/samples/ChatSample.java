package com.example.project.chat.samples;

import com.example.project.chat.model.Chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.example.project.chat.samples.MessageSample.getMessageMock;
import static com.example.project.samples.FriendsSample.getFriendMock;

public class ChatSample {

    public static Chat getChatMock(){
        return Chat.builder()
                .id(1L)
                .users(new ArrayList<>(Arrays.asList(getFriendMock())))
                .messages(new ArrayList<>(Arrays.asList(getMessageMock())))
                .build();
    }
    public static Chat getChatMockv2(){
        return Chat.builder()
                .id(2L)
                .users(new ArrayList<>())
                .messages(new ArrayList<>())
                .build();
    }
}
