package com.example.project.repositories;

import com.example.project.domain.GroupRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupRoom,Long> {

    Optional<GroupRoom> findByName(String name);

    List<GroupRoom> findAllByGameName(String name);

    List<GroupRoom> findAllByGameIdAndCategoryId(Long gameId,Long categoryId);

    List<GroupRoom> findAllByGameIdAndGame_InGameRolesId(Long gameId,Long inGameRoleId);

    List<GroupRoom> findAllByGameIdAndCategoryIdAndGame_InGameRolesId(Long gameId,Long categoryId,Long inGameRoleId);


}
