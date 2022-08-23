package com.example.project.repositories;

import com.example.project.domain.GroupRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupRoom,Long> {

    Optional<GroupRoom> findByName(String name);

    GroupRoom findGroupRoomByJoinCode(String joinCode);

    boolean existsByJoinCode(String joinCode);

    @Query("SELECT g FROM GroupRoom g JOIN FETCH g.users WHERE g.id = :id")
    Optional<GroupRoom> findById(@Param("id") Long id);
    List<GroupRoom> findAllByGameNameAndOpenIsTrue(String name);

    List<GroupRoom> findAllByGameIdAndCategoryIdAndOpenIsTrue(Long gameId, Long categoryId);
    List<GroupRoom> findAllByGameIdAndCityAndOpenIsTrue(Long gameId, String city);

    List<GroupRoom> findAllByGameIdAndCategoryIdAndCityAndOpenIsTrue(Long gameId, Long categoryId, String city);
    List<GroupRoom> findAllByGameIdAndGame_InGameRolesIdAndOpenIsTrue(Long gameId, Long inGameRoleId);

    List<GroupRoom> findAllByGameIdAndCategoryIdAndGameInGameRolesIdAndOpenIsTrue(Long gameId, Long categoryId, Long inGameRoleId);

    @Transactional
    @Modifying
    @Query("UPDATE GroupRoom g set g.deleted=true WHERE g.id=:id")
    void softDeleteById(@Param("id") Long id);


}
