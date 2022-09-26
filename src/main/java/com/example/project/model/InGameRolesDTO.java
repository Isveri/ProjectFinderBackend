package com.example.project.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@Getter
@Builder
public class InGameRolesDTO {
    private String name;
    private Long id;
}
