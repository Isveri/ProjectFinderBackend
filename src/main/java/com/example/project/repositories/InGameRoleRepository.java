package com.example.project.repositories;

import com.example.project.domain.InGameRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InGameRoleRepository extends JpaRepository<InGameRole,Long> {
}
