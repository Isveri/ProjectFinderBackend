package com.example.project.chat.model;

import com.example.project.model.UserMsgDTO;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class MessageStatusDTO {
    private Long id;
    private MessageStatus.Status status;
    private UserMsgDTO user;
}
