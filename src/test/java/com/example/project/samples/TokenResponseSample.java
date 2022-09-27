package com.example.project.samples;

import com.example.project.model.auth.ChangePasswordDTO;
import com.example.project.model.auth.TokenResponse;

public class TokenResponseSample {

    public static TokenResponse getTokenResponseMock(){
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken("Heh");
        return tokenResponse;
    }

    public static ChangePasswordDTO getChangePasswordDTOMock(){
        return ChangePasswordDTO.builder()
                .newPassword("new")
                .oldPassword("old")
                .build();
    }
}
