package com.example.project.chat.controller;

import com.example.project.chat.model.MessageDTO;
import com.example.project.chat.model.MessageLogsDTO;
import com.example.project.chat.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;


@RestController
@AllArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;

    @MessageMapping("/chat/{groupId}")
    @SendTo("/topic/messages/{groupId}")
    public MessageDTO send(MessageDTO messageDTO, @DestinationVariable Long groupId) throws Exception {
        return chatService.save(messageDTO, groupId);

    }
    @GetMapping("/api/v1/chatLogs/{groupId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<MessageDTO> getChatLogs(@PathVariable Long groupId){
        return chatService.getChatLogs(groupId);
    }

    @GetMapping("/api/v1/users/chatLogs/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<MessageLogsDTO> getUserChatLogs(@PathVariable Long userId){

        return chatService.getUserChatLogs(userId);
    }
}
