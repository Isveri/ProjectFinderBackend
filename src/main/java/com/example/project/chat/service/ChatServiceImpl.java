package com.example.project.chat.service;

import com.example.project.chat.mappers.MessageMapper;
import com.example.project.chat.model.Chat;
import com.example.project.chat.model.Message;
import com.example.project.chat.model.MessageDTO;
import com.example.project.chat.repositories.ChatRepository;
import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import com.example.project.exceptions.GroupNotFoundException;
import com.example.project.repositories.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.project.utils.UserDetailsHelper.getCurrentUser;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final GroupRepository groupRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;

    @Transactional
    @Override
    public MessageDTO save(MessageDTO messageDTO, Long groupId) {

        User user = getCurrentUser();
        GroupRoom groupRoom = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group not found"));
        if (groupRoom.getUsers().contains(user)) {
            Chat chat = chatRepository.findById(groupRoom.getChat().getId()).orElseThrow(() -> new GroupNotFoundException("Chat not found"));
            Message msg = messageMapper.mapMessageDTOTOMessage(messageDTO);
            LocalDateTime now = LocalDateTime.now();
            msg.setTime(now.format(DateTimeFormatter.ofPattern("HH:mm")));
            msg.setChat(chat);
            chat.getMessages().add(msg);
            chatRepository.save(chat);
            return messageMapper.mapMessageToMessageDTO(msg);
        }
        return null;
    }
}
