package com.example.project.integration.repositories;

import com.example.project.domain.Role;
import com.example.project.domain.User;
import com.example.project.integration.bootData.BootData;
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
public class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private BootData bootData;

    private List<User> users;
    private User user,userAdmin;
    private Role role;

    @BeforeEach
    @Rollback
    void setUp(){
        users=bootData.createUsers();
        users=bootData.setUserPrivilages(users);
        user=users.get(1);
        userAdmin=users.get(0);
    }

    @Test
    void should_return_role_user(){
        //given
        role=user.getRole();
        //when
        Role result=roleRepository.findByName(role.getName());
        //then
        assertEquals(role,result);
    }
    @Test
    void should_return_role_admin(){
        //given
        role=userAdmin.getRole();
        //when
        Role result=roleRepository.findByName(role.getName());
        //then
        assertEquals(role,result);
    }
    @Test
    void should_not_return_role(){
        //when
        Role result=roleRepository.findByName("ROLE_user");
        //then
        assertNull(result);
    }
}
