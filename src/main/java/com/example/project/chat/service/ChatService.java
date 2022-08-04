package com.example.project.chat.service;

import com.example.project.chat.model.MessageDTO;

public interface ChatService {

    MessageDTO save(MessageDTO messageDTO,Long groupId);
}
