package com.example.project.controllers;

import com.example.project.model.UserDTO;
import com.example.project.model.auth.ChangePasswordDTO;
import com.example.project.model.auth.TokenResponse;
import com.example.project.model.auth.UserCredentials;
import com.example.project.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = AuthController.BASE_AUTH_URL)
public class AuthController {

    private final AuthService authService;

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
    public ResponseEntity<TokenResponse> createNewAccount(@Valid @RequestBody UserDTO userDto) {
        return ResponseEntity.ok(authService.createNewAccount(userDto));
    }

    @PostMapping("/password-change")
    public ResponseEntity<?> changeUserPassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        authService.changePassword(changePasswordDTO);
        return ResponseEntity.ok("");
    }
    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(){
        authService.deleteUser();
        return ResponseEntity.ok("");
    }
}