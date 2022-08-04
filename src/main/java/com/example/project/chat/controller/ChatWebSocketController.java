package com.example.project.chat.controller;

import com.example.project.chat.model.Message;
import com.example.project.chat.model.MessageDTO;
import com.example.project.chat.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;


@Controller
@AllArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;

    @MessageMapping("/chat/{groupId}")
    @SendTo("/topic/messages/{groupId}")
    public MessageDTO send(MessageDTO messageDTO, @DestinationVariable Long groupId) throws Exception {
        return chatService.save(messageDTO, groupId);

    }
}
