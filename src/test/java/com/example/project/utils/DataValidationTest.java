package com.example.project.utils;

import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import com.example.project.exceptions.SomethingWrongException;
import com.example.project.mappers.UserMapper;
import com.example.project.model.GroupRoomDTO;
import com.example.project.model.UserDTO;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.UserRepository;
import com.example.project.services.UserServiceImpl;
import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import com.example.project.exceptions.validation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.example.project.samples.UserMockSample.*;
import static com.example.project.samples.GroupRoomSample.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

class DataValidationTest {

    @Mock
    private  UserRepository userRepository ;
    @Mock
    private  Cities cities=new Cities();

    private DataValidation dataValidation;
    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataValidation=new DataValidation(userRepository);

        user=getUserMock();
        userDTO = getUserDTOMock();
    }

    @Test
    void correct_emailCreate() {
        //given
        String email="evistifate@gmail.com";
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.emailCreate(email));
    }
    @Test
    void incorrect_emailCreate_throw_BadEmailException(){
        //given
        String email="ev@istifate@gmail.com";
        String expectedMsg="Wrong email structure";
        //when+then
        assertThrows(BadEmailException.class, () -> {
            dataValidation.emailCreate(email);
        });

    }
    @Test
    void incorrect_emailCreate_throw_EmailAlreadyTakenException(){
        //given
        String email="evistifate@gmail.com";
        String expectedMsg="email is already connected to account";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        //when+then
       assertThrows(EmailAlreadyTakenException.class, () -> {
            dataValidation.emailCreate(email);
        });
    }

    @Test
    void correct_emailUpdate() {
        //given
        User userC=getCurrentUserMock();
        String email="test@gmail.com";
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.emailUpdate(email,userC));
    }
    @Test
    void incorrect_emailUpdate_throw_BadEmailException() {
        //given
        User userC=getCurrentUserMock();
        String email="t@est@gmail.com";
        //when+then
       assertThrows(BadEmailException.class, () -> {
            dataValidation.emailUpdate(email,userC);
        });
    }

    @Test
    void correct_usernameCreate() {
        //given
        String username="Evi";
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.usernameCreate(username));
    }
    @Test
    void incorrect_usernameCreate_throw_BadUsernameException() {
        //given
        String username="Evi test";
        //when+then
       assertThrows(BadUsernameException.class, () -> {
            dataValidation.usernameCreate(username);
        });
    }
    @Test
    void incorrect_usernameCreate_throw_UsernameAlreadyTakenException() {
        //given
        String username="Evi";
        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        //when+then
        assertThrows(UsernameAlreadyTakenException.class, () -> {
            dataValidation.usernameCreate(username);
        });
    }
    @Test
    void correct_usernameUpdate() {
        //given
        User userC=getCurrentUserMock();
        String username="EviTest";
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.usernameUpdate(username,userC));
    }
    @Test
    void incorrect_usernameUpdate_throw_BadUsernameException() {
        //given
        User userC=getCurrentUserMock();
        String username="Evi Test";
        //when+then
        assertThrows(BadUsernameException.class, () -> {
            dataValidation.usernameUpdate(username,userC);
        });
    }
    @Test
    void correct_usernameLogin() {
        //given
        String username="Evi";
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.usernameLogin(username));
    }
    @Test
    void incorrect_usernameLogin_throw_BadUsernameException() {
        //given
        String username="Evi>";
        //when+then
        assertThrows(BadUsernameException.class, () -> {
            dataValidation.usernameLogin(username);
        });
    }

    @Test
    void correct_password() {
        //given
        String pswd="Password0";
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.password(pswd));
    }
    @Test
    void incorrect_password_throw_BadPasswordException() {
        //test for testing regex w{3,}
        //given
        String pswd="pass>";
        //when+then
        assertThrows(BadPasswordException.class, () -> {
            dataValidation.password(pswd);
        });
    }
    @Test
    void correct_profileName() {
        //given
        String name="Janusz";
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.profileName(name));
    }
    @Test
    void incorrect_profileName_throw_BadProfileNameException() {
        //given
        String name="Janusz1";
        //when+then
        assertThrows(BadProfileNameException.class, () -> {
            dataValidation.profileName(name);
        });
    }

    @Test
    void correct_groupName() {
        //given
        String groupname="Group Mock";
        GroupRoom groupRoom=getGroupRoomMock();
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.groupName(groupname,groupRoom));
    }
    @Test
    void incorrect_groupName_throw_BadGroupNameException() {
        //given
        String groupname="Group <Mock>";
        GroupRoom groupRoom=getGroupRoomMock();
        //when+then
        assertThrows(BadGroupNameException.class, () -> {
            dataValidation.groupName(groupname,groupRoom);
        });
    }

    @Test
    void correct_age() {
        //given
        Integer age = 19;
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.age(age));
    }
    @Test
    void incorrect_age_throw_BadAgeException() {
        //given
        Integer age = 1999;
        //when+then
        assertThrows(BadAgeException.class, () -> {
            dataValidation.age(age);
        });
    }
    @Test
    void correct_phone() {
        //given
        Integer phone=987654321;
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.phone(phone));
    }
    @Test
    void incorrect_phone_throw_BadPhoneException() {
        //given
        Integer phone=98765432;
        //when+then
        assertThrows(BadPhoneException.class, () -> {
            dataValidation.phone(phone);
        });
    }
    @Test
    void correct_city() {
        //given
        String city="Warsaw";
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.city(city));
    }

    @Test
    void incorrect_city_throw_BadCityException() {
        //given
        String city="London";
        //when+then
        assertThrows(BadCityException.class, () -> {
            dataValidation.city(city);
        });
    }

    @Test
    void correct_profileInfo() {
        //given
        String text="Random text to test";
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.profileInfo(text));
    }
    @Test
    void incorrect_profileInfo_throw_BadProfileInfoException() {
        //given
        String text="<Random text to test>";
        //when+then
        assertThrows(BadProfileInfoException.class, () -> {
            dataValidation.profileInfo(text);
        });
    }
    @Test
    void correct_groupDesc() {
        //given
        String text="Random text to test";
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.groupDesc(text));
    }
    @Test
    void incorrect_groupDesc_throw_BadGroupDescException() {
        //given
        String text="<Random text to test>";
        //when+then
        assertThrows(BadGroupDescException.class, () -> {
            dataValidation.groupDesc(text);
        });
    }
    @Test
    void correct_userLimitCreate() {
        //given
        Integer users=2;
        GroupRoomDTO groupRoomDTO=getGroupRoomDTOMock();
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.userLimitCreate(users,groupRoomDTO));
    }
    @Test
    void incorrect_userLimitCreate_throw_SomethingWrongException() {
        //given
        Integer users=2;
        GroupRoomDTO groupRoomDTO=getGroupRoomDTOGameRolesTrueMock();

        //when+then
        assertThrows(SomethingWrongException.class, () -> {
            dataValidation.userLimitCreate(users,groupRoomDTO);
        });
    }
    @Test
    void incorrect_userLimitCreate_throw_BadUserLimitException() {
        //given
        Integer users=999;
        GroupRoomDTO groupRoomDTO=getGroupRoomDTOMock();

        //when+then
        assertThrows(BadUserLimitException.class, () -> {
            dataValidation.userLimitCreate(users,groupRoomDTO);
        });
    }
    @Test
    void correct_userLimitUpdate() {
        //given
        Integer users=2;
        GroupRoom groupRoom=getGroupRoomMock();
        //when+then
        Assertions.assertDoesNotThrow(() -> dataValidation.userLimitUpdate(users,groupRoom));
    }
    @Test
    void incorrect_userLimitUpdate_throw_SomethingWrongException() {
        //given
        Integer users=2;
        GroupRoom groupRoom=getGroupRoomGameRolesTrueMock();

        //when+then
        assertThrows(SomethingWrongException.class, () -> {
            dataValidation.userLimitUpdate(users,groupRoom);
        });
    }
    @Test
    void incorrect_userLimitUpdate_throw_BadUserLimitException() {
        //given
        Integer users=999;
        GroupRoom groupRoom=getGroupRoomMock();

        //when+then
        assertThrows(TooLowUserLimitException.class, () -> {
            dataValidation.userLimitUpdate(users,groupRoom);
        });
    }
}