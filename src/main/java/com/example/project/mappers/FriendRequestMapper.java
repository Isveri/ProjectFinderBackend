package com.example.project.mappers;

import com.example.project.chat.mappers.ChatMapper;
import com.example.project.chat.mappers.MessageMapper;
import com.example.project.domain.FriendRequest;
import com.example.project.model.FriendRequestDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true),uses = {ChatMapper.class, MessageMapper.class})
public abstract class FriendRequestMapper {

    public abstract FriendRequestDTO mapFriendRequestToFriendRequestDTO(FriendRequest friendRequest);

    public abstract FriendRequest mapFriendRequestDTOToFriendRequest(FriendRequestDTO friendRequestDTO);

}
