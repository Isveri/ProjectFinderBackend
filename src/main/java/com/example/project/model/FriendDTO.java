package com.example.project.model;

import com.example.project.chat.model.ChatDTO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class FriendDTO {

    private Long id;
    private UserMsgDTO user;
    private boolean online;
    private Long chatId;
}
