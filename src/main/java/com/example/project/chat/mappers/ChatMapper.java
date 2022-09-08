package com.example.project.chat.mappers;

import com.example.project.chat.model.Chat;
import com.example.project.chat.model.ChatDTO;
import com.example.project.mappers.FriendMapper;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true),uses= FriendMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ChatMapper {
    public abstract ChatDTO mapChatToChatDTO(Chat chat);
}
