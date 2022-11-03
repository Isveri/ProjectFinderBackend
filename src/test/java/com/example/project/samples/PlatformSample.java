package com.example.project.samples;

import com.example.project.domain.Platform;

import static com.example.project.samples.UserMockSample.getCurrentUserMock;

public class PlatformSample {

    public static Platform getPlatformMock() {
        return Platform.builder()
                .platformType(Platform.PlatformType.DISCORD)
                .user(getCurrentUserMock())
                .username("Evistix")
                .id(1L)
                .discriminator("3453")
                .avatar("")
                .build();
    }
}
