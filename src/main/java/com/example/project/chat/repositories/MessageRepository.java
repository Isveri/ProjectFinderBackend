package com.example.project.chat.repositories;

import com.example.project.chat.model.Message;
import com.example.project.chat.model.MessageDTO;
import com.example.project.chat.model.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByUserId(Long id);

    @Query("Select m from Message m join fetch m.statuses ms where m.chat.id =:chatId and ms.status=:status and ms.user.id=:userId")
    List<Message> findAllNotReadByChatId(@Param("chatId") Long chatId, @Param("status")MessageStatus.Status status,@Param("userId") Long userId);

    @Query("SELECT COUNT (m) FROM Message m JOIN m.statuses ms WHERE ms.status=:status and m.user.id=:userId")
    int countAllByStatusesUser(@Param("status") MessageStatus.Status status, @Param("userId") Long userId);
}
