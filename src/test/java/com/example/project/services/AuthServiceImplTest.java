package com.example.project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceImplTest {

    private MockMvc mockMvc;
    @Mock
    private AuthService authService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private UserService userService;


    @BeforeEach
    void setUp() {

    }

    @Test
    void getToken() {
    }

    @Test
    void createVerificationToken() {
    }

    @Test
    void createEmailChangeToken() {
    }

    @Test
    void getVerificationToken() {
    }

    @Test
    void createNewAccount() {
    }

    @Test
    void confirmAccountRegister() {
    }

    @Test
    void confirmDeleteAccount() {
    }

    @Test
    void confirmEmailChange() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void sendMessage() {
    }

    @Test
    void deleteUser() {
    }
}