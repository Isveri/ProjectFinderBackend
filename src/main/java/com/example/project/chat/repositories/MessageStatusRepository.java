package com.example.project.chat.repositories;

import com.example.project.chat.model.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageStatusRepository extends JpaRepository<MessageStatus,Long> {
}
