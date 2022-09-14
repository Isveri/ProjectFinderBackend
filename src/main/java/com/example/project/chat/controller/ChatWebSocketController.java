package com.example.project.chat.controller;

import com.example.project.chat.model.ChatDTO;
import com.example.project.chat.model.MessageDTO;
import com.example.project.chat.model.MessageLogsDTO;
import com.example.project.chat.model.UnreadMessageCountDTO;
import com.example.project.chat.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @MessageMapping("/privateChat/{chatId}")
    @SendTo("/topic/privateMessages/{chatId}")
    public MessageDTO sendPrivateMessage(MessageDTO messageDTO, @DestinationVariable Long chatId) throws Exception{
        return chatService.savePrivate(messageDTO,chatId);
    }


    @GetMapping("/api/v1/chat/{chatId}")
    public ResponseEntity<List<MessageDTO>> getChat(@PathVariable Long chatId){
        return ResponseEntity.ok(chatService.getChatMessages(chatId));
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

    @GetMapping("/api/v1/deletedGroupLogs/{groupId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<MessageDTO> getDeletedGroupChatLogs(@PathVariable Long groupId) {
        return chatService.getDeletedGroupChatLogs(groupId);
    }

    @PatchMapping("/api/v1/messageRead/{chatId}")
    public ResponseEntity<List<MessageDTO>> setMessageAsRead(@PathVariable Long chatId){
        return ResponseEntity.ok(chatService.setMessagesAsRead(chatId));
    }

    @GetMapping("/api/v1/unreadMessages")
    public ResponseEntity<List<UnreadMessageCountDTO>> countUnreadMessages(){
        return ResponseEntity.ok(chatService.countUnreadMessages());
    }
}
