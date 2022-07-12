package com.example.project.services;

import com.example.project.domain.User;
import com.example.project.model.UserDTO;
import com.example.project.model.UserGroupsListDTO;
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
    void getOutOfGroup(Long groupRoomId);

    void deleteUserById(Long id);
}
