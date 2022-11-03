package com.example.project.mappers;

import com.example.project.domain.Platform;
import com.example.project.model.PlatformDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.example.project.samples.PlatformSample.getPlatformMock;
import static org.junit.jupiter.api.Assertions.*;

class PlatformMapperTest {

    private final PlatformMapper platformMapper = Mappers.getMapper(PlatformMapper.class);

    @Test
    void should_map_platform_to_platformDTO() {
        //given
        Platform platform = getPlatformMock();

        //when
        PlatformDTO result = platformMapper.mapPlatformToPlatformDTO(platform);

        //then
        assertEquals(platform.getPlatformType(), result.getPlatformType());
        assertEquals(platform.getAvatar(), result.getAvatar());
        assertEquals(platform.getDiscriminator(), result.getDiscriminator());
        assertEquals(platform.getId(), result.getId());
        assertEquals(platform.getUsername(),result.getUsername());
    }
}