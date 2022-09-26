package com.example.project.controllers;

import com.example.project.model.*;
import com.example.project.services.UserService;
import org.apache.coyote.Response;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PutMapping("/report/{userId}")
    public ResponseEntity<?> reportUser(@RequestBody ReportDTO reportDTO, @PathVariable Long userId){
        userService.reportUser(reportDTO,userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/sendFriendRequest/{invitedUserId}")
    public ResponseEntity<?> sendFriendRequest(@PathVariable Long invitedUserId){
        userService.sendFriendRequest(invitedUserId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/loadFriendRequests")
    public ResponseEntity<List<FriendRequestDTO>> getFriendRequests(){
        return ResponseEntity.ok(userService.loadFriendRequests());
    }

    @PutMapping("/acceptFriendRequest/{requestId}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long requestId){
        userService.acceptFriendRequest(requestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/declineFriendRequest/{requestId}")
    public ResponseEntity<?> declineFriendRequest(@PathVariable Long requestId){
        userService.declineFriendRequest(requestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/loadFriends")
    public ResponseEntity<List<FriendDTO>> getFriendList(){
        return ResponseEntity.ok(userService.getFriendList());
    }

    @GetMapping("/reportedUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReportedUserDTO>> getReportedUsers(){
        return ResponseEntity.ok(userService.getReportedUsers());
    }

    @DeleteMapping("/deleteReports/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteReports(@PathVariable Long userId){
        userService.deleteReports(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/banned")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BannedUserDTO>> getBannedUsers(){
        return ResponseEntity.ok(userService.getBannedUsers());
    }

    @GetMapping("/unban/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> unbanUser(@PathVariable Long userId){
        userService.unbanUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/banUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> banUser(@RequestBody BannedUserDTO bannedUserDTO){
        userService.banUser(bannedUserDTO);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<Void> joinGroupRoom(@PathVariable Long groupId, @RequestBody InGameRolesDTO inGameRoles) {
        UserDTO temp = userService.joinGroupRoom(groupId,inGameRoles);
        return new ResponseEntity<>(HttpStatus.OK);
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
