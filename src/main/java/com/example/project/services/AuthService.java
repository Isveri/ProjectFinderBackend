package com.example.project.services;


import com.example.project.domain.User;
import com.example.project.model.UserDTO;
import com.example.project.model.auth.ChangePasswordDTO;
import com.example.project.model.auth.TokenResponse;
import com.example.project.model.auth.UserCredentials;
import com.example.project.model.auth.VerificationToken;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    TokenResponse getToken(UserCredentials userCredentials);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    void createNewAccount(UserDTO userDto, HttpServletRequest request);

    TokenResponse confirmAccountRegister(String token);

    String confirmDeleteAccount(WebRequest request, Model model, String token);

    void changePassword(ChangePasswordDTO changePasswordDTO);

    void deleteUser();
}
