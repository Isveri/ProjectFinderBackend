package com.example.project.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@EqualsAndHashCode
@Getter
public class UserGroupsListDTO {
    private Long id;
    private RoleDTO role;
    private List<GroupRoomDTO> groupRooms;
}
