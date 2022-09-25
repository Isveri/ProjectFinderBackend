package com.example.project.controllers;

import com.example.project.model.auth.TokenResponse;
import com.example.project.model.auth.UserCredentials;
import com.example.project.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static reactor.core.publisher.Mono.when;


class AuthControllerTest {

    @Mock
    private AuthService authService;
    private MockMvc mockMvc;
    private ApplicationEventPublisher eventPublisher;
    private AuthController authController;
    private static final String BASE_AUTH_URL = "/api/v1/auth";


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authService,eventPublisher);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void authenticateUser() {

    }

    @Test
    void createNewAccount() {
    }

    @Test
    void confirmAccountRegister() {
    }

    @Test
    void confirmEmailChange() {
    }

    @Test
    void emailChange() {
    }

    @Test
    void deleteAccount() {
    }

    @Test
    void confirmDeleteAccount() {
    }

    @Test
    void changeUserPassword() {
    }
}