package com.example.project.chat.mappers;

import com.example.project.chat.model.*;
import com.example.project.mappers.UserMapper;
import org.mapstruct.*;

@Mapper(builder = @Builder(disableBuilder = true),
        uses = {UserMapper.class,MessageStatusMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class MessageMapper {
    public abstract MessageDTO mapMessageToMessageDTO(Message message);

    public abstract Message mapMessageDTOTOMessage(MessageDTO messageDTO);

    @Mapping(target="groupName",source = "chat.groupRoom.name")
    public abstract MessageLogsDTO mapMessageToMessageLogsDTO(Message message);
}
