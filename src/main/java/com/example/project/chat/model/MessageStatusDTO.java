package com.example.project.chat.model;

import com.example.project.model.UserMsgDTO;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Setter
@Getter
public class MessageStatusDTO {
    private Long id;
    private MessageStatus.Status status;
    private UserMsgDTO user;
}
