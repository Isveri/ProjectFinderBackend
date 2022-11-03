package com.example.project.chat.mappers;

import com.example.project.chat.model.Chat;
import com.example.project.chat.model.ChatDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.example.project.chat.samples.ChatSample.getChatMock;
import static org.junit.jupiter.api.Assertions.*;

class ChatMapperTest {

    private final ChatMapper chatMapper = Mappers.getMapper(ChatMapper.class);


    @Test
    void should_map_chat_to_chatDTO() {
        //given
        Chat chat = getChatMock();

        //when
        ChatDTO result = chatMapper.mapChatToChatDTO(chat);

        //then
        assertEquals(chat.getId(), result.getId());
        assertEquals(chat.getMessages().get(0).getId(),result.getMessages().get(0).getId());
    }
}