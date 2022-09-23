package com.example.project.repositories;

import com.example.project.domain.User;
import com.example.project.model.auth.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    boolean existsByUserId(Long userId);

    boolean existsByEmail(String email);

    void deleteByUserId(Long userId);
}
