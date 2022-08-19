package com.example.project.chat.repositories;

import com.example.project.chat.model.Message;
import com.example.project.chat.model.MessageDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByUserId(Long id);
}
