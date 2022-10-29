package com.example.project.samples;

import com.example.project.model.auth.ChangePasswordDTO;
import com.example.project.model.auth.TokenResponse;
import com.example.project.model.auth.VerificationToken;

import static com.example.project.samples.UserMockSample.getUserMock;

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
    public static VerificationToken getVerificationTokenMock() {
        return VerificationToken.builder()
                .token("mock")
                .user(getUserMock())
                .id(1L)
                .build();
    }
}
