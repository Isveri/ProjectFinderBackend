package com.example.project.chat.service;

import com.example.project.chat.mappers.MessageMapper;
import com.example.project.chat.model.Chat;
import com.example.project.chat.model.Message;
import com.example.project.chat.model.MessageDTO;
import com.example.project.chat.repositories.ChatRepository;
import com.example.project.chat.repositories.MessageRepository;
import com.example.project.domain.GroupRoom;
import com.example.project.exceptions.NotFoundException;
import com.example.project.mappers.UserMapper;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final GroupRepository groupRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserMapper userMapper;
    private final MessageMapper messageMapper;
    @Transactional
    @Override
    public MessageDTO save(MessageDTO messageDTO, Long groupId) {
        GroupRoom groupRoom = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Group not found"));
        Chat chat = chatRepository.findById(groupRoom.getChat().getId()).orElseThrow(()->new NotFoundException("Chat not found"));
        Message msg = messageMapper.mapMessageDTOTOMessage(messageDTO);
        msg.setTime(LocalDateTime.now());
        msg.setChat(chat);
        chat.getMessages().add(msg);
        chatRepository.save(chat);
        return messageMapper.mapMessageToMessageDTO(msg);
    }
}
