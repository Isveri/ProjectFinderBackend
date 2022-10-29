package com.example.project.services;

import com.example.project.domain.GroupRoom;
import com.example.project.domain.Role;
import com.example.project.domain.User;
import com.example.project.exceptions.AccountBannedException;
import com.example.project.exceptions.AccountNotEnabledException;
import com.example.project.exceptions.NotGroupLeaderException;
import com.example.project.exceptions.TokenAlreadySendException;
import com.example.project.exceptions.validation.EmailAlreadyTakenException;
import com.example.project.exceptions.validation.UsernameAlreadyTakenException;
import com.example.project.mappers.UserMapper;
import com.example.project.model.UserDTO;
import com.example.project.model.auth.TokenResponse;
import com.example.project.model.auth.UserCredentials;
import com.example.project.model.auth.VerificationToken;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.RoleRepository;
import com.example.project.repositories.UserRepository;
import com.example.project.repositories.VerificationTokenRepository;
import com.example.project.security.JwtTokenUtil;
import com.example.project.utils.DataValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static com.example.project.samples.GroupRoomSample.getGroupRoomMock;
import static com.example.project.samples.RoleMockSample.getRoleMock;
import static com.example.project.samples.TokenResponseSample.getTokenResponseMock;
import static com.example.project.samples.TokenResponseSample.getVerificationTokenMock;
import static com.example.project.samples.UserMockSample.*;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserService userService;

    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private DataValidation dataValidation;
    @Mock
    private JavaMailSender javaMailSender;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthServiceImpl(userDetailsService, jwtTokenUtil, passwordEncoder, userRepository, groupRepository, roleRepository, userMapper, userService, eventPublisher,
                verificationTokenRepository, dataValidation, javaMailSender);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getPrincipal()).thenReturn(getCurrentUserMock());
    }

    @Test
    void should_return_token_response() {
        //given
        UserCredentials userCredentials = getUserCredentialsMock();
        User user = getUserMock();
        user.setEnabled(true);
        when(userDetailsService.loadUserByUsername(any(String.class))).thenReturn(user);
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

        //when
        TokenResponse result = authService.getToken(userCredentials);

        //then
        assertNotNull(result);
        verify(dataValidation, times(1)).usernameLogin(userCredentials.getUsername());
        verify(dataValidation, times(1)).password(userCredentials.getPassword());
        verify(userDetailsService, times(1)).loadUserByUsername(userCredentials.getUsername());
        verify(passwordEncoder, times(1)).matches(userCredentials.getPassword(), user.getPassword());
        verify(jwtTokenUtil, times(1)).generateAccessToken(user);
    }

    @Test
    void should_throw_account_banned_exception() {
        //given
        UserCredentials userCredentials = getUserCredentialsMock();
        User user = getUserMock();
        user.setEnabled(true);
        user.setAccountNonLocked(false);
        when(userDetailsService.loadUserByUsername(any(String.class))).thenReturn(user);

        //when
        Exception exception = assertThrows(AccountBannedException.class, () -> authService.getToken(userCredentials));

        //then
        assertNotNull(exception);
        verify(dataValidation, times(1)).usernameLogin(userCredentials.getUsername());
        verify(dataValidation, times(1)).password(userCredentials.getPassword());
        verify(userDetailsService, times(1)).loadUserByUsername(userCredentials.getUsername());
        verify(passwordEncoder, times(0)).matches(userCredentials.getPassword(), user.getPassword());
        verify(jwtTokenUtil, times(0)).generateAccessToken(user);
    }

    @Test
    void should_throw_account_not_enabled_exception() {
        //given
        UserCredentials userCredentials = getUserCredentialsMock();
        User user = getUserMock();
        user.setEnabled(false);
        when(userDetailsService.loadUserByUsername(any(String.class))).thenReturn(user);

        //when
        Exception exception = assertThrows(AccountNotEnabledException.class, () -> authService.getToken(userCredentials));

        //then
        assertNotNull(exception);
        verify(dataValidation, times(1)).usernameLogin(userCredentials.getUsername());
        verify(dataValidation, times(1)).password(userCredentials.getPassword());
        verify(userDetailsService, times(1)).loadUserByUsername(userCredentials.getUsername());
        verify(passwordEncoder, times(0)).matches(userCredentials.getPassword(), user.getPassword());
        verify(jwtTokenUtil, times(0)).generateAccessToken(user);
    }

    @Test
    void createVerificationToken() {
        //given
        User user = getUserMock();
        String token = "mock";
        when(verificationTokenRepository.existsByUserId(any(Long.class))).thenReturn(false);

        //when
        authService.createVerificationToken(user, token);

        //then
        verify(verificationTokenRepository, times(1)).existsByUserId(user.getId());
        verify(verificationTokenRepository, times(1)).save(any());

    }

    @Test
    void should_create_and_save_email_change_token() {
        //given
        User user = getUserMock();
        String token = "mock";
        String email = "evisitfate1@gmail.com";
        when(verificationTokenRepository.existsByUserId(any(Long.class))).thenReturn(false);
        when(verificationTokenRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);

        //when
        authService.createEmailChangeToken(user, token, email);

        //then
        verify(verificationTokenRepository, times(1)).save(any());
    }

    @Test
    void should_throw_token_already_send_exception() {
        //given
        User user = getUserMock();
        String token = "mock";
        String email = "evisitfate1@gmail.com";
        when(verificationTokenRepository.existsByUserId(any(Long.class))).thenReturn(true);

        //when
        Exception exception = assertThrows(TokenAlreadySendException.class, () -> authService.createEmailChangeToken(user, token, email));

        //then
        assertNotNull(exception);
        verify(verificationTokenRepository, times(1)).existsByUserId(user.getId());
    }

    @Test
    void should_throw_email_already_taken_exception() {
        //given
        User user = getUserMock();
        String token = "mock";
        String email = "evisitfate1@gmail.com";
        when(verificationTokenRepository.existsByUserId(any(Long.class))).thenReturn(false);
        when(verificationTokenRepository.existsByEmail(any(String.class))).thenReturn(true);
        //when
        Exception exception = assertThrows(EmailAlreadyTakenException.class, () -> authService.createEmailChangeToken(user, token, email));

        //then
        assertNotNull(exception);
        verify(verificationTokenRepository, times(1)).existsByUserId(user.getId());
    }

    @Test
    void getVerificationToken() {
        //given
        VerificationToken verificationToken = getVerificationTokenMock();
        String token = "mock";
        when(verificationTokenRepository.findByToken(any(String.class))).thenReturn(verificationToken);

        //when
        VerificationToken result = authService.getVerificationToken(token);

        //then
        assertNotNull(result);
        verify(verificationTokenRepository, times(1)).findByToken(token);

    }

    @Test
    void createNewAccount() {
        //given
        UserDTO userDTO = getUserDTOMock();
        User user = getUserMock();
        Role role = getRoleMock();
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(userMapper.mapUserDTOToUser(any())).thenReturn(user);
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(roleRepository.findByName(any(String.class))).thenReturn(role);
        when(passwordEncoder.encode(any(String.class))).thenReturn(userDTO.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.mapUserToUserDTO(any(User.class))).thenReturn(userDTO);

        //when
        authService.createNewAccount(userDTO, request);

        //then
        verify(userMapper, times(1)).mapUserDTOToUser(userDTO);
        verify(userRepository, times(1)).findByUsername(userDTO.getUsername());
        verify(roleRepository, times(1)).findByName(role.getName());
        verify(eventPublisher, times(1)).publishEvent(any());


    }

    @Test
    void should_throw_Username_already_taken_exception() {
        //given
        UserDTO userDTO = getUserDTOMock();
        User user = getUserMock();
        MockHttpServletRequest request = new MockHttpServletRequest();

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.ofNullable(user));

        //when
        Exception exception = assertThrows(UsernameAlreadyTakenException.class, () -> authService.createNewAccount(userDTO, request));

        //then
        assertNotNull(exception);
        verify(userRepository, times(1)).findByUsername(userDTO.getUsername());
    }

    @Test
    void confirmAccountRegister() {
        //given
        VerificationToken verificationToken = getVerificationTokenMock();
        String token = "mock";
        User user = getUserMock();
        when(verificationTokenRepository.findByToken(any(String.class))).thenReturn(verificationToken);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(jwtTokenUtil.generateAccessToken(any(User.class))).thenReturn(token);

        //when
        TokenResponse result = authService.confirmAccountRegister(token);

        //then
        assertNotNull(result);
        verify(verificationTokenRepository, times(3)).findByToken(token);
        verify(userRepository, times(1)).findById(user.getId());
        verify(jwtTokenUtil, times(1)).generateAccessToken(user);
        verify(userRepository, times(1)).save(user);
        verify(verificationTokenRepository, times(1)).delete(verificationToken);
    }

    @Test
    void confirmDeleteAccount() {

//        //given
//        User user = getCurrentUserMock();
//        String token = "mock";
//        VerificationToken verificationToken = getVerificationTokenMock();
//        GroupRoom gr = getGroupRoomMock();
//        when(verificationTokenRepository.findByToken(any(String.class))).thenReturn(verificationToken);
//        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
//        when(groupRepository.findAllByGroupLeaderId(user.getId())).thenReturn(Collections.singletonList(gr));
//
//        //when
//        authService.deleteUser();
//
//        //then
//        verify(verificationTokenRepository, times(1)).findByToken(token);
//        verify(userRepository, times(1)).findById(user.getId());
//        verify(groupRepository, times(1)).findAllByGroupLeaderId(user.getId());
//        verify(userService, times(1)).getOutOfGroup(gr.getId());
//        verify(userRepository, times(1)).softDeleteById(gr.getId());

    }

    @Test
    void confirmEmailChange() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void sendMessage() {
    }
}