package com.example.project.samples;

import com.example.project.model.UserDTO;

public class UserMockSample {

    public static UserDTO getUserDTOMock(){
        return UserDTO.builder()
                .id(1L)
                .username("Evi")
                .age(21)
                .city("Lublin")
                .email("evistifate@gmail.com")
                .info("Brak ")
                .name("Harold")
                .build();
    }
}
