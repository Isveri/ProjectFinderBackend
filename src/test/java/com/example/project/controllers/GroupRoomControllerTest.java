package com.example.project.controllers;

import com.example.project.services.GroupRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

class GroupRoomControllerTest {


    @Mock
    private GroupRoomService groupRoomService;
    private MockMvc mockMvc;
    private GroupRoomController groupRoomController;
    private static final String baseUrl = "/api/v1/groups";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        groupRoomController = new GroupRoomController(groupRoomService);
        mockMvc = MockMvcBuilders.standaloneSetup(groupRoomController).build();

    }

    @Test
    void getUserByName() {
    }

    @Test
    void getAllGroups() {
    }

    @Test
    void getGroupsByGame() {
    }

    @Test
    void createGroupRoom() {
    }

    @Test
    void getDeletedGroups() {
    }

    @Test
    void getGroupRoomById() {
    }

    @Test
    void updateGroupRoom() {
    }

    @Test
    void deleteGroupRoomById() {
    }

    @Test
    void changeVisibility() {
    }

    @Test
    void getGroupsByGameCategory() {
    }

    @Test
    void getGroupsByGameInGameRole() {
    }

    @Test
    void getGroupsByGameCategoryRole() {
    }

    @Test
    void getGroupsByGameCity() {
    }

    @Test
    void getGroupsByGameCategoryCity() {
    }

    @Test
    void generateJoinCode() {
    }

    @Test
    void joinGroupByCode() {
    }

    @Test
    void makeGroupRoomLeader() {
    }

    @Test
    void removeUserFromGroup() {
    }
}