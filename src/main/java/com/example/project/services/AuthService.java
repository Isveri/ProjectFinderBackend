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

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    TokenResponse getToken(UserCredentials userCredentials);

    void createVerificationToken(User user, String token);

    void createEmailChangeToken(User user, String token, String email);

    VerificationToken getVerificationToken(String VerificationToken);

    void createNewAccount(UserDTO userDto, HttpServletRequest request);

    TokenResponse confirmAccountRegister(String token);

    String confirmDeleteAccount(String token);

    void confirmEmailChange(String token);
    void changePassword(ChangePasswordDTO changePasswordDTO);

    void sendMessage(MimeMessage mimeMessage);

    void deleteUser();
}
