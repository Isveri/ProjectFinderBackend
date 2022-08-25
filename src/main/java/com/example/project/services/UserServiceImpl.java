package com.example.project.services;

import com.example.project.chat.model.CustomNotification;
import com.example.project.chat.model.CustomNotificationDTO;
import com.example.project.chat.service.SseService;
import com.example.project.domain.GroupRoom;
import com.example.project.domain.Report;
import com.example.project.domain.Role;
import com.example.project.domain.User;
import com.example.project.exceptions.*;
import com.example.project.mappers.ReportMapper;
import com.example.project.mappers.UserGroupListMapper;
import com.example.project.mappers.UserMapper;
import com.example.project.model.*;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.ReportRepository;
import com.example.project.repositories.RoleRepository;
import com.example.project.repositories.UserRepository;
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
        dataValidation.email(userDTO.getEmail(),user);
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
                .orElseThrow(()-> new UsernameNotFoundException("There is no user with this username"));
    }

    @Override
    public List<BannedUserDTO> getBannedUsers() {
        return userRepository.findAllByBanned(true)
                .stream()
                .map(userMapper::mapUserToBannedUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportedUserDTO> getReportedUsers() {
        Map<String,ReportedUserDTO> reportedUsers = new HashMap<>();

        reportRepository.findAll()
                .forEach((report)->{
                    if(!reportedUsers.containsKey(report.getReportedUser().getUsername())) {
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
    public UserDTO joinGroupRoom(Long groupRoomId) {
        User currentUser = getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new UserNotFoundException("User not found id:" + currentUser.getId()));

        GroupRoom groupRoom = groupRepository.findById(groupRoomId)
                .orElseThrow(() -> new GroupNotFoundException("Group room not found"));

        if (user.getGroupRooms().contains(groupRoom)) {
            throw new AlreadyInGroupException("User id:" + user.getId() + " is already in group");
        } else {
            user.getGroupRooms().add(groupRoom);
            userRepository.save(user);
            sseService.sendSseEventToUser(CustomNotificationDTO.builder().msg(user.getUsername() + " joined group").type(CustomNotification.NotifType.INFO).build(), groupRoom, null);
            return userMapper.mapUserToUserDTO(user);
        }
    }

    @Override
    public void changeProfilePicture(MultipartFile profilePicture) {
        User currentUser = getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new UserNotFoundException("User not found id:" + currentUser.getId()));
        //TODO zrobic usuwanie aktualnego zdjecia z folderu zeby nie bylo syfu
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
                .orElseThrow(() -> new GroupNotFoundException("Group room not found"));
    }

    @Override
    public void getOutOfGroup(Long groupRoomId) {
        User currentUser = getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new GroupNotFoundException("User not found id:" + currentUser.getId()));
        GroupRoom groupRoom = groupRepository.findById(groupRoomId)
                .orElseThrow(() -> new GroupNotFoundException("Group room not found"));

        user.getGroupRooms().remove(groupRoom);
        if (Objects.equals(groupRoom.getGroupLeader(), user)) {
            groupRoom.setGroupLeader(groupRoom.getUsers().get(0));
        }
        if (groupRoom.getUsers().size() == 1) {
            groupRepository.softDeleteById(groupRoom.getId());
        } else {
            groupRepository.save(groupRoom);
        }
        userRepository.save(currentUser);

    }

    @Override
    public void reportUser(ReportDTO reportDTO,Long userId) {
        User reportedBy = getCurrentUser();
        User userToReport = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User not found id:" +userId));

        if(!reportRepository.existsByReportedByIdAndReportedUserId(reportedBy.getId(),userToReport.getId())) {
            Report report = reportMapper.mapReportDTOToReport(reportDTO);
            report.setReportedBy(reportedBy);
            userToReport.getReports().add(report);
            report.setReportedUser(userToReport);
            report.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            reportRepository.save(report);
        }else{
            throw new AlreadyReportedException("You already reported this user");
        }

    }

    @Override
    public void banUser(BannedUserDTO bannedUserDTO) {
        User admin = getCurrentUser();
        Long userId = bannedUserDTO.getId();
        User userToBan = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User doesnt exist id:"+userId));
        if(userToBan.isBanned()){
            throw new AlreadyBannedException("User is already banned");
        }
        userToBan.setBanned(true);
        userToBan.setBannedBy(admin.getUsername());
        userToBan.setReason(bannedUserDTO.getReason());
        userRepository.save(userToBan);
    }

    @Override
    public void unbanUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User with id:"+userId+" doesnt exist"));
        user.setBanned(false);
        user.setBannedBy(null);
        user.setReason(null);
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
