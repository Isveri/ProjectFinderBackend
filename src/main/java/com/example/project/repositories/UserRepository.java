package com.example.project.repositories;

import com.example.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByName(String name);

    Optional<User> findByUsername(String username);

    List<User> findAllByBanned(boolean value);
    Boolean existsByEmail(String email);
//    Optional<User> findByUsernameAndPassword(String username, String password);

}
