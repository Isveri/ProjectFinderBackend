package com.example.project.mappers;

import com.example.project.domain.FriendRequest;
import com.example.project.model.FriendRequestDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.example.project.samples.FriendsSample.getFriendRequestDTOMock;
import static com.example.project.samples.FriendsSample.getFriendRequestMock;
import static org.junit.jupiter.api.Assertions.*;

class FriendRequestMapperTest {

    private final FriendRequestMapper friendRequestMapper = Mappers.getMapper(FriendRequestMapper.class);

    @Test
    void should_map_friend_request_to_friend_requestDTO() {
        //given
        FriendRequest friendRequest = getFriendRequestMock();

        //when
        FriendRequestDTO result = friendRequestMapper.mapFriendRequestToFriendRequestDTO(friendRequest);

        //then
        assertEquals(friendRequest.getId(), result.getId());
        assertEquals(friendRequest.getSendingUser().getId(), result.getSendingUser().getId());
        assertEquals(friendRequest.getInvitedUser().getId(), result.getInvitedUser().getId());

    }

    @Test
    void should_map_friend_requestDTO_to_friend_request() {
        //given
        FriendRequestDTO friendRequestDTO = getFriendRequestDTOMock();

        //when
        FriendRequest result = friendRequestMapper.mapFriendRequestDTOToFriendRequest(friendRequestDTO);

        //then
        assertEquals(friendRequestDTO.getId(), result.getId());
        assertEquals(friendRequestDTO.getSendingUser().getId(), result.getSendingUser().getId());
        assertEquals(friendRequestDTO.getInvitedUser().getId(), result.getInvitedUser().getId());


    }
}