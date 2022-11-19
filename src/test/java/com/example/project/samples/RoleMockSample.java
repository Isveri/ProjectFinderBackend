package com.example.project.samples;

import com.example.project.domain.Role;

public class RoleMockSample {

    public static Role getRoleMock(){
        return Role.builder()
                .name("ROLE_USER")
                .build();
    }
    public static Role getAdminRoleMock(){
        return Role.builder().name("ROLE_ADMIN")
                .build();
    }
}
