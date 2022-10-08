package com.example.project.mappers;

import com.example.project.domain.Platform;
import com.example.project.model.PlatformDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true))
public abstract class PlatformMapper {

    public abstract PlatformDTO mapPlatformToPlatformDTO(Platform platform);
}
