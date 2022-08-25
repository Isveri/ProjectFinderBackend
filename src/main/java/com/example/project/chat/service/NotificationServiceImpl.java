package com.example.project.chat.service;

import com.example.project.chat.mappers.NotificationMapper;
import com.example.project.chat.model.CustomNotificationDTO;
import com.example.project.chat.repositories.NotificationRepository;
import com.example.project.utils.UserDetailsHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public List<CustomNotificationDTO> getAllNotifications() {
        return notificationRepository.findAllByUserId(UserDetailsHelper.getCurrentUser().getId())
                .stream()
                .map(notificationMapper::mapCustomNotificationToCustomNotificationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void removeNotification(Long notifId) {
        notificationRepository.deleteById(notifId);
    }
}
