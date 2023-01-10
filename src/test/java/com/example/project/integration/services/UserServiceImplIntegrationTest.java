package com.example.project.integration.services;

import com.example.project.domain.*;
import com.example.project.integration.bootData.BootData;
import com.example.project.model.*;
import com.example.project.repositories.*;
import com.example.project.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.example.project.samples.UserMockSample.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserServiceImplIntegrationTest {



    @Autowired
    private UserService userService;

    @Autowired
    private BootData bootData;

    private UserDTO userDTO;
    private User user;



    @Test
    @Rollback
    void should_return_user_in_list() {
        //given
        List<User> users = bootData.createUsers();
        bootData.setUserPrivilages(users);
        user = users.get(0);
        int listSize = 6;

        //when
        List<UserDTO> results = userService.getAllUsers();

        //then
        assertFalse(results.isEmpty());
        assertEquals(results.size(), listSize);
        assertEquals(results.get(0).getId(), user.getId());
    }

    @Test
    @Rollback
    void should_return_empty_list() {

        //when
        List<UserDTO> results = userService.getAllUsers();

        //then
        assertTrue(results.isEmpty());
    }


    @Test
    @Rollback
    void should_throw_ConstraintViolationException() {
        //given
        userDTO = getUserDTOMock();

        //when
        Exception exception = assertThrows(ConstraintViolationException.class, () -> userService.save(userDTO));

        //then
        assertNotNull(exception);

    }

    @Test
    @Rollback
    void should_return_userDTO_by_given_username() {
        //given
        List<User> users = bootData.createUsers();
        bootData.setUserPrivilages(users);
        user = users.get(0);
        String username = "Evi";

        //when
        UserDTO result = userService.getUserByUsername(username);

        //then
        assertNotNull(result);
        assertEquals(result.getUsername(), username);

    }

    @Test
    @Rollback
    void should_throw_UsernameNotFoundException() {
        //given
        String username = "Evi";

        //when
        Exception exception = assertThrows(UsernameNotFoundException.class,()->{userService.getUserByUsername(username);});

        //then
        assertNotNull(exception);

    }

    @Test
    @Rollback
    void should_return_list_of_banned_users() {
        //given
        List<User> users = bootData.createUsers();
        bootData.setUserPrivilages(users);
        user = users.get(0);
        String bannedUserUsername = "Satoru";

        //when
        List<BannedUserDTO> result = userService.getBannedUsers();

        //then
        assertEquals(result.get(1).getUsername(),bannedUserUsername);

    }

    @Test
    @Rollback
    void should_return_empty_list_of_banned_users(){

        //when
        List<BannedUserDTO> result = userService.getBannedUsers();

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    @Rollback
    void should_return_UserProfileDTO(){
        //given
        List<User> users = bootData.createUsers();
        bootData.setUserPrivilages(users);
        user = users.get(0);

        //when
        UserProfileDTO result = userService.getUserProfile(user.getId());

        //then
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getId(),result.getId());
    }

}