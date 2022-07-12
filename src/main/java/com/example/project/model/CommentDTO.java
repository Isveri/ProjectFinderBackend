package com.example.project.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class CommentDTO {
    private Long id;
    private String text;
    private UserDTO user;
    private Long groupId;
}
