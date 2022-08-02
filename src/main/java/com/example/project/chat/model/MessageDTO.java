package com.example.project.chat.model;

import com.example.project.model.UserDTO;
import com.example.project.model.UserProfileDTO;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class MessageDTO {
    private Long id;
    private String text;
    private UserProfileDTO user;
    private LocalDateTime time;
}
