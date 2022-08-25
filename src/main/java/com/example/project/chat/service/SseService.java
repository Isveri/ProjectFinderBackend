package com.example.project.chat.service;

import com.example.project.chat.model.CustomNotificationDTO;
import com.example.project.domain.GroupRoom;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
    void sendSseEventToUser(CustomNotificationDTO customNotificationDTO, GroupRoom groupRoom, Long modifiedUserId);

    SseEmitter createEmitter();

}
