package com.example.project.repositories;

import com.example.project.domain.GroupRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupRoom,Long>, JpaSpecificationExecutor<GroupRoom> {

    Optional<GroupRoom> findByName(String name);

    GroupRoom findGroupRoomByJoinCode(String joinCode);

    boolean existsByJoinCode(String joinCode);

    @Query("SELECT g FROM GroupRoom g JOIN FETCH g.users WHERE g.id = :id")
    Optional<GroupRoom> findById(@Param("id") Long id);

    @Query(value = "SELECT * FROM GROUP_ROOM g WHERE g.deleted=true AND g.id= :id",nativeQuery = true)
    GroupRoom findDeletedById(@Param("id") Long id);

    @Query(value = "SELECT * FROM GROUP_ROOM g WHERE g.deleted=true",nativeQuery = true)
    List<GroupRoom> findAllDeletedGroups();
    Page<GroupRoom> findAllByGameNameAndOpenIsTrue(String name, Pageable pageable);

    List<GroupRoom> findAllByGroupLeaderId(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE GroupRoom g set g.deleted=true WHERE g.id=:id")
    void softDeleteById(@Param("id") Long id);


}
