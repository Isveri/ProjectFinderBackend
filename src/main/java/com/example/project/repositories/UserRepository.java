package com.example.project.repositories;

import com.example.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByName(String name);

    Optional<User> findByUsername(String username);

    List<User> findAllByAccountNonLockedNot(boolean value);
    Boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User g set g.enabled=false WHERE g.id=:id")
    void softDeleteById(@Param("id") Long id);
//    Optional<User> findByUsernameAndPassword(String username, String password);

}
