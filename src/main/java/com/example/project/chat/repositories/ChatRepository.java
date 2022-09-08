package com.example.project.chat.repositories;

import com.example.project.chat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,Long> {

    Chat findChatByGroupRoomId(Long groupId);

    @Query("SELECT c FROM Chat c JOIN FETCH c.users WHERE c.id = :id")
    Optional<Chat> findByIdFetch(@Param("id") Long id);
}
