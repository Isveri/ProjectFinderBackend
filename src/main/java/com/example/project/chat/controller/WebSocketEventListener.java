package com.example.project.chat.controller;

import com.example.project.chat.model.MessageDTO;
import com.example.project.domain.User;
import com.example.project.exceptions.NotFoundException;
import com.example.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.util.*;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessagingTemplate messagingTemplate;

    private Map<String,String> destination = new HashMap<>();
    private Map<String,Map<String,Long>> connectedUsers = new HashMap<>();
//TODO mozna cos zrobic z ta mapa connectedUsers bo trzymanie tego w pamieci 2/10

    //TODO POPRAWIC WYYGLAD
    @EventListener
    public void handleSessionSubscribe(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();
        List<String> idList = headers.getNativeHeader("destination");
        String[] splited = idList.get(0).split("/");
        String id = splited[splited.length - 1];
        destination.put(headers.getSessionId(), id);
        Principal principal =  headers.getUser();
        User user = (User) (principal != null ? ((UsernamePasswordAuthenticationToken) principal).getPrincipal() : null);

        Map<String,Long> users;
        if(connectedUsers.get(id)==null){
             users = new HashMap<>();
        }else {
             users = connectedUsers.get(id);
        }
        users.put(headers.getSessionId(),user.getId());
        connectedUsers.put(id,users);
        Set<Long> usersIdToSend = new HashSet<>();
        for(Map.Entry<String,Long> entry: connectedUsers.get(id).entrySet()){
            usersIdToSend.add(entry.getValue());
        }
        MessageDTO messageDTO = MessageDTO.builder().text(username + " joined chat").connectedUsers(new ArrayList<>(usersIdToSend)).build();
        messagingTemplate.convertAndSend("/topic/messages/"+id,messageDTO);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();
        String id = destination.get(event.getSessionId());
        connectedUsers.get(id).remove(headers.getSessionId());
        Set<Long> usersIdToSend = new HashSet<>();
        for(Map.Entry<String,Long> entry: connectedUsers.get(id).entrySet()){
            usersIdToSend.add(entry.getValue());
        }
        MessageDTO messageDTO = MessageDTO.builder().text(username + " left chat").connectedUsers(new ArrayList<>(usersIdToSend)).build();
        messagingTemplate.convertAndSend("/topic/messages/"+id,messageDTO);
    }
}
