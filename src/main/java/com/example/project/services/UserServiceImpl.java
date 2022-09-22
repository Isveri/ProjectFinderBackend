package com.example.project.services;

import com.example.project.chat.model.Chat;
import com.example.project.chat.model.CustomNotification;
import com.example.project.chat.model.CustomNotificationDTO;
import com.example.project.chat.repositories.ChatRepository;
import com.example.project.chat.service.SseService;
import com.example.project.domain.*;
import com.example.project.exceptions.*;
import com.example.project.mappers.*;
import com.example.project.model.*;
import com.example.project.repositories.*;
import com.example.project.utils.DataValidation;
import com.example.project.utils.FileHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.project.utils.UserDetailsHelper.getCurrentUser;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final UserGroupListMapper userGroupListMapper;
    private final SseService sseService;

    private final ReportMapper reportMapper;
    private final ChatRepository chatRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final FriendMapper friendMapper;
    private final FriendRequestMapper friendRequestMapper;
    private final DataValidation dataValidation;
    private final ReportRepository reportRepository;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> userMapper.mapUserToUserDTO(user))
                .collect(Collectors.toList());
    }


    @Override
    public UserDTO save(UserDTO userDTO) {

        Role userRole = roleRepository.findByName("ROLE_USER");
        User user = userMapper.mapUserDTOToUser(userDTO);
        user.setRole(userRole);
        return userMapper.mapUserToUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO saveAndReturnDTO(User user) {
        return userMapper.mapUserToUserDTO(userRepository.save(user));
    }


    @Override
    public UserDTO updateUserByDTO(UserDTO userDTO) {
        User currentUser = getCurrentUser();
        long id = currentUser.getId();
        User user = userRepository.findById(id).orElseThrow(() -> new GroupNotFoundException("User not found id:" + id));
        dataValidation.emailUpdate(userDTO.getEmail(), user);
        dataValidation.age(userDTO.getAge());
        return saveAndReturnDTO(userMapper.updateUserFromUserDTO(userDTO, user));
    }

    @Override
    public UserDTO getLoggedUser() {
        User currentUser = getCurrentUser();
        long id = currentUser.getId();
        return userRepository.findById(id)
                .map(userMapper::mapUserToUserDTO)
                .map(userDTO -> {
                    userDTO.setId(id);
                    return userDTO;
                })
                .orElseThrow(() -> new UserNotFoundException("User not found id:" + id));
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::mapUserToUserDTO)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with this username"));
    }

    @Override
    public List<BannedUserDTO> getBannedUsers() {
        return userRepository.findAllByAccountNonLockedNot(true)
                .stream()
                .map(userMapper::mapUserToBannedUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportedUserDTO> getReportedUsers() {
        Map<String, ReportedUserDTO> reportedUsers = new HashMap<>();

        reportRepository.findAllByReportedUserEnabled(true)
                .forEach((report) -> {
                    if (!reportedUsers.containsKey(report.getReportedUser().getUsername())) {
                        reportedUsers.put(report.getReportedUser().getUsername(), new ReportedUserDTO());
                    }
                    ReportedUserDTO reportedUserDTO = reportedUsers.get(report.getReportedUser().getUsername());
                    reportedUserDTO.setReportedUser(userMapper.mapUserToUserMsgDTO(report.getReportedUser()));
                    reportedUserDTO.addReport(reportMapper.mapReportToReportDTO(report));
                    reportedUsers.put(report.getReportedUser().getUsername(), reportedUserDTO);
                });
        return new ArrayList<ReportedUserDTO>(reportedUsers.values());
    }


    @Override
    public UserGroupsListDTO getUserGroups() {
        User currentUser = getCurrentUser();
        long id = currentUser.getId();
        return userRepository.findById(id)
                .map(userGroupListMapper::mapuserToUserGroupsListDTO).orElseThrow(() -> new UserNotFoundException("User not found id:" + id));
    }

    @Override
    public UserDTO joinGroupRoom(Long groupRoomId, InGameRolesDTO inGameRolesDTO) {
        User currentUser = getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new UserNotFoundException("User not found id:" + currentUser.getId()));

        GroupRoom groupRoom = groupRepository.findById(groupRoomId)
                .orElseThrow(() -> new GroupNotFoundException("Group room not found"));

        if (user.getGroupRooms().contains(groupRoom)) {
            throw new AlreadyInGroupException("User id:" + user.getId() + " is already in group");
        } else {
            user.getGroupRooms().add(groupRoom);
            if (groupRoom.isInGameRolesActive()) {
                if (inGameRolesDTO.getId() == null) {
                    groupRoom.getTakenInGameRoles().stream().filter((takenInGameRole -> takenInGameRole.getUser() == null)).findFirst().orElseThrow(null).setUser(currentUser);
                } else {
                    groupRoom.getTakenInGameRoles().stream().filter((takenInGameRole -> takenInGameRole.getInGameRole().getId().equals(inGameRolesDTO.getId()))).findFirst().orElseThrow(null).setUser(currentUser);
                }
            }
            userRepository.save(user);
            sseService.sendSseEventToUser(CustomNotificationDTO.builder().msg(user.getUsername() + " joined group").type(CustomNotification.NotifType.INFO).build(), groupRoom, null);
            return userMapper.mapUserToUserDTO(user);
        }
    }

    @Override
    public void changeProfilePicture(MultipartFile profilePicture) {
        User currentUser = getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new UserNotFoundException("User not found id:" + currentUser.getId()));
        user.setProfileImgName(user.getId() + "-" + FileHandler.save(profilePicture, user.getId()));
        userRepository.save(user);
    }

    @Override
    public Resource getProfilePicture(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found id:" + userId));
        if (user.getProfileImgName() != null) {
            return FileHandler.load(user.getProfileImgName());
        }
        return null;
    }

    @Override
    public UserProfileDTO getUserProfile(Long userId) {

        return userRepository.findById(userId)
                .map(userMapper::mapUserToUserProfileDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public void getOutOfGroup(Long groupRoomId) {
        User currentUser = getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new UserNotFoundException("User not found id:" + currentUser.getId()));
        GroupRoom groupRoom = groupRepository.findById(groupRoomId)
                .orElseThrow(() -> new GroupNotFoundException("Group room not found"));

        user.getGroupRooms().remove(groupRoom);
        if (groupRoom.isInGameRolesActive()) {
            groupRoom.getTakenInGameRoles().stream().filter((takenInGameRole) -> user.equals(takenInGameRole.getUser())).findAny().orElse(new TakenInGameRole()).setUser(null);
        }
        if (Objects.equals(groupRoom.getGroupLeader(), user)) {
            groupRoom.setGroupLeader(groupRoom.getUsers().stream().filter(usr -> !user.equals(usr)).findFirst().orElseThrow(null));
        }
        if (groupRoom.getUsers().size() == 1) {
            groupRepository.softDeleteById(groupRoom.getId());
        } else {
            groupRepository.save(groupRoom);
        }
        userRepository.save(currentUser);
        sseService.sendSseEventToUser(CustomNotificationDTO.builder().msg(user.getUsername() + " left group").type(CustomNotification.NotifType.REMOVED).build(), groupRoom, user.getId());

    }

    @Override
    public void reportUser(ReportDTO reportDTO, Long userId) {
        User reportedBy = getCurrentUser();
        User userToReport = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found id:" + userId));

        if (!reportRepository.existsByReportedByIdAndReportedUserId(reportedBy.getId(), userToReport.getId())) {
            Report report = reportMapper.mapReportDTOToReport(reportDTO);
            report.setReportedBy(reportedBy);
            userToReport.getReports().add(report);
            report.setReportedUser(userToReport);
            report.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            reportRepository.save(report);
        } else {
            throw new AlreadyReportedException("You already reported this user");
        }

    }

    @Override
    public void banUser(BannedUserDTO bannedUserDTO) {
        User admin = getCurrentUser();
        Long userId = bannedUserDTO.getId();
        User userToBan = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User doesnt exist id:" + userId));
        if (!userToBan.isAccountNonLocked()) {
            throw new AlreadyBannedException("User is already banned");
        }
        userToBan.setAccountNonLocked(false);
        userToBan.setBannedBy(admin.getUsername());
        userToBan.setReason(bannedUserDTO.getReason());
        userToBan.setReports(null);
        reportRepository.deleteAll(reportRepository.findAllByReportedUserId(userId));
        userRepository.save(userToBan);
    }

    @Override
    public void deleteReports(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User doesnt exist id:" + userId));
        user.setReports(null);
        reportRepository.deleteAll(reportRepository.findAllByReportedUserId(userId));
        userRepository.save(user);
    }

    @Override
    public void unbanUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id:" + userId + " doesnt exist"));
        user.setAccountNonLocked(true);
        user.setBannedBy(null);
        user.setReason(null);
        userRepository.save(user);
    }

    @Override
    public void sendFriendRequest(Long invitedUserId) {
        User user = getCurrentUser();
        User invitedUser = userRepository.findById(invitedUserId).orElseThrow(() -> new UserNotFoundException("User with id:" + invitedUserId + " doesnt exist"));
        if (!friendRequestRepository.existsBySendingUserIdAndInvitedUserId(user.getId(), invitedUserId)) {
            if (user.equals(invitedUser)) {
                throw new AlreadyFriendException("Cant invite yourself");
            } else if (IsNotOnFriendList(user, invitedUser)) {
                FriendRequest friendRequest = FriendRequest.builder().sendingUser(user).invitedUser(invitedUser).build();
                friendRequestRepository.save(friendRequest);
                sseService.sendSseFriendEvent(CustomNotificationDTO.builder().msg("New friend request").type(CustomNotification.NotifType.FRIENDREQUEST).build(),invitedUserId);
            } else {
                throw new AlreadyInvitedException(invitedUser.getUsername() + " already friend");
            }
        } else {
            throw new AlreadyInvitedException(invitedUser.getUsername() + " already invited");
        }
    }

    private boolean IsNotOnFriendList(User user, User invitedUser) {
        return invitedUser.getFriendList().stream().filter(friend -> user.equals(friend.getUser())).findFirst().orElse(null) == null;
    }

    @Override
    public List<FriendRequestDTO> loadFriendRequests() {
        return friendRequestRepository.findAllByInvitedUserId(getCurrentUser().getId())
                .stream()
                .map(friendRequestMapper::mapFriendRequestToFriendRequestDTO)
                .collect(Collectors.toList());
    }


    @Override
    public void acceptFriendRequest(Long friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId).orElseThrow();
        User user = userRepository.findById(getCurrentUser().getId()).orElseThrow(() -> new UserNotFoundException("User doesnt exist"));
        User sendingUser = userRepository.findById(friendRequest.getSendingUser().getId()).orElseThrow(() -> new UserNotFoundException("User doesnt exist"));
        if (IsNotOnFriendList(sendingUser, user)) {
            Chat chat = Chat.builder().notPrivate(false).build();
            chatRepository.save(chat);
            Friend friend = Friend.builder().chat(chat).user(sendingUser).build();
            friendRepository.save(friend);
            user.getFriendList().add(friend);
            friend = Friend.builder().chat(chat).user(user).build();
            sendingUser.getFriendList().add(friend);
            friendRepository.save(friend);

            userRepository.saveAll(Arrays.asList(user, sendingUser));
            sseService.sendSseFriendEvent(CustomNotificationDTO.builder().msg("New friend request").type(CustomNotification.NotifType.FRIENDREQUEST).build(),sendingUser.getId());
            sseService.sendSseFriendEvent(CustomNotificationDTO.builder().msg("New friend request").type(CustomNotification.NotifType.FRIENDREQUEST).build(),user.getId());
        } else {
            throw new AlreadyFriendException(sendingUser.getUsername() + " is already your friend");
        }
        friendRequestRepository.delete(friendRequest);
    }

    @Override
    public void declineFriendRequest(Long friendRequestId) {
        friendRequestRepository.delete(friendRequestRepository.findById(friendRequestId).orElseThrow());
    }

    @Override
    public List<FriendDTO> getFriendList() {
        User user = userRepository.findById(getCurrentUser().getId()).orElseThrow(() -> new UserNotFoundException("User not found"));

        return user.getFriendList().stream().map(friendMapper::mapFriendToFriendDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
