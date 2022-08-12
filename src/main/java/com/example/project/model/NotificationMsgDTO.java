package com.example.project.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class NotificationMsgDTO {
    private String text;
    private boolean isNegative;
    private String type;
    private Long groupId;
}
