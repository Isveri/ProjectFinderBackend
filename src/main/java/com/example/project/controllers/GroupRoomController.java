package com.example.project.controllers;

import com.example.project.model.MessageDTO;
import com.example.project.model.GroupRoomDTO;
import com.example.project.services.GroupRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupRoomController {
    private final GroupRoomService groupRoomService;

    public GroupRoomController(GroupRoomService groupRoomService) {
        this.groupRoomService = groupRoomService;
    }

    @GetMapping
    public ResponseEntity<GroupRoomDTO> getUserByName(@RequestParam String name) {
        return ResponseEntity.ok(groupRoomService.getGroupByName(name));
    }

    @GetMapping("/all")
    public ResponseEntity<List<GroupRoomDTO>> getAllGroups() {
        return ResponseEntity.ok(groupRoomService.getAllGroups());
    }

    @PostMapping("/add")
    public ResponseEntity<?> createGroupRoom(@RequestBody GroupRoomDTO groupRoomDTO){
        return ResponseEntity.ok(groupRoomService.save(groupRoomDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupRoomDTO> getGroupRoomById(@PathVariable Long id){
        return ResponseEntity.ok(groupRoomService.getGroupById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroupRoom(@PathVariable Long id, @RequestBody GroupRoomDTO groupRoomDTO){
        return ResponseEntity.ok(groupRoomService.updateGroupRoomByDTO(id,groupRoomDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupRoomById(@PathVariable Long id){
        groupRoomService.deleteGroupRoomById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/newComment")
    public ResponseEntity<Void> addComment(@RequestBody MessageDTO messageDTO) {
        groupRoomService.addComment(messageDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long commentId){
        groupRoomService.deleteCommentById(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
