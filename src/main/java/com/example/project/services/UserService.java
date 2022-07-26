package com.example.project.services;

import com.example.project.domain.User;
import com.example.project.model.UserDTO;
import com.example.project.model.UserGroupsListDTO;
import com.example.project.model.UserProfileDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    List<UserDTO> getAllUsers();
    UserDTO save(UserDTO user);
    UserDTO saveAndReturnDTO(User user);
    UserDTO updateUserByDTO(UserDTO userDTO);
    UserDTO getLoggedUser();
    UserGroupsListDTO getUserGroups();
    UserDTO joinGroupRoom(Long groupRoomId);

    UserProfileDTO getUserProfile(Long userId);
    void getOutOfGroup(Long groupRoomId);

    void deleteUserById(Long id);
}
