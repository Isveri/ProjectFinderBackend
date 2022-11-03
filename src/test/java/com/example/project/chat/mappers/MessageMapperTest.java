package com.example.project.chat.mappers;

import com.example.project.chat.model.Chat;
import com.example.project.chat.model.Message;
import com.example.project.chat.model.MessageDTO;
import com.example.project.chat.model.MessageLogsDTO;
import com.example.project.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;

import static com.example.project.chat.samples.ChatSample.getChatMock;
import static com.example.project.chat.samples.MessageSample.*;
import static com.example.project.samples.GroupRoomSample.getGroupRoomMock;
import static com.example.project.samples.UserMockSample.getUserMsgDTOMock;
import static org.junit.jupiter.api.Assertions.*;

class MessageMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private final MessageStatusMapper messageStatusMapper = Mappers.getMapper(MessageStatusMapper.class);

    private final MessageMapper messageMapper = new MessageMapperImpl(userMapper,messageStatusMapper);


    @Test
    void should_map_message_to_messageDTO() {
        //given
        Message message = getMessageMock();

        //when
        MessageDTO result = messageMapper.mapMessageToMessageDTO(message);

        //then
        assertEquals(message.getId(), result.getId());
        assertEquals(message.getDate(), result.getDate());
        assertEquals(message.getText(),result.getText());
        assertEquals(message.getUser().getId(), result.getUser().getId());
        assertEquals(message.getStatuses().get(0).getId(),result.getStatuses().get(0).getId());

    }

    @Test
    void should_map_messageDTO_to_message() {
        //given
        MessageDTO messageDTO = getMessageDTOMock();
        messageDTO.setUser(getUserMsgDTOMock());
        messageDTO.setStatuses(Collections.singletonList(getMessageStatusDTOMock()));

        //when
        Message result = messageMapper.mapMessageDTOTOMessage(messageDTO);

        //then
        assertEquals(messageDTO.getId(), result.getId());
        assertEquals(messageDTO.getDate(), result.getDate());
        assertEquals(messageDTO.getText(),result.getText());
        assertEquals(messageDTO.getUser().getId(), result.getUser().getId());
        assertEquals(messageDTO.getStatuses().get(0).getId(),result.getStatuses().get(0).getId());
    }

    @Test
    void should_map_message_to_message_logsDTO() {
        //given
        Message message = getMessageMock();
        Chat chat = getChatMock();
        chat.setGroupRoom(getGroupRoomMock());
        message.setChat(chat);

        //when
        MessageLogsDTO result = messageMapper.mapMessageToMessageLogsDTO(message);

        //then
        assertEquals(message.getId(),result.getId());
        assertEquals(message.getUser().getId(),result.getUser().getId());
        assertEquals(message.getDate(),result.getDate());
        assertEquals(message.getText(),result.getText());
        assertEquals(message.getChat().getGroupRoom().getName(),result.getGroupName());
    }
}