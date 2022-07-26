package com.example.project.controllers;

import com.example.project.model.UserDTO;
import com.example.project.model.UserGroupsListDTO;
import com.example.project.model.UserProfileDTO;
import com.example.project.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.save(userDTO));
    }

    @GetMapping
    public ResponseEntity<UserDTO> getAlreadyLoggedUser(){
        return ResponseEntity.ok(userService.getLoggedUser());
    }

    @GetMapping("/my-groups")
    public ResponseEntity<UserGroupsListDTO> getUserGroups(){
        return ResponseEntity.ok(userService.getUserGroups());
    }


    @PutMapping("/edit")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.updateUserByDTO(userDTO));
    }

    @PatchMapping("/joinGroup/{groupId}")
    public ResponseEntity<Void> joinGroupRoom(@PathVariable Long groupId){
        UserDTO temp = userService.joinGroupRoom(groupId);
        if(temp == null){
           return new ResponseEntity<>(HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @DeleteMapping("/my-groups/{groupId}")
    public ResponseEntity<Void> exitGroupRoom(@PathVariable Long groupId) {
        userService.getOutOfGroup(groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileDTO> showUserProfile(@PathVariable Long userId){
        UserProfileDTO temp = userService.getUserProfile(userId);
        if(temp == null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
            return ResponseEntity.ok(temp);
    }

}
