package com.example.project.controllers;

import com.example.project.model.MessageDTO;
import com.example.project.services.GroupRoomService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class RoomChatController {

    private final GroupRoomService groupRoomService;


    @PostMapping("/newComment")
    public ResponseEntity<Void> addMessage(@RequestBody MessageDTO messageDTO) {
        groupRoomService.addMessage(messageDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteMessageById(@PathVariable Long commentId){
        groupRoomService.deleteMessageById(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
