package com.example.project.chat.controller;

import com.example.project.chat.model.Message;
import com.example.project.chat.model.MessageDTO;
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

    @MessageMapping("/chat/{groupId}")
    @SendTo("/topic/messages/{groupId}")
    public MessageDTO send(MessageDTO messageDTO, @DestinationVariable Long groupId) throws Exception {
        return MessageDTO.builder().user(messageDTO.getUser()).groupId(messageDTO.getGroupId()).text(messageDTO.getText()).time(LocalDateTime.now()).build();
    }
}
