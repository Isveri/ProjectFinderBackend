package com.example.project.chat.service;

import com.example.project.chat.mappers.NotificationMapper;
import com.example.project.chat.model.CustomNotification;
import com.example.project.chat.model.CustomNotificationDTO;
import com.example.project.chat.repositories.NotificationRepository;
import com.example.project.domain.User;
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

import static com.example.project.chat.samples.CustomNotificationSample.getCustomNotificationDTOMock;
import static com.example.project.chat.samples.CustomNotificationSample.getCustomNotificationMoc;
import static com.example.project.samples.UserMockSample.getCurrentUserMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private NotificationMapper notificationMapper;

    private NotificationService notificationService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationService = new NotificationServiceImpl(notificationRepository, notificationMapper);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(getCurrentUserMock());
    }

    @Test
    void getAllNotifications() {
        //given
        User user = getCurrentUserMock();
        CustomNotificationDTO customNotificationDTO = getCustomNotificationDTOMock();
        CustomNotification customNotification = getCustomNotificationMoc();
        when(notificationRepository.findAllByUserId(any(Long.class))).thenReturn(Collections.singletonList(customNotification));
        when(notificationMapper.mapCustomNotificationToCustomNotificationDTO(any(CustomNotification.class))).thenReturn(customNotificationDTO);

        //when
        List<CustomNotificationDTO> result = notificationService.getAllNotifications();

        //then
        assertNotNull(result.get(0));

        verify(notificationRepository, times(1)).findAllByUserId(user.getId());
        verify(notificationMapper, times(1)).mapCustomNotificationToCustomNotificationDTO(customNotification);
    }

    @Test
    void removeNotification() {
        //given
        CustomNotification customNotification = getCustomNotificationMoc();

        //when
        notificationService.removeNotification(customNotification.getId());

        //then
        verify(notificationRepository, times(1)).deleteById(customNotification.getId());
    }
}