package com.example.project.chat.model;

import com.example.project.chat.model.CustomNotification.NotifType;
import com.example.project.model.GroupNotifInfoDTO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CustomNotificationDTO {

    private Long id;
    private String msg;
    private NotifType type;
    private GroupNotifInfoDTO groupRoom;
    private Long removedUserId;
}
