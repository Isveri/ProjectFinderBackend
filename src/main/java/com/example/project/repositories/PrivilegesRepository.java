package com.example.project.repositories;

import com.example.project.domain.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PrivilegesRepository extends JpaRepository<Privilege,Long> {
    Privilege findByName(String name);
}
