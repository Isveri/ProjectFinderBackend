package com.example.project.repositories;

import com.example.project.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game,Long> {

    Optional<Game> findByName(String name);
}
