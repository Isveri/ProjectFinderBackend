package com.example.project.chat.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class UnreadMessageCountDTO {
    private Long id;
    private int count;
    private Long userId;
}
