package com.example.project.chat.model;

import com.example.project.model.UserDTO;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class MessageDTO {
    private Long id;
    private String text;
    private UserDTO user;
    private Long groupId;
}
