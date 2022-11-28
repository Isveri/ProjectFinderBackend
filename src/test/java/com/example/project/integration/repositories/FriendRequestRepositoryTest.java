package com.example.project.integration.repositories;

import com.example.project.domain.FriendRequest;
import com.example.project.domain.User;
import com.example.project.integration.bootData.BootData;
import com.example.project.repositories.FriendRequestRepository;
import com.example.project.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FriendRequestRepositoryTest {
    @Autowired
    FriendRequestRepository friendRequestRepository;
    @Autowired
    private BootData bootData;

    private List<FriendRequest> friendRequests;
    private List<User> users;

    @BeforeEach
    @Rollback
    void setUp(){
        users=bootData.createUsers();
        bootData.createFriendRequests(users);
    }
    @Test
    void should_return_all_friendRequest_by_inviteduserid(){
        //given
        User invitedUser=users.get(0);
        //when
        List<FriendRequest> result=friendRequestRepository.findAllByInvitedUserId(invitedUser.getId());
        //then
        assertFalse(result.isEmpty());
        assertEquals(result.get(0).getInvitedUser(),invitedUser);
    }
    @Test
    void should_return_null_by_inviteduserid(){
        //given
        User invitedUser=users.get(4);
        //when
        List<FriendRequest> result=friendRequestRepository.findAllByInvitedUserId(invitedUser.getId());
        //then
        assertTrue(result.isEmpty());
    }
    @Test
    void exist_by_sendId_and_invitedId_should_return_true(){
        //given
        User invitedUser=users.get(0);
        User sendingUser=users.get(1);
        //when
        Boolean result=friendRequestRepository.existsBySendingUserIdAndInvitedUserId(sendingUser.getId(),invitedUser.getId());
        //then
        assertTrue(result);
    }

}
