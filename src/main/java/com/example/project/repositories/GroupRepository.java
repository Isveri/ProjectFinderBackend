package com.example.project.repositories;

import com.example.project.domain.GroupRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupRoom,Long> {

    Optional<GroupRoom> findByName(String name);

    List<GroupRoom> findAllByGameNameAndOpenIsTrue(String name);

    List<GroupRoom> findAllByGameIdAndCategoryIdAndOpenIsTrue(Long gameId, Long categoryId);

    List<GroupRoom> findAllByGameIdAndGame_InGameRolesIdAndOpenIsTrue(Long gameId, Long inGameRoleId);

    List<GroupRoom> findAllByGameIdAndCategoryIdAndGameInGameRolesIdAndOpenIsTrue(Long gameId, Long categoryId, Long inGameRoleId);


}
