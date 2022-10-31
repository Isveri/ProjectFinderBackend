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
import static org.mockito.Mockito.when;

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
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.emailCreate(email));
        //then
    }
    @Test
    void incorrect_emailCreate_throw_BadEmailException(){
        //given
        String email="ev@istifate@gmail.com";
        String expectedMsg="Wrong email structure";
        //when
        Exception exception = assertThrows(BadEmailException.class, () -> {
            dataValidation.emailCreate(email);
        });
        //then
    }
    @Test
    void incorrect_emailCreate_throw_EmailAlreadyTakenException(){
        //given
        String email="evistifate@gmail.com";
        String expectedMsg="email is already connected to account";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        //when
        Exception exception = assertThrows(EmailAlreadyTakenException.class, () -> {
            dataValidation.emailCreate(email);
        });
        //then
    }

    @Test
    void correct_emailUpdate() {
        //given
        User userC=getCurrentUserMock();
        String email="test@gmail.com";
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.emailUpdate(email,userC));
        //then
    }
    @Test
    void incorrect_emailUpdate_throw_BadEmailException() {
        //given
        User userC=getCurrentUserMock();
        String email="t@est@gmail.com";
        //when
        Exception exception = assertThrows(BadEmailException.class, () -> {
            dataValidation.emailUpdate(email,userC);
        });
        //then
    }

    @Test
    void correct_emailLogin() {
        //given

        //when
        //then
    }

    @Test
    void correct_usernameCreate() {
        //given
        String username="Evi";
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.usernameCreate(username));
        //then
    }
    @Test
    void incorrect_usernameCreate_throw_BadUsernameException() {
        //given
        String username="Evi test";
        //when
        Exception exception = assertThrows(BadUsernameException.class, () -> {
            dataValidation.usernameCreate(username);
        });
        //then
    }
    @Test
    void incorrect_usernameCreate_throw_UsernameAlreadyTakenException() {
        //given
        String username="Evi";
        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        //when
        Exception exception = assertThrows(UsernameAlreadyTakenException.class, () -> {
            dataValidation.usernameCreate(username);
        });
        //then
    }
    @Test
    void correct_usernameUpdate() {
        //given
        User userC=getCurrentUserMock();
        String username="EviTest";
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.usernameUpdate(username,userC));
        //then
    }
    @Test
    void incorrect_usernameUpdate_throw_BadUsernameException() {
        //given
        User userC=getCurrentUserMock();
        String username="Evi Test";
        //when
        Exception exception = assertThrows(BadUsernameException.class, () -> {
            dataValidation.usernameUpdate(username,userC);
        });
        //then
    }
    @Test
    void correct_usernameLogin() {
        //test for testing regex w{3,}
        //given
        String username="Evi";
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.usernameLogin(username));
        //then
    }
    @Test
    void incorrect_usernameLogin_throw_BadUsernameException() {
        //given
        String username="Evi>";
        //when
        Exception exception = assertThrows(BadUsernameException.class, () -> {
            dataValidation.usernameLogin(username);
        });
        //then
    }

    @Test
    void correct_password() {
        //given
        String pswd="Password0";
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.password(pswd));
        //then
    }
    @Test
    void incorrect_password_throw_BadPasswordException() {
        //test for testing regex w{3,}
        //given
        String pswd="pass>";
        //when
        Exception exception = assertThrows(BadPasswordException.class, () -> {
            dataValidation.password(pswd);
        });
        //then
    }
    @Test
    void correct_profileName() {
        //given
        String name="Janusz";
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.profileName(name));
        //then
    }
    @Test
    void incorrect_profileName_throw_BadProfileNameException() {
        //given
        String name="Janusz1";
        //when
        Exception exception = assertThrows(BadProfileNameException.class, () -> {
            dataValidation.profileName(name);
        });
        //then
    }

    @Test
    void correct_groupName() {
        //given
        String groupname="Group Mock";
        GroupRoom groupRoom=getGroupRoomMock();
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.groupName(groupname,groupRoom));
        //then
    }
    @Test
    void incorrect_groupName_throw_BadGroupNameException() {
        //given
        String groupname="Group <Mock>";
        GroupRoom groupRoom=getGroupRoomMock();
        //when
        Exception exception = assertThrows(BadGroupNameException.class, () -> {
            dataValidation.groupName(groupname,groupRoom);
        });
        //then
    }

    @Test
    void correct_age() {
        //given
        Integer age = 19;
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.age(age));
        //then
    }
    @Test
    void incorrect_age_throw_BadAgeException() {
        //given
        Integer age = 1999;
        //when
        Exception exception = assertThrows(BadAgeException.class, () -> {
            dataValidation.age(age);
        });
        //then
    }
    @Test
    void correct_phone() {
        //given
        Integer phone=987654321;
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.phone(phone));
        //then
    }
    @Test
    void incorrect_phone_throw_BadPhoneException() {
        //given
        Integer phone=98765432;
        //when
        Exception exception = assertThrows(BadPhoneException.class, () -> {
            dataValidation.phone(phone);
        });
        //then
    }
    @Test
    void correct_city() {
        //given
        String city="Warsaw";
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.city(city));
        //then
    }

    @Test
    void incorrect_city_throw_BadCityException() {
        //given
        String city="London";
        //when
        Exception exception = assertThrows(BadCityException.class, () -> {
            dataValidation.city(city);
        });
        //then
    }

    @Test
    void correct_profileInfo() {
        //given
        String text="Random text to test";
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.profileInfo(text));
        //then
    }
    @Test
    void incorrect_profileInfo_throw_BadProfileInfoException() {
        //given
        String text="<Random text to test>";
        //when
        Exception exception = assertThrows(BadProfileInfoException.class, () -> {
            dataValidation.profileInfo(text);
        });
        //then
    }
    @Test
    void correct_groupDesc() {
        //given
        String text="Random text to test";
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.groupDesc(text));
        //then
    }
    @Test
    void incorrect_groupDesc_throw_BadGroupDescException() {
        //given
        String text="<Random text to test>";
        //when
        Exception exception = assertThrows(BadGroupDescException.class, () -> {
            dataValidation.groupDesc(text);
        });
        //then
    }
    @Test
    void correct_userLimitCreate() {
        //given
        Integer users=2;
        GroupRoomDTO groupRoomDTO=getGroupRoomDTOMock();
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.userLimitCreate(users,groupRoomDTO));
        //then
    }
    @Test
    void incorrect_userLimitCreate_throw_SomethingWrongException() {
        //given
        Integer users=2;
        GroupRoomDTO groupRoomDTO=getGroupRoomDTOGameRolesTrueMock();

        //when
        Exception exception = assertThrows(SomethingWrongException.class, () -> {
            dataValidation.userLimitCreate(users,groupRoomDTO);
        });
        //then
    }
    @Test
    void incorrect_userLimitCreate_throw_BadUserLimitException() {
        //given
        Integer users=999;
        GroupRoomDTO groupRoomDTO=getGroupRoomDTOMock();

        //when
        Exception exception = assertThrows(BadUserLimitException.class, () -> {
            dataValidation.userLimitCreate(users,groupRoomDTO);
        });
        //then
    }
    @Test
    void correct_userLimitUpdate() {
        //given
        Integer users=2;
        GroupRoom groupRoom=getGroupRoomMock();
        //when
        Assertions.assertDoesNotThrow(() -> dataValidation.userLimitUpdate(users,groupRoom));
        //then
    }
    @Test
    void incorrect_userLimitUpdate_throw_SomethingWrongException() {
        //given
        Integer users=2;
        GroupRoom groupRoom=getGroupRoomGameRolesTrueMock();

        //when
        Exception exception = assertThrows(SomethingWrongException.class, () -> {
            dataValidation.userLimitUpdate(users,groupRoom);
        });
        //then
    }
    @Test
    void incorrect_userLimitUpdate_throw_BadUserLimitException() {
        //given
        Integer users=999;
        GroupRoom groupRoom=getGroupRoomMock();

        //when
        Exception exception = assertThrows(TooLowUserLimitException.class, () -> {
            dataValidation.userLimitUpdate(users,groupRoom);
        });
        //then
    }
}