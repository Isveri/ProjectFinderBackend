package com.example.project.chat.service;

import com.example.project.chat.mappers.MessageMapper;
import com.example.project.chat.model.*;
import com.example.project.chat.repositories.ChatRepository;
import com.example.project.chat.repositories.MessageRepository;
import com.example.project.chat.repositories.MessageStatusRepository;
import com.example.project.domain.Friend;
import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import com.example.project.exceptions.EmptyMessageException;
import com.example.project.exceptions.GroupNotFoundException;
import com.example.project.exceptions.NotGroupLeaderException;
import com.example.project.exceptions.UserNotFoundException;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.project.utils.UserDetailsHelper.getCurrentUser;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final GroupRepository groupRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final UserRepository userRepository;
    private final SseService sseService;
    private final MessageRepository messageRepository;
    private final MessageStatusRepository messageStatusRepository;


    @Transactional
    @Override
    public MessageDTO save(MessageDTO messageDTO, Long groupId) {

        if (messageDTO.getText() != null) {
            User user = getCurrentUser();
            GroupRoom groupRoom = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group not found"));
            if (groupRoom.getUsers().contains(user) || user.getRole().getName().equals("ROLE_ADMIN")) {
                Chat chat = chatRepository.findById(groupRoom.getChat().getId()).orElseThrow(() -> new GroupNotFoundException("Chat not found"));
                Message msg = getMessage(messageDTO, chat);
                chat.getMessages().add(msg);
                chatRepository.save(chat);
                return messageMapper.mapMessageToMessageDTO(msg);
            }
        }
        throw new EmptyMessageException("Cannot send empty message");
    }

    private Message getMessage(MessageDTO messageDTO, Chat chat) {
        if (!messageDTO.getText().isBlank()) {
            Message msg = messageMapper.mapMessageDTOTOMessage(messageDTO);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            msg.setDate(LocalDateTime.parse(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), formatter));
            msg.setChat(chat);
            msg.setUser(getCurrentUser());
            return msg;
        }
        throw new EmptyMessageException("Cannot send empty message");
    }

    @Transactional
    @Override
    public MessageDTO savePrivate(MessageDTO messageDTO, Long chatId) {
        if (messageDTO.getText() != null) {
            Chat chat = chatRepository.findById(chatId).orElseThrow();
            Message msg = getMessage(messageDTO, chat);
            Friend friend = chat.getUsers().stream().filter((fr -> !getCurrentUser().equals(fr.getUser()))).findAny().orElseThrow(() -> new UserNotFoundException("User doesnt exist"));
            MessageStatus msgStatus = MessageStatus.builder().user(friend.getUser()).build();
            msgStatus.setMessage(msg);
            messageStatusRepository.save(msgStatus);
            msg.setStatuses(List.of(msgStatus));
            chat.getMessages().add(msg);
            chatRepository.save(chat);
            sseService.sendSseFriendEvent(CustomNotificationDTO.builder().msg("New message").type(CustomNotification.NotifType.PRIVATE_MESSAGE).build(), friend.getUser().getId());
            return messageMapper.mapMessageToMessageDTO(msg);
        }
        throw new EmptyMessageException("Cannot send empty message");
    }

    @Override
    public List<MessageDTO> setMessagesAsRead(Long chatId) {

        User user = getCurrentUser();
        List<Message> messageList = messageRepository.findAllNotReadByChatId(chatId, MessageStatus.Status.UNREAD, user.getId());
        messageList.forEach((message -> {
            messageRepository.save(setStatus(user, message));
        }));
        sseService.sendSseFriendEvent(CustomNotificationDTO.builder().msg("New message").type(CustomNotification.NotifType.PRIVATE_MESSAGE).build(), user.getId());
        return null;
    }

    @Override
    public List<MessageDTO> getChatMessages(Long chatId) {
        return messageRepository.findAllByChatId(chatId)
                .stream()
                .map(messageMapper::mapMessageToMessageDTO)
                .collect(Collectors.toList());
    }

    private Message setStatus(User user, Message message) {
        message.getStatuses()
                .stream()
                .filter(messageStatus ->
                        user.equals(messageStatus.getUser())
                ).findFirst().orElseThrow(() -> new NotFoundException("Status of message not found")).setStatus(MessageStatus.Status.READ);
        return message;
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

        if (admin.getRole().getName().equals("ROLE_ADMIN")) {
            return messageRepository.findAllByUserIdAndChat_notPrivate(userId,true)
                    .stream()
                    .map(messageMapper::mapMessageToMessageLogsDTO)
                    .collect(Collectors.toList());
        }
        throw new NotGroupLeaderException("Not authorized");
    }


    @Override
    public List<UnreadMessageCountDTO> countUnreadMessages() {
        List<UnreadMessageCountDTO> unreadMessages = new ArrayList<>();
        User user = userRepository.findById(getCurrentUser().getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.getFriendList().forEach((friend -> {
            UnreadMessageCountDTO unreadMessageCountDTO = new UnreadMessageCountDTO();
            unreadMessageCountDTO.setUserId(friend.getUser().getId());
            unreadMessageCountDTO.setCount(messageRepository.countAllByStatusesUser(MessageStatus.Status.UNREAD, friend.getUser().getId(), friend.getChat().getId()));
            unreadMessages.add(unreadMessageCountDTO);
        }));
        return unreadMessages;
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

