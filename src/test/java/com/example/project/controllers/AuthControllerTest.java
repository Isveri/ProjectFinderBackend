package com.example.project.controllers;

import com.example.project.model.UserDTO;
import com.example.project.model.auth.ChangePasswordDTO;
import com.example.project.model.auth.TokenResponse;
import com.example.project.model.auth.UserCredentials;
import com.example.project.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;

import static com.example.project.converters.Converter.convertObjectToJsonBytes;
import static com.example.project.samples.TokenResponseSample.getChangePasswordDTOMock;
import static com.example.project.samples.TokenResponseSample.getTokenResponseMock;
import static com.example.project.samples.UserMockSample.getUserCredentialsMock;
import static com.example.project.samples.UserMockSample.getUserDTOMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthControllerTest {

    @Mock
    private AuthService authService;
    private MockMvc mockMvc;
    private ApplicationEventPublisher eventPublisher;
    private AuthController authController;
    private static final String baseUrl = "/api/v1/auth";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authService, eventPublisher);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void authenticateUser() throws Exception {
        //given
        final TokenResponse tokenResponse = getTokenResponseMock();
        final UserCredentials userCredentials = getUserCredentialsMock();
        byte[] content = convertObjectToJsonBytes(userCredentials);
        when(authService.getToken(any(UserCredentials.class))).thenReturn(tokenResponse);

        //when + then
        mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").value(tokenResponse.getToken()));

        verify(authService, times(1)).getToken(userCredentials);
    }

    @Test
    void createNewAccount() throws Exception {
        //given
        final UserDTO userDTO = getUserDTOMock();
        byte[] content = convertObjectToJsonBytes(userDTO);
        final MockHttpServletRequest request = new MockHttpServletRequest();
        //when + then
        mockMvc.perform(post(baseUrl + "/new-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());

//        verify(authService,times(1)).createNewAccount(userDTO,request);
    }

    @Test
    void confirmAccountRegister() throws Exception{
        //given
        final String token = "token";
        final TokenResponse tokenResponse = getTokenResponseMock();
        when(authService.confirmAccountRegister(any(String.class))).thenReturn(tokenResponse);

        //when + then
        mockMvc.perform(get(baseUrl+"/confirmAccountRegister").param("token",token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").value(tokenResponse.getToken()));

        verify(authService, times(1)).confirmAccountRegister(token);
    }

    @Test
    void confirmEmailChange() throws Exception {
        //given
        final String token = "token";

        //when + then
        mockMvc.perform(get(baseUrl+"/confirmEmailChange").param("token",token))
                .andExpect(status().isOk());

        verify(authService, times(1)).confirmEmailChange(token);
    }

    @Test
    void emailChange() {
    }

    @Test
    void deleteAccount() {
    }

    @Test
    void confirmDeleteAccount() throws Exception{
        //given
        final String token = "token";
        final TokenResponse tokenResponse = getTokenResponseMock();

        //when + then
        mockMvc.perform(get(baseUrl+"/deleteAccountConfirm").param("token",token))
                .andExpect(status().isOk());

        verify(authService, times(1)).confirmDeleteAccount(token);
    }

    @Test
    void changeUserPassword() throws Exception {
        //given
        final ChangePasswordDTO changePasswordDTO = getChangePasswordDTOMock();
        byte[] content = convertObjectToJsonBytes(changePasswordDTO);

        // when + then
        mockMvc.perform(post(baseUrl + "/password-change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());

        verify(authService,times(1)).changePassword(changePasswordDTO);
    }
}