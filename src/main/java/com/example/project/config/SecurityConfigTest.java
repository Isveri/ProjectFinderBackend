package com.example.project.config;

import com.example.project.domain.Role;
import com.example.project.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@Profile("test")
public class SecurityConfigTest {
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        User user = User.builder().username("Test")
                .password(passwordEncoder.encode("secret"))
                .role(Role.builder().name("ROLE_ADMIN").build())
                .build();
        InMemoryUserDetailsManager memoryUserDetailsManager = new InMemoryUserDetailsManager();
        memoryUserDetailsManager.createUser(user);
        return memoryUserDetailsManager;
    }
}
