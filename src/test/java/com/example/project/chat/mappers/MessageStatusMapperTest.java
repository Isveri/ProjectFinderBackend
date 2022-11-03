package com.example.project.chat.mappers;

import com.example.project.chat.model.MessageStatus;
import com.example.project.chat.model.MessageStatusDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.example.project.chat.samples.MessageSample.getMessageStatusDTOMock;
import static com.example.project.chat.samples.MessageSample.getMessageStatusMock;
import static org.junit.jupiter.api.Assertions.*;


class MessageStatusMapperTest {

    private final MessageStatusMapper messageStatusMapper = Mappers.getMapper(MessageStatusMapper.class);

    @Test
    void should_map_message_status_to_message_statusDTO() {
        //given
        MessageStatus messageStatus = getMessageStatusMock();

        //when
        MessageStatusDTO result = messageStatusMapper.mapMessageStatusToMessageStatusDTO(messageStatus);

        //then
        assertEquals(messageStatus.getId(), result.getId());
        assertEquals(messageStatus.getStatus(), result.getStatus());
        assertEquals(messageStatus.getUser().getId(), result.getUser().getId());
    }

    @Test
    void should_map_message_statusDTO_to_message_status() {
        //given
        MessageStatusDTO messageStatusDTO = getMessageStatusDTOMock();

        //when
        MessageStatus result = messageStatusMapper.mapMessageStatusDTOToMessageStatus(messageStatusDTO);

        //then
        assertEquals(messageStatusDTO.getStatus(), result.getStatus());
        assertEquals(messageStatusDTO.getId(), result.getId());
        assertEquals(messageStatusDTO.getUser().getId(), result.getUser().getId());

    }
}