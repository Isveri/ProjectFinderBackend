package com.example.project.chat.mappers;

import com.example.project.chat.model.Message;
import com.example.project.mappers.UserMapper;
import com.example.project.chat.model.MessageDTO;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true),
        uses = UserMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class MessageMapper {
    public abstract MessageDTO mapMessageToMessageDTO(Message message);

    public abstract Message mapMessageDTOTOMessage(MessageDTO messageDTO);
}
