package com.example.project.services;

import com.example.project.domain.GroupRoom;
import com.example.project.model.NotificationMsgDTO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
    void sendSseEventToUser(NotificationMsgDTO notificationMsgDTO, GroupRoom groupRoom, Long modifiedUserId);

    SseEmitter createEmitter();

}
