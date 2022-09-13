package com.example.project.chat.service;

import com.example.project.chat.model.MessageDTO;
import com.example.project.chat.model.MessageLogsDTO;
import com.example.project.chat.model.UnreadMessageCountDTO;

import java.util.List;

public interface ChatService {

    MessageDTO save(MessageDTO messageDTO,Long groupId);

    MessageDTO savePrivate(MessageDTO messageDTO, Long chatId);

    List<MessageDTO> setMessagesAsRead(Long chatId);

    List<MessageDTO> getChatLogs(Long groupId);
    List<MessageLogsDTO> getUserChatLogs(Long userId);
    List<UnreadMessageCountDTO> countUnreadMessages();
    List<MessageDTO> getDeletedGroupChatLogs(Long groupId);
}
