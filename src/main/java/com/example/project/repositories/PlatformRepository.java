package com.example.project.repositories;

import com.example.project.domain.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformRepository extends JpaRepository<Platform,Long> {
}
