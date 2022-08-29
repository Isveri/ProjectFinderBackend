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

    @Query(value = "SELECT * FROM GROUP_ROOM g WHERE g.deleted=true AND g.id= :id",nativeQuery = true)
    GroupRoom findDeletedById(@Param("id") Long id);

    @Query(value = "SELECT * FROM GROUP_ROOM g WHERE g.deleted=true",nativeQuery = true)
    List<GroupRoom> findAllDeletedGroups();
    List<GroupRoom> findAllByGameNameAndOpenIsTrue(String name);

    List<GroupRoom> findAllByGameIdAndCategoryIdAndOpenIsTrue(Long gameId, Long categoryId);
    List<GroupRoom> findAllByGameIdAndCityAndOpenIsTrue(Long gameId, String city);

    List<GroupRoom> findAllByGameIdAndCategoryIdAndCityAndOpenIsTrue(Long gameId, Long categoryId, String city);
    @Query("SELECT g FROM GroupRoom g JOIN FETCH g.takenInGameRoles tr JOIN FETCH tr.inGameRole r WHERE g.open=true AND g.game.id=:gameId AND r.id=:inGameRoleId AND tr.user IS NULL")
    List<GroupRoom> findAllByGameRole(@Param("gameId") Long gameId, @Param("inGameRoleId")Long inGameRoleId);

    @Query("SELECT g FROM GroupRoom g JOIN FETCH g.takenInGameRoles tr JOIN FETCH tr.inGameRole r WHERE g.open=true AND g.game.id=:gameId AND g.category.id=:categoryId AND " +
            "r.id=:inGameRoleId AND tr.user IS NULL")
    List<GroupRoom> findAllByGameCategoryRole(@Param("gameId") Long gameId, @Param("categoryId") Long categoryId, @Param("inGameRoleId") Long inGameRoleId);

    @Transactional
    @Modifying
    @Query("UPDATE GroupRoom g set g.deleted=true WHERE g.id=:id")
    void softDeleteById(@Param("id") Long id);


}
