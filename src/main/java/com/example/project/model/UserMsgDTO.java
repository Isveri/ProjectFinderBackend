package com.example.project.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMsgDTO {
    private Long id;
    private String username;
}
