package com.example.project.integration.repositories;

import com.example.project.chat.model.Chat;
import com.example.project.domain.Category;
import com.example.project.domain.Game;
import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import com.example.project.integration.bootData.BootData;
import com.example.project.repositories.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class GroupRepositoryTest {
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    private BootData bootData;
    private GroupRoom groupRoom;
    private List<GroupRoom> groups;
    private List<User> users;
    private List<Game> games;
    private List<Category> categories;
    private List<Chat> chats;

    @BeforeEach
    @Rollback
    void setUp(){
        users=bootData.createUsers();
        users = bootData.setUserPrivilages(users);
        games=bootData.createGames();
        categories=bootData.createCategories(games);
        chats=bootData.createChats();
        groups=bootData.createGroupRooms(users,categories,games,chats);
        groupRoom=groups.get(0);

    }
    //given
    //when
    //then

    @Test
    void should_return_group_by_name(){
        //given
        String groupName=groupRoom.getName();
        //when
        Optional<GroupRoom> result=groupRepository.findByName(groupName);
        //then
        assertEquals(groupName,result.get().getName());
    }
    @Test
    void should_return_NoSuchElementException(){
        //given
        String groupName="!@#";
        //when
        Optional<GroupRoom> result=groupRepository.findByName(groupName);
        //then
        assertThrows(NoSuchElementException.class, () -> {
            result.get();
        });
    }
    @Test
    void should_return_allDeletedGroups(){
        //given
        GroupRoom groupRoomDeleted=groups.get(0);
        groupRoomDeleted.setDeleted(true);
        //when
        List<GroupRoom> result=groupRepository.findAllDeletedGroups();
        //then
        assertFalse(result.isEmpty());
        assertEquals(result.get(0).getName(),groupRoomDeleted.getName());
    }
    @Test
    void should_return_null(){
        //given
        Long groupId=groupRoom.getId();
        //when
        GroupRoom result=groupRepository.findDeletedById(groupId);
        //then
        assertNull(result);
    }
    @Test
    void should_return_all_groups_by_leaderId(){
        //given
        User leader=users.get(2);
        //when
        List<GroupRoom> result=groupRepository.findAllByGroupLeaderId(leader.getId());
        //then
        assertFalse(result.isEmpty());
        assertEquals(result.get(0).getGroupLeader(),leader);
    }

}
