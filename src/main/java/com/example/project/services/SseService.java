package com.example.project.services;

import com.example.project.chat.model.NotificationMsg;

public interface SseService {
    void sendSseEventToUser(NotificationMsg notificationMsg);

}
