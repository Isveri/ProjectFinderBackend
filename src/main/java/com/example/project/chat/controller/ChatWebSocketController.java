package com.example.project.chat.controller;

import com.example.project.chat.model.Message;
import com.example.project.chat.model.MessageDTO;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@AllArgsConstructor
public class ChatWebSocketController {

    @MessageMapping("/temp/**")
    @SendTo("/topic/messages")
    public MessageDTO send(MessageDTO messageDTO) throws Exception {
        return MessageDTO.builder().username("test").build();
    }
}
