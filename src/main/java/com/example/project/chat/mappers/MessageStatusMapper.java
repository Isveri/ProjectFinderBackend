package com.example.project.chat.mappers;

import com.example.project.chat.model.MessageStatus;
import com.example.project.chat.model.MessageStatusDTO;
import com.example.project.mappers.FriendMapper;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true),uses= MessageMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class MessageStatusMapper {

    public abstract MessageStatusDTO mapMessageStatusToMessageStatusDTO(MessageStatus messageStatus);
    public abstract MessageStatus mapMessageStatusDTOToMessageStatus(MessageStatusDTO messageStatusDTO);
}
