package com.example.project.chat.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class NotificationMsg {
    private String text;
    private boolean isNegative;
}
