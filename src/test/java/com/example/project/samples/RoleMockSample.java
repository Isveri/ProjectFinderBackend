package com.example.project.samples;

import com.example.project.domain.Role;

public class RoleMockSample {

    public static Role getRoleMock(){
        return Role.builder()
                .build();
    }
}
