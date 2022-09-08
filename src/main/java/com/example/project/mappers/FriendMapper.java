package com.example.project.mappers;

import com.example.project.chat.mappers.ChatMapper;
import com.example.project.chat.mappers.MessageMapper;
import com.example.project.domain.Friend;
import com.example.project.model.FriendDTO;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(builder = @Builder(disableBuilder = true),uses = {ChatMapper.class, MessageMapper.class},injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class FriendMapper {


    public abstract FriendDTO mapFriendToFriendDTO(Friend friend);

}
