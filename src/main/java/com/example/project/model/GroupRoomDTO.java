package com.example.project.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class GroupRoomDTO {
    private Long id;
    private String name;
    private String description;
    private List<UserDTO> users;
    private List<MessageDTO> comments;
}
