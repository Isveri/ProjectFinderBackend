package com.example.project.services;

import com.example.project.domain.GroupRoom;
import com.example.project.domain.InGameRole;
import com.example.project.domain.Role;
import com.example.project.domain.User;
import com.example.project.exceptions.NotFoundException;
import com.example.project.mappers.GroupRoomMapper;
import com.example.project.mappers.InGameRolesMapper;
import com.example.project.mappers.UserGroupListMapper;
import com.example.project.mappers.UserMapper;
import com.example.project.model.*;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.InGameRoleRepository;
import com.example.project.repositories.RoleRepository;
import com.example.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupRoomMapper groupRoomMapper;
    private final RoleRepository roleRepository;
    private final UserGroupListMapper userGroupListMapper;
    private final InGameRoleRepository inGameRoleRepository;
    private final InGameRolesMapper inGameRolesMapper;


    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user->userMapper.mapUserToUserDTO(user))
                .collect(Collectors.toList());
    }



    @Override
    public UserDTO save(UserDTO userDTO) {

//        return userMapper.mapUserToUserDTO(userRepository.save(userMapper.mapUserDTOToUser(userDTO)));
        Role userRole = roleRepository.findByName("ROLE_USER");
        User user = userMapper.mapUserDTOToUser(userDTO);
        user.setRole(userRole);
        return userMapper.mapUserToUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO saveAndReturnDTO(User user) {
        return userMapper.mapUserToUserDTO(userRepository.save(user));
    }


    @Override
    public UserDTO updateUserByDTO(UserDTO userDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = currentUser.getId();
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found id:"+id));
        user.setId(id);
        user.setUsername(userDTO.getUsername());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setCity(userDTO.getCity());
        user.setPhone(userDTO.getPhone());

        List<Long> ids = userDTO.getInGameRoles().stream()
                .map(InGameRolesDTO::getId).collect(Collectors.toList());
        List<InGameRole> inGameRoleList = inGameRoleRepository.findAllById(ids);

        user.setInGameRoles(inGameRoleList);
        return saveAndReturnDTO(user);
    }

    @Override
    public UserDTO getLoggedUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = currentUser.getId();
        return userRepository.findById(id)
                .map(userMapper::mapUserToUserDTO)
                .map(userDTO -> {
                    userDTO.setId(id);
                    return userDTO;})
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public UserGroupsListDTO getUserGroups() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = currentUser.getId();
        return userRepository.findById(id)
                .map(userGroupListMapper::mapuserToUserGroupsListDTO).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public UserDTO joinGroupRoom(Long groupRoomId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new NotFoundException("User not found id:"+currentUser.getId()));;


        GroupRoom groupRoom = groupRepository.findById(groupRoomId)
                .orElseThrow(() -> new NotFoundException("Group room not found"));

        if(user.getGroupRooms().contains(groupRoom)){
            return null;
        }else{
        user.getGroupRooms().add(groupRoom);
        userRepository.save(user);
        return userMapper.mapUserToUserDTO(user);
        }
    }

    @Override
    public void changeProfilePicture(MultipartFile profilePicture) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new NotFoundException("User not found id:"+currentUser.getId()));
    }

    @Override
    public UserProfileDTO getUserProfile(Long userId) {

        return userRepository.findById(userId)
                .map(userMapper::mapUserToUserProfileDTO)
                .orElseThrow(() -> new NotFoundException("Group room not found"));
    }

    @Override
    public void getOutOfGroup(Long groupRoomId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new NotFoundException("User not found id:"+currentUser.getId()));;


        GroupRoom groupRoom = groupRepository.findById(groupRoomId)
                .orElseThrow(() -> new NotFoundException("Group room not found"));

        user.getGroupRooms().remove(groupRoom);
        if(Objects.equals(groupRoom.getGroupLeader(),user)){
        groupRoom.setGroupLeader(groupRoom.getUsers().get(0));}
        groupRepository.save(groupRoom);
        userRepository.save(currentUser);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }


}
