package com.example.project.services;

import com.example.project.domain.GroupRoom;
import com.example.project.domain.Role;
import com.example.project.domain.User;
import com.example.project.exceptions.AccountBannedException;
import com.example.project.exceptions.DeleteUserException;
import com.example.project.exceptions.GroupNotFoundException;
import com.example.project.exceptions.UserNotFoundException;
import com.example.project.exceptions.validation.EmailAlreadyTakenException;
import com.example.project.exceptions.validation.WrongPasswordException;
import com.example.project.mappers.UserMapper;
import com.example.project.model.UserDTO;
import com.example.project.model.auth.ChangePasswordDTO;
import com.example.project.model.auth.TokenResponse;
import com.example.project.model.auth.UserCredentials;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.RoleRepository;
import com.example.project.repositories.UserRepository;
import com.example.project.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static com.example.project.utils.UserDetailsHelper.getCurrentUser;
import com.example.project.utils.DataValidation;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {


    private final UserDetailsService userDetailsService;
    private final GroupRoomService groupRoomService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserService userService;
    private final DataValidation dataValidation;

    @Override
    public TokenResponse getToken(UserCredentials userCredentials) {
        User user = (User) userDetailsService.loadUserByUsername(userCredentials.getUsername());
        if(user.isBanned()){
            throw new AccountBannedException("Account banned");
        }
        else if (passwordEncoder.matches(userCredentials.getPassword(), user.getPassword()))
            return new TokenResponse(jwtTokenUtil.generateAccessToken(user));
        return null;
    }

    @Override
    public TokenResponse createNewAccount(UserDTO userDto) {
        User newUser = userMapper.mapUserDTOToUser(userDto);
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        String email = userDto.getEmail();
        if (userRepository.findByUsername(username).isPresent()) {
            return null;
        } else {
            Role userRole = roleRepository.findByName("ROLE_USER");
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setRole(userRole);

            User createdUser = userMapper.mapUserDTOToUser(userService.save(userMapper.mapUserToUserDTO(newUser)));

            return new TokenResponse(jwtTokenUtil.generateAccessToken(createdUser));
        }
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        User currentUser = getCurrentUser();
        long id = currentUser.getId();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found id:" + id));
        dataValidation.password(changePasswordDTO.getOldPassword());
        dataValidation.password(changePasswordDTO.getNewPassword());
        if(passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())){
            try {
                user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                userRepository.save(user);
            } catch (Exception e) {
               throw new EmailAlreadyTakenException("something wrong with upd password");
            }
        }else{
            throw new WrongPasswordException("Wrong password");
        }
    }
    @Override
    public void deleteUser(){
        User currentUser = getCurrentUser();
        long id = currentUser.getId();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found id:" + id));
        List<GroupRoom> UserGroupRooms= groupRepository.findAllByGroupLeaderId(id);
        try{
            for (GroupRoom groupRoom : UserGroupRooms) {
                userService.getOutOfGroup(groupRoom.getId());
            }
            userRepository.softDeleteById(id);

        }catch(Exception e){
            throw new DeleteUserException("Something wrong with deleting a user");
        }

    }

}
