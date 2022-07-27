package com.example.project.services;

import com.example.project.domain.User;
import com.example.project.model.UserDTO;
import com.example.project.model.UserGroupsListDTO;
import com.example.project.model.UserProfileDTO;
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
    UserGroupsListDTO getUserGroups();
    UserDTO joinGroupRoom(Long groupRoomId);

    void changeProfilePicture(MultipartFile profilePicture);
    UserProfileDTO getUserProfile(Long userId);
    void getOutOfGroup(Long groupRoomId);

    void deleteUserById(Long id);
}
