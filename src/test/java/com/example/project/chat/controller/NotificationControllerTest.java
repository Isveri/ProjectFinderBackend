package com.example.project.chat.controller;

import com.example.project.chat.model.CustomNotificationDTO;
import com.example.project.chat.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static com.example.project.chat.samples.CustomNotificationSample.getCustomNotificationDTOMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationControllerTest {
    @Mock
    private NotificationService notificationService;

    private MockMvc mockMvc;

    private NotificationController notificationController;

    private final String baseUrl = "/api/v1/notification";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationController = new NotificationController(notificationService);
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();

    }

    @Test
    void getAllNotifications() throws Exception {
        //given
        CustomNotificationDTO customNotificationDTO = getCustomNotificationDTOMock();
        when(notificationService.getAllNotifications()).thenReturn(Collections.singletonList(customNotificationDTO));

        //when + then
        mockMvc.perform(get(baseUrl + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(customNotificationDTO));

        verify(notificationService, times(1)).getAllNotifications();
    }

    @Test
    void removeNotification() throws Exception{
        //given
        Long notifId = 1L;

        //when + then
        mockMvc.perform(delete(baseUrl + "/delete/" + notifId))
                .andExpect(status().isOk());

        verify(notificationService,times(1)).removeNotification(notifId);

    }
}