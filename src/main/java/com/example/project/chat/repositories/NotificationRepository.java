package com.example.project.chat.repositories;

import com.example.project.chat.model.CustomNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<CustomNotification,Long> {

    List<CustomNotification> findAllByUserId(Long userId);
}
