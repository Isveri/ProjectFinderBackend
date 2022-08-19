package com.example.project.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class BannedUserDTO {
    private Long id;
    private String username;
    private String bannedBy;
    private String reason;
}
