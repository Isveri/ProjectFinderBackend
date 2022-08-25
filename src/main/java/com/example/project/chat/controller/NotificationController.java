package com.example.project.chat.controller;

import com.example.project.chat.model.CustomNotificationDTO;
import com.example.project.chat.repositories.NotificationRepository;
import com.example.project.chat.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/all")
    public ResponseEntity<List<CustomNotificationDTO>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @DeleteMapping("/delete/{notifId}")
    public ResponseEntity<Void> removeNotification(@PathVariable Long notifId){
        notificationService.removeNotification(notifId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
