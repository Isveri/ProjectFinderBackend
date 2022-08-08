package com.example.project.controllers;

import com.example.project.domain.User;
import com.example.project.model.UserDTO;
import com.example.project.model.UserGroupsListDTO;
import com.example.project.model.UserProfileDTO;
import com.example.project.services.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.save(userDTO));
    }

    @GetMapping
    public ResponseEntity<UserDTO> getAlreadyLoggedUser() {
        return ResponseEntity.ok(userService.getLoggedUser());
    }

    @GetMapping("/my-groups")
    public ResponseEntity<UserGroupsListDTO> getUserGroups() {
        return ResponseEntity.ok(userService.getUserGroups());
    }


    @PutMapping("/edit")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUserByDTO(userDTO));
    }

    @PatchMapping("/joinGroup/{groupId}")
    public ResponseEntity<Void> joinGroupRoom(@PathVariable Long groupId) {
        UserDTO temp = userService.joinGroupRoom(groupId);
        if (temp == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @DeleteMapping("/my-groups/{groupId}")
    public ResponseEntity<Void> exitGroupRoom(@PathVariable Long groupId) {
        userService.getOutOfGroup(groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileDTO> showUserProfile(@PathVariable Long userId) {
        UserProfileDTO temp = userService.getUserProfile(userId);
        if (temp == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(temp);
    }

    @PatchMapping(path = "/profilePicture")
    public ResponseEntity<Resource> setProfilePicture(@RequestParam("profilePicture") MultipartFile pictureFile) {
        userService.changeProfilePicture(pictureFile);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/profilePicture/{userId}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable Long userId) {
        Resource file = userService.getProfilePicture(userId);
        if (file != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userId + "-" + file.getFilename() + "\"").body(file);
        }
        return null;
    }
}
