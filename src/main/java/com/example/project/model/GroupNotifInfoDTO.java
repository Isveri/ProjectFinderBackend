package com.example.project.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Setter
@Getter
public class GroupNotifInfoDTO {
    private Long id;
    private String name;
}
