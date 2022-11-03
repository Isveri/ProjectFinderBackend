package com.example.project.mappers;

import com.example.project.domain.Friend;
import com.example.project.model.FriendDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.example.project.samples.FriendsSample.getFriendMock;
import static org.junit.jupiter.api.Assertions.*;

class FriendMapperTest {

    private final FriendMapper friendMapper = Mappers.getMapper(FriendMapper.class);

    @Test
    void should_map_friend_to_friendDTO() {
        //given
        Friend friend = getFriendMock();

        //when
        FriendDTO result = friendMapper.mapFriendToFriendDTO(friend);

        //then
        assertEquals(friend.getId(), result.getId());
        assertEquals(friend.getUser().getId(), result.getUser().getId());
        assertEquals(friend.getChat().getId(), result.getChatId());
    }
}