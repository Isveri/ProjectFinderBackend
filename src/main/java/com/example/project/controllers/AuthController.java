package com.example.project.controllers;

import com.example.project.model.EmailDTO;
import com.example.project.model.UserDTO;
import com.example.project.model.auth.ChangePasswordDTO;
import com.example.project.model.auth.TokenResponse;
import com.example.project.model.auth.UserCredentials;
import com.example.project.security.emailConfirm.OnAccountDeleteCompleteEvent;
import com.example.project.security.emailConfirm.OnEmailChangeCompleteEvent;
import com.example.project.services.AuthService;
import com.example.project.utils.UserDetailsHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = AuthController.BASE_AUTH_URL)
public class AuthController {

    private final AuthService authService;

    private final ApplicationEventPublisher eventPublisher;

    public static final String BASE_AUTH_URL = "/api/v1/auth";

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserCredentials userCredentials) {
        TokenResponse token = authService.getToken(userCredentials);
        if (Objects.nonNull(token)) {
            return ResponseEntity.ok(token);
        } else {
            return new ResponseEntity<>("Login error", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/new-account")
    public ResponseEntity<TokenResponse> createNewAccount(@Valid @RequestBody UserDTO userDto,HttpServletRequest request) {
        authService.createNewAccount(userDto,request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/confirmAccountRegister")
    public ResponseEntity<TokenResponse> confirmAccountRegister(@RequestParam("token") String token) {
        return ResponseEntity.ok(authService.confirmAccountRegister(token));
    }

    @GetMapping("/confirmEmailChange")
    public ResponseEntity<TokenResponse> confirmEmailChange(@RequestParam("token") String token){
        return ResponseEntity.ok(authService.confirmEmailChange(token));
    }

    @PatchMapping("/emailChange")
    public ResponseEntity<?> emailChange(@Valid @RequestBody EmailDTO email, HttpServletRequest request){
        eventPublisher.publishEvent(new OnEmailChangeCompleteEvent(UserDetailsHelper.getCurrentUser(),request.getLocale(),email.getEmail(),request.getContextPath()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteAccount(HttpServletRequest request){
        eventPublisher.publishEvent(new OnAccountDeleteCompleteEvent(UserDetailsHelper.getCurrentUser(),request.getLocale(),request.getContextPath()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/deleteAccountConfirm")
    public ResponseEntity<?> confirmDeleteAccount(@RequestParam("token") String token){
        authService.confirmDeleteAccount(token);
        return new ResponseEntity<>(HttpStatus.OK);

    }


    @PostMapping("/password-change")
    public ResponseEntity<?> changeUserPassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        authService.changePassword(changePasswordDTO);
        return ResponseEntity.ok("");
    }

}