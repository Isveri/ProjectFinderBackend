package com.example.project.services;


import com.example.project.model.UserDTO;
import com.example.project.model.auth.ChangePasswordDTO;
import com.example.project.model.auth.TokenResponse;
import com.example.project.model.auth.UserCredentials;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    TokenResponse getToken(UserCredentials userCredentials);

    TokenResponse createNewAccount(UserDTO userDto);

    void changePassword(ChangePasswordDTO changePasswordDTO);

}
