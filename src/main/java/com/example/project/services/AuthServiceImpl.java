package com.example.project.services;

import com.example.project.domain.GroupRoom;
import com.example.project.domain.Role;
import com.example.project.domain.User;
import com.example.project.exceptions.*;
import com.example.project.exceptions.validation.EmailAlreadyTakenException;
import com.example.project.exceptions.validation.UsernameAlreadyTakenException;
import com.example.project.exceptions.validation.WrongPasswordException;
import com.example.project.mappers.UserMapper;
import com.example.project.model.UserDTO;
import com.example.project.model.UserMsgDTO;
import com.example.project.model.auth.ChangePasswordDTO;
import com.example.project.model.auth.TokenResponse;
import com.example.project.model.auth.UserCredentials;
import com.example.project.model.auth.VerificationToken;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.RoleRepository;
import com.example.project.repositories.UserRepository;
import com.example.project.repositories.VerificationTokenRepository;
import com.example.project.security.JwtTokenUtil;
import com.example.project.security.emailConfirm.OnAccountRegisterCompleteEvent;
import com.example.project.security.emailConfirm.OnEmailChangeCompleteEvent;
import com.example.project.utils.UserDetailsHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.project.utils.UserDetailsHelper.getCurrentUser;

import com.example.project.utils.DataValidation;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
@EnableAsync
public class AuthServiceImpl implements AuthService {


    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserService userService;

    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenRepository verificationTokenRepository;
    private final DataValidation dataValidation;
    private final JavaMailSender javaMailSender;


    @Override
    public TokenResponse getToken(UserCredentials userCredentials) {
        dataValidation.usernameLogin(userCredentials.getUsername());
        dataValidation.password(userCredentials.getPassword());
        User user = (User) userDetailsService.loadUserByUsername(userCredentials.getUsername());
        if(!user.isEnabled()){
            throw new AccountNotEnabledException("Account not enabled");
        }
        if (!user.isAccountNonLocked()) {
            throw new AccountBannedException("Account banned");
        } else if (passwordEncoder.matches(userCredentials.getPassword(), user.getPassword()))
            return new TokenResponse(jwtTokenUtil.generateAccessToken(user));
        return null;
    }

    @Override
    public void createVerificationToken(User user, String token) {
        if (verificationTokenRepository.existsByUserId(user.getId())) {
            throw new TokenAlreadySendException("Verification token already send");
        } else {
            VerificationToken myToken = VerificationToken.builder().token(token).user(user).build();
            verificationTokenRepository.save(myToken);
        }
    }

    @Override
    public void createEmailChangeToken(User user, String token, String email) {
        if (verificationTokenRepository.existsByUserId(user.getId())) {
            throw new TokenAlreadySendException("Verification token already send");
        }else if(verificationTokenRepository.existsByEmail(email)){
            throw new EmailAlreadyTakenException("Email taken:"+email);
        }else if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyTakenException("Email taken:"+email);
        }
        else {
            VerificationToken myToken = VerificationToken.builder().token(token).email(email).user(user).build();
            verificationTokenRepository.save(myToken);
        }
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return verificationTokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void createNewAccount(UserDTO userDto, HttpServletRequest request) {
        User newUser = userMapper.mapUserDTOToUser(userDto);
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        String email = userDto.getEmail();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyTakenException("Username already taken");
        } else {
            Role userRole = roleRepository.findByName("ROLE_USER");
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setRole(userRole);


            User createdUser = userMapper.mapUserDTOToUser(userService.save(userMapper.mapUserToUserDTO(newUser)));
            eventPublisher.publishEvent(new OnAccountRegisterCompleteEvent(createdUser,request.getLocale(),request.getContextPath()));

        }
    }

    @Override
    public TokenResponse confirmAccountRegister(String token) {

        validateVerificationToken(token);

        Long userId = verificationTokenRepository.findByToken(token).getUser().getId();
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found id:"+userId));
        user.setEnabled(true);
        userRepository.save(user);
        VerificationToken verificationToken = this.getVerificationToken(token);
        verificationTokenRepository.delete(verificationToken);
        return new TokenResponse(jwtTokenUtil.generateAccessToken(user));
    }

    private void validateVerificationToken(String token) {
        VerificationToken verificationToken = this.getVerificationToken(token);
        if (verificationToken == null) {

            throw new TokenExpiredException("Bad token");
        }

        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new TokenExpiredException("Token expired");
        }
    }

    @Override
    public String confirmDeleteAccount(String token) {

        validateVerificationToken(token);

        this.deleteUser();
        return "/account-deleted";
    }

    @Override
    public void confirmEmailChange(String token) {
        validateVerificationToken(token);

        VerificationToken verificationToken = this.getVerificationToken(token);
        Long userId = verificationTokenRepository.findByToken(token).getUser().getId();
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found id:"+userId));
        user.setEmail(verificationToken.getEmail());
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
    }


    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        User currentUser = getCurrentUser();
        long id = currentUser.getId();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found id:" + id));
        dataValidation.password(changePasswordDTO.getOldPassword());
        dataValidation.password(changePasswordDTO.getNewPassword());
        if (passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            try {
                user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                userRepository.save(user);
            } catch (Exception e) {
                throw new EmailAlreadyTakenException("something wrong with upd password");
            }
        } else {
            throw new WrongPasswordException("Wrong password");
        }
    }
    @Async
    @Override
    public void sendMessage(MimeMessage mimeMessage) {
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void deleteUser() {
        User currentUser = getCurrentUser();
        long id = currentUser.getId();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found id:" + id));
        List<GroupRoom> UserGroupRooms = groupRepository.findAllByGroupLeaderId(id);
        try {
            for (GroupRoom groupRoom : UserGroupRooms) {
                userService.getOutOfGroup(groupRoom.getId());
            }
            deleteVerificationToken(user);
            userRepository.softDeleteById(id);

        } catch (Exception e) {
            throw new DeleteUserException("Something wrong with deleting a user");
        }

    }


    private void deleteVerificationToken(User user) {
        VerificationToken token = verificationTokenRepository.findByUser(user);
        token.setUser(null);
        Long tempId = token.getId();
        verificationTokenRepository.save(token);
        verificationTokenRepository.deleteById(tempId);
    }

}
