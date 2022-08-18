package com.example.project.chat.service;

import com.example.project.chat.model.MessageDTO;

import java.util.List;

public interface ChatService {

    MessageDTO save(MessageDTO messageDTO,Long groupId);
    List<MessageDTO> getChatLogs(Long groupId);
}
