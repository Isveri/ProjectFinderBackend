package com.example.project.integration.repositories;

import com.example.project.domain.User;
import com.example.project.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.project.samples.UserMockSample.getBlockedUserMock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    @Rollback
    void setUp() {
        user = getBlockedUserMock();
        userRepository.save(user);
    }


    @Test
    void should_return_user_by_username() {
        //given

        String username = user.getUsername();

        //when
        Optional<User> result = userRepository.findByUsername(username);

        //then
        assertTrue(result.isPresent());
        assertEquals(result.get().getUsername(),user.getUsername());


    }

    @Test
    void should_return_all_blocked_users() {

        //when
        List<User> results = userRepository.findAllByAccountNonLockedNot(true);

        //then
        assertFalse(results.isEmpty());
        assertEquals(results.get(0).isAccountNonLocked(),user.getAccountNonLocked());

    }

    @Test
    void exist_by_email_should_return_true() {
        //given
        String email = user.getEmail();

        //when
        Boolean result = userRepository.existsByEmail(email);

        //then
        assertTrue(result);

    }

    @Test
    void exist_by_email_should_return_false() {
        //given
        String email = "notExist";

        //when
        Boolean result = userRepository.existsByEmail(email);

        //then
        assertFalse(result);

    }

}