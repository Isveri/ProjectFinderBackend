package com.example.project.chat.controller;

import com.example.project.chat.model.MessageDTO;
import com.example.project.chat.model.MessageLogsDTO;
import com.example.project.chat.model.UnreadMessageCountDTO;
import com.example.project.chat.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.Collections;

import static com.example.project.chat.samples.MessageSample.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChatWebSocketControllerTest {

    @Mock
    private ChatService chatService;
    private ChatWebSocketController chatWebSocketController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatWebSocketController = new ChatWebSocketController(chatService);
        mockMvc = MockMvcBuilders.standaloneSetup(chatWebSocketController).build();
    }

    @Test
    void send() throws Exception{
    }

    @Test
    void sendPrivateMessage() throws Exception{
    }

    @Test
    void getChat() throws Exception {
        //given
        MessageDTO msgDTO = getMessageDTOMock();
        Long chatId = 1L;
        when(chatService.getChatMessages(any(Long.class))).thenReturn(Collections.singletonList(msgDTO));

        //when + then
        mockMvc.perform(get("/api/v1/chat/"+chatId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(msgDTO));

        verify(chatService, times(1)).getChatMessages(chatId);
    }

    @Test
    void getChatLogs() throws Exception {

        //given
        MessageDTO msgDTO = getMessageDTOMock();
        Long groupId = 1L;
        when(chatService.getChatLogs(any(Long.class))).thenReturn(Collections.singletonList(msgDTO));

        //when + then
        mockMvc.perform(get("/api/v1/chatLogs/"+groupId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(msgDTO));

        verify(chatService, times(1)).getChatLogs(groupId);

    }

    @Test
    void getUserChatLogs() throws Exception{
        //given
        MessageLogsDTO msgDTO = getMessageLogsDTOMock();
        Long userId = 1L;
        when(chatService.getUserChatLogs(any(Long.class))).thenReturn(Collections.singletonList(msgDTO));

        //when + then
        mockMvc.perform(get("/api/v1/users/chatLogs/"+userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(msgDTO));

        verify(chatService, times(1)).getUserChatLogs(userId);
    }

    @Test
    void getDeletedGroupChatLogs() throws Exception{
        //given
        MessageDTO msgDTO = getMessageDTOMock();
        Long groupId = 1L;
        when(chatService.getDeletedGroupChatLogs(any(Long.class))).thenReturn(Collections.singletonList(msgDTO));

        //when + then
        mockMvc.perform(get("/api/v1/deletedGroupLogs/"+groupId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(msgDTO));

        verify(chatService, times(1)).getDeletedGroupChatLogs(groupId);
    }

    @Test
    void setMessageAsRead() throws Exception{
        //given
        MessageDTO msgDTO = getMessageDTOMock();
        Long chatId = 1L;
        when(chatService.setMessagesAsRead(any(Long.class))).thenReturn(Collections.singletonList(msgDTO));

        //when + then
        mockMvc.perform(patch("/api/v1/messageRead/"+chatId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(msgDTO));

        verify(chatService, times(1)).setMessagesAsRead(chatId);
    }

    @Test
    void countUnreadMessages() throws Exception{
        //given
        UnreadMessageCountDTO count = getUnreadMessageCountDTOMock();
        when(chatService.countUnreadMessages()).thenReturn(Collections.singletonList(count));

        //when + then
        mockMvc.perform(get("/api/v1/unreadMessages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(count));

        verify(chatService,times(1)).countUnreadMessages();
    }
}