package com.example.project.chat.service;

import com.example.project.chat.mappers.MessageMapper;
import com.example.project.chat.model.*;
import com.example.project.chat.repositories.ChatRepository;
import com.example.project.chat.repositories.MessageRepository;
import com.example.project.chat.repositories.MessageStatusRepository;
import com.example.project.domain.GroupRoom;
import com.example.project.domain.Role;
import com.example.project.domain.User;
import com.example.project.exceptions.EmptyMessageException;
import com.example.project.exceptions.NotGroupLeaderException;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.project.chat.samples.ChatSample.getChatMock;
import static com.example.project.chat.samples.MessageSample.*;
import static com.example.project.samples.GroupRoomSample.getGroupRoomMock;
import static com.example.project.samples.UserMockSample.getCurrentUserMock;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SseService sseService;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private MessageStatusRepository messageStatusRepository;

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatService = new ChatServiceImpl(groupRepository, chatRepository, messageMapper, userRepository, sseService, messageRepository, messageStatusRepository);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(getCurrentUserMock());

    }

    @Test
    void save() {
        //given
        MessageDTO messageDTO = getMessageDTOMock();
        Chat chat = getChatMock();
        GroupRoom groupRoom = getGroupRoomMock();
        Message message = getMessageMock();
        when(groupRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(groupRoom));
        when(chatRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(chat));
        when(messageMapper.mapMessageDTOTOMessage(any(MessageDTO.class))).thenReturn(message);
        when(messageMapper.mapMessageToMessageDTO(any(Message.class))).thenReturn(messageDTO);

        //when
        MessageDTO result = chatService.save(messageDTO, groupRoom.getId());

        //then
        assertNotNull(result);
        verify(groupRepository, times(1)).findById(groupRoom.getId());
        verify(chatRepository, times(1)).findById(chat.getId());
        verify(messageMapper,times(1)).mapMessageToMessageDTO(any());
    }

    @Test
    void savePrivate() {
        //given
        MessageDTO messageDTO = getMessageDTOMock();
        Chat chat = getChatMock();
        Message message = getMessageMock();
        when(chatRepository.findById(any(long.class))).thenReturn(Optional.ofNullable(chat));
        when(messageMapper.mapMessageDTOTOMessage(any(MessageDTO.class))).thenReturn(message);
        when(messageMapper.mapMessageToMessageDTO(any(Message.class))).thenReturn(messageDTO);

        //when
        MessageDTO result = chatService.savePrivate(messageDTO, chat.getId());

        //then
        assertNotNull(result);
        verify(chatRepository, times(1)).findById(chat.getId());
        verify(messageStatusRepository, times(1)).save(any());
        verify(chatRepository, times(1)).save(chat);
        verify(sseService, times(1)).sendSseFriendEvent(any(), any());
        verify(messageMapper, times(1)).mapMessageToMessageDTO(message);
    }

    @Test
    void should_throw_empty_message_exception(){
        //given
        MessageDTO messageDTO = MessageDTO.builder().build();
        Chat chat = getChatMock();

        //when
        Exception exception = assertThrows(EmptyMessageException.class, () -> chatService.savePrivate(messageDTO, chat.getId()));

        //then
        assertNotNull(exception);
    }

    @Test
    void setMessagesAsRead() {
        //given
        Message message = getMessageMock();
        Chat chat = getChatMock();
        User user = getCurrentUserMock();
        when(messageRepository.findAllNotReadByChatId(any(Long.class),any(),any(Long.class))).thenReturn(Collections.singletonList(message));

        //when
        chatService.setMessagesAsRead(chat.getId());

        verify(messageRepository, times(1)).findAllNotReadByChatId(chat.getId(), MessageStatus.Status.UNREAD,user.getId());
        verify(messageRepository, times(1)).save(message);
        verify(sseService, times(1)).sendSseFriendEvent(any(), any());

    }

    @Test
    void getChatMessages() {
        //given
        MessageDTO messageDTO = getMessageDTOMock();
        Long chatId = 1L;
        Message message = getMessageMock();
        when(messageRepository.findAllByChatId(any(Long.class))).thenReturn(Collections.singletonList(message));
        when(messageMapper.mapMessageToMessageDTO(any(Message.class))).thenReturn(messageDTO);

        //when
        List<MessageDTO> result = chatService.getChatMessages(chatId);

        //then
        assertNotNull(result);
        verify(messageRepository, times(1)).findAllByChatId(chatId);
        verify(messageMapper, times(1)).mapMessageToMessageDTO(message);
    }

    @Test
    void getChatLogs() {
        //given
        MessageDTO messageDTO = getMessageDTOMock();
        Chat chat = getChatMock();
        Message message = getMessageMock();
        Long groupId = 1L;

        User user = getCurrentUserMock();
        user.setRole(Role.builder().name("ROLE_ADMIN").build());

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(user);


        when(chatRepository.findChatByGroupRoomId(any(Long.class))).thenReturn(chat);
        when(messageMapper.mapMessageToMessageDTO(any(Message.class))).thenReturn(messageDTO);

        //when
        List<MessageDTO> result = chatService.getChatLogs(groupId);

        //then
        assertNotNull(result);
        verify(chatRepository, times(1)).findChatByGroupRoomId(groupId);
        verify(messageMapper, times(1)).mapMessageToMessageDTO(any(Message.class));

    }

    @Test
    void get_group_chat_logs_should_throw_not_group_leader_exception(){
        //given
        Long groupId = 1L;

        //when
        Exception exception = assertThrows(NotGroupLeaderException.class, () -> chatService.getChatLogs(groupId));

        //then
        assertNotNull(exception);
    }

    @Test
    void getUserChatLogs() {
        //given
        Message message = getMessageMock();
        MessageLogsDTO messageLogsDTO = getMessageLogsDTOMock();
        User user = getCurrentUserMock();
        user.setRole(Role.builder().name("ROLE_ADMIN").build());

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(user);

        when(messageRepository.findAllByUserIdAndChat_notPrivate(any(Long.class), any(Boolean.class))).thenReturn(Collections.singletonList(message));
        when(messageMapper.mapMessageToMessageLogsDTO(any(Message.class))).thenReturn(messageLogsDTO);

        //when
        List<MessageLogsDTO> result = chatService.getUserChatLogs(user.getId());

        //then
        assertNotNull(result);
        verify(messageRepository, times(1)).findAllByUserIdAndChat_notPrivate(user.getId(), true);
        verify(messageMapper, times(1)).mapMessageToMessageLogsDTO(message);
    }

    @Test
    void countUnreadMessages() {
        //given
        UnreadMessageCountDTO count = getUnreadMessageCountDTOMock();
        User user = getCurrentUserMock();
        Long chatId = 1L;
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(messageRepository.countAllByStatusesUser(any(MessageStatus.Status.class), any(Long.class), any(Long.class))).thenReturn(10);

        //when
        List<UnreadMessageCountDTO> result = chatService.countUnreadMessages();

        //then
        assertThat(result.get(0).getCount()).isEqualTo(count.getCount());
        verify(userRepository, times(1)).findById(user.getId());
        verify(messageRepository, times(1)).countAllByStatusesUser(any(), any(), any());

    }

    @Test
    void getDeletedGroupChatLogs() {
        //given
        Message message = getMessageMock();
        MessageDTO messageDTO = getMessageDTOMock();
        Long groupId = 1L;
        GroupRoom groupRoom = getGroupRoomMock();

        User user = getCurrentUserMock();
        user.setRole(Role.builder().name("ROLE_ADMIN").build());

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(user);

        when(groupRepository.findDeletedById(any(Long.class))).thenReturn(groupRoom);
        when(messageMapper.mapMessageToMessageDTO(any(Message.class))).thenReturn(messageDTO);

        //when
        List<MessageDTO> result = chatService.getDeletedGroupChatLogs(groupId);

        //then
        assertNotNull(result);
        verify(groupRepository, times(1)).findDeletedById(groupId);
        verify(messageMapper, times(1)).mapMessageToMessageDTO(any(Message.class));

    }
}