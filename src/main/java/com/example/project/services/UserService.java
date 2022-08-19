package com.example.project.services;

import com.example.project.chat.model.MessageDTO;
import com.example.project.domain.User;
import com.example.project.model.BannedUserDTO;
import com.example.project.model.UserDTO;
import com.example.project.model.UserGroupsListDTO;
import com.example.project.model.UserProfileDTO;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;


public interface UserService {

    List<UserDTO> getAllUsers();
    UserDTO save(UserDTO user);
    UserDTO saveAndReturnDTO(User user);
    UserDTO updateUserByDTO(UserDTO userDTO);
    UserDTO getLoggedUser();
    UserDTO getUserByUsername(String username);
    List<BannedUserDTO> getBannedUsers();
    UserGroupsListDTO getUserGroups();
    UserDTO joinGroupRoom(Long groupRoomId);

    void changeProfilePicture(MultipartFile profilePicture);
    Resource getProfilePicture(Long userId);
    UserProfileDTO getUserProfile(Long userId);
    void getOutOfGroup(Long groupRoomId);

    void banUser(BannedUserDTO bannedUserDTO);
    void unbanUser(Long userId);
    void deleteUserById(Long id);

}
