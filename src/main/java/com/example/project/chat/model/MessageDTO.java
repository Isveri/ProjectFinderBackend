package com.example.project.chat.model;

import com.example.project.model.UserMsgDTO;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class MessageDTO {
    private Long id;
    private String text;
    private UserMsgDTO user;
    private Long groupId;
    private String time;
    private List<Long> connectedUsers;
}
