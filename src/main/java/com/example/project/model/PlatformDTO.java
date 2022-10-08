package com.example.project.model;

import com.example.project.domain.Platform;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@Getter
@Builder
public class PlatformDTO {

    private Long id;

    private Platform.PlatformType platformType;

    private String username;

    private String avatar;

    private String discriminator;
}
