package com.example.project.services;

import com.example.project.chat.model.NotificationMsg;
import com.example.project.domain.GroupRoom;

public interface SseService {
    void sendSseEventToUser(NotificationMsg notificationMsg, GroupRoom groupRoom);

}
