package com.example.project.chat.service;

import com.example.project.chat.model.CustomNotificationDTO;

import java.util.List;

public interface NotificationService {

    List<CustomNotificationDTO> getAllNotifications();
    void removeNotification(Long notifId);
}
