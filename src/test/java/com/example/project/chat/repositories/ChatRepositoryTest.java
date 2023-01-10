package com.example.project.chat.repositories;

import com.example.project.chat.model.Chat;
import com.example.project.domain.*;
import com.example.project.integration.bootData.BootData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ChatRepositoryTest {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    private BootData bootData;
    private GroupRoom groupRoom;

    private Chat chat;

    @BeforeEach
    @Rollback
    void setUp() {
        List<User> users = bootData.createUsers();
        List<Chat> chats = bootData.createChats();
        List<Game> games = bootData.createGames();
        List<Category> categories = bootData.createCategories(games);
        List<GroupRoom> groupRooms = bootData.createGroupRooms(users, categories, games, chats);
        List<InGameRole> inGameRoles = bootData.createInGameRoles(games);
        bootData.setGroupsAndInGameRoles(bootData.setUserPrivilages(users), groupRooms, inGameRoles);
        groupRoom = groupRooms.get(0);
        chat = chats.get(0);

    }

    @Test
    void findChatByGroupRoomId_should_return_chat() {
        //given
        Long groupId = groupRoom.getId();

        //when
        Chat result = chatRepository.findChatByGroupRoomId(groupId);

        //then
        assertNotNull(result);
        assertEquals(groupRoom.getChat().getId(), result.getId());
    }

    @Test
    void findByIdFetch_should_return_optional_empty() {

        //given
        Long chatId = chat.getId();

        //when
        Optional<Chat> result = chatRepository.findByIdFetch(chatId);

        //then
        assertFalse(result.isPresent());
    }

}