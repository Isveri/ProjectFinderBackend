package com.example.project.controllers;

import com.example.project.model.UserDTO;
import com.example.project.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static com.example.project.samples.UserMockSample.getUserDTOMock;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    @Mock
    private UserService userService;
    private MockMvc mockMvc;
    private UserController userController;
    private static final String baseUrl = "/api/v1/users";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getAllUsers() throws Exception {

        //given
        final UserDTO userDTO = getUserDTOMock();
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(userDTO));

        //when + then
        mockMvc.perform(get(baseUrl+"/all"))
                .andExpect(status().isOk());
        verify(userService,times(1)).getAllUsers();

    }

    @Test
    void getUserByUsername() throws Exception {
        //given
        final UserDTO userDTO = getUserDTOMock();
        when(userService.getUserByUsername(any(String.class))).thenReturn(userDTO);

        //when + then
        mockMvc.perform(get(baseUrl+"/Evi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(userDTO.getId()))
                .andExpect(jsonPath("username").value(userDTO.getUsername()));

        verify(userService, times(1)).getUserByUsername("Evi");
    }

    @Test
    void reportUser() {
    }

    @Test
    void sendFriendRequest() {
    }

    @Test
    void getFriendRequests() {
    }

    @Test
    void acceptFriendRequest() {
    }

    @Test
    void declineFriendRequest() {
    }

    @Test
    void getFriendList() {
    }

    @Test
    void getReportedUsers() {
    }

    @Test
    void deleteReports() {
    }

    @Test
    void getBannedUsers() {
    }

    @Test
    void unbanUser() {
    }

    @Test
    void banUser() {
    }

    @Test
    void createUser() {
    }

    @Test
    void getAlreadyLoggedUser() {
    }

    @Test
    void getUserGroups() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void joinGroupRoom() {
    }

    @Test
    void exitGroupRoom() {
    }

    @Test
    void showUserProfile() {
    }

    @Test
    void setProfilePicture() {
    }

    @Test
    void getProfilePicture() {
    }
}