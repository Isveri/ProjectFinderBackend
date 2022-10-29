package com.example.project.services;

import com.example.project.chat.repositories.ChatRepository;
import com.example.project.chat.service.SseService;
import com.example.project.domain.*;
import com.example.project.exceptions.AlreadyBannedException;
import com.example.project.exceptions.AlreadyFriendException;
import com.example.project.exceptions.AlreadyInvitedException;
import com.example.project.exceptions.AlreadyReportedException;
import com.example.project.mappers.*;
import com.example.project.model.*;
import com.example.project.repositories.*;
import com.example.project.utils.DataValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.project.samples.FriendsSample.*;
import static com.example.project.samples.GroupRoomSample.*;
import static com.example.project.samples.InGameRolesSample.getInGameRoleDTOMock;
import static com.example.project.samples.ReportMockSample.*;
import static com.example.project.samples.RoleMockSample.getRoleMock;
import static com.example.project.samples.UserMockSample.*;
import static com.example.project.utils.UserDetailsHelper.getCurrentUser;
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

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userMapper, userRepository, groupRepository, roleRepository, userGroupListMapper, sseService, reportMapper,
                chatRepository, friendRequestRepository, friendRepository, friendMapper, friendRequestMapper, dataValidation, reportRepository);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getPrincipal()).thenReturn(getCurrentUserMock());

        user = getUserMock();
        userDTO = getUserDTOMock();

    }

    @Test
    void should_return_user_in_list() {
        // given
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(userMapper.mapUserToUserDTO(user)).thenReturn(userDTO);

        // when
        List<UserDTO> users = userService.getAllUsers();

        //then

        assertThat(users.get(0).getName())
                .isEqualTo(user.getName());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).mapUserToUserDTO(user);
    }

    @Test
    void should_save_and_return_userDTO() {
//        //given
        Role role = getRoleMock();
        String roleName = "ROLE_USER";
        when(roleRepository.findByName(roleName)).thenReturn(role);
        when(userMapper.mapUserDTOToUser(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.mapUserToUserDTO(user)).thenReturn(userDTO);

        //when
        UserDTO result = userService.save(userDTO);

        //then
        assertThat(result.getId())
                .isEqualTo(user.getId());

        verify(roleRepository, times(1)).findByName(roleName);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).mapUserToUserDTO(user);
        verify(userMapper, times(1)).mapUserDTOToUser(userDTO);
    }

    @Test
    void should_return_userDTO() {
        //given
        when(userMapper.mapUserToUserDTO(user)).thenReturn(userDTO);
        when(userRepository.save(user)).thenReturn(user);

        //when
        UserDTO result = userService.saveAndReturnDTO(user);

        //then
        assertThat(result.getId())
                .isEqualTo(user.getId());

        verify(userMapper, times(1)).mapUserToUserDTO(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void should_update_and_return_userDTO() {

        //given
        User user = getCurrentUserMock();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.updateUserFromUserDTO(userDTO, user)).thenReturn(user);
        when(userService.saveAndReturnDTO(user)).thenReturn(userDTO);

        //when
        UserDTO result = userService.updateUserByDTO(userDTO);

        //then
        assertThat(result.getUsername()).isEqualTo(userDTO.getUsername());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userMapper, times(1)).updateUserFromUserDTO(userDTO, user);
    }

    @Test
    void should_return_userProfileDTO() {

        //given
        User currentUser = getCurrentUserMock();
        UserProfileDTO userProfileDTO = getUserProfileDTOMock();
        Long id = 2L;
        when(userRepository.findById(id)).thenReturn(
                Optional.ofNullable(currentUser));
        when(userMapper.mapUserToUserProfileDTO(currentUser)).thenReturn(userProfileDTO);

        // when
        UserProfileDTO result = userService.getLoggedUser();

        //then

        assertThat(result.getId()).isEqualTo(currentUser.getId());
        verify(userRepository, times(1)).findById(id);
        verify(userMapper, times(1)).mapUserToUserProfileDTO(currentUser);
    }

    @Test
    void should_return_userDTO_by_given_username() {
        //given
        String username = "Evi";
        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));
        when(userMapper.mapUserToUserDTO(user)).thenReturn(userDTO);

        //when
        UserDTO result = userService.getUserByUsername(username);

        //then
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userMapper, times(1)).mapUserToUserDTO(user);

    }

    @Test
    void should_return_list_of_banned_users() {
        User bannedUser = getBannedUserMock();
        BannedUserDTO bannedUserDTO = getBannedUserDTOMock();
        when(userRepository.findAllByAccountNonLockedNot(true)).thenReturn(Collections.singletonList(bannedUser));
        when(userMapper.mapUserToBannedUserDTO(bannedUser)).thenReturn(bannedUserDTO);

        //when
        List<BannedUserDTO> result = userService.getBannedUsers();

        //then
        assertThat(result.get(0).getId()).isEqualTo(bannedUser.getId());

        verify(userRepository, times(1)).findAllByAccountNonLockedNot(true);
        verify(userMapper, times(1)).mapUserToBannedUserDTO(user);

    }

    @Test
    void should_return_list_of_reported_users() {
        //given
        Report report = getReportMock();
        ReportDTO reportDTO = getReportDTOMock();
        UserMsgDTO userMsgDTO = getUserMsgDTOMock();
        when(reportRepository.findAllByReportedUserEnabled(true)).thenReturn(Collections.singletonList(report));
        when(userMapper.mapUserToUserMsgDTO(user)).thenReturn(userMsgDTO);
        when(reportMapper.mapReportToReportDTO(report)).thenReturn(reportDTO);

        //when
        List<ReportedUserDTO> result = userService.getReportedUsers();

        //then
        assertThat(result.get(0).getReportedUser().getId()).isEqualTo(user.getId());
        assertThat(result.get(0).getReports().get(0).getId()).isEqualTo(report.getId());

        verify(reportRepository, times(1)).findAllByReportedUserEnabled(true);
        verify(userMapper, times(1)).mapUserToUserMsgDTO(user);
        verify(reportMapper, times(1)).mapReportToReportDTO(report);


    }

    @Test
    void should_return_user_groups_as_UserGroupsListDTO() {
        //given
        UserGroupsListDTO userGroupsListDTO = getUserGroupListDTOMock();
        Long id = 2L;
        User currentUser = getCurrentUserMock();
        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(currentUser));
        when(userGroupListMapper.mapuserToUserGroupsListDTO(currentUser)).thenReturn(userGroupsListDTO);

        //when
        UserGroupsListDTO result = userService.getUserGroups();

        //then
        assertThat(result.getId()).isEqualTo(userGroupsListDTO.getId());
        assertThat(result.getGroupRooms().get(0).getId()).isEqualTo(userGroupsListDTO.getGroupRooms().get(0).getId());
        verify(userRepository, times(1)).findById(id);
        verify(userGroupListMapper, times(1)).mapuserToUserGroupsListDTO(currentUser);

    }

    @Test
    void should_join_group_and_return_userDTO() {
        //given
        GroupRoom groupRoom = getGroupRoomMock();
        Long id = 2L;
        Long groupId = 1L;
        User currentUser = getCurrentUserMock();
        UserDTO currentUserDTO = getCurrentUserDTOMock();
        InGameRolesDTO inGameRolesDTO = getInGameRoleDTOMock();

        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(currentUser));
        when(groupRepository.findById(groupId)).thenReturn(Optional.ofNullable(groupRoom));
        when(userMapper.mapUserToUserDTO(currentUser)).thenReturn(currentUserDTO);

        //when
        UserDTO result = userService.joinGroupRoom(groupId, inGameRolesDTO);

        //then
        assertThat(result.getId()).isEqualTo(currentUser.getId());

        verify(userRepository, times(1)).findById(id);
        verify(groupRepository, times(1)).findById(groupId);
        verify(userMapper, times(1)).mapUserToUserDTO(currentUser);

    }

    @Test
    void changeProfilePicture() {
        //given
        User currentUser = getCurrentUserMock();
        MultipartFile file = new MockMultipartFile("file",new byte[]{});
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(currentUser));

        //when
        userService.changeProfilePicture(file);

        //then
        verify(userRepository, times(1)).findById(currentUser.getId());
    }

    @Test
    void getProfilePicture() {
    }


    @Test
    void should_return_user_profileDTO() {
        //given
        UserProfileDTO userProfileDTO = getUserProfileDTOMock();
        Long userId = 2L;
        User currentUser = getCurrentUserMock();
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(currentUser));
        when(userMapper.mapUserToUserProfileDTO(currentUser)).thenReturn(userProfileDTO);

        //when
        UserProfileDTO result = userService.getUserProfile(userId);

        //then
        assertThat(result.getId()).isEqualTo(currentUser.getId());
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).mapUserToUserProfileDTO(currentUser);
    }


    @Test
    void getOutOfGroup() {
        //given
        User currentUser = getCurrentUserMock();
        GroupRoom groupRoom = getCurrentUserGroupRoomMock();
        Long groupId = 1L;
        when(userRepository.findById(currentUser.getId())).thenReturn(Optional.of(currentUser));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(groupRoom));

        //when
        userService.getOutOfGroup(1L);

        //then
        verify(userRepository, times(1)).findById(currentUser.getId());
        verify(groupRepository, times(1)).findById(groupId);
    }

    @Test
    void should_save_report_for_user() {
        //given
        Long userId = 1L;
        User currentUser = getCurrentUserMock();
        Report report = getReportMock();
        ReportDTO reportDTO = getReportDTOMockv2();
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        when(reportRepository.existsByReportedByIdAndReportedUserId(currentUser.getId(), user.getId())).thenReturn(false);
        when(reportMapper.mapReportDTOToReport(reportDTO)).thenReturn(report);

        //when
        userService.reportUser(reportDTO, userId);

        //then
        verify(userRepository, times(1)).findById(userId);
        verify(reportRepository, times(1)).existsByReportedByIdAndReportedUserId(currentUser.getId(), user.getId());
        verify(reportMapper, times(1)).mapReportDTOToReport(reportDTO);
    }

    @Test
    void should_throw_user_already_reported_exception() {
        //given
        Long userId = 1L;
        User currentUser = getCurrentUserMock();
        Report report = getReportMock();
        ReportDTO reportDTO = getReportDTOMockv2();
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        when(reportRepository.existsByReportedByIdAndReportedUserId(currentUser.getId(), user.getId())).thenReturn(true);
        when(reportMapper.mapReportDTOToReport(reportDTO)).thenReturn(report);

        //when
        Exception exception = assertThrows(AlreadyReportedException.class, () -> userService.reportUser(reportDTO, userId));

        //then
        assertTrue(exception.getMessage().contains("You already reported this user"));
        verify(userRepository, times(1)).findById(userId);
        verify(reportRepository, times(1)).existsByReportedByIdAndReportedUserId(currentUser.getId(), user.getId());
        verify(reportMapper, times(0)).mapReportDTOToReport(reportDTO);

    }


    @Test
    void should_ban_user() {
        //given
        BannedUserDTO bannedUserDTO = getBannedUserDTOMock();
        Long bannedUserId = bannedUserDTO.getId();
        User bannedUser = getReportedUserMock();
        Report report = getReportMock();
        when(userRepository.findById(bannedUserId)).thenReturn(Optional.ofNullable(bannedUser));
        when(reportRepository.findAllByReportedUserId(bannedUserId)).thenReturn(Collections.singletonList(report));

        //when
        userService.banUser(bannedUserDTO);

        //then
        verify(userRepository, times(1)).findById(bannedUserId);
        verify(reportRepository, times(1)).findAllByReportedUserId(bannedUserId);

    }

    @Test
    void should_return_user_already_banned_exception() {
        //given
        BannedUserDTO bannedUserDTO = getBannedUserDTOMock();
        Long bannedUserId = bannedUserDTO.getId();
        User bannedUser = getBannedUserMock();
        Report report = getReportMock();
        when(userRepository.findById(bannedUserId)).thenReturn(Optional.ofNullable(bannedUser));
        when(reportRepository.findAllByReportedUserId(bannedUserId)).thenReturn(Collections.singletonList(report));

        //when
        Exception exception = assertThrows(AlreadyBannedException.class, () -> userService.banUser(bannedUserDTO));

        //then
        assertNotNull(exception);
        verify(userRepository, times(1)).findById(bannedUserId);
        verify(reportRepository, times(0)).findAllByReportedUserId(bannedUserId);

    }

    @Test
    void should_delete_reports() {
        //given
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        //when
        userService.deleteReports(user.getId());

        //then
        verify(userRepository, times(1)).findById(user.getId());
        verify(reportRepository,times(1)).deleteAll(any());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void should_unban_user() {
        //given
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));

        //when
        userService.unbanUser(user.getId());

        //then
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void should_send_friend_request() {
        //given
        User invitedUser = getInvitedUserMock();
        User currentUser = getCurrentUserMock();
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(invitedUser));
        when(friendRequestRepository.existsBySendingUserIdAndInvitedUserId(any(Long.class),any(Long.class))).thenReturn(false);

        //when
        userService.sendFriendRequest(invitedUser.getId());

        //then
        verify(userRepository, times(1)).findById(invitedUser.getId());
        verify(friendRequestRepository,times(1)).existsBySendingUserIdAndInvitedUserId(currentUser.getId(), invitedUser.getId());
        verify(sseService,times(1)).sendSseFriendEvent(any(),any());

    }

    @Test
    void should_throw_already_friend_exception() {
        //given
        User invitedUser = getInvitedUserMock();
        User currentUser = getCurrentUserMock();
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(invitedUser));
        when(friendRequestRepository.existsBySendingUserIdAndInvitedUserId(any(Long.class),any(Long.class))).thenReturn(true);

        //when
        Exception exception = assertThrows(AlreadyInvitedException.class, () -> userService.sendFriendRequest(invitedUser.getId()));

        //then
        assertNotNull(exception);
        verify(userRepository, times(1)).findById(invitedUser.getId());
        verify(sseService,times(0)).sendSseFriendEvent(any(),any());

    }

    @Test
    void should_return_friend_requestDTO_list() {
        //given
        User currentUser = getCurrentUserMock();
        FriendRequest friendRequest = getFriendRequestMock();
        FriendRequestDTO friendRequestDTO = getFriendRequestDTOMock();
        when(friendRequestRepository.findAllByInvitedUserId(any(Long.class))).thenReturn(Collections.singletonList(friendRequest));
        when(friendRequestMapper.mapFriendRequestToFriendRequestDTO(any(FriendRequest.class))).thenReturn(friendRequestDTO);

        //when
        List<FriendRequestDTO> result = userService.loadFriendRequests();

        //then
        assertThat(result.get(0).getId()).isEqualTo(friendRequest.getId());
        verify(friendRequestRepository, times(1)).findAllByInvitedUserId(currentUser.getId());
        verify(friendRequestMapper,times(1)).mapFriendRequestToFriendRequestDTO(friendRequest);


    }

    @Test
    void accept_method_should_throw_already_friend_exception() {
        //given
        FriendRequest fr = getFriendRequestMock();
        User currentUser = getCurrentUserMock();
        User sendingUser = getInvitedUserMock();
        when(friendRequestRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(fr));
        when(userRepository.findById(sendingUser.getId())).thenReturn(Optional.of(sendingUser));
        when(userRepository.findById(currentUser.getId())).thenReturn(Optional.of(currentUser));

        //when
        Exception exception = assertThrows(AlreadyFriendException.class, () -> userService.acceptFriendRequest(fr.getId()));

        //then
        assertNotNull(exception);
        verify(friendRequestRepository, times(1)).findById(fr.getId());
        verify(userRepository,times(1)).findById(sendingUser.getId());
        verify(userRepository, times(1)).findById(currentUser.getId());
        verify(chatRepository,times(0)).save(any());
        verify(friendRepository, times(0)).save(any());
        verify(sseService,times(0)).sendSseFriendEvent(any(),any());

    }

    @Test
    void should_accept_friend_request() {
        //given
        FriendRequest fr = getFriendRequestMockv2();
        User currentUser = getCurrentUserMock();
        User sendingUser = getSendingUserMock();
        when(friendRequestRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(fr));
        when(userRepository.findById(sendingUser.getId())).thenReturn(Optional.of(sendingUser));
        when(userRepository.findById(currentUser.getId())).thenReturn(Optional.of(currentUser));

        //when
        userService.acceptFriendRequest(fr.getId());

        //then
        verify(friendRequestRepository, times(1)).findById(fr.getId());
        verify(userRepository,times(2)).findById(any(Long.class));
        verify(chatRepository,times(1)).save(any());
        verify(friendRepository, times(2)).save(any());
        verify(sseService,times(2)).sendSseFriendEvent(any(),any());

    }

    @Test
    void should_decline_friend_request() {
        //given
        FriendRequest friendRequest = getFriendRequestMock();
        Long requestId = 1L;
        when(friendRequestRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(friendRequest));

        //when
        userService.declineFriendRequest(requestId);

        //then
        verify(friendRequestRepository, times(1)).findById(requestId);
        verify(friendRequestRepository, times(1)).delete(friendRequest);

    }

    @Test
    void should_return_friend_list() {
        //given
        Friend friend = getFriendMock();
        FriendDTO friendDTO = getFriendDTOMock();
        User currentUser = getCurrentUserMock();
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(currentUser));
        when(friendMapper.mapFriendToFriendDTO(any(Friend.class))).thenReturn(friendDTO);

        //when
        List<FriendDTO> result = userService.getFriendList();

        //then
        assertThat(result.get(0).getId()).isEqualTo(friend.getId());
        verify(userRepository, times(1)).findById(currentUser.getId());

        verify(friendMapper,times(1)).mapFriendToFriendDTO(friend);

    }

    @Test
    void should_delete_user() {
        //given
        Long userId = 1L;

        //when
        userService.deleteUserById(userId);

        //then
        verify(userRepository, times(1)).deleteById(userId);
    }
}