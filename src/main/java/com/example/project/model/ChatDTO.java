package com.example.project.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class ChatDTO {

    private Long id;
    private List<MessageDTO> messages;
}
