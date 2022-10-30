package com.example.project.chat.service;

import com.example.project.chat.mappers.NotificationMapper;
import com.example.project.chat.model.CustomNotification;
import com.example.project.chat.model.CustomNotificationDTO;
import com.example.project.chat.repositories.NotificationRepository;
import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import com.example.project.mappers.GroupRoomMapper;
import com.example.project.model.GroupNotifInfoDTO;
import com.example.project.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.project.chat.samples.CustomNotificationSample.getCustomNotificationDTOMock;
import static com.example.project.chat.samples.CustomNotificationSample.getCustomNotificationMoc;
import static com.example.project.chat.samples.GroupRoomNotifSample.getGroupNotifInfoDTOMock;
import static com.example.project.samples.GroupRoomSample.getGroupRoomMock;
import static com.example.project.samples.UserMockSample.getCurrentUserMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SseServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private NotificationMapper notificationMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRoomMapper groupRoomMapper;

    @Mock
    private Map<Long, SseEmitter> emitters;

    private SseService sseService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sseService = new SseServiceImpl(notificationRepository, notificationMapper, userRepository, groupRoomMapper);
        emitters = new HashMap<>();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(getCurrentUserMock());

    }

    @Test
    void sendSseEventToUser() {
        //given
        CustomNotificationDTO customNotificationDTO = getCustomNotificationDTOMock();
        CustomNotification customNotification = getCustomNotificationMoc();
        GroupNotifInfoDTO grNotif = getGroupNotifInfoDTOMock();
        GroupRoom groupRoom = getGroupRoomMock();
        User user = getCurrentUserMock();
        Long userId = 2L;
        when(groupRoomMapper.mapGroupRoomToGroupNotifInfoDTO(any(GroupRoom.class))).thenReturn(grNotif);
        when(notificationMapper.mapCustomNotificationDTOToCustomNotification(any(CustomNotificationDTO.class))).thenReturn(customNotification);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));


        //when
        sseService.sendSseEventToUser(customNotificationDTO, groupRoom, userId);

        //then
        verify(groupRoomMapper, times(2)).mapGroupRoomToGroupNotifInfoDTO(groupRoom);
        verify(notificationMapper, times(2)).mapCustomNotificationDTOToCustomNotification(customNotificationDTO);
        verify(notificationRepository, times(2)).save(any());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void sendSseFriendEvent() {
        //given
        User user = getCurrentUserMock();
        sseService.createEmitter();
        CustomNotificationDTO customNotificationDTO = getCustomNotificationDTOMock();

        //when
        sseService.sendSseFriendEvent(customNotificationDTO, user.getId());


    }

    @Test
    void createEmitter() {

        //when
        SseEmitter result = sseService.createEmitter();

        //then
        assertNotNull(result);

    }
}