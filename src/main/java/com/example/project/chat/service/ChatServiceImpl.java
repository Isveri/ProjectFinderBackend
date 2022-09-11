package com.example.project.chat.service;

import com.example.project.chat.mappers.MessageMapper;
import com.example.project.chat.model.Chat;
import com.example.project.chat.model.Message;
import com.example.project.chat.model.MessageDTO;
import com.example.project.chat.model.MessageLogsDTO;
import com.example.project.chat.repositories.ChatRepository;
import com.example.project.chat.repositories.MessageRepository;
import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import com.example.project.exceptions.GroupNotFoundException;
import com.example.project.exceptions.NotGroupLeaderException;
import com.example.project.exceptions.UserNotFoundException;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.project.utils.UserDetailsHelper.getCurrentUser;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final GroupRepository groupRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;


    @Transactional
    @Override
    public MessageDTO save(MessageDTO messageDTO, Long groupId) {

        User user = getCurrentUser();
        GroupRoom groupRoom = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group not found"));
        if (groupRoom.getUsers().contains(user) || user.getRole().getName().equals("ROLE_ADMIN")) {
            Chat chat = chatRepository.findById(groupRoom.getChat().getId()).orElseThrow(() -> new GroupNotFoundException("Chat not found"));
            Message msg = getMessage(messageDTO, chat);
            return messageMapper.mapMessageToMessageDTO(msg);
        }
        return null;
    }

    private Message getMessage(MessageDTO messageDTO, Chat chat) {
        Message msg = messageMapper.mapMessageDTOTOMessage(messageDTO);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        msg.setDate(LocalDateTime.parse(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),formatter));
        msg.setChat(chat);
        msg.setUser(getCurrentUser());
        chat.getMessages().add(msg);
        chatRepository.save(chat);
        return msg;
    }

    @Transactional
    @Override
    public MessageDTO savePrivate(MessageDTO messageDTO, Long chatId) {
        //TODO EXCEPTION dodac
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        Message msg = getMessage(messageDTO,chat);
        return messageMapper.mapMessageToMessageDTO(msg);

    }

    @Override
    public List<MessageDTO> getChatLogs(Long groupId) {
        User user = getCurrentUser();
        if (user.getRole().getName().equals("ROLE_ADMIN")) {
            return chatRepository.findChatByGroupRoomId(groupId).getMessages()
                    .stream()
                    .map(messageMapper::mapMessageToMessageDTO)
                    .collect(Collectors.toList());
        }
        throw new NotGroupLeaderException("Not authorized");
    }

    @Override
    public List<MessageLogsDTO> getUserChatLogs(Long userId) {
        User admin = getCurrentUser();

        if(admin.getRole().getName().equals("ROLE_ADMIN")) {
            return messageRepository.findAllByUserId(userId)
                    .stream()
                    .map(messageMapper::mapMessageToMessageLogsDTO)
                    .collect(Collectors.toList());
        }
        throw new NotGroupLeaderException("Not authorized");
    }

    @Override
    public List<MessageDTO> getDeletedGroupChatLogs(Long groupId) {
        User user = getCurrentUser();
        if (user.getRole().getName().equals("ROLE_ADMIN")) {
            Chat chat = groupRepository.findDeletedById(groupId).getChat();
            return chat.getMessages()
                    .stream()
                    .map(messageMapper::mapMessageToMessageDTO)
                    .collect(Collectors.toList());
        }
        throw new NotGroupLeaderException("Not authorized");
    }
}

