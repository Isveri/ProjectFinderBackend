package com.example.project.samples;

import com.example.project.domain.Friend;
import com.example.project.domain.Role;
import com.example.project.domain.User;
import com.example.project.model.*;
import com.example.project.model.auth.UserCredentials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.project.samples.FriendsSample.getFriendMock;
import static com.example.project.samples.GroupRoomSample.getGroupRoomMock;
import static com.example.project.samples.ReportMockSample.getReportDTOMock;

public class UserMockSample {

    public static User getUserMock(){
        return User.builder()
                .id(1L)
                .username("Evi")
                .password("pass")
                .age(21)
                .city("Lublin")
                .reports(new ArrayList<>())
                .friendList(new ArrayList<>())
                .email("evistifate@gmail.com")
                .info("Idk")
                .name("Patryk")
                .build();
    }
    public static User getInvitedUserMock(){
        return User.builder()
                .id(1L)
                .username("Evi")
                .age(21)
                .friendList(new ArrayList<>())
                .city("Lublin")
                .reports(new ArrayList<>())
                .email("evistifate@gmail.com")
                .info("Idk")
                .name("Patryk")
                .build();
    }

    public static User getSendingUserMock(){
        return User.builder()
                .id(3L)
                .username("Evistix")
                .age(21)
                .friendList(new ArrayList<>())
                .city("Lublin")
                .reports(new ArrayList<>())
                .email("evistifate@gmail.com")
                .info("Idk")
                .name("Patryk")
                .build();
    }

    public static User getCurrentUserMock(){
        List<Friend> friends = new ArrayList<>();
        friends.add(getFriendMock());
        return User.builder()
                .id(2L)
                .username("Evistix")
                .age(21)
                .groupRooms(new ArrayList<>())
                .city("Lublin")
                .friendList(friends)
                .email("evistifate@gmail.com")
                .info("Idk")
                .role(Role.builder().name("ROLE_USER").build())
                .name("Patryk")
                .build();
    }
    public static UserDTO getCurrentUserDTOMock(){
        return UserDTO.builder()
                .id(2L)
                .username("Evistix")
                .age(21)
                .city("Lublin")
                .email("evistifate@gmail.com")
                .info("Idk")
                .name("Patryk")
                .build();
    }

    public static UserProfileDTO getUserProfileDTOMock(){
        return UserProfileDTO.builder()
                .id(2L)
                .username("Evistix")
                .age(21)
                .city("Lublin")
                .email("evistifate@gmail.com")
                .info("Idk")
                .name("Patryk")
                .build();
    }
    public static UserCredentials getUserCredentialsMock(){
        return UserCredentials.builder()
                .password("pass")
                .username("Evi")
                .build();
    }

    public static UserMsgDTO getCurrentUserMsgDTOMock(){
        return UserMsgDTO.builder()
                .id(2L)
                .username("Evistix")
                .build();
    }

    public static UserMsgDTO getUserMsgDTOMock(){
        return UserMsgDTO.builder()
                .id(1L)
                .username("Evi")
                .build();
    }

    public static UserMsgDTO getUserMsgDTOMockv2(){
        return UserMsgDTO.builder()
                .id(2L)
                .username("Evistix")
                .build();
    }


    public static UserDTO getUserDTOMock(){
        return UserDTO.builder()
                .id(1L)
                .username("Evi")
                .age(21)
                .city("Lublin")
                .email("evistifate@gmail.com")
                .info("Idk")
                .name("Patryk")
                .build();
    }

    public static ReportedUserDTO getReportedUserDTOMock(){
        return ReportedUserDTO.builder()
                .reportedUser(getUserMsgDTOMock())
                .reports(Arrays.asList(getReportDTOMock())).build();
    }

    public static BannedUserDTO getBannedUserDTOMock(){
        return BannedUserDTO.builder()
                .id(1L)
                .bannedBy("Evi")
                .reason("Toxicity")
                .username("User")
                .build();
    }

    public static User getBannedUserMock(){
        return User.builder()
                .id(1L)
                .username("Evi")
                .age(21)
                .city("Lublin")
                .email("evistifate@gmail.com")
                .info("Idk")
                .name("Patryk")
                .accountNonLocked(false)
                .build();
    }
    public static User getReportedUserMock(){
        return User.builder()
                .id(1L)
                .username("Evi")
                .age(21)
                .city("Lublin")
                .email("evistifate@gmail.com")
                .info("Idk")
                .name("Patryk")
                .reports(new ArrayList<>())
                .accountNonLocked(true)
                .build();
    }
}
