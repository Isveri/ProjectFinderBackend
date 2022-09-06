package com.example.project.services;

import com.example.project.domain.User;
import com.example.project.model.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface UserService {

    List<UserDTO> getAllUsers();
    UserDTO save(UserDTO user);
    UserDTO saveAndReturnDTO(User user);
    UserDTO updateUserByDTO(UserDTO userDTO);
    UserDTO getLoggedUser();
    UserDTO getUserByUsername(String username);
    List<BannedUserDTO> getBannedUsers();

    List<ReportedUserDTO> getReportedUsers();
    UserGroupsListDTO getUserGroups();
    UserDTO joinGroupRoom(Long groupRoomId,InGameRolesDTO inGameRolesDTO);

    void changeProfilePicture(MultipartFile profilePicture);
    Resource getProfilePicture(Long userId);
    UserProfileDTO getUserProfile(Long userId);
    void getOutOfGroup(Long groupRoomId);
    void reportUser(ReportDTO reportDTO, Long userId);
    void banUser(BannedUserDTO bannedUserDTO);

    void deleteReports(Long userId);
    void unbanUser(Long userId);
    void deleteUserById(Long id);

}
