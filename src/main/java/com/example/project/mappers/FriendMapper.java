package com.example.project.mappers;

import com.example.project.domain.Friend;
import com.example.project.model.FriendDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true))
public abstract class FriendMapper {

    public abstract FriendDTO mapFriendToFriendDTO(Friend friend);

}
