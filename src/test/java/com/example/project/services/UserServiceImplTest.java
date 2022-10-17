package com.example.project.services;

import com.example.project.chat.repositories.ChatRepository;
import com.example.project.chat.service.SseService;
import com.example.project.domain.Role;
import com.example.project.domain.User;
import com.example.project.mappers.*;
import com.example.project.model.UserDTO;
import com.example.project.repositories.*;
import com.example.project.utils.DataValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static com.example.project.samples.UserMockSample.getUserDTOMock;
import static com.example.project.samples.UserMockSample.getUserMock;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserGroupListMapper userGroupListMapper;

    @Mock
    private SseService sseService;
    @Mock
    private ReportMapper reportMapper;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private FriendRequestRepository friendRequestRepository;
    @Mock
    private FriendRepository friendRepository;
    @Mock
    private FriendMapper friendMapper;
    @Mock
    private FriendRequestMapper friendRequestMapper;
    @Mock
    private DataValidation dataValidation;
    @Mock
    private ReportRepository reportRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userMapper, userRepository, groupRepository, roleRepository, userGroupListMapper, sseService, reportMapper,
                chatRepository, friendRequestRepository, friendRepository, friendMapper, friendRequestMapper, dataValidation, reportRepository);
    }

    @Test
    void getAllUsers() {
        // given
        UserDTO userDTO = getUserDTOMock();
        User user = getUserMock();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(userMapper.mapUserToUserDTO(user)).thenReturn(userDTO);

        // when
        List<UserDTO> users = userService.getAllUsers();

        //then

        assertThat(users.get(0).getName())
                .isEqualTo(user.getName());

        verify(userRepository,times(1)).findAll();
        verify(userMapper,times(1)).mapUserToUserDTO(user);
    }

    @Test
    void save() {
//        //given
//        Role role = Role

    }

    @Test
    void saveAndReturnDTO() {
    }

    @Test
    void updateUserByDTO() {
    }

    @Test
    void getLoggedUser() {
    }

    @Test
    void getUserByUsername() {
    }

    @Test
    void getBannedUsers() {
    }

    @Test
    void getReportedUsers() {
    }

    @Test
    void getUserGroups() {
    }

    @Test
    void joinGroupRoom() {
    }

    @Test
    void changeProfilePicture() {
    }

    @Test
    void getProfilePicture() {
    }

    @Test
    void getUserProfile() {
    }

    @Test
    void getOutOfGroup() {
    }

    @Test
    void reportUser() {
    }

    @Test
    void banUser() {
    }

    @Test
    void deleteReports() {
    }

    @Test
    void unbanUser() {
    }

    @Test
    void sendFriendRequest() {
    }

    @Test
    void loadFriendRequests() {
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
    void deleteUserById() {
    }
}