package com.example.project.repositories;

import com.example.project.domain.GroupRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupRoom,Long> {

    Optional<GroupRoom> findByName(String name);

    GroupRoom findGroupRoomByJoinCode(String joinCode);

//    @Query("Select GroupRoom from GroupRoom JOIN FETCH GroupRoom.users")
//    Optional<GroupRoom> findByGroupId(Long groupId);

    boolean existsByJoinCode(String joinCode);

    @Query("SELECT g FROM GroupRoom g JOIN FETCH g.users WHERE g.id = :id")
    Optional<GroupRoom> findById(@Param("id") Long id);
    List<GroupRoom> findAllByGameNameAndOpenIsTrue(String name);

    List<GroupRoom> findAllByGameIdAndCategoryIdAndOpenIsTrue(Long gameId, Long categoryId);

    List<GroupRoom> findAllByGameIdAndGame_InGameRolesIdAndOpenIsTrue(Long gameId, Long inGameRoleId);

    List<GroupRoom> findAllByGameIdAndCategoryIdAndGameInGameRolesIdAndOpenIsTrue(Long gameId, Long categoryId, Long inGameRoleId);


}
