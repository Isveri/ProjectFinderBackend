package com.example.project.controllers;

import com.example.project.model.*;
import com.example.project.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static com.example.project.converters.Converter.convertObjectToJsonBytes;
import static com.example.project.samples.FriendsSample.getFriendDTOMock;
import static com.example.project.samples.FriendsSample.getFriendRequestDTOMock;
import static com.example.project.samples.GroupRoomSample.getUsersGroupListDTOMock;
import static com.example.project.samples.ReportMockSample.getReportDTOMock;
import static com.example.project.samples.UserMockSample.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        final String username = "Evi";
        when(userService.getUserByUsername(any(String.class))).thenReturn(userDTO);

        //when + then
        mockMvc.perform(get(baseUrl+"/"+username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(userDTO.getId()))
                .andExpect(jsonPath("username").value(userDTO.getUsername()));

        verify(userService, times(1)).getUserByUsername(username);
    }

    @Test
    void reportUser() throws Exception {
        // given
        final ReportDTO reportDTO = getReportDTOMock();
        byte[] content = convertObjectToJsonBytes(reportDTO);
        final Long userId = 2L;

        //when + then
        mockMvc.perform(put(baseUrl+"/report/"+userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());

        verify(userService,times(1)).reportUser(reportDTO,userId);
    }

    @Test
    void sendFriendRequest() throws Exception {
        //given
        final Long invitedUserId = 1L;

        //when + then
        mockMvc.perform(post(baseUrl+"/sendFriendRequest/"+invitedUserId))
                .andExpect(status().isOk());

        verify(userService,times(1)).sendFriendRequest(invitedUserId);

    }

    @Test
    void getFriendRequests() throws Exception {
        //given

        final FriendRequestDTO friendRequestDTO = getFriendRequestDTOMock();

        when(userService.loadFriendRequests()).thenReturn(Collections.singletonList(friendRequestDTO));


        //when + then
        mockMvc.perform(get(baseUrl+"/loadFriendRequests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(friendRequestDTO.getId()))
                .andExpect(jsonPath("$[0].sendingUser").value(friendRequestDTO.getSendingUser()))
                .andExpect(jsonPath("$[0].invitedUser").value(friendRequestDTO.getInvitedUser()))
                .andExpect(jsonPath("$[0].accepted").value(friendRequestDTO.isAccepted()));

        verify(userService, times(1)).loadFriendRequests();

    }

    @Test
    void acceptFriendRequest() throws Exception {

        //given
        final Long requestId = 1L;


        //when + then
        mockMvc.perform(put(baseUrl+"/acceptFriendRequest/"+requestId))
                .andExpect(status().isOk());

        verify(userService,times(1)).acceptFriendRequest(requestId);

    }

    @Test
    void declineFriendRequest() throws Exception {
        //given
        final Long requestId = 1L;


        //when + then
        mockMvc.perform(put(baseUrl+"/declineFriendRequest/"+requestId))
                .andExpect(status().isOk());

        verify(userService,times(1)).declineFriendRequest(requestId);
    }

    @Test
    void getFriendList() throws Exception {
        //given
        final FriendDTO friendDTO = getFriendDTOMock();
        when(userService.getFriendList()).thenReturn(Collections.singletonList(friendDTO));

        //when + then
        mockMvc.perform(get(baseUrl+"/loadFriends"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(friendDTO.getId()))
                .andExpect(jsonPath("$[0].user").value(friendDTO.getUser()))
                .andExpect(jsonPath("$[0].chatId").value(friendDTO.getChatId()));


        verify(userService, times(1)).getFriendList();

    }

    @Test
    void getReportedUsers() throws Exception {
        // given
        final ReportedUserDTO reportedUserDTO = getReportedUserDTOMock();
        when(userService.getReportedUsers()).thenReturn(Collections.singletonList(reportedUserDTO));

        //when + then

        mockMvc.perform(get(baseUrl+"/reportedUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reportedUser").value(reportedUserDTO.getReportedUser()))
                .andExpect(jsonPath("$[0].reports").isNotEmpty());

        verify(userService, times(1)).getReportedUsers();
    }

    @Test
    void deleteReports() throws Exception {
        //given
        final Long userId = 1L;

        //when + then
        mockMvc.perform(delete(baseUrl+"/deleteReports/"+userId))
                .andExpect(status().isOk());

        verify(userService,times(1)).deleteReports(userId);

    }

    @Test
    void getBannedUsers() throws Exception {
        //given
        final BannedUserDTO bannedUserDTO = getBannedUserDTOMock();
        when(userService.getBannedUsers()).thenReturn(Collections.singletonList(bannedUserDTO));

        //when + then

        mockMvc.perform(get(baseUrl+"/banned"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bannedUserDTO.getId()))
                .andExpect(jsonPath("$[0].bannedBy").value(bannedUserDTO.getBannedBy()))
                .andExpect(jsonPath("$[0].reason").value(bannedUserDTO.getReason()))
                .andExpect(jsonPath("$[0].username").value(bannedUserDTO.getUsername()));

        verify(userService,times(1)).getBannedUsers();

    }

    @Test
    void unbanUser() throws Exception{
        //given
        final Long userId = 1L;

        //when + then

        mockMvc.perform(get(baseUrl+"/unban/"+userId))
                .andExpect(status().isOk());

        verify(userService,times(1)).unbanUser(userId);

    }

    @Test
    void banUser() throws Exception {

        //given
        final BannedUserDTO bannedUserDTO = getBannedUserDTOMock();
        byte[] content = convertObjectToJsonBytes(bannedUserDTO);

        //when + then
        mockMvc.perform(put(baseUrl+"/banUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());

        verify(userService,times(1)).banUser(bannedUserDTO);
    }

    @Test
    void createUser() throws Exception {
        //given
        final UserDTO userDTO = getUserDTOMock();
        byte[] content = convertObjectToJsonBytes(userDTO);
        when(userService.save(any(UserDTO.class))).thenReturn(userDTO);

        //when + then
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(userDTO.getUsername()))
                .andExpect(jsonPath("id").value(userDTO.getId()))
                .andExpect(jsonPath("age").value(userDTO.getAge()))
                .andExpect(jsonPath("city").value(userDTO.getCity()))
                .andExpect(jsonPath("name").value(userDTO.getName()))
                .andExpect(jsonPath("info").value(userDTO.getInfo()));

        verify(userService,times(1)).save(userDTO);

    }

    @Test
    void getAlreadyLoggedUser() throws Exception{
        //given
        final UserDTO userDTO = getUserDTOMock();
        when(userService.getLoggedUser()).thenReturn(userDTO);

        //when + then
        mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(userDTO.getUsername()))
                .andExpect(jsonPath("id").value(userDTO.getId()))
                .andExpect(jsonPath("age").value(userDTO.getAge()))
                .andExpect(jsonPath("city").value(userDTO.getCity()))
                .andExpect(jsonPath("name").value(userDTO.getName()))
                .andExpect(jsonPath("info").value(userDTO.getInfo()));

        verify(userService,times(1)).getLoggedUser();
    }

    @Test
    void getUserGroups() throws Exception{
        //given
        final UserGroupsListDTO userGroupsListDTO = getUsersGroupListDTOMock();
        when(userService.getUserGroups()).thenReturn(userGroupsListDTO);

        //when + then
        mockMvc.perform(get(baseUrl+"/my-groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(userGroupsListDTO.getId()))
                .andExpect(jsonPath("groupRooms").isNotEmpty());

        verify(userService,times(1)).getUserGroups();
    }

    @Test
    void updateUser() throws Exception {
    }

    @Test
    void joinGroupRoom() throws Exception {
    }

    @Test
    void exitGroupRoom() throws Exception {
    }

    @Test
    void showUserProfile() throws Exception{
    }

    @Test
    void setProfilePicture() throws Exception{
    }

    @Test
    void getProfilePicture() throws Exception{
    }
}