package com.example.project.services;

import com.example.project.domain.Role;
import com.example.project.domain.User;
import com.example.project.exceptions.AccountBannedException;
import com.example.project.mappers.UserMapper;
import com.example.project.model.UserDTO;
import com.example.project.model.auth.TokenResponse;
import com.example.project.model.auth.UserCredentials;
import com.example.project.repositories.RoleRepository;
import com.example.project.repositories.UserRepository;
import com.example.project.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {


    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserService userService;

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

}
