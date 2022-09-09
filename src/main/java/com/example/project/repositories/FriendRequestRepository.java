package com.example.project.repositories;

import com.example.project.domain.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest,Long> {


    List<FriendRequest> findAllByInvitedUserId(Long invitedUserId);
    boolean existsBySendingUserIdAndInvitedUserId(Long sendinUserId, Long invitedUserId);
}
