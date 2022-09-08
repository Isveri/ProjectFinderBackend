package com.example.project.model;

import com.example.project.chat.model.ChatDTO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendDTO {

    private Long id;
    private UserMsgDTO user;
    private boolean online;
    private ChatDTO chat;
}
