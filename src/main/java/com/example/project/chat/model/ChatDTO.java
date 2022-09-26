package com.example.project.chat.model;

import com.example.project.chat.model.MessageDTO;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Setter
@Getter
public class ChatDTO {

    private Long id;
    private List<MessageDTO> messages;
}
