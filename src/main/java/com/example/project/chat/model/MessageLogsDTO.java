package com.example.project.chat.model;

import com.example.project.model.UserMsgDTO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageLogsDTO {

    private Long id;
    private String text;
    private UserMsgDTO user;
    private String time;
    private String groupName;
}
